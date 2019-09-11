var table, $, currentPkgId, currentAppName = "";
var deviceTable, pkgTable;
//layui.config({
//    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
//});
layui.use(['form', 'layer', 'table', 'util'], function () {
    var form = layui.form,
        util = layui.util,
        layer = parent.layer === undefined ? layui.layer : top.layer;
    $ = layui.jquery,
        table = layui.table;

    pkgTable = table.render({
        elem: '#businessTableList',
        id: "businessTable",
        url: '/mall/business/dataTable',
        page: true,
        height: "full-128",
        limits: [8, 16, 24, 32, 48],
        limit: 8,
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {
                field: 'bCdnQr', title: '商家二维码', align: "center",  fixed: "left", templet: function (d) {
                    return '<a class="layui-btn layui-btn-xs layui-btn-danger ' + auth_create_qr + '" href="http://image.futruedao.com/' + d.bCdnQr + '" target="_blank">下载商家二维码</a>';
                }
            },
            {field: 'bName', title: '商户名（公司名）', align: "center"},
            {field: 'bTicket', title: '统一社会信用代码', align: "center"},
            {field: 'bSingleNo', title: '客服编号', align: "center"},
            {field: 'bPerson', title: '法人', align: "center"},
            {field: 'bPhone', title: '授权手机号',  align: "center"},
            {field: 'bAddress', title: '商户地址', align: "center"},
            {field: 'bSolePrice', title: '折扣', align: "center"},
            {
                field: 'createTime', title: '入驻时间', align: "center", templet: function (d) {
                    return util.toDateString(d.createTime);
                }
            },
            {
                field: 'mchSta', title: '状态', align: "center", width: 100, templet: function (d) {
                    switch (d.mchSta){
                        case "1":
                            return '<button class="layui-btn layui-btn-xs layui-btn-danger">审核中</button>';
                            break
                        case "2":
                            return '<button class="layui-btn layui-btn-xs">审核通过</button>';
                            break;
                        case "3":
                            return '<button class="layui-btn layui-btn-xs layui-btn-primary">审核失败</button>';
                            break
                        default:
                            return '';
                    }
                }
            },
            {field: 'mchId', title: '银联商户号', align: "center"},
            {field: 'mchKey', title: '银联密文', align: "center"},
            {
                field: '', title: '操作', align: "center",width: 200, fixed: 'right', templet: function (d) {
                    return '<div><a class="layui-btn layui-btn-xs layui-btn-normal ' + auth_edit + '" lay-event="edit" style="padding-left: 16px; padding-right: 16px">编辑</a><a class="layui-btn layui-btn-xs layui-btn-danger ' + auth_delete + '" lay-event="del" style="padding-left: 16px; padding-right: 16px">删除</a></div>';
                    //<a class="layui-btn layui-btn-xs layui-btn-danger '+auth_create_qr+'" lay-event="createQR" style="padding-left: 16px; padding-right: 16px">生成付款码</a>
                }
            }
        ]],
        where: {
            "phone": $('#b_phone').val()
        },
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    // 应用列表操作
    table.on('tool(businessTableList)', function (obj) {
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent) {
            case "edit":
                openPkg("/mall/business/edit", data);
                break;
            case "addDevice":
                currentPkgId = data.id;
                currentAppName = data.appName;
                openDevice("/mall/business/add", data.id, data.appName);
                break;
            case "createQR":
                currentPkgId = data.bSingleNo;
                currentAppName = data.bTicket;
                openBusinessQR("/mall/business/downloadBusinessQR", data);
                break;
            case "del":
                layer.confirm('确认是否删除?', {
                    icon: 3,
                    title: '温馨提示',
                    closeBtn: 0,
                }, function (index) {
                    layer.close(index);
                    top.layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
                    top.asyncRequest({
                        url: "/mall/business/del/" + data.id,
                        type: "get",
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                tips('删除成功');
                                pkgTableReload();
                            } else {
                                tips('删除失败');
                            }
                        },
                        error: function () {

                        }
                    });
                }, function (index) {
                    layer.close(index);
                });
                break;
        }
    });

    function openPkg(url, data) {
        var index = layui.layer.open({
            title: "商户信息",
            type: 2,
            area: ['500px', '520px'],
            offset: '100px',
            content: "/mall/business/page",
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if (data) {
                    body.find(".id").val(data.id);
                    body.find(".bName").val(data.bName);
                    body.find("#bName").attr("disabled", "disabled");
                    body.find("#bTicket").attr("disabled", "disabled");
//                    body.find("#bTicket").addClass('layui-hide');
//                    body.find("#bName").addClass('layui=hide');
                    body.find(".bTicket").val(data.bTicket);
                    body.find(".bSingleNo").val(data.bSingleNo);
                    body.find(".bPerson").val(data.bPerson);
                    body.find(".bPhone").val(data.bPhone);
                    body.find(".bAddress").val(data.bAddress);
                    body.find(".bSolePrice").val(data.bSolePrice);
                    body.find("#bCdnQr").val(data.mchId);
                    body.find("#bSign").val(data.mchKey);

                }
            }
        });
        // layui.layer.full(index);
        // window.sessionStorage.setItem("index", index);
        // //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        // $(window).on("resize", function () {
        //     layui.layer.full(window.sessionStorage.getItem("index"));
        // });
    }

    function openCreate(url, data) {
        var index = layui.layer.open({
            title: "商户信息",
            type: 2,
            area: ['500px', '520px'],
            offset: '100px',
            content: "/mall/business/createPage",
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if (data) {
                    body.find(".id").val(data.id);
                    body.find(".bName").val(data.bName);
                    body.find("#bName").attr("disabled", "disabled");
                    body.find("#bTicket").attr("disabled", "disabled");
                    //                    body.find("#bTicket").addClass('layui-hide');
                    //                    body.find("#bName").addClass('layui=hide');
                    body.find(".bTicket").val(data.bTicket);
                    body.find(".bSingleNo").val(data.bSingleNo);
                    body.find(".bPerson").val(data.bPerson);
                    body.find(".bPhone").val(data.bPhone);
                    body.find(".bAddress").val(data.bAddress);
                    body.find(".bSolePrice").val(data.bSolePrice);

                }
            }
        });
        // layui.layer.full(index);
        window.sessionStorage.setItem("index", index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize", function () {
            layui.layer.full(window.sessionStorage.getItem("index"));
        });
    }

    function openBusinessQR(url, data) {
        var index = layui.layer.open({
            title: "商户二维码付款信息",
            type: 2,
            area: ['500px', '400px'],
            offset: '100px',
            data: data.field,
            content: "/mall/business/createQR/" + data.id,
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if (data) {
                    body.find(".id").val(data.id);
                    body.find(".bName").val(data.bName);
                    body.find(".bTicket").val(data.bTicket);
                    body.find(".bSingleNo").val(data.bSingleNo);
                    body.find(".bPerson").val(data.bPerson);
                    body.find(".bPhone").val(data.bPhone);
                    body.find(".bAddress").val(data.bAddress);
                    body.find(".bSolePrice").val(data.bSolePrice);
                }
            }
        });
        layui.layer.full(index);
        window.sessionStorage.setItem("index", index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize", function () {
            layui.layer.full(window.sessionStorage.getItem("index"));
        });
    }

    $('#device_search').on('click', function () {
        pkgTableReload();
    });

    $('#pkg_add').on('click', function () {
        openCreate("/mall/business/add");
    });

    $('#pkg_del').on('click', function () {
        var checkStatus = table.checkStatus('businessTable'), ids = [];
        if (checkStatus.data.length == 0) {
            tips('请选择删除数据');
            return false;
        }

        for (var i = 0, len = checkStatus.data.length; i < len; i++) {
            ids.push(checkStatus.data[i].id)
        }

        layer.confirm('确认是否删除?', {
            icon: 3,
            title: '温馨提示',
            closeBtn: 0,
        }, function (index) {
            layer.close(index);
            top.layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
            top.asyncRequest({
                url: "/mall/business/del/" + ids,
                type: "get",
                dataType: "json",
                data: {
                    ids: ids
                },
                success: function (result) {
                    if (result.success) {
                        tips('删除成功');
                        pkgTableReload();
                    } else {
                        tips('删除失败');
                    }
                },
                error: function () {

                }
            });
        }, function (index) {
            layer.close(index);
        });
    });

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }

});

function pkgTableReload() {
    table.reload("businessTable", {
        page: {
            curr: 1
        },
        where: {
            "phone": $('#b_phone').val()
        },
    })
}
