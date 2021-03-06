layui.use(['form', 'layer', 'jquery'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer
        $ = layui.jquery;

    //登录按钮
    form.on("submit(login)", function (data) {
        var _this = $(this);
        _this.text("登录中...").attr("disabled", "disabled").addClass("layui-disabled");
        $.ajax({
            url: "/login",
            type: "post",
            data: $('#login').serialize(),
            dataType: "json",
            success: function (data) {
                _this.text("登录").removeAttr("disabled").removeClass("layui-disabled");
                if(data.success){
                    top.location.href = data.msg;
                }else{
                    $("#errorTip").html(data.msg);
                    $("#code").val("");
                    $("#captcha").attr("src", "/captcha?" +Math.random());
                }
            },
            error: function (data) {
                _this.text("登录").removeAttr("disabled").removeClass("layui-disabled");
            }
        });
        //阻止表单跳转
        return false;
    })

    //表单输入效果
    $(".loginBody .input-item").click(function (e) {
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    });

    $(".loginBody .layui-form-item .layui-input").focus(function () {
        $(this).parent().addClass("layui-input-focus");
    });

    $(".loginBody .layui-form-item .layui-input").blur(function () {
        $(this).parent().removeClass("layui-input-focus");
        if ($(this).val() != '') {
            $(this).parent().addClass("layui-input-active");
        } else {
            $(this).parent().removeClass("layui-input-active");
        }
    });
});
