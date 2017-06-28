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
            this.initForm();
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
        initForm: function () {
            $.ajax({
                url: "./get",
                dataType: "json"
            }).then(function (data) {
                if (data.responseStatus == 200) {
                    if (!$.isEmptyObject(data.result)) {
                        $("#dataNodeConfiguration-form").loadData(data.result);
                    }
                } else {
                    main.messageAlert("加载dataNodeConfiguration失败")
                }
            })
        },
        messageAlert: function (message, callback) {
            $("#alertMessage").modal('show');
            $("#alert-content").empty().html(message);
            if (callback) {
                $("#alert-submit").show().off('click')().on("click", function () {
                    callback();
                    $("#alertMessage").modal('hide');
                })
            } else {
                $("#alert-submit").hide().off('click')();
            }
        },
        eventBind: function () {
            $("#dataNodeConfiguration-submit").on("click", function () {
                var $dataNodeConfiguration = $("#dataNodeConfiguration-form");
                if (!validator.checkAll($dataNodeConfiguration)) {
                    return false;
                }

                // validate options
                var options = $("#dataNodeConfiguration-options").val();
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

                $dataNodeConfiguration.ajaxSubmit({
                    url: "./add",
                    dataType: "json",
                    type: "POST",
                    success: function (data) {
                        if (data.responseStatus == 200) {
                            $("#dataNodeConfiguration-id").val(data.result);
                        } else {
                            main.messageAlert("保存失败");
                        }
                    }
                })
            })

            $("#dataNodeConfiguration-clear").on("click", function () {
                var id = $("#dataNodeConfiguration-id").val();
                if (id) {
                    var $dataNodeConfiguration = $("#dataNodeConfiguration-form");
                    $dataNodeConfiguration.ajaxSubmit({
                        url: "./delete",
                        dataType: "json",
                        type: "POST",
                        success: function (data) {
                            if (data.responseStatus == 200) {
                                $dataNodeConfiguration.clearForm(true);
                            } else {
                                main.messageAlert("清空失败");
                            }
                        }
                    })
                }
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
    };

    main.init();

})($);