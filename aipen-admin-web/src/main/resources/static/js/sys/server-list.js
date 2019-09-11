layui.use(['form','layer','table','util'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        elem: '#serverTableList',
        id : "serverTable",
        url : '/sys/server/dataTable',
        height : "full-105",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'domain', title: '主机记录', align:"left", width: 200},
            {field: 'type', title: '语音类型', align:"left", width: 100,templet:function(d){
            	switch(d.type){
            	case 1:
            		return '<a class="layui-btn layui-btn-radius layui-btn-danger layui-btn-xs">ASR</a>';
            	case 2:
            		return '<a class="layui-btn layui-btn-radius layui-btn-danger layui-btn-xs">TTS</a>';
            	case 3:
            		return '<a class="layui-btn layui-btn-radius layui-btn-danger layui-btn-xs">NMT</a>';
            	default:
            		return '';
            	}
            }},
            {field: 'version', title: '版本', width: 200, align:"center"},
            {field: 'priority', title: '权重值', align:"center", width: 150},
            {field: 'avaliable', title: '是否最优版本', width: 150, align:"center",templet:function(d){
            	switch(d.avaliable){
            	case 1:
            		return '<a class="layui-btn layui-btn-radius layui-btn-danger layui-btn-xs">是</a>';
            	case 2:
            		return '<a class="layui-btn layui-btn-radius layui-btn-xs">否</a>';
            	default:
            		return '';
            	}
            }},
            {title: '操作', minWidth:150, fixed:"right", align:"center", templet: function (d) {
                        return  '<a class="layui-btn layui-btn-xs layui-btn-danger '+auth_delete+'" lay-event="del">删除</a>';
                }
            }
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

    /*function open(url, data, pdata){
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
    }*/

    function openServer(url, data){
        var index = layui.layer.open({
            title : "服务器信息",
            type : 2,
            area: ['450px', '360px'],
            offset: '80px',
            content: ["/sys/server/add", 'no'],
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr('action', url);
                if(data){
                    body.find(".id").val(data.id);
                    body.find(".domain").val(data.groupDesc);
                    body.find(".version").val(data.groupCode);
                    body.find(".priority").val(data.groupCode);
                    body.find(".avaliable").val(data.groupCode);
                    body.find(".type").val(data.groupCode);
                    form.render();
                }
            }
        })
    }

    $('#addServer').on('click', function () {
        openServer('/sys/server/addServer');
    });
    
    // 列表操作
    table.on('tool(serverTableList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent){
            case "del":
                top.layer.confirm('确定删除此项?',{icon:3, title:'提示信息'},function(index){
                    top.asyncRequest({
                        url: "/sys/server/delServer/" + data.id,
                        type: "get",
                        dataType: "json",
                        success: function (result) {
                            if(result.success) {
                                refresh();
                                tips('删除成功');
                            }else {
                            	tips(result.msg);
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