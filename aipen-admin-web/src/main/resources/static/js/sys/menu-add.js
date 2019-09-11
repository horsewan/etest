layui.use(['form','layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

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
                    tips('保存失败');
                }
            },
            error: function () {
                tips('保存异常');
            }
        });
        return false;
    });

    // 监听单选按钮
    form.on("radio(menuType)", function(data){
        switch (data.value){
            case "0":
                $('#pidSelect select[name="pid"]').empty().html('<option value="0">根目录</option>');
                form.render('select');
                break;
            case "1":
                selectRender('0');
                break;
            case "2":
                selectRender('1');
                break;
        }
    });

    // 重新渲染
    function selectRender(menuType) {
        var select_options = '<option value="">请选择</option>';
        switch (menuType){
            case '0':
                for (var i=0; i<selectJson.length; i++) {
                    if(selectJson[i].menuType === menuType){
                        select_options += '<option value="'+selectJson[i].id+'">'+selectJson[i].title+'</option>';
                    }
                }
                break
            case '1':
                for (var i=0; i<selectJson.length; i++) {
                    // 是目录
                    if(selectJson[i].pid == 0 && selectJson[i].menuType === '0'){
                        select_options += '<optgroup label="'+selectJson[i].title+'">';
                        for (var j=0; j<selectJson.length; j++) {
                            if(selectJson[i].id == selectJson[j].pid && selectJson[j].menuType === '1'){
                                select_options += '<option value="'+selectJson[j].id+'">'+selectJson[j].title+'</option>';
                            }
                        }
                        select_options += '</optgroup>';
                    } else if(selectJson[i].pid == 0 && selectJson[i].menuType === '1'){  // 是页面
                        select_options += '<option value="'+selectJson[i].id+'">'+selectJson[i].title+'</option>';
                    }
                }
                break;
        }
        $('#pidSelect select[name="pid"]').empty().html(select_options);
        form.render('select');
    }

    $('#close').on('click', function () {
        close('', false, false);
    });
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