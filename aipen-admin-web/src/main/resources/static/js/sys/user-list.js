//layui.config({
//    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
//});
layui.use(['form','layer','table','util'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        elem: '#userTableList',
        id : "userTable",
        url : '/sys/user/dataTable',
        page : true,
        height : "full-105",
        limits : [10,15,20,25],
        limit : 20,
        cols : [[
            {type: "checkbox", fixed:"left", width: 50},
            {field: 'loginName', title: '登录名（客服编号）', align:"center"},
            {field: 'name', title: '姓名', align:"center"},
            /*{field: 'sex', title: '性别', width: 80, align:"center", templet:function (d) {
                switch (d.sex){
                    case 'M':
                        return '<a class="layui-btn layui-btn-radius layui-btn-normal layui-btn-xs">男</a>';
                    case 'F':
                        return '<a class="layui-btn layui-btn-radius layui-btn-warm layui-btn-xs">女</a>';
                    default:
                        return '';
                }
            }},*/
            /*{field: 'email', title: '电子邮箱', align:"center"},*/
            {field: 'phone', title: '授权手机号', align:"center"},
            {field: 'isDisable', title: '是否禁用', align:"center", width: 100, templet: function (d) {
                switch (d.isDisable){
                    case 'N':
                        return '<a class="layui-btn layui-btn-radius layui-btn-danger layui-btn-xs">已禁用</a>';
                    case 'Y':
                        return '<a class="layui-btn layui-btn-radius layui-btn-xs">未禁用</a>';
                    default:
                        return '';
                }
            }},
            /*{field: 'delStatus', title: '删除状态', align:"center", width: 100, templet: function (d) {
                switch (d.delStatus){
                    case 'N':
                        return '<a class="layui-btn layui-btn-radius layui-btn-danger layui-btn-xs">已删除</a>';
                    case 'Y':
                        return '<a class="layui-btn layui-btn-radius layui-btn-xs">未删除</a>';
                    default:
                        return '';
                }
            }},*/
            {field: '', title: '操作', width: 280, align:"center", templet:function (d) {
                return  '<a class="layui-btn layui-btn-xs layui-btn-normal '+auth_edit+'" lay-event="edit" style="padding-left: 16px; padding-right: 16px">编辑</a>' +
                    '<a class="layui-btn layui-btn-xs layui-btn-danger '+auth_delete+'" lay-event="del" style="padding-left: 16px; padding-right: 16px">删除</a>' +
                    '<a class="layui-btn layui-btn-xs layui-btn-warm '+auth_reset+'" lay-event="reset" style="padding-left: 16px; padding-right: 16px">重置密码</a>'
            }}
        ]],
        where: {
            "loginName": $('#loginName').val(),
            "phone": $('#phone').val()
         },
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    function open(url,data){
        var index = layui.layer.open({
            title : "客服总监信息",
            type : 2,
            area: ['400px', '380px'],
            content : "/sys/user/page?id=" + (data ? data.id : ''),
            success : function(layero, index){
                //window[layero.find('iframe')[0]['name']].submitListener(url);
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if(data){
                    body.find(".id").val(data.id);
                    body.find(".loginName").val(data.loginName);
                    body.find(".name").val(data.name);
                    body.find(".sex input[value="+data.sex+"]").prop('checked', true);
                    body.find(".isDisable input[name='isDisable']").prop('checked', (data.isDisable === 'Y' ? true : false));
                    body.find(".email").val(data.email);
                    body.find(".phone").val(data.phone);
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3,
                        time: 2000
                    });
                },500)
            }
        });
        // layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        });
    }

    function tableReload() {
        table.reload("userTable",{
            page: {
                curr: 1
            },
           where: {
               "loginName": $('#loginName').val(),
               "phone": $('#phone').val()
            },
        })
    }
    
    // 搜索
    $("#search").on("click",function(){
        tableReload();
    });

    // 添加
    $('#add').on('click', function () {
        open("/sys/user/add");
    });

    // 列表操作
    table.on('tool(userTableList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent){
            case "edit":
                open("/sys/user/edit", data);
                break;
            case "del":
                top.layer.confirm('确定删除此项?',{icon:3, title:'提示信息'},function(index){
                    top.asyncRequest({
                        url: "/sys/user/delete/" + data.id,
                        type: "GET",
                        dataType: "json",
                        success: function (result) {
                            if(result.success) {
                                tips('删除成功');
                                tableReload();
                            }else {
                                tips('删除失败');
                            }
                        },
                        error: function () {
                            tips('删除失败');
                        }
                    });
                });
                break;
            case "reset":

                top.asyncRequest({
                    url: "/sys/user/reset/" + data.id,
                    type: "GET",
                    dataType: "json",
                    success: function (result) {
                        if(result.success) {
                            tips(result.msg);
                        }else {
                            tips(result.msg);
                        }
                    },
                    error: function () {
                        tips('请求失败');
                    }
                });

                break;
        }
    });

    form.on('switch(isDisable)', function(data){
        var tipText = '确定禁用当前系统用户?';
        if(data.elem.checked){
            tipText = '确定禁用当前系统用户?'
        }
        layer.confirm(tipText,{
            icon: 3,
            title:'温馨提示',
            closeBtn: 0,
            cancel : function(index){
                data.elem.checked = !data.elem.checked;
                form.render();
                layer.close(index);
            }
        },function(index){
            layer.close(index);
        },function(index){
            data.elem.checked = !data.elem.checked;
            form.render();
            layer.close(index);
        });
    });

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
})