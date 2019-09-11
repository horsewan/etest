//layui.config({
//    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
//});
var table,$;

layui.use(['form','layer','table','util'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer;
        $ = layui.jquery,
        table = layui.table;

    var tableIns = table.render({
        elem: '#roleTableList',
        id : "roleTable",
        url : '/sys/city/dataTable',
        where: {
            shengName : $('#shengName').val(),
            cityName : $('#cityName').val(),
            platformCode : $("#platformCode").val()
        },
        page : true,
        height : "full-105",
        limits : [10,15,20,25],
        limit : 20,
        cols : [[
            {type: 'checkbox', fixed:"left", width: 50},
            {field: 'shengNo', title: '省份编码', align:"center", width: 250},
            {field: 'shengName', title: '省份名称', align:"center", width: 250},
            {field: 'cityNo', title: '城市编码', align:"center", width: 250},
            {field: 'cityName', title: '城市名称', align:"center", width: 250},
            {field: 'agentNo', title: '平台编号', align:"center", width: 250},
           /* {field: 'delStatus', title: '删除状态', width: 120, align:"center", templet:function (d) {
                switch(d.delStatus) {
                    case 'N':
                        return '<a class="layui-btn layui-btn-radius layui-btn-danger layui-btn-xs">已删除</a>';
                    case 'Y':
                        return '<a class="layui-btn layui-btn-radius layui-btn-xs">未删除</a>';
                    default:
                        return '';
                }
            }},*/
            {field: '', title: '操作', width: 250, align:"center", templet:function (d) {
                return  '<a class="layui-btn layui-btn-xs layui-btn-normal '+auth_edit+'" lay-event="edit">编辑</a>' +
                        '<a class="layui-btn layui-btn-xs layui-btn-danger '+auth_delete+'" lay-event="del">撤销平台编号</a>';
            }}
        ]],
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    function open(url, data){
        var index = layui.layer.open({
            title : "城市信息",
            type : 2,
            area: ['400px', '380px'],
            content : "/sys/city/page?id=" + (data ? data.id : ''),
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if(data){
                    body.find(".id").val(data.id);
                    body.find(".shengNo").val(data.cityNo);
                    body.find(".shengName").val(data.cityName);
                    body.find(".cityNo").val(data.cityNo);
                    body.find(".cityName").val(data.cityName);
                    body.find(".agentNo").val(data.agentNo);
                }
                setTimeout(function(){
                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3,
                        time: 2000
                    });
                },500)
            }
        })
        // layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }

    // 搜索
    $("#search").on("click",function(){
        tableReload();
    });

    // 添加
    $('#add').on('click', function () {
        open("/sys/city/add");
    });

    // 列表操作
    table.on('tool(roleTableList)', function(obj){
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent){
            case "edit":
                open("/sys/city/edit", data);
                break;
            case "del":
                top.layer.confirm('确定撤销平台编号此项?',{icon:3, title:'提示信息'},function(index){
                    /*top.asyncRequest({
                        url: "/sys/city/delete/" + data.id,
                        type: "get",
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
                    });*/
                    top.asyncRequest({
                        url: "/sys/city/edit/agentNo/" + data.id,
                        type: "POST",
                        dataType: "json",
                        success: function (result) {
                            if(result.success) {
                                tips('撤销平台编号成功');
                                tableReload();
                            }else {
                                tips('撤销平台编号失败');
                            }
                        },
                        error: function () {
                            tips('撤销平台编号失败');
                        }
                    });
                });
                break;
        }
    });


    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
});

function tableReload() {
    table.reload("roleTable",{
        page: {
            curr: 1
        },
        where: {
            shengName : $('#shengName').val(),
            cityName : $('#cityName').val(),
            platformCode : $("#platformCode").val()
        }
    })
}