var table, $, currentPkgId, currentAppName = "";
var deviceTable, pkgTable;
layui.config({
    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
});
layui.use(['form', 'layer', 'table', 'util'], function () {
    var form = layui.form,
        util = layui.util,
        layer = parent.layer === undefined ? layui.layer : top.layer;
    $ = layui.jquery,
        table = layui.table;

    pkgTable = table.render({
        elem: '#financeShopTableList',
        id: "financeShopTable",
        url: '/mall/finance/dataTable',
        page: true,
        even: true,
        height: "full-128",
        limits: [8, 16, 24, 32, 48],
        limit: 8,
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: 'nickName', title: '企业名称', align: "center", width: 220},
            {field: 'mobile', title: '手机号', align: "center", width: 220},
            {field: 'singleNo', title: '客服编号', align: "center"},
            {field: 'nums', title: '总成交量', align: "center"},
            {field: 'amountRealpay', title: '总成交金额', align: "center"},
            {field: 'sumUser', title: '总成交用户', width: 150, align: "center"},
            {field: 'activeUser', title: '活跃用户', width: 100, align: "center"},
            {
                field: 'back', title: '回头率', width: 100, align: "center"
            }
            // ,
            // {
            //     field: 'createTime', title: '交易时间', align:"center", templet:function (d) {
            //          return util.toDateString(d.createTime);
            //     }
            //  }

        ]],
        where: {
            "phone": $('#bPhone').val(),
            "userType": 'shop',
            "singleNo": $("#agentNo").val()
        },
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });


    function openPkg(url, data) {
        var index = layui.layer.open({
            title: "商户信息",
            type: 2,
            area: ['500px', '400px'],
            offset: '100px',
            content: "/mall/financeShop/page",
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
        openCreate("/mall/financeShop/add");
    });


    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }

});

function pkgTableReload() {
    table.reload("financeShopTable", {
        page: {
            curr: 1
        },
        where: {
            "phone": $('#bPhone').val(),
            "userType": 'shop',
            "singleNo": $("#agentNo").val()
        },
    })
}
