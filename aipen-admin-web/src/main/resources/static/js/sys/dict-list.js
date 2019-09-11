layui.use(['form','layer','table','util'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        elem: '#dictTableList',
        id : "dictTable",
        url : '/sys/dict/dataTable',
        height : "full-105",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'groupDesc', title: '字典组名称', align:"left", width: 200, templet:function (d) {
                if(d.pid == 0){
                    return '<i class="layui-icon" style="cursor:pointer;">&#xe623;</i>' + d.groupDesc;
                }else{
                    return '';
                }
            }},
            {field: 'groupCode', title: '字典组编码', width: 200, align:"center", templet: function (d) {
                    if(d.pid == 0){
                        return d.groupCode;
                    }else{
                        return '';
                    }
                }},
            {field: 'labelName', title: '字典名', align:"center", width: 150},
            {field: 'valueCode', title: '字典值', width: 100, align:"center"},
            /*{field: 'delStatus', title: '删除状态', width: 100, align:"center", templet:function (d) {
                switch(d.delStatus) {
                    case 'N':
                        return '<a class="layui-btn layui-btn-radius layui-btn-danger layui-btn-xs">已删除</a>';
                    case 'Y':
                        return '<a class="layui-btn layui-btn-radius layui-btn-xs">未删除</a>';
                    default:
                        return '';
                }
            }},*/
            {title: '操作', minWidth:150, fixed:"right", align:"center", templet: function (d) {
                switch(d.pid) {
                    case 0:
                        return  '<a class="layui-btn layui-btn-xs '+auth_add+'" lay-event="add">添加字典</a>\n' +
                                '<a class="layui-btn layui-btn-xs layui-btn-normal '+auth_edit+'" lay-event="editGroup">编辑</a>\n' +
                                '<a class="layui-btn layui-btn-xs layui-btn-danger '+auth_delete+'" lay-event="del">删除</a>';
                    default:
                        return  '<a class="layui-btn layui-btn-xs layui-btn-normal '+auth_edit+'" lay-event="edit">编辑</a>\n' +
                                '<a class="layui-btn layui-btn-xs layui-btn-danger '+auth_delete+'" lay-event="del">删除</a>';
                }
            }}
        ]],
        done: function(res, curr, count){
            /*res.data.forEach(function (item, index) {
                //$('.layui-table-box tbody tr[data-index="'+index+'"]').addClass('layui-hide');
            });*/
        },
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    function open(url, data, pdata){
        var index = layui.layer.open({
            title : "字典信息",
            type : 2,
            area: ['450px', '350px'],
            offset: '80px',
            content: ["/sys/dict/page", 'no'],
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr('action', url);
                if(data){
                    body.find(".id").val(data.id);
                    body.find(".pid").val(data.pid);
                    body.find(".groupDesc").val(data.groupDesc);
                    body.find(".groupCode").val(data.groupCode);
                    body.find(".labelName").val(data.labelName);
                    body.find(".valueCode").val(data.valueCode);
                    form.render();
                }
                if(pdata){
                    body.find(".pid").val(pdata.id);
                    body.find(".groupDesc").val(pdata.groupDesc);
                    body.find(".groupCode").val(pdata.groupCode);
                }
            }
        })
    }

    function openGroup(url, data){
        var index = layui.layer.open({
            title : "字典组信息",
            type : 2,
            area: ['450px', '300px'],
            offset: '80px',
            content: ["/sys/dict/group", 'no'],
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr('action', url);
                if(data){
                    body.find(".id").val(data.id);
                    body.find(".groupDesc").val(data.groupDesc);
                    body.find(".groupCode").val(data.groupCode);
                    body.find(".groupCode").prop('readOnly', true);
                    form.render();
                }
            }
        })
    }

    $('#addGroup').on('click', function () {
        openGroup('/sys/dict/addGroup');
    });

    // 列表操作
    table.on('tool(dictTableList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent){
            case "add":
                open("/sys/dict/add", null, data);
                break;
            case "edit":
                open("/sys/dict/edit", data);
                break;
            case "editGroup":
                openGroup("/sys/dict/editGroup", data);
                break;
            case "del":
                top.layer.confirm('确定删除此项?',{icon:3, title:'提示信息'},function(index){
                    top.asyncRequest({
                        url: "/sys/dict/delete/" + data.id,
                        type: "get",
                        dataType: "json",
                        success: function (result) {
                            if(result.success) {
                                refresh();
                                tips('删除成功');
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
        }
    });


    // 刷新
    function refresh() {
        location.reload();
    }

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
})