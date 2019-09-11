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
        elem: '#pkgTableList',
        id: "pkgTable",
        url: '/nq/pkg/dataTable/2',
        page: true,
        height: "full-128",
        limits: [8, 16, 24, 32, 48],
        limit: 8,
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: 'appPkg', title: '客服经理编号', align: "center"},
            {field: 'appName', title: '客服经理名称', align: "center", width: 120},
            {field: 'signature', title: '授权手机号', align: "center"},
            /*{
                field: 'appType', title: '类型', align: "center", width: 100, templet: function (d) {
                    switch (d.appType) {
                        case "1":
                            return '翻译机';
                        case "2":
                            return '蓝牙';
                        default:
                            return '';
                    }
                }
            },*/
            /*{
                field: 'valid', title: '校验代理', align: "center", width: 120, templet: function (d) {
                    switch (d.valid) {
                        case 'N':
                            return '<input type="checkbox" name="switch" lay-skin="switch" value="' + d.id + '" lay-text="开启|关闭" lay-filter="valid" '+auth_check_device+' />';
                        case 'Y':
                            return '<input type="checkbox" name="switch" lay-skin="switch" value="' + d.id + '" lay-text="开启|关闭" lay-filter="valid" checked '+auth_check_device+' />';
                        default:
                            return '';
                    }
                }
            },*/
            /*{
                field: 'voiceEngineSwitch', title: '引擎开关',align: "center",width:100,templet:function (d) {
                    switch (d.voiceEngineSwitch) {
                        case "0":
                            return '<input type="checkbox" name="EngineSwitch" lay-skin="switch" value="' + d.id + '" lay-text="开启|关闭"  lay-filter="engine_switch" ' + auth_engine_switch + ' />';
                        case "1":
                            return '<input type="checkbox" name="EngineSwitch" lay-skin="switch" value="' + d.id + '" lay-text="开启|关闭" lay-filter="engine_switch" ' + auth_engine_switch + ' checked />';
                        default:
                            return '<input type="checkbox" name="EngineSwitch" lay-skin="switch"  value="' + d.id + '" lay-text="开启|关闭" lay-filter="engine_switch" ' + auth_engine_switch + ' checked />';
                    }
                }
            },*/
            /*{
                field: 'enableStatus',title:'客服经理开关',align:"center",width:100,templet:function (d) {
                    switch (d.enableStatus) {
                        case "N":
                            return '<input type="checkbox" name="enableStatus" lay-skin="switch" value="' + d.id + '" lay-text="开启|关闭"  lay-filter="enable_status" ' + auth_enable_status + ' />';
                        case "Y":
                            return '<input type="checkbox" name="enableStatus" lay-skin="switch" value="' + d.id + '" lay-text="开启|关闭" lay-filter="enable_status" ' + auth_enable_status + ' checked />';
                        default:
                            return '';
                    }
                }
            },*/
            /*{
                field: 'defaultUseCount', title: '使用次数', align: "center", width: 100, templet: function (d) {
                    if (d.defaultUseCount == -1) {
                        return '无限次';
                    } else {
                        return d.defaultUseCount;
                    }
                }
            },*/
            {
                field: '', title: '操作', align: "center", width: 250, fixed: 'right', templet: function (d) {
                    return '<div><a class="layui-btn layui-btn-xs layui-btn-normal '+auth_edit+'" lay-event="edit" style="padding-left: 16px; padding-right: 16px">编辑</a><a class="layui-btn layui-btn-xs layui-btn-danger '+auth_list+'" lay-event="checkDevice" style="padding-left: 16px; padding-right: 16px">查看客服列表</a>'
                           ;/*'<a class="layui-btn layui-btn-xs '+auth_add+' " lay-event="addDevice" style="padding-left: 16px; padding-right: 16px">添加客服</a></div>'<a class="layui-btn layui-btn-xs layui-btn-warm '+auth_add+' " lay-event="batchImport">批量导入客服</a>*/
                }
            }
        ]],
        where: {
            "appPkg": $('#appPkg').val(),
            "phone": $('#phone').val()
        },
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    initDeviceTable();

    // 应用列表操作
    table.on('tool(pkgTableList)', function (obj) {
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent) {
            case "edit":
                openPkg("/nq/pkg/edit", data);
                break;
            case "addDevice":
                currentPkgId = data.id;
                currentAppName = data.appName;
                openDevice("/nq/ble/add", data.id, data.appName);
                break;
            case "checkDevice":
                currentPkgId = data.id;
                currentAppName = data.appPkg;
                $('#mac').val("");
                $('#agentNo').val("");
                deviceTableReload(data.appPkg);//appName是客服经理编号
                break;
            case "batchImport":
                var index = layui.layer.open({
                    title: "导入蓝牙设备MAC信息",
                    type: 2,
                    area: ['350px', '150px'],
                    offset: 200,
                    content: ["/nq/ble/import/" + data.id, 'no'],
                    success: function (layero, index) {

                    }
                });
                break;
        }
    });
