layui.use(['form','layer','upload'],function(){
    var form = layui.form,
        upload = layui.upload,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    var batchImport = upload.render({
        elem: '#upload',
        url: '/nq/ble/import/' + $('#pkgId').val(),
        accept: 'file',
        exts: 'xls|xlsx',
        before: function(obj){
            parent.layer.msg('文件上传中，请稍候...',{icon: 16, time:false, shade:0.8});
        },
        done: function(res){
            console.log(res)
            if(res.success){
                close("文件上传成功，后台正在导入数据，稍后请刷新查看导入数据");
            }else{
                close("文件上传失败");
            }
        },
        error: function(){
            //请求异常回调
        }
    });
    // 关闭窗口
    function close(msg) {
        parent.layer.closeAll('');
        if(msg){
            parent.layer.msg(msg, {
                time: 3000
            });
        }
    }
});