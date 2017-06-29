;(function ($) {
    var main = {
        init: function () {
            this.initNodeList();
            this.initScroll();
        },
        initNodeList: function () {
            $.ajax({
                url: "list",
                dataType: "json"
            }).then(function (data) {
                if (data.responseStatus == 200) {
                    var result = data.result;
                    var nodeList = "";
                    if (result.length > 0) {
                        for (var i in result) {
                            var nodePath = result[i];
                            var nodeAddress = nodePath.substring(nodePath.lastIndexOf("/") + 1);
                            nodeList +=
                                '<div class="col-sm-2" data-value="' + nodePath + '">' +
                                '<div class="x_panel ">' +
                                '   <div class="x_title">' +
                                '       <h2>' + nodeAddress + '</h2>' +
                                '       <div class="clearfix"></div>' +
                                '   </div>' +
                                // '<div class="x_content">' +
                                // '    <div style="text-align: center; margin-bottom: 17px">' +
                                // '       <span class="chart" data-percent="86">' +
                                // '       <span class="percent"></span>' +
                                // '       </span>' +
                                // '    </div>' +
                                // '</div>' +
                                '</div>' +
                                '</div>'
                        }
                    } else {
                        nodeList = "暂无注册node";
                    }
                    $("#nodeList").html(nodeList);
                    // render memory
                    // $("#nodeList>div").each(function () {
                    //     var $this = $(this);
                    //     var nodePath = $this.attr("data-value");
                    //     var nodeAddress = nodePath.substring(nodePath.lastIndexOf("/") + 1);
                    //     $.ajax({
                    //         url: "nodeMemory",
                    //         data: {nodeAddress: nodeAddress},
                    //         dataType: "json"
                    //     }).then(function (data) {
                    //         if (data.responseStatus == 200) {
                    //             $this.find('.chart')
                    //                 .data("percent", data.result)
                    //                 .easyPieChart({
                    //                     easing: 'easeOutElastic',
                    //                     delay: 0,
                    //                     barColor: '#26B99A',
                    //                     trackColor: '#fff',
                    //                     scaleColor: false,
                    //                     lineWidth: 20,
                    //                     trackWidth: 16,
                    //                     lineCap: 'butt',
                    //                     onStep: function (from, to, percent) {
                    //                         $(this.el).find('.percent').text(Math.round(percent));
                    //                     }
                    //                 });
                    //         }
                    //     })
                    // })
                    // bind event
                    $("#nodeList>div").click(function () {
                        var nodePath = $(this).attr("data-value");
                        $("#nodeHeader").html(nodePath);
                        $("#dataInstance").show();
                        if (!main.hasInitInstanceTable) {
                            main.initInstanceTable(nodePath);
                            main.hasInitInstanceTable = true;
                        } else {
                            var dataTable = $('#dataInstanceTable').dataTable();
                            dataTable.fnSettings().ajax.data = {nodePath: nodePath};
                            dataTable.api().ajax.reload(null, false);
                        }
                    })

                    $("#nodeList>div:eq(0)").click();
                }
            })
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
                $("#alert-submit").show().off('click')().on("click", function () {
                    callback();
                })
            } else {
                $("#alert-submit").hide().off('click')();
            }
        },
        hasInitInstanceTable: false,
        initInstanceTable: function (nodePath) {
            var Table = {
                init: function (nodePath) {
                    var id = "dataInstanceTable";
                    var table = Table._initTable(id, nodePath);
                    Table._removeTheadClass(id);
                    Table.eventBind(id, table);
                },
                _removeTheadClass: function (id) {
                    $("#" + id + " thead>tr>th").first().removeClass("fa fa-plus");
                },
                _initTable: function (id, nodePath) {
                    var table = $("#" + id).DataTable({
                        searching: false,
                        ordering: false,
                        lengthChange: false,
                        serverSide: true,
                        pageLength: 20,
                        ajax: {
                            "url": "./instanceList",
                            "contentType": "application/x-www-form-urlencoded",
                            "type": "POST",
                            "data": {nodePath: nodePath}
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
                            },
                            {
                                data: null,
                                title: "操作",
                                render: function (data) {
                                    return "<span class='label label-info dataInstance-log' style='font-weight: 100'>日志</span>"
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

                    // 日志
                    $tbody.on("click", ".dataInstance-log", function () {
                        var $tr = $(this).closest('tr');
                        var data = table.row($tr).data();
                        var id = "dataLogTable";
                        if (!Table.hasInitLogTable) {
                            $('#logList').show();
                            Table._initLogTable(id, data.name);
                            Table.hasInitLogTable = true;
                        } else {
                            var dataTable = $('#dataLogTable').dataTable();
                            dataTable.fnSettings().ajax.data = {instanceName: data.name};
                            dataTable.api().ajax.reload(null, false);
                        }
                    })
                },
                _format: function (rowData) {
                    var div = $('<div/>')
                        .addClass('loading')
                        .text('Loading...');

                    var nodePath = $("#nodeHeader").html();
                    var nodeAddress = nodePath.substring(nodePath.lastIndexOf("/") + 1);
                    $.ajax({
                        url: './instanceDetail',
                        data: {
                            nodeAddress: nodeAddress,
                            instanceName: rowData.name
                        },
                        dataType: 'json',
                        success: function (json) {
                            if (json.responseStatus == 200) {
                                div
                                    .html(JSON.stringify(json.result))
                                    .removeClass('loading');
                            } else {
                                main.messageAlert("加载详情失败");
                            }
                        }
                    });

                    return div;
                },
                hasInitLogTable: false,
                _initLogTable: function (id, instanceName) {
                    var table = $("#" + id).DataTable({
                        searching: false,
                        ordering: false,
                        lengthChange: false,
                        serverSide: true,
                        pageLength: 20,
                        ajax: {
                            "url": "./logList",
                            "contentType": "application/x-www-form-urlencoded",
                            "type": "POST",
                            "data": {instanceName: instanceName}
                        },
                        columns: [
                            {
                                data: "createTime",
                                title: "日期",
                                render: function (data) {
                                    var date = new Date(data);
                                    var month = (date.getMonth() + 1) + "";
                                    var hours = date.getHours() + "";
                                    var minutes = date.getMinutes() + "";
                                    var seconds = date.getSeconds() + "";
                                    return date.getFullYear() + "-" +
                                        (month.length > 1 ? month : "0" + month) + "-" +
                                        date.getDate() + " " +
                                        (hours.length > 1 ? hours : "0" + hours) + ":" +
                                        (minutes.length > 1 ? minutes : "0" + minutes) + ":" +
                                        (seconds.length > 1 ? seconds : "0" + seconds);
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

                    });

                    // bind event
                    var $tbody = $('#' + id + ' tbody');
                    // 绑定选中
                    $tbody.on('click', 'tr', function () {
                        if ($(this).hasClass('selected')) {
                            $(this).removeClass('selected');
                        }
                        else {
                            table.$('tr.selected').removeClass('selected');
                            $(this).addClass('selected');
                            var data = table.row($(this)).data();
                            $.ajax({
                                url: "logDetail",
                                data: {logId: data.id},
                                dataType: "json"
                            }).then(function (data) {
                                if (data.responseStatus == 200) {
                                    $("#logDetail").empty().html(data.result.message)
                                } else {
                                    main.messageAlert("获取日志详情失败")
                                }
                            })
                        }
                    });

                    return table;
                },
            };

            Table.init(nodePath);
        }
    };

    main.init();

})($);