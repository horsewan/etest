//layui.config({
//    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
//});
layui.use(['form', 'layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    form.verify({
        checkPhone: function (value, item) {
            if(new RegExp("(?:0|86|+86)?1[3456789] d{9}").test(value)){
                return '手机号码格式错误';
            }
        },
        //只能输入数字
        checkNumber: function (value, item) {
            if(new RegExp("(?:0|86|+86)?1[3456789] d{9}").test(value)){
                return '统一社会信用编码格式错误';
            }
        }
    });

    // 小数点校验
    $('#bSolePrice').keyup(function(){
        $(this)[0].value= $(this)[0].value.match(/\d+(\.\d{0,2})?/) ? $(this)[0].value.match(/\d+(\.\d{0,2})?/)[0] : ''
    })

    form.on("submit(save)", function(data){
        top.layer.msg('保存提交中，请稍候',{icon: 16, time:false, shade:0.8});
        top.asyncRequest({
            url: data.form.action,
            type: "POST",
            data: data.field,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    close(result.msg,data.id);
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
    function close(msg,id) {
        if(msg){
            tips(msg);
        }
        parent.layer.closeAll('');
        // if(id){
            parent.layui.table.reload('businessTable',{page:{curr:$(".layui-laypage-em").next().html()}});   //刷新父页
        // }
    }

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
});