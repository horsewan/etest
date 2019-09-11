layui.use(['form', 'layer', 'table', 'util', 'laydate'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        util = layui.util,
        table = layui.table;
    laydate.render({
        elem: '#recordTimeStart'
    });

    laydate.render({
        elem: '#recordTimeEnd'
    });

    var tableIns = table.render({
        elem: '#recordTableList',
        id: "recordTable",
        url: '/sys/record/recordTable',
        method: 'POST',
        contentType: 'application/json',
        where: {
            "searchParams": {
                "search_eq_pkg_name": $('input[name="pkg_name"]').val(),
                "search_like_device_info": $('input[name="device_info"]').val(),
                "search_between_op_time_start": $('input[name="recordTimeStart"]').val() ? $('input[name="recordTimeStart"]').val() + " 00:00:00" : '',
                "search_between_op_time_end": $('input[name="recordTimeEnd"]').val() ? $('input[name="recordTimeEnd"]').val() + " 23:59:59" : ''
            }
        },
        page: true,
        height: "full-105",
        limits: [10, 15, 20, 25],
        limit: 20,
        cols: [[
            {field: 'action', title: '操作', align: "center", width: 100},
            {field: 'engine', title: '引擎', align: "center", width: 100},
            {field: 'pkgName', title: '包名', align: "center",width:170,
                templet: function (d) {
                    return '<span style="color: yellowgreen">'+d.pkgName+'</span>';
                }},
            {field: 'opStatus', title: '操作状态', align: "center",width: 150,
                templet: function (d) {
                    if (d.opStatus=='Y') {
                        return '<span style="color: black">成功</span>'
                    } else {
                        return '<span style="color: red">失败</span>'
                    }
                }
            },
            {field: 'opLossTime', title: '耗时(秒)', align: "center",width: 180,
                templet: function (d) {
                if(d.opLossTime!=null&&d.opLossTime!=''){
                    var losstime = d.opLossTime/1000;
                    return losstime;
                }else{
                    return '未知时间';
                }
                }},
            {field: 'opTime', title: '操作时间', align: "center",width: 180},
            {field: 'exception', title: '异常',align: "center",
                templet: function (d) {
                    if(d.exception!=null){
                        return '<span style="color: darkred">'+d.exception+'</span>';
                    }else{
                        return '';
                    }
                }},
            {field: 'deviceInfo', title: '设备信息', align: "center",
                templet: function (d) {
                    try {
                        var obj = JSON.parse(d.deviceInfo);
                        if (typeof obj == "object") {
                            var property = '<div style="height: 100%;font-size: 30px"><textarea style="width: 100%;height: 100%;font-size:17px;background-color: #A8A297">';
                            for(var item in obj){
                                property+='"'+item+'"'+':'+'"'+obj[item]+'"'+'\r\n';
                            }
                            property+='</textarea>'+' </div>';
                            return property;
                        }else{
                            return '<textarea style="width: 100%;height: 100%;font-size:17px;">'+d.deviceInfo+'</textarea>';
                        }
                    } catch(e) {
                        return '<textarea style="width: 100%;height: 100%;font-size:17px;">'+d.deviceInfo+'</textarea>';
                    }
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
    $("#search").on("click", function () {
        table.reload("recordTable", {
            page: {
                curr: 1
            },
            where: {
                "searchParams": {
                    "search_eq_pkg_name": $('input[name="pkg_name"]').val(),
                    "search_like_device_info": $('input[name="device_info"]').val(),
                    "search_between_op_time_start": $('input[name="recordTimeStart"]').val() ? $('input[name="recordTimeStart"]').val() + " 00:00:00" : '',
                    "search_between_op_time_end": $('input[name="recordTimeEnd"]').val() ? $('input[name="recordTimeEnd"]').val() + " 23:59:59" : ''
                }
            }
        })
    });
})