//引擎开关操作
    form.on('switch(engine_switch)', function (data) {
        var tipText = data.elem.checked ? '确认开启引擎?' : '确认关闭引擎?';
        layer.confirm(tipText, {
            icon: 3,
            title: '温馨提示',
            closeBtn: 0,
        }, function (index) {
            layer.close(index);
            top.layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
            top.asyncRequest({
                url: "/nq/pkg/engine/" + data.value,
                type: "get",
                data: data.field,
                dataType: "json",
                success: function (result) {
                    if (result.success) {
                        tips(data.elem.checked ? '引擎已开启' : '引擎已关闭');
                    } else {
                        tips(data.elem.checked ? '开启失败' : '关闭失败');
                        data.elem.checked = !data.elem.checked;
                        form.render();
                        layer.close(index);
                    }
                },
                error: function () {
                    tips(data.elem.checked ? '开启失败' : '关闭失败');
                    data.elem.checked = !data.elem.checked;
                    form.render();
                    layer.close(index);
                }
            });
        }, function (index) {
            data.elem.checked = !data.elem.checked;
            form.render();
            layer.close(index);
        });
    });
    //应用开关操作
    form.on('switch(enable_status)', function (data) {
        var tipText = data.elem.checked ? '确认开启应用?' : '确认关闭应用?';
        layer.confirm(tipText, {
            icon: 3,
            title: '温馨提示',
            closeBtn: 0,
        }, function (index) {
            layer.close(index);
            top.layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
            top.asyncRequest({
                url: "/nq/pkg/app/" + data.value,
                type: "get",
                data: data.field,
                dataType: "json",
                success: function (result) {
                    if (result.success) {
                        tips(data.elem.checked ? '应用已开启' : '应用已关闭');
                    } else {
                        tips(data.elem.checked ? '开启失败' : '关闭失败');
                        data.elem.checked = !data.elem.checked;
                        form.render();
                        layer.close(index);
                    }
                },
                error: function () {
                    tips(data.elem.checked ? '开启失败' : '关闭失败');
                    data.elem.checked = !data.elem.checked;
                    form.render();
                    layer.close(index);
                }
            });
        }, function (index) {
            data.elem.checked = !data.elem.checked;
            form.render();
            layer.close(index);
        });
    });
    // 设备列表操作
    table.on('tool(bleTableList)', function (obj) {
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent) {
            case "edit":
                openDevice("/nq/ble/edit", data.pkgId, data.ausn, data);
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
                        url: "/nq/ble/del/" + data.id,
                        type: "get",
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                tips('删除成功');
                                deviceTableReload(data.appName);
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

    form.on('switch(valid)', function (data) {
        var tipText = data.elem.checked ? '确认开启设备校验?' : '确认关闭设备校验?';
        layer.confirm(tipText, {
            icon: 3,
            title: '温馨提示',
            closeBtn: 0,
        }, function (index) {
            layer.close(index);
            top.layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
            top.asyncRequest({
                url: "/nq/pkg/verify/" + data.value,
                type: "get",
                data: data.field,
                dataType: "json",
                success: function (result) {
                    if (result.success) {
                        tips(data.elem.checked ? '开启成功' : '关闭成功');
                    } else {
                        tips(data.elem.checked ? '开启失败' : '关闭失败');
                        data.elem.checked = !data.elem.checked;
                        form.render();
                        layer.close(index);
                    }
                },
                error: function () {
                    tips(data.elem.checked ? '开启失败' : '关闭失败');
                    data.elem.checked = !data.elem.checked;
                    form.render();
                    layer.close(index);
                }
            });
        }, function (index) {
            data.elem.checked = !data.elem.checked;
            form.render();
            layer.close(index);
        });
    });

    form.on('switch(status)', function (data) {
        var tipText = data.elem.checked ? '确认启用当前设备?' : '确认禁用当前设备?';
        layer.confirm(tipText, {
            icon: 3,
            title: '温馨提示',
            closeBtn: 0,
        }, function (index) {
            layer.close(index);
            top.layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
            top.asyncRequest({
                url: "/nq/ble/status/" + data.value,
                type: "get",
                data: data.field,
                dataType: "json",
                success: function (result) {
                    if (result.success) {
                        tips(data.elem.checked ? '启用成功' : '禁用成功');
                    } else {
                        tips(data.elem.checked ? '启用失败' : '禁用失败');
                        data.elem.checked = !data.elem.checked;
                        form.render();
                        layer.close(index);
                    }
                },
                error: function () {
                    tips(data.elem.checked ? '启用失败' : '禁用失败');
                    data.elem.checked = !data.elem.checked;
                    form.render();
                    layer.close(index);
                }
            });
        }, function (index) {
            data.elem.checked = !data.elem.checked;
            form.render();
            layer.close(index);
        });
    });

    function openPkg(url, data) {

        var index = layui.layer.open({
            title: "客服经理信息",
            type: 2,
            area: ['300px', '300px'],
            offset: '100px',
            content: ["/nq/pkg/page?appId=" + (data ? data.id : 0), 'no'],
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                if (data) {
                    body.find(".id").val(data.id);
                    body.find(".appPkg").val(data.appPkg);
                    body.find("#signatureDiv").removeClass('layui-hide');
                    body.find(".signature").val(data.signature);
                    body.find(".appName").val(data.appName);
                    if (data.defaultUseCount != -1) {
                        body.find("input[name='useCount'][value='0']").prop('checked', true);
                        body.find("#numInput").parent().removeClass('layui-hide');
                        body.find("#numInput").val(data.defaultUseCount);
                    }
                    body.find("select[name='appType'] option[value="+data.appType+"]").prop("selected", true);
                }
            }
        });
        // layui.layer.full(index);
        window.sessionStorage.setItem("index",index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize",function(){
            layui.layer.full(window.sessionStorage.getItem("index"));
        });
    }

    function openDevice(url, pkgId, appName, data) {
        var index = layui.layer.open({
            title: "客服信息",
            type: 2,
            content: ["/nq/ble/page", 'no'],
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                body.find("form").attr("action", url);
                body.find(".pkgId").val(pkgId);
                body.find(".appName").val(appName);
                if (data) {
                    body.find(".id").val(data.id);
                    body.find(".mac").val(data.mac);
                    body.find(".ausn").val(data.ausn);
                    body.find("#ausn").removeClass('layui=hide');
                }
            }
        });
    }

    $('#pkg_search').on('click', function () {
        pkgTableReload();
    });

    $('#pkg_add').on('click', function () {
        openPkg("/nq/pkg/add");
    });

    $('#pkg_del').on('click', function () {
        var checkStatus = table.checkStatus('pkgTable'), ids = [];
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
                url: "/nq/pkg/del",
                type: "post",
                dataType: "json",
                data: {
                    ids: ids
                },
                success: function (result) {
                    if (result.success) {
                        tips('删除成功');
                        pkgTableReload();
                        initDeviceTable();
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


    $('#asr_config').on('click', function () {
        languageEngine('asr');
    });
    $('#nmt_config').on('click', function () {
        languageEngine('nmt');
    });
    $('#tts_config').on('click', function () {
        languageEngine('tts');
    });
    
    $('#server_config').on('click', function () {
        languageEngine('server');
    });

    function tips(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }

    $('#device_search').on('click', function () {
        if(""==currentAppName){
            tips("请先点击查看客服列表按钮!")
            return;
        }

        table.reload("deviceTable", {
            url: "/nq/ble/dataTable/" + currentAppName,
            page: {
                curr: 1
            },
            where: {
                'mac': $('#mac').val(),
                'agentNo': $('#agentNo').val()
            }
        });
    })

    function initDeviceTable() {
        deviceTable = table.render({
            elem: '#bleTableList',
            id: "deviceTable",
            page: true,
            height: "full-128",
            limits: [10, 15, 20, 25],
            limit: 20,
            cols: [[
                {field: 'ausn', title: '客服编号', align: "center"},
                {field: 'mac', title: '客服手机号', align: "center"},
                /*{
                    field: 'enableStatus', title: '客服状态', align: "center", width: 100, templet: function (d) {
                        switch (d.enableStatus) {
                            case 'N':
                                return '<input type="checkbox" name="switch" lay-skin="switch" value="' + d.id + '" lay-text="启用|禁用" lay-filter="status" ' + auth_status + ' />';
                            case 'Y':
                                return '<input type="checkbox" name="switch" lay-skin="switch" value="' + d.id + '" lay-text="启用|禁用" lay-filter="status" ' + auth_status + ' checked />';
                            default:
                                return '';
                        }
                    }
                },*/
                {
                    field: '', title: '操作', align: "center", width: 180, templet: function (d) {
                        return '<div style="height: 30px;line-height: 30px;"><a class="layui-btn layui-btn-xs layui-btn-normal '+auth_edit+'" lay-event="edit" style="padding-left: 16px; padding-right: 16px">编辑</a><a class="layui-btn layui-btn-xs layui-btn-danger '+auth_delete+'" lay-event="del" style="padding-left: 16px; padding-right: 16px">删除</a></div>';
                    }
                }
            ]],
            where: {
                'mac': $('#mac').val(),
                'agentNo': $('#agentNo').val()
            },
            responseFilter: function (result) {
                top.loginCheck(result);
            }
        });
    }
});

function pkgTableReload() {
//    $('#imei').val('');
    table.reload("pkgTable", {
        page: {
            curr: 1
        },
        where: {
            "appPkg": $('#appPkg').val(),
            "phone": $('#phone').val()
        }
    })
}

function deviceTableReload(appName) {
    //currentPkgId = pkgId;
    table.reload("deviceTable", {
        url: "/nq/ble/dataTable/" + appName,
        page: {
            curr: 1
        },where: {
            'mac': $('#mac').val(),
            'agentNo': $('#agentNo').val()
        }
    })
}