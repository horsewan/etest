layui.use(['form', 'upload', 'layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        $ = layui.jquery;

    // $('.layui-input').keyup(function(){
    //     $(this)[0].value= $(this)[0].value.match(/\d+(\.\d{0,2})?/) ? $(this)[0].value.match(/\d+(\.\d{0,2})?/)[0] : ''
    // })

    function checkNum() {
        $(this)[0].value = $(this)[0].value.match(/\d+(\.\d{0,2})?/) ? $(this)[0].value.match(/\d+(\.\d{0,2})?/)[0] : '';
    }
    form.on("submit(save)", function(data){
        top.layer.msg('保存提交中，请稍候',{icon: 16, time:false, shade:0.8});
        top.asyncRequest({
            url: data.form.action,
            type: "post",
            data: data.field,
            dataType: "json",
            success: function (result) {
                if(result.success){
                    close('保存成功', true, true);
                }else{
                    tips('保存失败，'+result.msg);
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

    //上传缩略图
    upload.render({
        elem: '#thumbBox',
        url:  '/mall/product/upload',
        done: function(res, index, upload){
            if(res.success){
                $('input[name="thumbPicUrl"]').val(res.data);
                $('img.thumbImg').attr('src', res.data);
                $('.thumbBox').css("background","#fff");
            }
        }
    });
})