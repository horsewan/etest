//layui.config({
//    version: true //一般用于更新模块缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
//});
layui.use(['form', 'layer', 'table', 'util'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        util = layui.util,
        table = layui.table;

    //用户列表
    var tableIns = table.render({
        elem: '#userList',
        id: "userListTable",
        url: '/nq/user/dataTable',
        where: {
            nickName: $('#nickName').val(),
            phone: $('#phone').val(),
        },
        page: true,
        height: "full-128",
        limits: [10, 15, 20, 25],
        limit: 20,
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {
                field: 'delStatus', title: '开启/禁用用户', align: 'center', width: 150, templet: function (d) {
                    switch (d.delStatus) {
                        case 'Y':
                            return '<a class="layui-btn layui-btn-xs" lay-event="updateStaByID">关闭禁用</a>';
                        case 'N':
                            return '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="updateStaByID">开启禁用</a>';
                        default:
                            return '';
                    }
                }
            },
            {field: 'agentNo', title: '特助（客服）编号', align: "center", width: 250},
            {
                field: 'headImg', title: '头像', align: 'center', width: 100, templet: function (d) {
                    if (d.headImg) {
                        return '<img width="50" height="50" src="' + d.headImg + '" />';
                    } else {
                        return '';
                    }
                }
            },
            {field: 'mobile', title: '手机号', align: "center", width: 250},
            {field: 'nickName', title: '昵称', align: "center", width: 250},
            {
                field: 'sex', title: '性别', align: 'center', width: 100, templet: function (d) {
                    switch (d.sex) {
                        case 'F':
                            return '<a class="layui-btn layui-btn-radius layui-btn-normal layui-btn-xs">女</a>';
                        case 'M':
                            return '<a class="layui-btn layui-btn-radius layui-btn-warm layui-btn-xs">男</a>';
                        default:
                            return '';
                    }
                }
            },
            {
                field: 'registerTime', title: '注册时间', align: "center", width: 160, templet: function (d) {
                    if (d.registerTime) {
                        return util.toDateString(d.registerTime, 'yyyy-MM-dd HH:mm:ss');
                    } else {
                        return '';
                    }
                }
            },
            {
                field: 'updateTime', title: '更新时间', align: "center", width: 160, templet: function (d) {
                    if (d.updateTime) {
                        return util.toDateString(d.updateTime, 'yyyy-MM-dd HH:mm:ss');
                    } else {
                        return '';
                    }
                }
            },
            {field: 'addressD', title: '地址', align: "center", width: 250},

            {
                title: '操作', width: 150, fixed: "right", align: "center", templet: function (d) {
                    return '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>'
                    return '<a class="layui-btn layui-btn-xs ' + auth_detail + '" lay-event="detail">查看</a>'
                }
            }
        ]],
        responseFilter: function (result) {
            top.loginCheck(result);
        }
    });

    $(getSysAPISta());

    // 搜索
    $("#search").on("click", function () {
        tableReload();
    });

    $("#updateSta").on("click", function () {
                var updateStaText = $("#updateSta").text();
                layer.confirm("是否要["+updateStaText+"]", {
                    icon: 3,
                    title: '温馨提示',
                    closeBtn: 0,
                }, function (index) {
                    layer.close(index);
                    top.layer.msg(updateStaText+'中，请稍候...', {icon: 16, time: false, shade: 0.8});
                    top.asyncRequest({
                        url: "/nq/user/sysapi",
                        type: "post",
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                tip(result.msg);
                                getSysAPISta();
                            }
                        },
                        error: function () {
                            layer.close(index);
                        }
                    });
                }, function (index) {
                    layer.close(index);
                });
    });

    $("#updateStaReg").on("click", function () {
                var updateStaRegText = $("#updateStaReg").text();
                layer.confirm("是否要["+updateStaRegText+"]", {
                    icon: 3,
                    title: '温馨提示',
                    closeBtn: 0,
                }, function (index) {
                    layer.close(index);
                    top.layer.msg(updateStaRegText+'中，请稍候...', {icon: 16, time: false, shade: 0.8});
                    top.asyncRequest({
                        url: "/nq/user/userRegSta",
                        type: "post",
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                tip(result.msg);
                                getSysAPISta();
                            }
                        },
                        error: function () {
                            layer.close(index);
                        }
                    });
                }, function (index) {
                    layer.close(index);
                });
    });

    function getSysAPISta(){
          top.asyncRequest({
              url: "/nq/user/getSysAPI",
              type: "get",
              dataType: "json",
              success: function (result) {
                  if (result.success) {
                    if (result.data) {
                         $("#updateSta").text(result.data.sysApi=='Y'?'关闭普通用户模块':'开启普通用户模块');
                         $("#updateStaReg").text(result.data.sysUserReg=='Y'?'关闭普通用户不可注册':'开启普通用户不可注册');
                         if(result.data.sysApi=='Y'){
                            $("#updateSta").addClass('layui-btn-danger');
                         }else{
                             $("#updateSta").removeClass('layui-btn-danger');
                         }
                         if(result.data.sysUserReg=='Y'){
                             $("#updateStaReg").addClass('layui-btn-danger');
                          }else{
                              $("#updateStaReg").removeClass('layui-btn-danger');
                          }
                     }
                  }
              },
              error: function () {
                  layer.close(index);
              }
          });
    }

    function tableReload() {
        table.reload("userListTable", {
            page: {
                curr: $(".layui-laypage-em").next().html()
            },
            where: {
                nickName: $('#nickName').val(),
                phone: $('#phone').val()
            }
        })
    }
    

    function open(data) {
        var index = layui.layer.open({
            title: "用户信息",
            type: 2,
            content: "/nq/user/page",
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                if (data) {
                    body.find(".id").val(data.id);
                    body.find(".uid").val(data.uid);
                    body.find(".nickName").val(data.nickName);
                    body.find(".headImg").attr('src', data.headImg);
                    body.find(".sex input[value=" + data.sex + "]").prop('checked', true);
                    body.find(".delStatus").prop('checked', data.delStatus == 'N' ? false : true);
                    form.render();
                }
                setTimeout(function () {
                    layui.layer.tips('点击此处返回列表', '.layui-layer-setwin .layui-layer-close', {
                        tips: 3
                    });
                }, 500)
            }
        })
        layui.layer.full(index);
        window.sessionStorage.setItem("index", index);
        //改变窗口大小时，重置弹窗的宽高，防止超出可视区域（如F12调出debug的操作）
        $(window).on("resize", function () {
            layui.layer.full(window.sessionStorage.getItem("index"));
        })
    }

    // 列表操作
    table.on('tool(userList)', function (obj) {
        var layEvent = obj.event,
            data = obj.data;
        switch (layEvent) {
            case "detail":
                open(data);
                break
            case "del":
                layer.confirm("是否删除此用户?", {
                    icon: 3,
                    title: '温馨提示',
                    closeBtn: 0,
                }, function (index) {
                    layer.close(index);
                    top.layer.msg('删除中，请稍候...', {icon: 16, time: false, shade: 0.8});
                    top.asyncRequest({
                        url: "/nq/user/del/" + data.id,
                        type: "get",
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                tip("删除成功");
                                tableReload();
                            } else {
                                tip("删除失败");
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
                case "updateStaByID":
                layer.confirm("是否禁用此用户?", {
                    icon: 3,
                    title: '温馨提示',
                    closeBtn: 0,
                }, function (index) {
                    layer.close(index);
                    top.layer.msg('禁用中，请稍候...', {icon: 16, time: false, shade: 0.8});
                    top.asyncRequest({
                        url: "/nq/user/updateStaId/" + data.id+"/"+data.delStatus,
                        type: "get",
                        dataType: "json",
                        success: function (result) {
                            if (result.success) {
                                tip("禁用成功");
                                tableReload();
                            } else {
                                tip("禁用失败");
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
        }
    });

    function tip(msg) {
        top.layer.closeAll('loading')
        top.layer.msg(msg, {
            time: 2000
        });
    }
})