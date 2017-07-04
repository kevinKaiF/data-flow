;(function($) {
    var profile = {
        init: function () {
            profile.initNProgress();
            $(function () {
                profile.settingValidator();
                profile.init_sidebar();
                profile.initPanelEvent();
                profile.extension();
            })
        },
        settingValidator : function() {
          if (validator && validator.message) {
              validator.message.invalid='非法',
              validator.message.checked='请勾选',
              validator.message.empty='非空',
              validator.message.min='太短',
              validator.message.max='太长',
              validator.message.number_min='太小',
              validator.message.number_max='太大',
              validator.message.url='非法',
              validator.message.number='非数字',
              validator.message.email='非法',
              validator.message.email_repeat='重复',
              validator.message.password_repeat='不一致',
              validator.message.repeat='不一致',
              validator.message.complete='未完成',
              validator.message.select='请选择'
          }
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
                contentHeight -= $NAV_MENU.height() + footerHeight + 25;

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
        initPanelEvent : function () {
            $('.collapse-link').on('click', function(e) {
                e.stopPropagation();
                var $BOX_PANEL = $(this).closest('.x_panel'),
                    $ICON = $(this).find('i'),
                    $BOX_CONTENT = $BOX_PANEL.find('.x_content');

                // fix for some div with hardcoded fix class
                if ($BOX_PANEL.attr('style')) {
                    $BOX_CONTENT.slideToggle(200, function(){
                        $BOX_PANEL.removeAttr('style');
                    });
                } else {
                    $BOX_CONTENT.slideToggle(200);
                    $BOX_PANEL.css('height', 'auto');
                }

                $ICON.toggleClass('fa-chevron-up fa-chevron-down');
            });
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
                        if (data[name] || (data[name] == 0 && (data[name] + "").length > 0)) {
                            // remove the selected attribute of option dom
                            $this.find("option").each(function() {
                                $(this).removeAttr("selected")
                            })

                            $this.val(data[name]);
                            $this.find("option").each(function(index) {
                                if($(this).val() == data[name]) {
                                    $this.get(0).selectedIndex = index;
                                    $(this).attr("selected", true);
                                }
                            })
                        }
                    })
                }
            }
        }
    }
    profile.init();
})($);