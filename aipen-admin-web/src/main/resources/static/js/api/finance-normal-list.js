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
        elem: '#financeNormalTableList',
        id: "financeNormalTable",
        url: '/mall/finance/dataTable',
        page: true,
        height: "full-128",
        limits: [10, 15, 20, 25],
        limit: 10,
        even:true,
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: 'singleNo', title: '客服编号', align: "center"},
            {field: 'mobile', title: '手机号', align: "center"},
            {field: 'nums', title: '用户总成交量', align: "center"},
            {field: 'shopNums', title: '商家总成交量', align: "center"},
            {field: 'amountRealpay', title: '用户总成交金额', align: "center"},
            {field: 'shopPercentage', title: '商家总成交金额', align: "center"},
            // {field: 'money', title: '总省钱金额', align: "center"},
            {field: 'userPercentage', title: '用户提成', align: "center"},
            {field: 'money', title: '商家提成', align: "center"}
            // ,{field: 'shopSort', title: '常去商家',width: 300, align: "center"}
            // ,
            // {
            //     field: 'createTime', title: '交易时间', align:"center", templet:function (d) {
            //          return util.toDateString(d.createTime);
            //     }
            //  }

        ]],
        where: {
            "phone": $('#bPhone').val(),
            "userType":'normal',
            "singleNo":$("#agentNo").val()
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
            content: "/mall/financeNormal/page",
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if (data) {
                    body.find(".id").val(data.id);
                    body.find(".bName").val(data.bName);
                    body.find("#bName").attr("disabled","disabled");
                    body.find("#bTicket").attr("disabled","disabled");
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
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        });
    }


    $('#device_search').on('click', function () {
        pkgTableReload();
    });

    $('#pkg_add').on('click', function () {
        openCreate("/mall/financeNormal/add");
    });


    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }

});

function pkgTableReload() {
    table.reload("financeNormalTable", {
        page: {
            curr: 1
        },
        where: {
            "phone": $('#bPhone').val(),
            "userType":'normal',
            "singleNo":$("#agentNo").val()
        },
    })
}
