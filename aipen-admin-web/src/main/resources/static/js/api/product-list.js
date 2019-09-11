//layui.config({
//    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
//});
layui.use(['form', 'layer', 'table', 'util'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        util = layui.util,
        table = layui.table;

    var tableIns = table.render({
        elem: '#productTableList',
        id: "productTable",
        url: '/mall/product/dataTable',
        where: {
            "productNo": $('input[name="productNo"]').val(),
            "productName": $('input[name="productName"]').val(),/*,
            "oneClass": $('select[name="oneClass"] option:selected').val(),*/
            "status": $('select[name="status"] option:selected').val()
        },
        page: true,
        height: "full-105",
        limits: [10, 15, 20, 25],
        limit: 20,
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: 'productNo', title: '爆品编号', align: "center", width: 120},
            {field: 'productName', title: '爆品名称', align: "center"},
            {field: 'productDesc', title: '爆品简称', align: "center"},
            {field: 'thumbPicUrl', title: '缩略图', align: "center", width: 100,
                templet: function (d) {
                    if (d.thumbPicUrl) {
                        return '<img width="80" height="55" src="' + d.thumbPicUrl + '" />';
                    } else {
                        return '';
                    }
                }
            },
            {field: 'oneClass', title: '一级分类', align: "center", width: 100, templet:function (d) {
                    switch (d.oneClass) {
                       case "1":
                            return '红包';
                       case "2":
                            return '爆品';
                    }
                }
             },
            {field: 'productDetail', title: '红包面额', align: "center", width: 100},
            {field: 'originalPrice', title: '原价', align: "center", width: 80},
            {field: 'salePrice', title: '促销价', align: "center", width: 80},
            {field: 'stockQuantity', title: '库存量', align: "center", width: 80},
            {field: 'soldQuantity', title: '已售量', align: "center", width: 80},
            {
                field: 'createTime', title: '创建时间', align: "center", width: 160,
                templet: function (d) {
                    if (d.createTime) {
                        return util.toDateString(d.createTime, 'yyyy-MM-dd HH:mm:ss');
                    } else {
                        return '';
                    }
                }
            },
           {
               field: 'updateTime', title: '修改时间', align: "center", width: 160,
               templet: function (d) {
                   if (d.updateTime) {
                       return util.toDateString(d.updateTime, 'yyyy-MM-dd HH:mm:ss');
                   } else {
                       return '';
                   }
               }
           },
            {
                field: 'status', title: '状态', align: "center", width: 100, event: 'status', templet: function (d) {
                    switch (d.status){
                        case "1":
                            return '<button class="layui-btn layui-btn-xs layui-btn-danger">未上架</button>';
                            break
                        case "2":
                            return '<button class="layui-btn layui-btn-xs">售卖中</button>';
                            break;
                        case "3":
                            return '<button class="layui-btn layui-btn-xs layui-btn-primary">已下架</button>';
                            break
                        default:
                            return '';
                    }
                }
            },
            {
                field: '', title: '操作', width: 140, align: "center", templet: function (d) {
                    return '<a class="layui-btn layui-btn-xs layui-btn-normal ' + auth_edit + '" lay-event="edit">编辑</a>' +
                           '<a class="layui-btn layui-btn-xs layui-btn-danger' + auth_delete + '" lay-event="delete">删除</a>';
                }
            }
        ]],
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    function open(url, data) {
        var index = layui.layer.open({
            title: "产品信息",
            type: 2,
            area: ['700px', '730px'],
            content: "/mall/product/page",
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if (data) {
                    body.find(".id").val(data.id);
                    body.find(".productNo").val(data.productNo);
                    body.find(".productNo").parent().parent().removeClass('layui-hide');
                    body.find(".productName").val(data.productName);
                    body.find(".productDesc").val(data.productDesc);
                    body.find("select[name='oneClass'] option[value="+data.oneClass+"]").prop("selected", true);
                    body.find(".originalPrice").val(data.originalPrice);
                    body.find(".salePrice").val(data.salePrice);
                    body.find(".stockQuantity").val(data.stockQuantity);
                    body.find(".productDetail").val(data.productDetail);
                    body.find(".thumbImg").attr("src",data.thumbPicUrl);
                    body.find("input[name='thumbPicUrl']").val(data.thumbPicUrl);
                }
                setTimeout(function () {
                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3,
                        time: 2000
                    });
                }, 500)
            }
        });
        // layui.layer.full(index);
        window.sessionStorage.setItem("index", index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize", function () {
            layui.layer.full(window.sessionStorage.getItem("index"));
        });
    }

    function tableReload() {
        table.reload("productTable", {
            page: {
                curr: 1
            },
            where: {
                "productNo": $('input[name="productNo"]').val(),
                "productName": $('input[name="productName"]').val(),/*,
                "oneClass": $('select[name="oneClass"] option:selected').val(),*/
                "status": $('select[name="status"] option:selected').val()
            }
        })
    }

    // 搜索
    $("#search").on("click", function () {
        tableReload();
    });

    // 新增
    $("#add").on("click", function () {
        open("/mall/product/add");
    });

    // 列表操作
    table.on('tool(productTableList)', function (obj) {
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent) {
            case "edit":
                open("/mall/product/edit", data);
                break;
            case "status":
                updateStatus(data.id, data.status);
                break;
            case "delete":
                layer.confirm("是否删除此爆品?", {
                    icon: 3,
                    title: '温馨提示',
                    closeBtn: 0,
                }, function (index) {
                    layer.close(index);
                    top.layer.msg('删除中，请稍候...', {icon: 16, time: false, shade: 0.8});
                    top.asyncRequest({
                        url: "/mall/product/delete/" + data.id,
                        type: "get",
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                tips("删除成功");
                                tableReload();
                            } else {
                                tips("删除失败");
                            }
                        },
                        error: function () {
                            layer.close(index);
                        }
                    });
                }, function (index) {
                    layer.close(index);
                });
                break;
            case "detail":
                var index = layui.layer.open({
                    title: "产品详情",
                    type: 2,
                    content: "/mall/product/detail/" + data.id,
                    success: function (layero, index) {
                        setTimeout(function () {
                            layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                                tips: 3,
                                time: 2000
                            });
                        }, 500)
                    }
                });
                layui.layer.full(index);
                window.sessionStorage.setItem("index", index);
                //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
                $(window).on("resize", function () {
                    layui.layer.full(window.sessionStorage.getItem("index"));
                });
                break;
        }
    });

    function updateStatus(id,status) {
        if(!auth_status){
            return;
        }
        var index = layui.layer.open({
            title: "",
            type: 1,
            closeBtn: 0,
            area: ['320px', '120px'],
            content: $('#statusDIV'),
            resize: false,
            btn: ['立即更新','关闭'],
            btnAlign: 'c',
            yes: function (index, layero) {
                var value = $("#selectStatus input[name='status']:checked").val();
                layui.layer.close(index);
                top.layer.msg('状态更新中，请稍候...', {icon: 16, time: false, shade: 0.8});
                top.asyncRequest({
                    url: "/mall/product/status/" + id + "/" + value,
                    type: "get",
                    dataType: "json",
                    success: function (result) {
                        console.log(result);
                        if (result.success) {
                            tips('更新成功');
                            tableReload();
                        } else {
                            tips('更新失败');
                        }
                    },
                    error: function () {
                        tips('更新失败');
                    }
                });
            },
            success: function(layero, index){
                $('#statusDIV').removeClass('layui-hide').parent().addClass('noScroll');
                $("#selectStatus input[name='status'][value='"+status+"']").prop('checked', true);
                form.render('radio','updateStatus');
            }
        });
    }

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
})