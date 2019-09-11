//layui.config({
//    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
//});
layui.use(['form', 'layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.verify({
        checkMac: function (value, item) {
            if(new RegExp("(?:0|86|+86)?1[3456789] d{9}").test(value)){
                return '手机号码格式错误';
            }
        }
    });

    form.on("submit(save)", function(data){
        top.layer.msg('保存提交中，请稍候',{icon: 16, time:false, shade:0.8});
        top.asyncRequest({
            url: data.form.action,
            type: "POST",
            data: data.field,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    close('保存成功', data.field.ausn);

                }else{
                    tips('保存失败，'+result.msg);
                }
            },
            error: function () {
                tips('保存异常，请确保用户服务可用');
            }
        });
        return false;
    })

    $('#close').on('click', function () {
        close('');
    });

    // 关闭窗口
    function close(msg, pkgId) {
        if(msg){
            tips(msg);
        }
        parent.layer.closeAll('');
        if(pkgId){
            parent.layui.table.reload("deviceTable",{page:{curr:$(".layui-laypage-em").next().html()}});
            // parent.deviceTableReload(pkgId);
        }
    }

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
});