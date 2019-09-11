layui.use(['form','layer','table','util'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        elem: '#menuTableList',
        id : "menuTable",
        url : '/sys/menu/dataTable',
        height : "full-105",
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: 'title', title: '菜单名称', align:"left", width: 250, templet:function (d) {
                if(d.pid == 0){
                    return '<i class="layui-icon" style="cursor:pointer;">&#xe623;</i>' + d.title;
                }
                switch (d.menuType) {
                    case '0':
                        return '<i class="layui-icon" style="cursor:pointer;">&#xe623;</i>' + d.title;
                    case '1':
                        return '<span class="layui-inline" style="text-indent: 2em;">├─ </span>' + d.title;
                    case '2':
                        return '<span class="layui-inline" style="text-indent: 4em;">├─ </span>' + d.title;
                    default:
                        return d.title;
                }
            }},
            {field: 'menuType', title: '菜单类型', width: 100, align:"center", templet: function (d) {
                switch (d.menuType) {
                    case '0':
                        return '<a class="layui-btn layui-btn-xs layui-btn-radius">目录</a>';
                    case '1':
                        return '<a class="layui-btn layui-btn-xs layui-btn-radius layui-btn-normal">页面</a>';
                    case '2':
                        return '<a class="layui-btn layui-btn-xs layui-btn-radius layui-btn-warm">按钮</a>';
                    default:
                        return '';
                }
            }},
            {field: 'href', title: '请求URL', align:"center"},
            {field: 'perm', title: '菜单权限', align:"center"},
            {field: 'sort', title: '排序', width: 100, align:"center"}
            /*,
            {title: '操作', minWidth:180, fixed:"right", align:"center", templet: function (d) {
                switch (d.menuType) {
                    case '0':
                        return  '<a class="layui-btn layui-btn-xs" lay-event="addPage">添加页面</a>\n' +
                                '<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="edit">编辑</a>\n' +
                                '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>';
                    case '1':
                        return  '<a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="addBtn">添加按钮</a>\n' +
                                '<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="edit">编辑</a>\n' +
                                '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>';
                    case '2':
                        return  '<a class="layui-btn layui-btn-xs layui-btn-warm" lay-event="edit">编辑</a>\n' +
                                '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>';
                    default:
                        return '';
                }
            }}*/
        ]],
        done: function(res, curr, count){
            res.data.forEach(function (item, index) {
                //$('.layui-table-box tbody tr[data-index="'+index+'"]').addClass('layui-hide');
            });
        },
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    function add(url){
        var index = layui.layer.open({
            title : "权限菜单信息",
            type : 2,
            content : "/sys/menu/add",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                setTimeout(function(){
                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }


    function edit(url, data){
        var index = layui.layer.open({
            title : "权限菜单信息",
            type : 2,
            content : "/sys/menu/edit",
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if(data){
                    body.find(".id").val(data.id);
                    body.find(".pid").val(data.pid);
                    body.find(".title").val(data.title);
                    body.find(".href").val(data.href);
                    body.find(".perm").val(data.perm);
                    body.find(".sort").val(data.sort);
                    switch (data.menuType) {
                        case '0':
                            body.find(".menuType").html('<input type="checkbox" name="menuType" value="'+data.menuType+'" title="目录" lay-skin="primary" checked readonly />');
                            break;
                        case '1':
                            body.find(".menuType").html('<input type="checkbox" name="menuType" value="'+data.menuType+'" title="页面" lay-skin="primary" checked readonly />');
                            break;
                        case '2':
                            body.find(".menuType").html('<input type="checkbox" name="menuType" value="'+data.menuType+'" title="按钮" lay-skin="primary" checked readonly />');
                            break;
                        default:
                            break;
                    }
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                },500)
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }

    // 添加
    $('#add').on('click', function () {
        add("/sys/menu/add");
    });

    // 编辑
    $('#edit').on('click', function () {
        var checkStatus = table.checkStatus('menuTable');
        if(checkStatus.data.length != 1){
            tips('请选择一行数据进行编辑');
            return false;
        }
        edit("/sys/menu/edit", checkStatus.data[0]);
    });

    // 删除
    $('#del').on('click', function () {
        var checkStatus = table.checkStatus('menuTable'),
            ids = [];
        if(checkStatus.data.length == 0){
            tips('请选择删除项');
            return false;
        }

        for (var i=0, len=checkStatus.data.length; i<len; i++) {
            ids.push(checkStatus.data[i].id)
        }

        top.layer.confirm('确定删除选择项?',{icon:3, title:'温馨提示'},function(index){
            top.asyncRequest({
                url: "/sys/menu/delete",
                type: "POST",
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
        });
    });

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }

    //监听单元格编辑
    table.on('edit(menuTableList)', function(obj){
        console.log(obj);
        var value = obj.value,
            data = obj.data,
            field = obj.field;
        console.log('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
    });

});