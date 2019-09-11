layui.config({
    base: '/js/',
}).extend({
    formSelects : 'formSelects-v3',
});
layui.use(['form', 'layer', 'formSelects'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        formSelects = layui.formSelects;

    $(".layui-input").css("width","200px");
    $(".layui-form-label").css("width","100");
    // 渲染formSelects
    renderSelect();

    /*form.verify({
        mustradio: function (value, item) {
            var va = $(item).find("input[type='radio']:checked").val();
            if (typeof (va) == "undefined") {
                return $(item).attr("lay-verify-msg");
            }
        },
        mustcheck: function (value, item) {
            var va = $(item).find("input[type='checkbox']:checked").val();
            if (typeof (va) == "undefined") {
                return $(item).attr("lay-verify-msg");
            }
        },
        mustselect: function (value, item) {
            var va = $(item).find("option:selected").val();
            if (typeof (va) == "undefined") {
                return $(item).attr("lay-verify-msg");
            }
        }
    });*/
    form.verify({
        checkPhone: function (value, item) {
            if(new RegExp("(?:0|86|+86)?1[3456789] d{9}").test(value)){
                return '手机号码格式错误';
            }
        }
    });
    form.on("submit(save)", function(data){

        // var ids = [];
        // formSelects.value('selectRoles', 'val').forEach(function(value){
        //     ids.push(parseInt(value));
        // });
        // if(ids.length == 0){
        //     layui.layer.msg('请配置角色', {icon: 5, time: 2000, anim: 6});
        //     return false;
        // }

        /*var phone =  $('input[name="phone"]').val();
        if(phone.length != 0){
            if(new RegExp("(?:0|86|+86)?1[3456789] d{9}").test(phone)){
               layui.layer.msg('手机号码格式错误', {icon: 5, time: 2000, anim: 6});
               return false;
            }
        }*/
        top.layer.msg('保存提交中，请稍候',{icon: 16, time:false, shade:0.8});
        // data.field.ids = ids;
        top.asyncRequest({
            url: data.form.action,
            type: "POST",
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
                tips('保存异常，请确保用户服务可用');
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
            parent.layui.table.reload('userTable',{page:{curr:$(".layui-laypage-em").next().html()}});   //主要代码
        }
    }

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
    
    function renderSelect() {
        formSelects.render({
            name: 'selectRoles',
            type: 2,
            data: {
                arr: roleJson,
                name: 'name',
                val: 'value',
                selected: 'selected'
            }
        });
    }
})