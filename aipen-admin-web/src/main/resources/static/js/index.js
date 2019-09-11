var $, tab, dataStr, layer;

layui.config({
    base: "js/"
}).extend({
    "bodyTab": "bodyTab"
})

layui.use(['bodyTab', 'form', 'element', 'layer', 'jquery'], function () {

    var form = layui.form,
        element = layui.element;
    $ = layui.$;
    layer = parent.layer === undefined ? layui.layer : top.layer;
    tab = layui.bodyTab({
        openTabNum: "50",
        url: "/sys/user/loadMenus"
    });

    //通过顶部菜单获取左侧二三级菜单
    function getMenuData() {
        $.getJSON(tab.tabConfig.url, function (result) {
            if (result.success) {
                dataStr = result.data;
            }
            tab.render();
        })
    }

    //页面加载时判断左侧菜单是否显示
    //通过顶部菜单获取左侧菜单
    $(".topLevelMenus li,.mobileTopLevelMenus dd").click(function () {
        if ($(this).parents(".mobileTopLevelMenus").length != "0") {
            $(".topLevelMenus li").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
        } else {
            $(".mobileTopLevelMenus dd").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
        }
        $(".layui-layout-admin").removeClass("showMenu");
        $("body").addClass("site-mobile");
        getMenuData($(this).data("menu"));
        //渲染顶部窗口
        tab.tabMove();
    })

    //隐藏左侧导航
    $(".hideMenu").click(function () {
        if ($(".topLevelMenus li.layui-this a").data("url")) {
            layer.msg("此栏目状态下左侧菜单不可展开");  //主要为了避免左侧显示的内容与顶部菜单不匹配
            return false;
        }
        $(".layui-layout-admin").toggleClass("showMenu");
        //渲染顶部窗口
        tab.tabMove();
    })

    //通过顶部菜单获取左侧二三级菜单
    getMenuData();

    //手机设备的简单适配
    $('.site-tree-mobile').on('click', function () {
        $('body').addClass('site-mobile');
    });
    $('.site-mobile-shade').on('click', function () {
        $('body').removeClass('site-mobile');
    });

    // 添加新窗口
    $("body").on("click", ".layui-nav .layui-nav-item a:not('.mobileTopLevelMenus .layui-nav-item a')", function () {
        //如果不存在子级
        if ($(this).siblings().length == 0) {
            addTab($(this));
            $('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
        }
        $(this).parent("li").siblings().removeClass("layui-nav-itemed");
    })

    //刷新后还原打开的窗口
    if (cacheStr == "true") {
        if (window.sessionStorage.getItem("menu") != null) {
            menu = JSON.parse(window.sessionStorage.getItem("menu"));
            curmenu = window.sessionStorage.getItem("curmenu");
            var openTitle = '';
            for (var i = 0; i < menu.length; i++) {
                openTitle = '';
                if (menu[i].icon) {
                    if (menu[i].icon.split("-")[0] == 'icon') {
                        openTitle += '<i class="seraph ' + menu[i].icon + '"></i>';
                    } else {
                        openTitle += '<i class="layui-icon">' + menu[i].icon + '</i>';
                    }
                }
                openTitle += '<cite>' + menu[i].title + '</cite>';
                openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + menu[i].layId + '">&#x1006;</i>';
                element.tabAdd("bodyTab", {
                    title: openTitle,
                    content: "<iframe src='" + menu[i].href + "' data-id='" + menu[i].layId + "'></frame>",
                    id: menu[i].layId
                })
                //定位到刷新前的窗口
                if (curmenu != "undefined") {
                    if (curmenu == '' || curmenu == "null") {  //定位到后台首页
                        element.tabChange("bodyTab", '');
                    } else if (JSON.parse(curmenu).title == menu[i].title) {  //定位到刷新前的页面
                        element.tabChange("bodyTab", menu[i].layId);
                    }
                } else {
                    element.tabChange("bodyTab", menu[menu.length - 1].layId);
                }
            }
            //渲染顶部窗口
            tab.tabMove();
        }
    } else {
        window.sessionStorage.removeItem("menu");
        window.sessionStorage.removeItem("curmenu");
    }

})

//打开新窗口
function addTab(_this) {
    tab.tabAdd(_this);
}

/**
 * TODO 异步请求封装方法
 */
function asyncRequest(opt) {
    opt = $.extend({
        url: '',
        type: 'get',
        dataType: 'json',
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        data: {}
    }, opt);
    $.ajax({
        url: opt.url,
        type: opt.type,
        dataType: opt.dataType,
        contentType: opt.contentType,
        data: opt.data,
        success: function (result) {
            var code = result.code || 0;
            switch (code) {
                // 登录超时
                case -1:
                    toLogin("登录超时,请重新登录");
                    break;
                // 异地登录，强制退出
                case 3:
                    toLogin("该账号已在其他机器登录,您已被强制退出");
                    break;
                default:
                    "function" === typeof opt.success && opt.success(result);
                    break;
            }
        },
        error: function (e) {
            "function" === typeof opt.error && opt.error(e);
        }
    });
}

function toLogin(msg) {
    layer.confirm(msg, {
        icon: 3,
        title: '温馨提示',
        closeBtn: 0,
        btn: ['重新登录'],
        btnAlign: 'c'
    }, function (index) {
        layer.close(index);
        window.sessionStorage.setItem("lockcms", false);
        top.window.location.href = "/login"
    });
}

function loginCheck(result) {
    if(result.code == -1 || result.code == 3){
        switch (result.code){
            case -1:
                toLogin("登录超时,请重新登录");
                break;
            case 3:
                toLogin("该账号已在其他机器登录,您已被强制退出");
                break;
        }
        return false;
    }
    return true;
}

/*$.fn.serializeObject*/

function serializeObject(jQueryObj) {
    var o = {};
    var a = jQueryObj.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};