var table,$;
layui.use(['form','layer','table','laytpl', 'util'],function(){
        $ = layui.jquery,
        table = layui.table;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        util = layui.util;

    var tableIns = table.render({
        elem: '#logTableList',
        id : "logTable",
        url : '/sys/log/dataTable',
        where: {
            loginName : $('#loginName').val()
        },
        page : true,
        height : "full-105",
        limits : [10,15,20,25],
        limit : 20,
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'logType', title: '日志类型',width:100, align:"center"},
            {field: 'loginName', title: '操作人', width:100, align:"center"},
            {field: 'createTime', title: '操作时间', align:"center", templet:function (d) {
                    return util.toDateString(d.createTime);
                }},
            {field: 'func', title: '功能', align:"center"},
            {field: 'ip', title: 'IP', width:120, align:"center"},
            {field: 'url', title: '请求URL', align:"center"},
            {field: 'params', title: '参数', align:"center"},
            {field: 'result', title: '结果', width:80, align:"center", templet:function (d) {
                switch(d.result) {
                    case 'Y':
                        return '<a class="layui-btn layui-btn-radius layui-btn-xs">成功</a>';
                    case 'N':
                        return '<a class="layui-btn layui-btn-radius layui-btn-xs layui-btn-danger">失败</a>';
                    default:
                        return '';
                }
            }},
            {field: 'exception', title: '异常信息', align:"center"},
            {title: '操作',fixed:"right",align:"center", templet:function () {
                   return '<a class="layui-btn layui-btn-xs layui-btn-danger '+auth_delete+'" lay-event="delete">删除</a>'
                }}
        ]],
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    // 搜索
    $("#search").on("click",function(){
        tableReload();
    });

    // 批量删除
    $('#batchDelete').on('click',function () {
        var checkStatus = table.checkStatus('logTable'),
            ids = [];
        if(checkStatus.data.length == 0){
            tips('请选择删除项');
            return false;
        }
        for (var i=0; i<checkStatus.data.length; i++) {
            ids.push(checkStatus.data[i].id)
        }
        top.layer.confirm('确定删除选择项?',{icon:3, title:'温馨提示'},function(index){
            deleteRecords(ids);
        });
    });

    //列表操作
    table.on('tool(logTableList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent){
            case 'delete':
                top.layer.confirm('确定删除此项?',{icon:3, title:'温馨提示'},function(index){
                    deleteRecords([data.id]);
                });
                break;
        }
    });

    
    function deleteRecords(ids) {
        top.asyncRequest({
            url: "/sys/log/delete",
            type: "post",
            data: {
                ids : ids
            },
            dataType: "json",
            success: function (result) {
                if(result.success) {
                    location.reload();
                    tips('删除成功');
                }else {
                    tips('删除失败');
                }
            },
            error: function () {
                tips('删除异常');
            }
        });
    }

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }

});
function tableReload() {
    table.reload("logTable",{
        page: {
            curr: 1
        },
        where: {
            loginName : $('#loginName').val()
        }
    })
}