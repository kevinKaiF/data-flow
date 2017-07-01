;(function ($) {
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
                $("#alert-submit").show().off('click').on("click", function () {
                    callback();
                    $("#alertMessage").modal('hide');
                })
            } else {
                $("#alert-submit").hide().off('click');
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
            })
        },
        parseDataSourceOutputType: function (type) {
            switch (type) {
                case 10:
                    return "MySQL";
                case 11:
                    return "Oracle";
                case 12 :
                    return "PostGreSQL";
                case 13 :
                    return "SQLServer";
                case 20 :
                    return "kafka";
                case 21 :
                    return "metaQ";
                case 22 :
                    return "rabbitMQ"
                default :
                    return '-';
            }
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
                            default :
                                return true;
                        }
                    }
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
                            } else {
                                main.messageAlert("添加dataInstance失败")
                            }
                        },
                    })

                    // set dataInstanceId
                    var dataInstanceId = $("#dataInstance-id").val();
                    $("#dataOutputMapping-dataInstanceId").val(dataInstanceId);
                    var dataTable = $('#dataOutputMappingTable').dataTable();
                    dataTable.fnSettings().ajax.data = {dataInstanceId: dataInstanceId};
                    dataTable.api().ajax.reload(null, false);
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
                            data : {schemaName : "*", topic : "-1"},
                            success: function (data) {
                                if (data.responseStatus == 200) {
                                    main.messageAlert("添加dataOutputMapping成功");
                                    $("#dataOutputMapping-id").val(data.result);
                                    $("#dataOutputMappingTable").dataTable().api().ajax.reload(null, false);
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
                                        return main.parseDataSourceOutputType(full.dataSourceOutput.type)
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
                            "data" : {producerOrConsumer:1}
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
                            {data: "tag", title: "tag"},
                            {
                                data: "type", title: "type", "render": function (data, type, full, meta) {
                                return main.parseDataSourceOutputType(data);
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
                            tr.next().children().eq(0).css("background", "white").off("click");
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
                        var data = {producerOrConsumer:1};
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
                        $("#dataInstance-name").val(-1);
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
                    var div = $('<div style="background: white"></div>')
                        .addClass('loading')
                        .text('Loading...');

                    $.ajax({
                        url: './get',
                        data: {
                            id: rowData.id
                        },
                        dataType: 'json',
                        success: function (json) {
                            var result = json.result;
                            // render dataInstance [Form]
                            var dataInstanceProperties = {
                                "name": "input",
                                "options": "textarea",
                                "transformScript": "textarea",
                            };
                            var dataInstanceForm = '<div class="container">' +
                                '<span class="section">实例详情</span>' +
                                '<form class="form-horizontal form-label-left col-sm-12">';
                            for (var i in dataInstanceProperties) {
                                var prop = i;
                                var type = dataInstanceProperties[i];
                                if (result[prop]) {
                                    var value = result[prop];
                                    dataInstanceForm += '<div class="item form-group col-sm-12" style="margin-bottom: 10px;;">'
                                        + '<label class="control-label col-md-2"> ' + prop + ' </label>'
                                        + '<div class="col-md-8">';
                                    if (type == "input") {
                                        dataInstanceForm += '<input class="form-control" readonly style="width: 100%;background: white" value="' + value + '"/>'
                                    } else if (type == "textarea") {
                                        dataInstanceForm += '<textarea class="form-control" readonly style="width: 100%;background: white" rows="8">' + value + ' </textarea>'
                                    }

                                    dataInstanceForm += '</div>'
                                        + '</div>';
                                }
                            }
                            dataInstanceForm += '</form></div>';

                            // render dataTable [Table]
                            var dataTable = '';
                            if (result["dataTables"] && result["dataTables"].length > 0) {
                                dataTable +=
                                    '<div class="container">' +
                                    '<span class="section">过滤表</span>' +
                                    '<div class="col-sm-12">' +
                                    '<table class="table table-hover dataTable no-footer" style="width: 100%;" role="grid">' +
                                    '<thead><tr role="row">' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">schemaName</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">tableName</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 80%;">columns</th>' +
                                    '</tr>' +
                                    '</thead>' +
                                    '<tbody>';
                                for (var i in result["dataTables"]) {
                                    var row = result["dataTables"][i];
                                    dataTable +=
                                        '<tr role="row">' +
                                        '<td>' + row.schemaName + '</td>' +
                                        '<td>' + row.tableName + '</td>' +
                                        '<td>' + row.columns + '</td>' +
                                        '</tr>'
                                }
                                dataTable +=
                                    '</tbody>' +
                                    '</table>' +
                                    '</div>' +
                                    '</div>';
                            }

                            // render dataOutputMapping [Table]
                            var dataOutputMappings = '';
                            if (result["dataOutputMappings"] && result["dataOutputMappings"].length > 0) {
                                dataOutputMappings +=
                                    '<div class="container">' +
                                    '<span class="section">输出映射</span>' +
                                    '<div class="col-sm-12">' +
                                    '<table class="table table-hover dataTable no-footer" style="width: 100%;" role="grid">' +
                                    '<thead><tr role="row">' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">schemaName</th>' +
                                    // '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">topic</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">dataSourceOutputId</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">type</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 70%;">options</th>' +
                                    '</tr>' +
                                    '</thead>' +
                                    '<tbody>';
                                for (var i in result["dataOutputMappings"]) {
                                    var row = result["dataOutputMappings"][i];
                                    dataOutputMappings +=
                                        '<tr role="row">' +
                                        '<td>' + row.schemaName + '</td>' +
                                        // '<td>' + row.topic + '</td>' +
                                        '<td>' + row.dataSourceOutput.id + '</td>' +
                                        '<td>' + main.parseDataSourceOutputType(row.dataSourceOutput.type) + '</td>' +
                                        '<td>' + row.dataSourceOutput.options + '</td>' +
                                        '</tr>'
                                }
                                dataOutputMappings +=
                                    '</tbody>' +
                                    '</table>' +
                                    '</div>' +
                                    '</div>';
                            }

                            var html = dataInstanceForm + dataTable + dataOutputMappings;
                            div
                                .html(html)
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