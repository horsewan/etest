var table,$, tableIns;
layui.use(['form','layer','table','laytpl', 'util'],function(){
        $ = layui.jquery,
        table = layui.table;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        util = layui.util;

    tableIns = table.render({
        elem: '#importLogTableList',
        id : "importLogTable",
        url : '/sys/import/dataTable',
        method: 'POST',
        contentType: 'application/json',
        where: {
            "searchParams": {
                search_rlike_op_name : $('#opName').val()
            }
        },
        page : true,
        height : "full-105",
        limits : [10,15,20,25],
        limit : 20,
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'importType', title: '导入类型',width:120, align:"center", templet: function (d) {
                    switch (d.importType){
                        case "1":
                            return '导入IMEI号';
                        case "2":
                            return '导入MAC地址';
                        default:
                            return '';
                    }
                }},
            {field: 'importMsg', title: '导入信息', align:"center"},
            {field: 'opName', title: '操作人', align:"center", width:120},
            {field: 'opTime', title: '操作时间', align:"center", width:180, templet:function (d) {
                    return util.toDateString(d.opTime);
                }},
            {title: '操作',fixed:"right",align:"center", width:120, templet:function () {
                   return '<a class="layui-btn layui-btn-xs layui-btn-danger '+auth_delete+'" lay-event="delete">删除</a>'
                }}
        ]],
        initSort: {
            field: 'opTime',
            type: 'desc'
        },
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
    table.on('tool(importLogTableList)', function(obj){
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
            url: "/sys/import/delete",
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
    table.reload("importLogTable",{
        page: {
            curr: 1
        },
        where: tableIns.where
    })
}