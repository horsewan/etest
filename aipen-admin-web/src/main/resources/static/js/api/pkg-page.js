layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    if ($.isArray(languageList)) {
        var html = '';
        for (var i = 0, len = languageList.length; i < len; i++) {
            if (languageList[i].selected) {
                html += '<input type="checkbox" name="language" lay-skin="primary" title="' + languageList[i].languageName + '" value="' + languageList[i].languageId + '" checked />';
            } else {
                html += '<input type="checkbox" name="language" lay-skin="primary" title="' + languageList[i].languageName + '" value="' + languageList[i].languageId + '" />';
            }

        }
        $('#languageList').html(html);
        form.render('checkbox');
    }

    $(".layui-form-label").css("width","100px");
    $(".layui-input").css("width","150px");

    form.on("submit(save)", function (data) {
        if (data.field.useCount != -1) {
            data.field.defaultUseCount = $('#numInput').val();
        } else {
            data.field.defaultUseCount = -1;
        }
        /* var languageIds = [];
         $.each($('#languageList input[type="checkbox"]:checked'), function (index, item) {
             languageIds.push($(this).val());
         });
         if(languageIds.length == 0){
             layui.layer.msg('请选择语种', {icon: 5, time: 1500, anim: 6});
             return false;
         }
         data.field.languageIds = languageIds;*/

        top.layer.msg('保存提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        top.asyncRequest({
            url: data.form.action,
            type: "POST",
            data: data.field,
            dataType: "json",
            success: function (result) {
                if (result.success) {
                    close('保存成功', true, true);
                } else {
                    tips('保存失败，'+result.msg);
                }
            },
            error: function () {
                tips('保存异常，请确保用户服务可用');
            }
        });
        return false;
    });

    form.on('radio(useCount)', function (data) {
        if (data.value == -1) {
            $('#numInput').parent().addClass('layui-hide')
        } else {
            $('#numInput').parent().removeClass('layui-hide')
        }
    });

    $('#close').on('click', function () {
        close('', false, false);
    });

    // 关闭窗口
    function close(msg, isTips, isRefresh) {
        if (isTips) {
            tips(msg);
        }
        parent.layer.closeAll('');
        if (isRefresh) {
            parent.layui.table.reload("pkgTable",{page:{curr:$(".layui-laypage-em").next().html()}});
            // parent.pkgTableReload();
        }
    }

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
});