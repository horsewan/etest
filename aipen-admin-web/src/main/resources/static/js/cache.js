var cacheStr = window.sessionStorage.getItem("cache"),
    oneLoginStr = window.sessionStorage.getItem("oneLogin");
layui.use(['form', 'jquery', "layer"], function () {
    var form = layui.form,
        $ = layui.jquery,
        layer = parent.layer === undefined ? layui.layer : top.layer;

    //判断是否设置过头像，如果设置过则修改顶部、左侧和个人资料中的头像，否则使用默认头像
    if (window.sessionStorage.getItem('userFace') && $(".userAvatar").length > 0) {
        $("#userFace").attr("src", window.sessionStorage.getItem('userFace'));
        $(".userAvatar").attr("src", $(".userAvatar").attr("src").split("images/")[0] + "images/" + window.sessionStorage.getItem('userFace').split("images/")[1]);
    } else {
        $("#userFace").attr("src", "../../images/face.jpg");
    }

    //锁屏
    function lockPage() {
        layer.open({
            title: false,
            type: 1,
            content: '<div class="admin-header-lock" id="lock-box">' +
            '<div class="admin-header-lock-img"><img src="/images/face.png" width="80" height="80" class="userAvatar"/></div>' +
            '<div class="admin-header-lock-name" id="lockUserName">「'+loginName+'」</div>' +
            '<div class="input_btn">' +
            '<input type="password" class="admin-header-lock-input layui-input" autocomplete="off" placeholder="请输入密码解锁.." name="lockPwd" id="lockPwd" />' +
            '<button class="layui-btn" id="unlock">解锁</button>' +
            '</div>' +
            '<p>请输入登录密码，否则不会解锁成功喔!!!</p>' +
            '</div>',
            closeBtn: 0,
            shade: 0.9,
            success: function () {
                //判断是否设置过头像，如果设置过则修改顶部、左侧和个人资料中的头像，否则使用默认头像
                if (window.sessionStorage.getItem('userFace') && $(".userAvatar").length > 0) {
                    $(".userAvatar").attr("src", $(".userAvatar").attr("src").split("images/")[0] + "images/" + window.sessionStorage.getItem('userFace').split("images/")[1]);
                }
            }
        })
        $(".admin-header-lock-input").focus();
    }

    $(".lockcms").on("click", function () {
        window.sessionStorage.setItem("lockcms", true);
        lockPage();
    })
    // 判断是否显示锁屏
    if (window.sessionStorage.getItem("lockcms") == "true") {
        lockPage();
    }
    // 解锁
    $("body").on("click", "#unlock", function () {
        var _this = $(this).siblings(".admin-header-lock-input");
        if (_this.val() == '') {
            layer.msg("请输入解锁密码！");
            _this.focus();
        } else {
            asyncRequest({
                url: "/unlock",
                type: "post",
                data: {
                    "password": _this.val()
                },
                success: function (result) {
                    if(result.success){
                        window.sessionStorage.setItem("lockcms", false);
                        _this.val('');
                        layer.closeAll("page");
                    }else{
                        layer.msg("密码错误，请重新输入！");
                        _this.val('').focus();
                    }
                },
                error: function () {
                    layer.msg("密码错误，请重新输入！");
                    _this.val('').focus();
                }
            });
            /*if ($(this).siblings(".admin-header-lock-input").val() == "123456") {
                window.sessionStorage.setItem("lockcms", false);
                $(this).siblings(".admin-header-lock-input").val('');
                layer.closeAll("page");
            } else {
                layer.msg("密码错误，请重新输入！");
                $(this).siblings(".admin-header-lock-input").val('').focus();
            }*/
        }
    });
    $(document).on('keydown', function (event) {
        var event = event || window.event;
        if (event.keyCode == 13) {
            $("#unlock").click();
        }
    });

    //退出
    $(".signOut").click(function () {
        window.sessionStorage.removeItem("menu");
        menu = [];
        window.sessionStorage.removeItem("curmenu");
    })
})