//layui.config({
//    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
//});
layui.use(['form', 'layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    $(function(

    ));

    form.on("submit(download)", function(data){
        top.layer.msg('商家二维码生成提交中，请稍候',{icon: 16, time:false, shade:0.8});
        top.asyncRequest({
            url: data.form.action,
            type: "POST",
            data: data.field,
            dataType: "json",
            success: function (result) {
                if(result.success){
//                    close('生成成功'+result.msg);
//                    tips(result.msg);
                    $('#download_business').attr('href','http://image.futruedao.com/'+result.msg);
//                    top.layer.msg('商家二维码生成提交中，请稍候',{icon: 16, time:false, shade:0.8});

                    /*top.asyncRequest({
                        url: "/mall/business/downLoadFile?path="+result.msg,
                        type: "GET",
                        data: data.field,
                        dataType: "json",
                        success: function (result) {
                            if(result.success){
                                close('下载二维码成功'+result.msg);
                            }else{
                                tips('下载二维码失败，'+result.msg);
                            }
                        },
                        error: function () {
                            tips('下载二维码异常，请确保用户服务可用');
                        }
                    });*/
                }else{
                    tips('生成失败，'+result.msg);
                }
            },
            error: function () {
                tips('生成异常，请确保用户服务可用');
            }
        });
        return false;
    })

    $('#close').on('click', function () {
        close('');
    });
    $('#share').on('click', function () {
        share();
    });

    $('#download_business').on('click', function () {

    });


    // 关闭窗口
    function close(msg) {
        if(msg){
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


});