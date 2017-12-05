;(function ($) {
    var main = {
        init: function () {
            this.initDataInstanceTable();
            this.eventBind();
            this.initValidation();
        },
        messageAlert: function (message, callback) {
            $("#alertMessage").modal('show');
            $("#alert-content").empty().html(message);
            if (callback) {
                $("#alert-submit").show().off('click').on("click", function () {
                    callback();
                })
            } else {
                $("#alert-submit").hide().off('click');
            }
        },
        eventBind: function () {
            $("#dataSourceOutputModal").on("hidden.bs.modal", function () {
                $("#dataSourceOutput-form").clearForm(true);
            })

            // the reference for DataSourceOutput's options
            $("#dataSourceOutput-options-doc").on("click", function () {
                $("#dataSourceOutput-options-modal").modal("show");
            })

            // collapse the search panel
            $("#search_panel_header").on("click", function () {
                $(this).find(".collapse-link").click();
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
        initDataInstanceTable: function () {
            var Table = {
                init: function () {
                    var id = "dataSourceOutputTable";
                    var table = Table._initTable(id);
                    Table.eventBind(id, table);
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
                        if (Table.__isEmpty(json[property[i]])) {
                            return false;
                        }
                    }
                    return true;
                },
                __validateDataOutputOptions: function (type, options) {
                    try {
                        var json = JSON.parse(options);
                        switch (parseInt(type)) {
                            case 10: // MySQL
                            case 11: // Oracle
                            case 12: // PostGreSQL
                            case 13: // SQLServer
                            case 14: // Hive
                                var props = ["username", "password", "host", "port", "jdbcUrl"];
                                return Table.__validateProperty(json, props);
                            case 20 : // Kafka
                                var props = ["bootstrap.servers"]
                                return Table.__validateProperty(json, props);
                            case 21 : // MetaQ
                                // TODO
                                return true;
                            case 22 : // RabbitMQ
                                return true;
                            case 23 : // ActiveMQ
                                var props = ["brokeUrl"];
                                return Table.__validateProperty(json, props);
                            case 30 : // ElasticSearch
                                var props = ["cluster.name", "cluster.host"];
                                return Table.__validateProperty(json, props);
                            case 31 : // hbase
                                var props = ["hbase.zookeeper.property.clientPort", "hbase.zookeeper.quorum", "hbase.master"]
                                return Table.__validateProperty(json, props);
                            default :
                                return false;
                        }
                    } catch (e) {
                        console.error("配置非JSON格式", e);
                        return false;
                    }
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
                            // {
                            //     className: 'fa fa-plus details-control',
                            //     orderable: false,
                            //     data: null,
                            //     defaultContent: ''
                            // },
                            {data: "id", title: "ID"},
                            {data: "name", title: "名称"},
                            {
                                data: "type", title: "类型", width: '10%', render: function (data) {
                                switch (data) {
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
                                        return "MetaQ";
                                    case 22 :
                                        return "RabbitMQ"
                                    case 23 :
                                        return "ActiveMQ"
                                    case 30 :
                                        return "ElasticSearch"
                                    case 31 :
                                        return "hbase"
                                    default :
                                        return '-';
                                }
                            }
                            },
                            {data: "options", title: "配置"},

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
                    // $tbody.on('click', 'td.details-control', function () {
                    //     var $td = $(this);
                    //     var tr = $td.closest('tr');
                    //     var row = table.row(tr);
                    //
                    //     if (row.child.isShown()) {
                    //         row.child.hide();
                    //         $td.removeClass('fa-minus').addClass("fa-plus");
                    //     }
                    //     else {
                    //         row.child(Table._format(row.data())).show();
                    //         $td.addClass('fa-minus').removeClass("fa-plus");
                    //     }
                    // });

                    // 搜索
                    $("#dataSourceOutput-searchForm-submit").on("click", function () {
                        var arr = $("#dataSourceOutput-searchForm").serializeArray();
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

                    // 新增
                    $("#add").on("click", function () {
                        $("#dataSourceOutputModal").modal("show");
                    })

                    // 编辑
                    $("#edit").on("click", function () {
                        var data = table.row(".selected").data();
                        if (data) {
                            $("#dataSourceOutputModal").modal("show");
                            // load data
                            $("#dataSourceOutput-form").loadData(data);
                        } else {
                            main.messageAlert("请选中一条数据")
                        }
                    })

                    // 删除
                    $("#delete").on("click", function () {
                        var data = table.row(".selected").data();
                        if (data) {
                            $.ajax({
                                url: "delete",
                                data: {id: data.id},
                                dataType: 'json',
                                type: "POST"
                            }).then(function (data) {
                                if (data.responseStatus == 200) {
                                    table.ajax.reload(null, false);
                                } else {
                                    main.messageAlert("删除dataSourceOutput失败")
                                }
                            })
                        } else {
                            main.messageAlert("请选择一条数据");
                            return false;
                        }
                    })

                    // 提交
                    $("#dataSourceOutput-form-submit").on("click", function () {
                        if (!validator.checkAll($("#dataSourceOutput-form"))) {
                            return false;
                        }
                        // validate options
                        var $dataSourceOutputOptions = $("#dataSourceOutput-options");
                        var options = $dataSourceOutputOptions.val();
                        var type = $("#dataSourceOutput-type").val();
                        if (!Table.__validateDataOutputOptions(type, options)) {
                            window.validator.mark($dataSourceOutputOptions, "配置非法")
                            return false;
                        } else {
                            window.validator.unmark($dataSourceOutputOptions)
                        }

                        $("#dataSourceOutput-form").ajaxSubmit({
                            url: "./add",
                            dataType: "json",
                            type: "POST",
                            success: function (data) {
                                if (data.responseStatus == 200) {
                                    $("#dataSourceOutputModal").modal("hide");
                                    $("#dataSourceOutputTable").dataTable().api().ajax.reload(null, false);
                                } else {
                                    main.messageAlert("保存dataSourceOutput失败")
                                }
                            }
                        })
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
        }
    };

    main.init();

})($);