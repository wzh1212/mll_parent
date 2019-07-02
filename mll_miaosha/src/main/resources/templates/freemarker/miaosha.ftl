<!DOCTYPE HTML>
<html>
<head>
    <title>商品详情</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- jquery -->
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <!-- bootstrap -->
    <link rel="stylesheet" type="text/css"  href="/bootstrap/css/bootstrap.min.css"/>
    <script type="text/javascript"  src="/bootstrap/js/bootstrap.min.js"></script>
    <!-- jquery-validator -->
    <script type="text/javascript"  src="/jquery-validation/jquery.validate.min.js"></script>
    <script type="text/javascript" src="/jquery-validation/localization/messages_zh.min.js"></script>
    <!-- layer -->
    <script type="text/javascript"  src="/layer/layer.js"></script>
    <!-- md5.js -->
    <script type="text/javascript"  src="/js/md5.min.js"></script>
    <!-- common.js -->
    <script type="text/javascript"  src="/js/common.js"></script>
</head>
<body>

<div class="panel panel-default">
    <div class="panel-heading">秒杀商品详情</div>
    <div class="panel-body">
        <#if user??>
            <span> 欢迎您${user}<br/></span>
        <#else>
                <span> 您还没有登录，请登陆后再操作<br/></span>
        </#if>
        <span>没有收货地址的提示。。。</span>
    </div>
    <table class="table" id="productList">
        <tr>
            <td>商品名称</td>
            <td colspan="3">${product.title}</td>
        </tr>
        <tr>
            <td>商品图片</td>
            <td colspan="3"><img src="${product.image}" width="200" height="200" /></td>
        </tr>
        <tr>
            <td>秒杀开始时间</td>
            <td>${product.startDate?string("yyyy-MM-dd HH:mm:ss")}</td>
            <td id="miaoshaTip">
                <input type="hidden" id="remainSeconds" value="${remainSeconds}" />
                <#if miaoshaStatus==0>
                    <span> 秒杀倒计时：<span id="countDown">${remainSeconds}</span>秒</span>
                <#elseif miaoshaStatus==1>
                        <span>秒杀进行中</span>
                <#else>
                    <span>秒杀结束</span>
                </#if>
            </td>
            <td>
                <#--<form id="miaoshaForm" method="post" action="/miaosha/do_miaosha">-->
                    <#--<button class="btn btn-primary btn-block" type="submit" id="buyButton">立即秒杀</button>-->
                    <#--<input type="hidden" name="productId" value="${product.productId}" />-->
                <#--</form>-->
          <#--      <button class="btn btn-primary btn-block" type="button" id = "buyButton" onclick="doMiaosha()">立即秒杀</button>
                <input type = "hidden" id = productId value="${product.productId?c}">-->

                <div class="row">
                    <div class="form-inline">
                        <img id="verifyCodeImg" width="80" height="32"  style="display:none" onclick="refreshVerifyCode()"/>
                        <input id="verifyCode"  class="form-control" style="display:none"/>
                        <button class="btn btn-primary" type="button" id="buyButton" onclick="doMiaosha()">立即秒杀</button>
                        <input type="hidden" id="productId" value="${product.productId?c}" />
                    </div>
                </div>

            </td>
        </tr>
        <tr>
            <td>商品原价</td>
            <td colspan="3" >${product.price?if_exists?string.number}</td>
        </tr>
        <tr>
            <td>秒杀价</td>
            <td colspan="3">${product.miaoshaPrice?if_exists?string.number}</td>
        </tr>
        <tr>
            <td>库存数量</td>
            <td colspan="3"> ${product.stockCount}</td>
        </tr>
    </table>
</div>
</body>
<script>
    $(function(){
        countDown();
    });
    function  doMiaosha() {
        $.ajax({
            url:"/miaosha/do_miaosha/"+$("#productId").val() + "/" + $("#verifyCode").val(),
            type:"POST",
            success:function (data) {
                if (data.code == 0 ){
                    //秒杀成功，查看订单详情
                    layer.msg("秒杀成功，查看我的订单")
                    $("#buyButton").attr("disabled",true);//置灰按钮
                } else {
                    //失败，打印秒杀失败信息
                    layer.msg(data.msg);
                }
            },
            error:function () {
                layer.msg("客户端请求有误");
            }
        })
    }
    function countDown(){
        var remainSeconds = $("#remainSeconds").val();
        var timeout;
        if(remainSeconds > 0){//秒杀还没开始，倒计时
            $("#buyButton").attr("disabled", true);
            timeout = setTimeout(function(){
                $("#countDown").text(remainSeconds - 1);
                $("#remainSeconds").val(remainSeconds - 1);
                countDown();
            },1000);
        }else if(remainSeconds == 0){//秒杀进行中
            $("#buyButton").attr("disabled", false);
            if(timeout){
                clearTimeout(timeout);
            }
            $("#miaoshaTip").html("秒杀进行中");

            //访问接口获得验证码图片,通过商品id组装一下
            $("#verifyCodeImg").attr("src", "/miaosha/verifyCode/"+$("#productId").val());
            $("#verifyCodeImg").show();
            $("#verifyCode").show();

        }else{//秒杀已经结束
            $("#buyButton").attr("disabled", true);
            $("#miaoshaTip").html("秒杀已经结束");
        }
    }

    // 刷新验证码
    function refreshVerifyCode(){
        //加时间改变请求url 避免被浏览器缓存
        $("#verifyCodeImg").attr("src", "/miaosha/verifyCode/"+$("#productId").val()+"?timestamp="+new Date().getTime());
    }


</script>
</html>