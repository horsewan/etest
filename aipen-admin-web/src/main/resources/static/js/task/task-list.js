layui.use(['form','layer','table','util'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        elem: '#taskTableList',
        id : "taskTable",
        url : '/task/dataTable',
        method: 'post',
        contentType: 'application/json',
        where: {

        },
        page : true,
        height : "full-105",
        limits : [10,15,20,25],
        limit : 20,
        cols : [[
            {type: "checkbox", fixed:"left", width: 50},
            {field: 'triggersName', title: '任务描述', align:"center"},
            {field: 'className', title: '执行类', align:"center"},
            {field: 'cronExpression', title: 'cron表达式', align:"center"},
            {field: 'status', title: '任务状态', align:"center", width: 100, templet:function (d) {
                    switch (d.status){
                        case 'ON':
                            return '<input type="checkbox" name="switch" lay-skin="switch" value="'+d.id+'" lay-text="运行|停止" lay-filter="status" '+auth_status+' checked />';
                        case 'OFF':
                            return '<input type="checkbox" name="switch" lay-skin="switch" value="'+d.id+'" lay-text="运行|停止" lay-filter="status" '+auth_status+' />';
                        default:
                            return '';
                    }
                }},
            {field: 'remarks', title: '备注', align:"center"},
            {field: '', title: '操作', width: 200, align:"center", templet:function (d) {
                return  '<a class="layui-btn layui-btn-xs layui-btn-warm '+auth_edit+'" lay-event="edit">编辑</a>';

            }}
        ]],
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    function open(url, data){
        var index = layui.layer.open({
            title : "定时任务",
            type : 2,
            area: ['600px', '350px'],
            offset: '100px',
            content: ["/task/page", 'no'],
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if(data){
                    body.find(".id").val(data.id);
                    body.find(".triggersName").val(data.triggersName);
                    body.find(".className").val(data.className);
                    body.find(".cronExpression").val(data.cronExpression);
                    body.find(".remarks").val(data.remarks);
                }
            }
        });
    }

    function tableReload() {
        table.reload("userTable",{
            page: {
                curr: 1
            },
            where: tableIns.where
        })
    }
    
    // 搜索
    $("#search").on("click",function(){
        tableReload();
    });

    // 添加
    $('#add').on('click', function () {
        open("/task/add");
    });

    // 列表操作
    table.on('tool(taskTableList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent){
            case "edit":
                open("/task/edit", data);
                break;
        }
    });

    form.on('switch(status)', function(data){
        var tipText = data.elem.checked ? '是否运行当前任务?' : '是否停止当前任务?';
        layer.confirm(tipText,{
            icon: 3,
            title:'温馨提示',
            closeBtn: 0,
        },function(index){
            layer.close(index);
            top.layer.msg('修改中，请稍候',{icon: 16,time:false,shade:0.8});
            top.asyncRequest({
                url: "/task/status/" + data.value,
                type: "get",
                dataType: "json",
                success: function (result) {
                    if(result.success){
                        tips(data.elem.checked ? '启用成功' : '停止成功');
                    }else{
                        data.elem.checked = !data.elem.checked;
                        form.render();
                        layer.close(index);
                        tips(data.elem.checked ? '启用失败' : '停止失败');
                    }
                },
                error: function () {
                    data.elem.checked = !data.elem.checked;
                    form.render();
                    layer.close(index);
                    tips(data.elem.checked ? '启用失败' : '停止失败');
                }
            });
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