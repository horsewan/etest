layui.use(['form', 'layer', 'table', 'util', 'laydate'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        util = layui.util,
        laydate = layui.laydate,
        table = layui.table;

    laydate.render({
        elem: '#payTimeStart'
    });

    laydate.render({
        elem: '#payTimeEnd'
    });

    var tableIns = table.render({
        elem: '#orderTableList',
        id: 'orderTable',
        url: '/mall/orders/dataTable',
        method: 'POST',
        contentType: 'application/json',
        where: {
            "searchParams": {
                "search_eq_uid": $("input[name='uid']").val(),
                "search_eq_order_number": $("input[name='orderNumber']").val(),
                "search_eq_order_status": $('select[name="orderStatus"] option:selected').val(),
                "search_between_pay_time_start": $('input[name="payTimeStart"]').val() ? $('input[name="payTimeStart"]').val() + " 00:00:00" : '',
                "search_between_pay_time_end": $('input[name="payTimeEnd"]').val() ? $('input[name="payTimeEnd"]').val() + " 23:59:59" : ''
            }
        },
        page: true,
        height: "full-105",
        limits: [20, 40, 80, 100],
        limit: 20,
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            /*{field: 'uid', title: '订单号', align: "center", width: 120, fixed: "left"},*/
            {field: 'orderNumber', title: '订单号', align: "center", width: 165, fixed: "left"},


            {
                field: 'orderStatus', title: '订单状态', align: "center", width: 100, fixed: "left", templet: function (d) {
                    switch (d.orderStatus) {
                        case '1':
                            return '<p style="color: #F79646;">待支付</p>';
                        case '2':
                            return '<p style="color: #BFBFBF;">已取消</p>';
                        case '3':
                            return '<p style="color: #9bbb59;">已支付</p>';
                        case '4':
                            return '<p style="color: #9bbb59;">已发货</p>';
                        case '5':
                            return '<p style="color: #9bbb59;">线下付</p>';
                        default:
                            return '';
                    }
                }
            },
            {
                field: 'orderTime', title: '下单时间', align: "center", width: 160,
                templet: function (d) {
                    if (d.orderTime) {
                        return util.toDateString(d.orderTime, 'yyyy-MM-dd HH:mm:ss');
                    } else {
                        return '';
                    }
                }
            },
            {field: 'amountPayable', title: '应付金额', align: "center", width: 100},
            {
                field: 'payTime', title: '支付时间', align: "center", width: 160, templet: function (d) {
                    if (d.payTime) {
                        return util.toDateString(d.payTime, 'yyyy-MM-dd HH:mm:ss');
                    } else {
                        return '';
                    }
                }
            },
            {field: 'amountRealpay', title: '支付金额', align: "center", width: 100},
//            {field: 'payWay', title: '支付方式', align: "center", width: 100},
            {
                field: 'payWay', title: '支付方式', align: "center", width: 100, templet: function (d) {
                    switch (d.payWay) {
                        case '1':
                            return '<p style="color: #F79646;">微信</p>';
                        case '2':
                            return '<p style="color: #BFBFBF;">支付宝</p>';
                        case '3':
                            return '<p style="color: #9bbb59;">云闪付</p>';
                        default:
                            return '';
                    }
                }
            },
            {field: 'signName', title: '收件人', align: "center", width: 80},
            {field: 'signPhone', title: '联系电话', align: "center", width: 100},
            {field: 'signAddress', title: '收件地址', align: "center", width: 250},
            {field: 'expNo', title: '运单号', align: "center", width: 120},
            /*{
                field: 'refundTime', title: '退款时间', align: "center", width: 160, templet: function (d) {
                    if (d.refundTime) {
                        return util.toDateString(d.refundTime, 'yyyy-MM-dd HH:mm:ss');
                    } else {
                        return '';
                    }
                }
            },
            {field: 'refundMoney', title: '退款金额', align: "center", width: 100},
            {field: 'refundReason', title: '退款原因', align: "center", width: 120},*/
            {
                field: '', title: '操作', width: 160, align: "center", fixed: "right", templet: function (d) {
                    /*return '<a class="layui-btn layui-btn-xs ' + auth_detail + '" lay-event="detail">订单详情</a>' +
                        '<a class="layui-btn layui-btn-xs ' + auth_exp + '" lay-event="exp">物流信息</a>';*/
                    switch (d.orderStatus) {
                        case '3':
                            return '<a class="layui-btn layui-btn-xs ' + auth_detail + '" lay-event="detail">订单详情</a><a class="layui-btn layui-btn-xs ' + auth_exp + '" lay-event="exp">物流信息</a>';
                        default:
                            return '<a class="layui-btn layui-btn-xs ' + auth_detail + '" lay-event="detail">订单详情</a>';
                    }

                }
            }
        ]],
        initSort: {
            field: 'payTime',
            type: 'desc'
        },
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });


    form.on('submit(uploadImg)', function (data) {
        loading = layer.load(1, {shade: [0.3, '#fff']});
        var $ = layui.jquery;
        var excel = layui.excel;
        var schoolList={
            "orderNumber": $("input[name='orderNumber']").val(),
            "status": $('select[name="orderStatus"] option:selected').val(),
            "payTimeStart": $('input[name="payTimeStart"]').val() ? $('input[name="payTimeStart"]').val() + " 00:00:00" : '',
            "payTimeEnd": $('input[name="payTimeEnd"]').val() ? $('input[name="payTimeEnd"]').val() + " 23:59:59" : ''
        };
        $.ajax({
            url: '/mall/orders/excelList',
            method: 'POST',
            contentType: 'application/json;charset=utf-8',
            dataType: 'json',
            data: JSON.stringify(schoolList),//{searchParams:JSON.stringify(schoolList)},
            success: function (res) {
                layer.close(loading);
                layer.msg(res.msg);
                // 假如返回的 res.data 是需要导出的列表数据
                console.log(res.data);//
                // 1. 数组头部新增表头
                res.data.unshift({
                    orderNumber: '订单号',
                    orderStatus: '订单状态',
                    payMoney: '应付金额',
                    money: '支付金额',
                    payWay: '支付方式',
                    orderTime: '下单时间',
                    payTime: '支付时间',
                    name: '收件人',
                    phone: '联系电话',
                    address: '收件地址',
                    expNo: '运单号'
                });
                // 3. 执行导出函数，系统会弹出弹框
                excel.exportExcel({
                    sheet1: res.data
                }, '订单信息列表.xlsx', 'xlsx');
            },
            error: function (res) {
                layer.close(loading);
                layer.msg(res.msg);
            }
        });
    });

    function open(url, data) {
        var index = layui.layer.open({
            title: "产品信息",
            type: 2,
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
                    body.find("select[name='oneClass'] option[value=" + data.oneClass + "]").prop("selected", true);
                    body.find(".originalPrice").val(data.originalPrice);
                    body.find(".salePrice").val(data.salePrice);
                    body.find(".stockQuantity").val(data.stockQuantity);
                }
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
    }

    function tableReload() {
        table.reload("orderTable", {
            page: {
                curr: 1
            },

            where: {
                "searchParams": {
                    "search_eq_uid": $("input[name='uid']").val(),
                    "search_eq_order_number": $("input[name='orderNumber']").val(),
                    "search_eq_order_status": $('select[name="orderStatus"] option:selected').val(),
                    "search_between_pay_time_start": $('input[name="payTimeStart"]').val() ? $('input[name="payTimeStart"]').val() + " 00:00:00" : '',
                    "search_between_pay_time_end": $('input[name="payTimeEnd"]').val() ? $('input[name="payTimeEnd"]').val() + " 23:59:59" : ''
                }
            }
        })
    }

    // 搜索
    $("#search").on("click", function () {
        tableReload();
    });
    //重置
    $("#searchReset").on("click", function () {
        // $("input[name='orderNumber']").val("");
        // // $('select[name="orderStatus"]')[0].rese;
        // $('input[name="payTimeStart"]').val("");
        // $('input[name="payTimeEnd"]').val("");
        form.render();
    });

    // 列表操作
    table.on('tool(orderTableList)', function (obj) {
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent) {
            case "edit":
                open("/mall/product/edit", data);
                break;
            case "status":
                updateStatus(data.id, data.status);
                break;
            case "detail":
                var index = layui.layer.open({
                    title: "订单详情",
                    type: 2,
                    area: ['700px', '350px'],
                    offset: '80px',
                    content: "/mall/orders/detail/" + data.orderNumber,
                    success: function (layero, index) {
                    }
                });
                break;
            case "exp":
                var index = layui.layer.open({
                    title: "物流信息",
                    type: 2,
                    area: ['450px', '450px'],
                    offset: '80px',
                    content: ["/mall/orders/exp/" + data.orderNumber, 'no'],
                    success: function (layero, index) {
                        var body = layui.layer.getChildFrame('body', index);
                        if (data) {
                            body.find("input[name='id']").val(data.id);
                            body.find("input[name='orderNumber']").val(data.orderNumber);
                            body.find("input[name='signName']").val(data.signName);
                            body.find("input[name='signPhone']").val(data.signPhone);
                            body.find(".signAddress").text(data.signAddress);
                            // body.find("select[name='expCode'] option[value=" + data.expCode + "]").prop("selected", true);
                            body.find("input[name='expNo']").val(data.expNo);
                        }
                    }
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
})