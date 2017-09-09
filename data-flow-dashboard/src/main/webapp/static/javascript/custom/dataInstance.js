;(function ($) {
    var main = {
        init: function () {
            this.initDataInstanceTable();
            this.eventBind();
            this.initValidation();
            this.initDataOutputMappingTable();
            this.initDataSourceOutputTable();
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
        showLoading: function () {
            $("#loading").show();
        },
        hideLoading: function () {
            $("#loading").hide();
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

            // the document for DataInstance's options
            $("#dataInstance-options-doc").on("click", function () {
                $("#dataInstance-options-modal").modal("show");
            })

            // the document for DataInstance's transformScript
            $("#dataInstance-transformScript-doc").on("click", function () {
                $("#dataInstance-transformScript-modal").modal("show");
            })

            // the document for DataOutputMapping's options
            $("#dataOutputMapping-options-doc").on("click", function () {
                $("#dataOutputMapping-options-modal").modal("show");
            })

            // the document for DataOutputMapping's transformScript
            $("#dataOutputMapping-transformScript-doc").on("click", function () {
                $("#dataOutputMapping-transformScript-modal").modal("show");
            })

            // collapse the search panel
            $("#search_panel_header").on("click", function () {
                $(this).find(".collapse-link").click();
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
                    return "Kafka";
                case 21 :
                    return "metaQ";
                case 22 :
                    return "rabbitMQ"
                case 23 :
                    return "activeMQ"
                case 30 :
                    return "ElasticSearch"
                case 31 :
                    return "hbase"
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
                                                                        tableDetailForm += '   <label class="control-label col-md-2 col-sm-2 col-xs-6" for="dataInstance-name">列名 <span class="required">*</span></label>'
                                                                        tableDetailForm += '    <div class="col-md-9 col-sm-9 col-xs-12">'
                                                                        // columns
                                                                        var columns = data.result.allColumns;
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
                                                                        tableDetailForm += '   <div class="col-md-offset-2 col-sm-offset-2 col-md-2 col-sm-2 col-xs-4">'
                                                                        tableDetailForm += '      <button id="dataTable-submit" type="button" class="btn btn-success">保存</button>'
                                                                        tableDetailForm += '   </div>'
                                                                        tableDetailForm += '   <div class="col-md-4 col-sm-4 col-xs-8"> '
                                                                        tableDetailForm += '      <button id="dataTable-cancel" type="button" class="btn btn-primary">清空</button>'
                                                                        tableDetailForm += '   </div>'
                                                                        tableDetailForm += ' </div>'

                                                                        tableDetailForm += '<input type="hidden" name="dataInstanceId" value="' + dataInstanceId + '"/>';
                                                                        tableDetailForm += '<input type="hidden" name="schemaName" value="' + schemaName + '"/>';
                                                                        tableDetailForm += '<input type="hidden" name="tableName" value="' + tableName + '"/>';
                                                                        tableDetailForm += '<input type="hidden" name="id" id="dataTable-id"/>';
                                                                        tableDetailForm += '</form>'
                                                                        var $dataInstanceTable = $("#dataInstance-table-detail");
                                                                        $dataInstanceTable.empty().html(tableDetailForm);

                                                                        // load form data
                                                                        delete data.result.allColumns;
                                                                        data.result.columns = data.result.columns.split(",")
                                                                        // load data into form fields when update
                                                                        data.result.id && $dataInstanceTable.loadData(data.result);

                                                                        // bind submitting form event
                                                                        $("#dataTable-submit").off('click').on("click", function () {
                                                                            var checkboxList = $("#dataTable-form :checkbox").fieldValue();
                                                                            // validate the length of checked checkboxes
                                                                            if (checkboxList && checkboxList.length > 0) {
                                                                                // when create
                                                                                var hasPrimaryKey = false;
                                                                                var primaryKeys = data.result.primaryKeys;
                                                                                for (var i in checkboxList) {
                                                                                    if (!hasPrimaryKey && primaryKeys.indexOf(checkboxList[i]) > -1) {
                                                                                        hasPrimaryKey = true;
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

                                                                        // bind deleting form event
                                                                        $("#dataTable-cancel").off('click').on('click', function () {
                                                                            var dataTableId = $("#dataTable-id").val();
                                                                            if (dataTableId) {
                                                                                $.ajax({
                                                                                    url: "deleteTable",
                                                                                    dataType: "json",
                                                                                    data: {id: dataTableId}
                                                                                }).then(function (data) {
                                                                                    if (data.responseStatus == 200) {
                                                                                        $("#dataTable-id").val('');
                                                                                        $("#dataTable-form").clearForm(false);
                                                                                    } else {
                                                                                        main.messageAlert("清除table失败");
                                                                                    }
                                                                                })
                                                                            } else {
                                                                                $("#dataTable-id").val('');
                                                                                $("#dataTable-form").clearForm(false);
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
                    $.ajax({
                        url: "addTable",
                        dataType: "json",
                        type: "POST",
                        data: data,
                        success: function (data) {
                            if (data.responseStatus == 200) {
                                var $dataTableId = $("#dataTable-id");
                                $dataTableId.val(data.result);
                                main.messageAlert("添加table成功");
                            } else {
                                main.messageAlert("添加table失败");
                            }
                        }
                    })
                },
                __isEmpty: function (data) {
                    if (typeof data === "undefined") {
                        return true;
                    } else {
                        return false;
                    }
                },
                __validateProperty: function (json, property) {
                    for (var i in property) {
                        if (wizard.__isEmpty(json[property[i]])) {
                            return false;
                        }
                    }
                    return true;
                },
                __validateDataInstanceOptions: function (type, options) {
                    try {
                        var json = JSON.parse(options);
                        switch (parseInt(type)) {
                            case 10 : // MySQL
                                var props = ["username", "password", "host", "port", "jdbcUrl", "slaveId"];
                                return wizard.__validateProperty(json, props);
                            case 11: // Oracle
                            case 12: // PostGreSQL
                            case 13: // SQLServer
                                var props = ["username", "password", "host", "port", "jdbcUrl"];
                                return wizard.__validateProperty(json, props);
                            case 20 : // Kafka
                                var props = ["bootstrap.servers", "topic"]
                                return wizard.__validateProperty(json, props);
                            case 21 : // metaQ
                                // TODO
                                return true;
                            case 22 : // rabbitMQ
                                return true;
                            case 23 : // activeMQ
                                var props = ["brokeUrl", "type"];
                                if (wizard.__validateProperty(json, props)) {
                                    // topic or queue
                                    if (json[props[1]] == 1) {  // topic
                                        return !wizard.__isEmpty(json["topic"])
                                    } else {                    // queue
                                        return !wizard.__isEmpty(json["queue"])
                                    }
                                } else {
                                    return false;
                                }
                            default :
                                return false;
                        }
                    } catch (e) {
                        console.error("配置非JSON格式", e);
                        return false;
                    }
                },
                __step1: function () {
                    // validate
                    if (!validator.checkAll($("#dataInstance-form"))) {
                        return false;
                    }

                    // validate the options property of DataInstance
                    var $dataInstanceOptions = $("#dataInstance-options");
                    var options = $dataInstanceOptions.val();
                    var type = $("#dataInstance-type").val();
                    if (!wizard.__validateDataInstanceOptions(type, options)) {
                        window.validator.mark($dataInstanceOptions, "配置非法")
                        return false;
                    } else {
                        window.validator.unmark($dataInstanceOptions)
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
                                if ($("#dataInstance-type").val() < 20) {
                                    wizard._beforeStep2Callback(data.result);
                                }
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
                    if ($("#dataInstance-type").val() < 20) {
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

                        $("#dataOutputMapping-schemaName-group").show();
                    } else {
                        $("#dataOutputMapping-schemaName-group").hide();
                        $("#dataOutputMapping-schemaName").empty().html("<option value='*' selected></option>");
                    }
                    var dataTable = $('#dataOutputMappingTable').dataTable();
                    dataTable.fnSettings().ajax.data = {dataInstanceId: dataInstanceId};
                    dataTable.api().ajax.reload(null, false);
                    return true;
                },
                __step3: function () {
                    return true;
                },
                __validateDataOutputMappingOptions: function (type, options) {
                    if (type < 20 || type == 30) {
                        return true;
                    }

                    try {
                        var json = JSON.parse(options);
                        switch (parseInt(type)) {
                            case 20 : // Kafka
                                var props = ["topic"]
                                return wizard.__validateProperty(json, props);
                            case 21 : // metaQ
                                // TODO
                                return true;
                            case 22 : // rabbitMQ
                                return true;
                            case 23 : // activeMQ
                                var props = ["type"];
                                if (wizard.__validateProperty(json, props)) {
                                    // topic or queue
                                    if (json[props[1]] == 1) {  // topic
                                        return !wizard.__isEmpty(json["topic"])
                                    } else {                    // queue
                                        return !wizard.__isEmpty(json["queue"])
                                    }
                                } else {
                                    return false;
                                }
                            default :
                                return false;
                        }
                    } catch (e) {
                        console.error("配置非JSON格式", e)
                        return false;
                    }
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

                        var type = $("#dataOutputMapping-dataSourceOutputType").val();
                        var $dataOutputMappingOptions = $("#dataOutputMapping-options");
                        var options = $dataOutputMappingOptions.val();
                        if (!wizard.__validateDataOutputMappingOptions(type, options)) {
                            window.validator.mark($dataOutputMappingOptions, "配置非法");
                            return false;
                        } else {
                            window.validator.unmark($dataOutputMappingOptions);
                        }

                        $dataOutputMapping.ajaxSubmit({
                            url: "./addMapping",
                            dataType: "json",
                            type: "POST",
                            success: function (data) {
                                if (data.responseStatus == 200) {
                                    main.messageAlert("添加dataOutputMapping成功");
                                    // $("#dataOutputMapping-id").val(data.result);
                                    $("#dataOutputMappingTable").dataTable().api().ajax.reload(null, false);
                                    // expand the first panel
                                    $("#headingOne").click()
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
                                data: "schemaName", title: "库名", width: "15%", render: function (data) {
                                return data == "*" ? "全部" : data;
                            }
                            },
                            {data: "options", title: "配置", width: "35%"},
                            {
                                data: "dataSourceOutput.id",
                                title: "输出源id",
                                width: "10%",
                                render: function (data, type, full, meta) {
                                    return full.dataSourceOutputId;
                                }
                            }, {
                                data: "dataSourceOutput.name",
                                title: "输出源名称",
                                width: "15%",
                                render: function (data, type, full, meta) {
                                    if (full.dataSourceOutput) {
                                        return full.dataSourceOutput.name
                                    } else {
                                        return "-"
                                    }
                                }
                            },
                            {
                                data: "dataSourceOutput.type",
                                title: "输出源类型",
                                width: "10%",
                                render: function (data, type, full, meta) {
                                    if (full.dataSourceOutput) {
                                        return main.parseDataSourceOutputType(full.dataSourceOutput.type)
                                    } else {
                                        return "-"
                                    }
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
                            {data: "name", title: "名称"},
                            {data: "tag", title: "标签"},
                            {
                                data: "producerOrConsumer", title: "模式", render: function (data) {
                                return data ? "消费者" : "生产者";
                            }
                            },
                            {
                                data: "type", title: "类型", "render": function (data, type, full, meta) {
                                return main.parseDataSourceOutputType(data);
                            }
                            },
                            {data: "nodePath", title: "节点路径"},
                            {
                                className: "dataInstance-status",
                                data: "status", title: "状态", render: function (data) {
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
                        main.showLoading();
                        var $button = $(this);
                        var $tr = $button.closest('tr');
                        var row = table.row($tr);
                        $.ajax({
                            url: "./start",
                            dataType: "json",
                            type: "POST",
                            data: {id: row.data().id},
                            complete: function () {
                                main.hideLoading();
                            }
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
                        main.showLoading();
                        var $button = $(this);
                        var $tr = $button.closest('tr');
                        var row = table.row($tr);
                        $.ajax({
                            url: "./stop",
                            dataType: "json",
                            type: "POST",
                            data: {id: row.data().id},
                            complete: function () {
                                main.hideLoading();
                            }
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
                        var data = {producerOrConsumer: 0};
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
                                    dataInstanceForm += '</div>';
                                }
                            }
                            dataInstanceForm += '</form></div>';

                            // render dataTable [Table]
                            var dataTable = '';
                            if (result["dataTables"] && result["dataTables"].length > 0) {
                                var dataInstanceId = result["id"]
                                dataTable +=
                                    '<div class="container" id="dataTableDetail_' + dataInstanceId + '">' +
                                    '<span class="section">过滤表</span>' +
                                    '<div class="col-sm-12">' +
                                    '<table class="table table-hover dataTable no-footer" style="width: 100%;" role="grid">' +
                                    '<thead><tr role="row">' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">库名</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">表名</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 70%;">列</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">操作</th>' +
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
                                        '<td><span class="label label-info dataTable-delete" data-instanceId="' + dataInstanceId + '" data-id="' + row.id + '" style="font-weight: 100">删除</span></td>' +
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
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">库名</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 15%;">配置</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 20%;">转换脚本</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">输出源id</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">输出源名称</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 10%;">输出源类型</th>' +
                                    '<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 25%;">输出源配置</th>' +
                                    '</tr>' +
                                    '</thead>' +
                                    '<tbody>';
                                for (var i in result["dataOutputMappings"]) {
                                    var row = result["dataOutputMappings"][i];
                                    dataOutputMappings +=
                                        '<tr role="row">' +
                                        '<td>' + row.schemaName + '</td>' +
                                        '<td>' + row.options + '</td>' +
                                        '<td>' + row.transformScript + '</td>' +
                                        '<td>' + row.dataSourceOutput.id + '</td>' +
                                        '<td>' + row.dataSourceOutput.name + '</td>' +
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
                            div.html(html).removeClass('loading');
                            // bind deleting dataTable
                            $(".dataTable-delete").each(function () {
                                var $this = $(this);
                                $this.off('click').on('click', function () {
                                    var dataTableId = $this.attr('data-id');
                                    var dataInstanceId = $this.attr('data-instanceId');
                                    $.ajax({
                                        url: "deleteTable",
                                        dataType: "json",
                                        data: {id: dataTableId}
                                    }).then(function (data) {
                                        if (data.responseStatus == 200) {
                                            var $tr = $this.parent().parent();
                                            var siblings = $tr.siblings();
                                            if (siblings && siblings.length == 0) {
                                                $("#dataTableDetail_" + dataInstanceId).remove();
                                            } else {
                                                $tr.remove();
                                            }
                                        } else {
                                            main.messageAlert("删除table失败")
                                        }
                                    })
                                })
                            })
                        }
                    });

                    return div;
                }
            };

            Table.init();
        },
        initDataSourceOutputTable: function () {
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
                            {
                                data: "type", title: "输出源类型", width: '25%', render: function (data) {
                                return main.parseDataSourceOutputType(data);
                            }
                            }, {
                                data: "name", title: "输出源名称", width: '25%'
                            },
                            {data: "options", title: "输出源配置"},

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
                            $("#dataOutputMapping-dataSourceOutputType").val(data.type)
                        }
                    });
                },
            };

            Table.init();
        }
    };

    main.init();

})($);