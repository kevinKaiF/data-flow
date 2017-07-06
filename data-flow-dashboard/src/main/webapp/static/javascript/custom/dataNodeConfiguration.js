;(function ($) {
    var main = {
        init: function () {
            this.initForm();
            this.eventBind();
            this.initValidation();
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
                $("#alert-submit").show().off('click').on("click", function () {
                    callback();
                    $("#alertMessage").modal('hide');
                })
            } else {
                $("#alert-submit").hide().off('click');
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