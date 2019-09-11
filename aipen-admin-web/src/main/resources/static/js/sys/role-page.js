layui.config({
    base: '/js/',
}).extend({
    authtree: 'authtree',
});
layui.use(['form', 'layer', 'authtree'],function(){
    $ = layui.jquery;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        authtree = layui.authtree;

    // 加载权限菜单
    authtree.render('#LAY-auth-tree-index', treeJson, 'ids[]');

    // 数据提交至后台保存
    form.on("submit(save)", function(data){

        var numReg = /^[0-9]+$/;
        var ids = [];
        $('input[type="checkbox"]:checked').each(function (index) {
            var val = $(this).val();
            console.log("--" + val);
            if(numReg.test(val)){
                ids.push(val);
            }
        });
        if(ids.length == 0){
            layui.layer.msg('请配置菜单权限', {icon: 5, time: 2000, anim: 6});
            return false;
        }
        top.layer.msg('保存提交中，请稍候',{icon: 16, time:false, shade:0.8});
        top.asyncRequest({
            url: data.form.action,
            type: "post",
            data: {
                id : data.field.id,
                roleName : data.field.roleName,
                roleCode : data.field.roleCode,
                ids : ids
            },
            dataType: "json",
            success: function (result) {
                if(result.success){
                    tips('保存成功', true, true);
                    parent.tableReload();
                    parent.layer.closeAll();
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

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }

    $('#close').on('click', function () {
        parent.layer.closeAll();
    });
});