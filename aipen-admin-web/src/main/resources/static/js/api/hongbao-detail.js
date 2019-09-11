//layui.config({
//    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
//});
layui.use(['form', 'layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.on("submit(save)", function(data){
        top.layer.msg('保存提交中，请稍候',{icon: 16, time:false, shade:0.8});
        var html = ue.getContent();
        data.field.html = html;
        top.asyncRequest({
            url: "/mall/product/saveDetail",
            type: "post",
            data: data.field,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    close('保存成功', true, true);
                }else{
                    tips('保存失败');
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
    }

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
})