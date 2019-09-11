layui.use(['form', 'layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.verify({
        check_pwd: function (value, item) {
            var password = $('input[name="'+$(item).data('check')+'"]').val();
            if(value != password){
                return '两次输入密码不一致';
            }
        }
    });

    form.on("submit(save)", function(data){
        top.layer.msg('保存提交中，请稍候',{icon: 16, time:false, shade:0.8});
        top.asyncRequest({
            url: "/sys/user/pwd",
            type: "post",
            data: data.field,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    top.location.href = "/logout"
                }else{
                    tips('保存失败!'+result.msg);
                }
            },
            error: function () {
                tips('保存异常');
            }
        });
        return false;
    });

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }

})