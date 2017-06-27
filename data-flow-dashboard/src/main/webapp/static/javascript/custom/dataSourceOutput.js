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
                })
            } else {
                $("#alert-submit").hide().off();
            }
        },
        eventBind: function () {
            $("#dataSourceOutputModal").on("hidden.bs.modal", function () {
                $("#dataSourceOutput-form").clearForm(true);
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
                        var options = $("#dataSourceOutput-options").val();
                        if (options) {
                            try {
                                var json = JSON.parse(options);
                                if ($.isEmptyObject(json)) {
                                    main.messageAlert("options不能为空JSON")
                                    return false;
                                }
                            } catch (e) {
                                main.messageAlert("options必须是JSON格式")
                                return false;
                            }
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