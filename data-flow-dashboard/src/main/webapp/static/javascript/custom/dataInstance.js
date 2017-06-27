;(function ($) {
    var profile = {
        init: function () {
            profile.initNProgress();
            $(function () {
                profile.init_sidebar();
                profile.extension();
            })
        },
        init_sidebar: function () {
            var $BODY = $('body'),
                $MENU_TOGGLE = $('#menu_toggle'),
                $SIDEBAR_MENU = $('#sidebar-menu'),
                $SIDEBAR_FOOTER = $('.sidebar-footer'),
                $LEFT_COL = $('.left_col'),
                $RIGHT_COL = $('.right_col'),
                $NAV_MENU = $('.nav_menu'),
                $FOOTER = $('footer');

            var setContentHeight = function () {
                // reset height
                $RIGHT_COL.css('min-height', $(window).height());

                var bodyHeight = $BODY.outerHeight(),
                    footerHeight = $BODY.hasClass('footer_fixed') ? -10 : $FOOTER.height(),
                    leftColHeight = $LEFT_COL.eq(1).height() + $SIDEBAR_FOOTER.height(),
                    contentHeight = bodyHeight < leftColHeight ? leftColHeight : bodyHeight;

                // normalize content
                contentHeight -= $NAV_MENU.height() + footerHeight;

                $RIGHT_COL.css('min-height', contentHeight);
            };

// toggle small or large menu
            $MENU_TOGGLE.on('click', function () {
                if ($BODY.hasClass('nav-md')) {
                    $SIDEBAR_MENU.find('li.active ul').hide();
                    $SIDEBAR_MENU.find('li.active').addClass('active-sm').removeClass('active');
                } else {
                    $SIDEBAR_MENU.find('li.active-sm ul').show();
                    $SIDEBAR_MENU.find('li.active-sm').addClass('active').removeClass('active-sm');
                }

                $BODY.toggleClass('nav-md nav-sm');

                setContentHeight();

                $('.dataTable').each(function () {
                    $(this).dataTable().fnDraw();
                });
            });

            setContentHeight();

            // fixed sidebar
            if ($.fn.mCustomScrollbar) {
                $('.menu_fixed').mCustomScrollbar({
                    autoHideScrollbar: true,
                    theme: 'minimal',
                    mouseWheel: {preventDefault: true}
                });
            }
        },
        initNProgress: function () {
            if (typeof NProgress != 'undefined') {
                $(document).ready(function () {
                    NProgress.start();
                });

                $(window).load(function () {
                    NProgress.done();
                });
            }
        },
        extension: function () {
            $.fn.loadData = function (data) {
                if ($.isPlainObject(data)) {
                    // input
                    $(this).find("input").each(function () {
                        var $this = $(this);
                        var type = $this.attr("type");
                        var name = $this.attr("name");
                        var value = $this.attr("value");
                        if (data[name]) {
                            switch (type) {
                                case "text":
                                case "number":
                                case "password":
                                case "hidden":
                                    $this.val(data[name])
                                    break;
                                case "radio":
                                    if (value == data[name]) {
                                        $this.attr("checked", true);
                                    } else {
                                        $this.attr("checked", false);
                                    }
                                    break;
                                case "checkbox" :
                                    var d = data[name];
                                    if ($.isArray(d) && d.indexOf(value) > -1) {
                                        $this.attr("checked", true);
                                    } else if (d == value) {
                                        $this.attr("checked", true);
                                    } else {
                                        $this.attr("checked", false);
                                    }
                            }
                        }
                    })
                    // textarea
                    $(this).find("textarea").each(function () {
                        var $this = $(this);
                        var name = $this.attr("name");
                        if (data[name]) {
                            $this.val(data[name]);
                        }
                    })

                    // select
                    $(this).find("select").each(function () {
                        var $this = $(this);
                        var name = $this.attr("name");
                        if (data[name]) {
                            $this.val(data[name]);
                            $this.find("option[value='" + data[name] + "']").attr("selected", true);
                        }
                    })
                }
            }
        }
    }
    profile.init();

    var main = {
        init: function () {
            this.initDataInstanceTable();
            this.eventBind();
            this.initValidation();
            this.initScroll();
            this.initDataOutputMappingTable();
            this.initDataSourceOutputMapping();
        },
        initScroll: function () {
            $("body").niceScroll({
                cursorcolor: "#2A3F54",
                cursorwidth: "10px",
                cursorborder: "0",
                cursoropacitymax: 0.7,
                mousescrollstep: 15
            })
        },
        messageAlert: function (message, callback) {
            $("#alertMessage").modal('show');
            $("#alert-content").empty().html(message);
            if (callback) {
                $("#alert-submit").show().off().on("click", function () {
                    callback();
                    $("#alertMessage").modal('hide');
                })
            } else {
                $("#alert-submit").hide().off();
            }
        },
        eventBind: function () {
            $("#dataInstanceModal").on("shown.bs.modal", function () {
                if (!main._hasInitFormWizard) {
                    main._initFormWizard();
                    main._hasInitFormWizard = true;
                } else {
                    // jump to step1
                    $("#step1").click();
                }
            })

            $("#dataInstanceModal").on("hidden.bs.modal", function () {
                // clear dataInstance form
                var $dataInstance = $("#dataInstance-form");
                $dataInstance.clearForm(true);
                $dataInstance.removeData();

                // clear dataTable form
                var promptingMessage = '<div style="margin: 0 auto; margin-top: 50px; text-align: center"><h3>配置过滤的表字段</h3></div>'
                $("#dataInstance-table-detail").empty().html(promptingMessage)
            })
        },
        initValidation: function () {
            if (typeof (validator) === 'undefined') {
                return;
            }

            // validate a field on "blur" event, a 'select' on 'change' event & a '.reuired' classed multifield on 'keyup':
            $('form')
                .on('blur', 'input[required], input.optional, select.required', validator.checkField)
                .on('change', 'select.required', validator.checkField)
                .on('keypress', 'input[required][pattern]', validator.keypress);

            $('.multi.required').on('keyup blur', 'input', function () {
                validator.checkField.apply($(this).siblings().last()[0]);
            });
        },
        _hasInitFormWizard: false,
        _initFormWizard: function () {
            var wizard = {
                init: function () {
                    this.initWizard();
                    this.eventBind();
                },
                initWizard: function () {
                    if (typeof ($.fn.smartWizard) === 'undefined') {
                        return;
                    }
                    $('#wizard').smartWizard({
                        onLeaveStep: wizard._leaveAStepCallback,
                        onFinish: wizard._onFinishCallback,
                        keyNavigation: false
                    });
                    $('#wizard .buttonNext').addClass('btn btn-success');
                    $('#wizard .buttonPrevious').addClass('btn btn-primary');
                    $('#wizard .buttonFinish').addClass('btn btn-default');
                },
                _leaveAStepCallback: function (obj, context) {
                    // ignore previous action
                    if (context.fromStep > context.toStep) {
                        return true;
                    } else {
                        switch (context.fromStep) {
                            case 1:
                                return wizard.__step1();
                            case 2:
                                return wizard.__step2();
                            case 3:
                                return wizard.__step3();
                            default :
                                return true;
                        }
                    }
                },
                _beforeStep2Callback: function (dataInstanceId) {
                    // render schema
                    $.ajax({
                        url: "schema",
                        data: {id: dataInstanceId, filter: false},
                        dataType: "json",
                        async: false
                    }).then(function (data) {
                        // 渲染所有schema
                        if (data.responseStatus == 200) {
                            var schemaList = "";
                            schemaList += ' <div class="menu_section">'
                            schemaList += '     <ul class="nav side-menu">'
                            for (var i in data.result) {
                                var schemaName = data.result[i];
                                schemaList += '     <li><a class="dataInstance-schema" data-dataInstanceId="' + dataInstanceId + '" data-schemaName="' + schemaName + '"><i class="fa fa-cube"></i> ' + schemaName + ' <span class="fa fa-chevron-right"></span></a>'
                                schemaList += '     </li>'
                            }
                            schemaList += '     </ul>'
                            schemaList += ' </div>'
                            // bind event and render tableList
                            var $dataInstanceSchemaList = $("#dataInstance-schemaList");
                            // store schemaList
                            $dataInstanceSchemaList.removeData().data("schemaList", data.result);
                            $dataInstanceSchemaList.niceScroll({
                                cursorcolor: "#ccc",
                                cursorwidth: "2px",
                                cursorborder: "0",
                                cursoropacitymax: 0.7,
                                mousescrollstep: 20
                            })

                            $dataInstanceSchemaList.empty().append(schemaList).find(".dataInstance-schema")
                                .each(function () {
                                    $(this).on('click', function (ev) {
                                        var $this = $(this);
                                        var $li = $this.parent();
                                        if ($li.children().length == 1) {
                                            var dataInstanceId = $this.attr("data-dataInstanceId");
                                            var schemaName = $this.attr("data-schemaName")
                                            $.ajax({
                                                url: "table",
                                                data: {id: dataInstanceId, schemaName: schemaName},
                                                dataType: "json",
                                                async: false
                                            }).then(function (data) {
                                                if (data.responseStatus == 200) {
                                                    var tableList = "";
                                                    tableList += '<ul class="nav child_menu">';
                                                    for (var i in data.result) {
                                                        var tableName = data.result[i]
                                                        tableList += '<li><a class="dataInstance-table" data-dataInstanceId="' + dataInstanceId + '" data-schemaName="' + schemaName + '" data-tableName="' + tableName + '">' + tableName + '</a></li>'
                                                    }
                                                    tableList += "</ul>"
                                                    // bind event and render table detail
                                                    $this.closest("li").append(tableList).find(".dataInstance-table")
                                                        .each(function () {
                                                            this.onclick = function () {
                                                                var $this = $(this);
                                                                $this.parent().parent().find("a.active").removeClass("active");
                                                                $this.addClass("active");
                                                                var dataInstanceId = $this.attr("data-dataInstanceId");
                                                                var schemaName = $this.attr("data-schemaName");
                                                                var tableName = $this.attr("data-tableName");
                                                                $.ajax({
                                                                    url: "tableDetail",
                                                                    type: 'POST',
                                                                    data: {
                                                                        id: dataInstanceId,
                                                                        schemaName: schemaName,
                                                                        tableName: tableName
                                                                    },
                                                                    dataType: "json"
                                                                }).then(function (data) {
                                                                    if (data.responseStatus == 200) {
                                                                        var tableDetailForm = "";
                                                                        tableDetailForm += '<form id="dataTable-form" class="form-horizontal form-label-left">'
                                                                        tableDetailForm += ' <div class="item form-group">'
                                                                        tableDetailForm += '   <label class="control-label col-md-2 col-sm-2 col-xs-6" for="dataInstance-name">name <span class="required">*</span></label>'
                                                                        tableDetailForm += '    <div class="col-md-9 col-sm-9 col-xs-12">'
                                                                        // columns
                                                                        var columns = data.result.columns;
                                                                        for (var i in columns) {
                                                                            var column = columns[i];
                                                                            if (i == 0) {
                                                                                tableDetailForm += '<label class="checkbox-inline" style="margin-left: 10px">'
                                                                            } else {
                                                                                tableDetailForm += '<label class="checkbox-inline">'
                                                                            }
                                                                            tableDetailForm += '   <input type="checkbox" name="columns" value="' + column + '">'
                                                                            tableDetailForm += column
                                                                            tableDetailForm += '    </input>'
                                                                            tableDetailForm += '</label>'
                                                                        }
                                                                        tableDetailForm += '   </div>'
                                                                        tableDetailForm += ' </div>'
                                                                        tableDetailForm += ' <div class="ln_solid"></div>'
                                                                        // submit button
                                                                        tableDetailForm += ' <div class="form-group">'
                                                                        tableDetailForm += '   <div class="col-md-offset-2 col-sm-offset-2 col-md-4 col-sm-4 col-xs-8">'
                                                                        tableDetailForm += '      <button id="dataTable-submit" type="button" class="btn btn-success">保存</button>'
                                                                        tableDetailForm += '   </div>'
                                                                        tableDetailForm += ' </div>'

                                                                        tableDetailForm += '<input type="hidden" name="dataInstanceId" value="' + dataInstanceId + '"/>';
                                                                        tableDetailForm += '<input type="hidden" name="schemaName" value="' + schemaName + '"/>';
                                                                        tableDetailForm += '<input type="hidden" name="tableName" value="' + tableName + '"/>';
                                                                        tableDetailForm += '<input type="hidden" name="id" id="dataTable-id"/>';
                                                                        tableDetailForm += '</form>'
                                                                        var $dataInstanceTable = $("#dataInstance-table-detail");
                                                                        $dataInstanceTable.empty().html(tableDetailForm);

                                                                        var dataInstance = $("#dataInstance-form").data("dataInstance");
                                                                        if (dataInstance && data.result.id) {
                                                                            var dataTables = dataInstance.dataTables;
                                                                            for (var i in dataTables) {
                                                                                if (dataTables[i].id == data.result.id) {
                                                                                    data.result.columns = dataTables[i].columns.split(",");
                                                                                }
                                                                            }
                                                                        }
                                                                        // load data into form fields when update
                                                                        data.result.id && $dataInstanceTable.loadData(data.result);

                                                                        // bind submitting form event
                                                                        $("#dataTable-submit").off().on("click", function () {
                                                                            var checkboxList = $("#dataTable-form :checkbox").fieldValue();
                                                                            // validate the length of checked checkboxes
                                                                            if (checkboxList && checkboxList.length > 0) {
                                                                                // when create
                                                                                var hasPrimaryKey = false;
                                                                                if (!$("#dataTable-id").val()) {
                                                                                    var primaryKeys = data.result.primaryKeys;
                                                                                    for (var i in checkboxList) {
                                                                                        if (!hasPrimaryKey && primaryKeys.indexOf(checkboxList[i]) > -1) {
                                                                                            hasPrimaryKey = true;
                                                                                        }
                                                                                    }
                                                                                }

                                                                                if (!hasPrimaryKey) {
                                                                                    main.messageAlert("勾选的列没有主键，继续？", wizard.__addTable);
                                                                                } else {
                                                                                    wizard.__addTable();
                                                                                }
                                                                            } else {
                                                                                main.messageAlert("请勾选需要过滤的列");
                                                                            }
                                                                        })
                                                                    } else {
                                                                        main.messageAlert("加载table详情失败");
                                                                    }
                                                                })
                                                            }
                                                        })
                                                } else {
                                                    main.messageAlert("加载table列表失败");
                                                }
                                            })
                                        }

                                        if ($li.is('.active')) {
                                            $li.removeClass('active active-sm');
                                            $('ul:first', $li).slideUp();
                                        } else {
                                            var $schemaList = $("#dataInstance-schemaList");
                                            // prevent closing menu if we are on child menu
                                            if (!$li.parent().is('.child_menu')) {
                                                $schemaList.find('li').removeClass('active active-sm');
                                                $schemaList.find('li ul').slideUp();
                                            }
                                            $li.addClass('active');

                                            $('ul:first', $li).slideDown();
                                        }
                                    });
                                })
                        } else {
                            main.messageAlert("加载schema列表失败");
                        }
                    })
                },
                __addTable: function () {
                    var data = {
                        columns: $("#dataTable-form :checkbox").fieldValue().join(","),
                        dataInstanceId: $("#dataTable-form input[name='dataInstanceId']").val(),
                        schemaName: $("#dataTable-form input[name='schemaName']").val(),
                        tableName: $("#dataTable-form input[name='tableName']").val(),
                        id: $("#dataTable-form input[name='id']").val()
                    }
                    $("#dataTable-form").ajaxSubmit({
                        url: "addTable",
                        dataType: "json",
                        type: "POST",
                        data: data,
                        success: function (data) {
                            if (data.responseStatus == 200) {
                                var $dataTableId = $("#dataTable-id");
                                !$dataTableId.val() && $dataTableId.val(data.result);
                            } else {
                                main.messageAlert("添加table失败");
                            }
                        }
                    })
                },
                __step1: function () {
                    // validate
                    if (!validator.checkAll($("#dataInstance-form"))) {
                        return false;
                    }

                    var success = false;
                    $("#dataInstance-form").ajaxSubmit({
                        url: "add",
                        type: "POST",
                        async: false,
                        dataType: "json",
                        success: function (data) {
                            if (data.responseStatus == 200) {
                                success = true;
                                $("#dataInstance-id").val(data.result);
                                wizard._beforeStep2Callback(data.result);
                            } else {
                                main.messageAlert("添加dataInstance失败")
                            }
                        },
                    })
                    return success;
                },
                __renderSelectOptions: function (data) {
                    var options = "";
                    if ($.isArray(data) && data.length > 0) {
                        options += '<option value="*">全部</option>';
                        for (var i in data) {
                            options += '<option value="' + data[i] + '">' + data[i] + '</option>'
                        }
                    }

                    $("#dataOutputMapping-schemaName").empty().html(options);
                },
                __step2: function () {
                    // set dataInstanceId
                    var dataInstanceId = $("#dataInstance-id").val();
                    $("#dataOutputMapping-dataInstanceId").val(dataInstanceId);
                    // render select options
                    $.ajax({
                        url: "./schema",
                        data: {id: $("#dataInstance-id").val(), filter: true},
                        dataType: "json",
                    }).then(function (data) {
                        if (data.responseStatus == 200) {
                            wizard.__renderSelectOptions(data.result);
                        } else {
                            main.messageAlert("渲染schema列表失败")
                        }
                    })

                    var dataTable = $('#dataOutputMappingTable').dataTable();
                    dataTable.fnSettings().ajax.data = {dataInstanceId: dataInstanceId};
                    dataTable.api().ajax.reload(null, false);
                    return true;
                },
                __step3: function () {
                    return true;
                },
                eventBind: function () {
                    $("#dataOutputMapping-submit").on("click", function () {
                        var $dataOutputMapping = $("#dataOutputMapping-form");
                        if (!validator.checkAll($dataOutputMapping)) {
                            return false;
                        }

                        var dataSourceOutputId = $("#dataOutputMapping-dataSourceOutputId").val();
                        if (!dataSourceOutputId) {
                            main.messageAlert("请选择输出数据源");
                            return false;
                        }

                        var dataInstanceId = $("#dataOutputMapping-dataInstanceId").val();
                        if (!dataInstanceId) {
                            main.messageAlert('数据实例已失效');
                            return false;
                        }

                        $dataOutputMapping.ajaxSubmit({
                            url: "./addMapping",
                            dataType: "json",
                            type: "POST",
                            success: function (data) {
                                if (data.responseStatus == 200) {
                                    $("#dataOutputMapping-id").val(data.result);
                                    $("#dataOutputMappingTable").dataTable().api().ajax.reload(null, false);
                                    // 重新渲染schemaList
                                    $.ajax({
                                        url: "./schema",
                                        data: {id: $("#dataInstance-id").val(), filter: true},
                                        dataType: "json",
                                    }).then(function (data) {
                                        if (data.responseStatus == 200) {
                                            wizard.__renderSelectOptions(data.result);
                                        } else {
                                            main.messageAlert("渲染schema列表失败")
                                        }
                                    })
                                } else {
                                    main.messageAlert("添加dataOutputMapping失败");
                                }
                            }
                        })
                    })
                },
                _onFinishCallback: function () {
                    $("#dataInstanceModal").modal("hide");
                    $("#dataInstanceTable").dataTable().api().ajax.reload(null, false);
                    return true;
                }
            };
            wizard.init();
        },
        initDataOutputMappingTable: function () {
            var Table = {
                init: function () {
                    var id = "dataOutputMappingTable";
                    var table = Table._initTable(id);
                    Table.eventBind(id, table);
                },
                _initTable: function (id) {
                    var table = $("#" + id).DataTable({
                        searching: false,
                        ordering: false,
                        lengthChange: false,
                        serverSide: true,
                        pageLength: 5,
                        ajax: {
                            "url": "./listMapping",
                            "contentType": "application/x-www-form-urlencoded",
                            "type": "POST",
                            "data": {dataInstanceId: -1}
                        },
                        columns: [
                            {
                                data: "schemaName", title: "schemaName", render: function (data) {
                                return data == "*" ? "全部" : data;
                            }
                            },
                            {data: "topic", title: "topic"},
                            {
                                data: "dataSourceOutput.id",
                                title: "dataSourceOutput.id",
                                render: function (data, type, full, meta) {
                                    return full.dataSourceOutputId;
                                }
                            },
                            {
                                data: "dataSourceOutput.type",
                                title: "dataSourceOutput.type",
                                render: function (data, type, full, meta) {
                                    if (full.dataSourceOutput) {
                                        switch (full.dataSourceOutput.type) {
                                            case 20 :
                                                return "kafka";
                                        }
                                    } else {
                                        return "-"
                                    }
                                }
                            },
                            {
                                data: "dataSourceOutput.options",
                                title: "dataSourceOutput.options",
                                render: function (data, type, full, meta) {
                                    return full.dataSourceOutput ? full.dataSourceOutput.options : '-';
                                }
                            }, {
                                data: null,
                                title: "操作",
                                render: function () {
                                    return "<span class='label label-info dataOutputMapping-delete' style='font-weight: 100'>删除</span>"
                                }
                            }
                        ],
                        language: {
                            "decimal": "",
                            "emptyTable": "暂无数据",
                            "info": "显示第 _START_ 条到第 _END_ 条，共 _TOTAL_ 条（每页 20 条）",
                            "infoEmpty": "第1页/共0页",
                            "infoFiltered": "",
                            "infoPostFix": "",
                            "thousands": ",",
                            "loadingRecords": "Loading...",
                            "processing": "Processing...",
                            "paginate": {
                                "first": "首页",
                                "last": "尾页",
                                "next": "下一页",
                                "previous": "上一页"
                            },
                        }

                    })

                    return table;
                },
                eventBind: function (id, table) {
                    // 搜索
                    // $("#dataNodeConfiguration-searchForm-submit").on("click", function () {
                    //     var arr = $("#dataInstance-searchForm").serializeArray();
                    //     var data = {};
                    //     for (var i in arr) {
                    //         data[arr[i].name] = arr[i].value;
                    //     }
                    //     var dataTable = $('#' + id).dataTable();
                    //     dataTable.fnSettings().ajax.data = data;
                    //     dataTable.api().ajax.reload(null, false);
                    // })

                    // 绑定选中
                    var $tbody = $('#' + id + ' tbody');
                    $tbody.on('click', 'tr', function () {
                        if ($(this).hasClass('selected')) {
                            $(this).removeClass('selected');
                        }
                        else {
                            table.$('tr.selected').removeClass('selected');
                            $(this).addClass('selected');
                        }
                    });

                    // 删除
                    $tbody.on("click", ".dataOutputMapping-delete", function () {
                        var $tr = $(this).closest('tr');
                        var data = table.row($tr).data();
                        $.ajax({
                            url: "./deleteMapping",
                            data: {id: data.id},
                            dataType: 'json',
                            type: "POST"
                        }).then(function (data) {
                            if (data.responseStatus == 200) {
                                table.ajax.reload(null, false);
                            } else {
                                main.messageAlert("删除dataOutputMapping失败")
                            }
                        })
                    })
                }
            }

            Table.init();
        },
        initDataInstanceTable: function () {
            var Table = {
                init: function () {
                    var id = "dataInstanceTable";
                    var table = Table._initTable(id);
                    Table._removeTheadClass(id);
                    Table.eventBind(id, table);
                },
                _removeTheadClass: function (id) {
                    $("#" + id + " thead>tr>th").first().removeClass("fa fa-plus");
                },
                _initTable: function (id) {
                    var table = $("#" + id).DataTable({
                        searching: false,
                        ordering: false,
                        lengthChange: false,
                        serverSide: true,
                        pageLength: 20,
                        ajax: {
                            "url": "./list",
                            "contentType": "application/x-www-form-urlencoded",
                            "type": "POST",
                        },
                        columns: [
                            {
                                className: 'fa fa-plus details-control',
                                orderable: false,
                                data: null,
                                defaultContent: '',
                                width: "7px"
                            },
                            {data: "name", title: "name"},
                            {data: "host", title: "host"},
                            {data: "port", title: "port"},
                            {
                                data: "type", title: "type", "render": function (data, type, full, meta) {
                                if (data == 1) {
                                    return "MySQL"
                                }
                            }
                            },
                            {data: "nodePath", title: "nodePath"},
                            {
                                className: "dataInstance-status",
                                data: "status", title: "status", render: function (data) {
                                if (data == -1) {
                                    return "创建中"
                                } else if (data == 0) {
                                    return "已创建"
                                } else if (data == 1) {
                                    return "已启动"
                                } else if (data == 2) {
                                    return "已关停"
                                }
                            }
                            }, {
                                data: null,
                                title: "操作",
                                render: function (data, type, full) {
                                    switch (full.status) {
                                        case -1:
                                            return "-";
                                        case 0 :
                                        case 2:
                                            return "<span class='label label-primary dataInstance-start' style='font-weight: 100'>启动</span>"
                                        case 1 :
                                            return "<span class='label label-warning dataInstance-stop' style='font-weight: 100'>关停</span>"
                                        default :
                                            return "未知";
                                    }
                                }
                            }
                        ],
                        language: {
                            "decimal": "",
                            "emptyTable": "暂无数据",
                            "info": "显示第 _START_ 条到第 _END_ 条，共 _TOTAL_ 条（每页 20 条）",
                            "infoEmpty": "第1页/共0页",
                            "infoFiltered": "",
                            "infoPostFix": "",
                            "thousands": ",",
                            "loadingRecords": "Loading...",
                            "processing": "Processing...",
                            "paginate": {
                                "first": "首页",
                                "last": "尾页",
                                "next": "下一页",
                                "previous": "上一页"
                            },
                        }

                    })

                    return table;
                },
                eventBind: function (id, table) {
                    // 绑定展开
                    var $tbody = $('#' + id + ' tbody');
                    $tbody.on('click', 'td.details-control', function () {
                        var $td = $(this);
                        var tr = $td.closest('tr');
                        var row = table.row(tr);

                        if (row.child.isShown()) {
                            row.child.hide();
                            $td.removeClass('fa-minus').addClass("fa-plus");
                        }
                        else {
                            row.child(Table._format(row.data())).show();
                            $td.addClass('fa-minus').removeClass("fa-plus");
                        }
                    });

                    // 启动
                    $tbody.on("click", "td>span.dataInstance-start", function () {
                        var $button = $(this);
                        var $tr = $button.closest('tr');
                        var row = table.row($tr);
                        $.ajax({
                            url: "./start",
                            dataType: "json",
                            type: "POST",
                            data: {id: row.data().id}
                        }).then(function (data) {
                            if (data.responseStatus == 200) {
                                $("#dataInstanceTable").dataTable().api().ajax.reload(null, false);
                                main.messageAlert("启动dataInstance成功")
                            } else {
                                main.messageAlert("启动dataInstance失败")
                            }
                        })
                    })

                    // 关停
                    $tbody.on("click", "td>span.dataInstance-stop", function () {
                        var $button = $(this);
                        var $tr = $button.closest('tr');
                        var row = table.row($tr);
                        $.ajax({
                            url: "./stop",
                            dataType: "json",
                            type: "POST",
                            data: {id: row.data().id}
                        }).then(function (data) {
                            if (data.responseStatus == 200) {
                                $("#dataInstanceTable").dataTable().api().ajax.reload(null, false);
                                main.messageAlert("关停dataInstance成功")
                            } else {
                                main.messageAlert("关停dataInstance失败")
                            }
                        })
                    })

                    // 搜索
                    $("#dataInstance-searchForm-submit").on("click", function () {
                        var arr = $("#dataInstance-searchForm").serializeArray();
                        var data = {};
                        for (var i in arr) {
                            data[arr[i].name] = arr[i].value;
                        }
                        var dataTable = $('#' + id).dataTable();
                        dataTable.fnSettings().ajax.data = data;
                        dataTable.api().ajax.reload(null, false);
                    })

                    // 绑定选中
                    $tbody.on('click', 'tr', function () {
                        if ($(this).hasClass('selected')) {
                            $(this).removeClass('selected');
                        }
                        else {
                            table.$('tr.selected').removeClass('selected');
                            $(this).addClass('selected');
                        }
                    });

                    // 添加
                    $("#add").on("click", function () {
                        $("#dataInstanceModal").modal("show")
                    })
                    // 编辑
                    $("#edit").on("click", function () {
                        var data = table.row(".selected").data();
                        if (data) {
                            // load data
                            $.ajax({
                                url: "get",
                                data: {id: data.id},
                                dataType: "json"
                            }).then(function (d) {
                                var $dataInstance = $("#dataInstance-form");
                                $dataInstance.removeData().data("dataInstance", d.result);
                                $dataInstance.loadData(d.result);
                            })
                            $("#dataInstanceModal").modal("show")
                        } else {
                            main.messageAlert("请选中一条数据")
                        }

                    })

                    // 删除
                    $("#delete").on("click", function () {
                        var data = table.row(".selected").data();
                        if (data.status == 1) { // 正在运行的不能被删除
                            main.messageAlert("正在运行的dataInstance不能被删除")
                        } else {
                            $.ajax({
                                url: "delete",
                                data: {ids: [data.id]},
                                dataType: 'json',
                                type: "POST"
                            }).then(function (data) {
                                if (data.responseStatus == 200) {
                                    table.ajax.reload(null, false);
                                } else {
                                    main.messageAlert("删除dataInstance失败")
                                }
                            })
                        }
                    })
                },
                _format: function (rowData) {
                    var div = $('<div/>')
                        .addClass('loading')
                        .text('Loading...');

                    $.ajax({
                        url: './get',
                        data: {
                            id: rowData.id
                        },
                        dataType: 'json',
                        success: function (json) {
                            div
                                .html(json.toString())
                                .removeClass('loading');
                        }
                    });

                    return div;
                }
            };

            Table.init();
        },
        initDataSourceOutputMapping: function () {
            var Table = {
                init: function () {
                    var id = "dataSourceOutputTable";
                    var table = Table._initTable(id);
                    Table.eventBind(id, table);
                },
                _initTable: function (id) {
                    var table = $("#" + id).DataTable({
                        searching: false,
                        ordering: false,
                        lengthChange: false,
                        serverSide: true,
                        pageLength: 5,
                        ajax: {
                            "url": "../dataSourceOutput/list",
                            "contentType": "application/x-www-form-urlencoded",
                            "type": "POST",
                        },
                        columns: [
                            {data: "username", title: "username", width: '20%'},
                            {
                                data: "type", title: "type", width: '10%', render: function (data) {
                                if (data == 20) {
                                    return "Kafka"
                                }
                            }
                            },
                            {data: "options", title: "options"},

                        ],
                        language: {
                            "decimal": "",
                            "emptyTable": "暂无数据",
                            "info": "显示第 _START_ 条到第 _END_ 条，共 _TOTAL_ 条（每页 20 条）",
                            "infoEmpty": "第1页/共0页",
                            "infoFiltered": "",
                            "infoPostFix": "",
                            "thousands": ",",
                            "loadingRecords": "Loading...",
                            "processing": "Processing...",
                            "paginate": {
                                "first": "首页",
                                "last": "尾页",
                                "next": "下一页",
                                "previous": "上一页"
                            },
                        }

                    })
                    return table;
                },
                eventBind: function (id, table) {
                    var $tbody = $('#' + id + ' tbody');
                    // 绑定选中
                    $tbody.on('click', 'tr', function () {
                        if ($(this).hasClass('selected')) {
                            $(this).removeClass('selected');
                        }
                        else {
                            table.$('tr.selected').removeClass('selected');
                            var $this = $(this);
                            $this.addClass('selected');
                            var data = table.row($this).data();
                            $("#dataOutputMapping-dataSourceOutputId").val(data.id)
                        }
                    });
                },
            };

            Table.init();
        }
    };

    main.init();

})($);