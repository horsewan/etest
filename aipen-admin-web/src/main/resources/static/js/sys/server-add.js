layui.config({
    base: '/js/',
}).extend({
    formSelects : 'formSelects-v3',
});

//layui.use(['form','layer'],function(){
layui.use(['form', 'layer', 'formSelects'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    formSelects = layui.formSelects;

    // 数据提交至后台保存
    form.on("submit(save)", function(data){
        top.layer.msg('保存提交中，请稍候',{icon: 16,time:false,shade:0.8});
        top.asyncRequest({
            url: data.form.action,
            type: "POST",
            data: data.field,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    close('保存成功', true, true);
                }else{
                    tips(result.msg);
                }
            },
            error: function () {
                tips('保存异常');
            }
        });
        return false;
    })

    $('#close').on('click', function () {
        close('', false, false);
    });

    // 关闭窗口
    function close(msg, isTips, isRefresh) {
        if(isTips){
            tips(msg);
        }
        parent.layer.closeAll('');
        if(isRefresh){
            parent.location.reload();
        }
    }

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
})