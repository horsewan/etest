package com.eningqu.domain.api;

/**
 * 描    述：  TODO  结算中心
 * 作    者：  YangHuangPing
 * 邮    箱：  171341296@qq.com
 * 日    期：  2018/12/27 15:39
 */
public class FinanceResult{


    //TODO 周/月/年用户、交易金额、支付订单状态分别统计
    /*select COUNT(uid) AS uidTotal,sum(amount_realpay) amountTotal,
    SUM(IF(order_status = 1, 1, 0)) AS status1Total,
    SUM(IF(order_status = 2, 1, 0)) AS status2Total,
    SUM(IF(order_status = 3, 1, 0)) AS status3Total,
    SUM(IF(order_status = 4, 1, 0)) AS status4Total,
    SUM(IF(order_status = 5, 1, 0)) AS status5Total from nq_orders */
    //where YEAR(pay_time)=YEAR(NOW())
    //DATE_FORMAT( pay_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )
    //to_days(pay_time) = to_days(now())

    //周/月/年支付用户、交易金额、支付订单状态分别统计
    private String uidTotal;//总用户
    private String amountTotal;//总交易金额
    private String status1Total;//待支付	1
    private String status2Total;//已取消	2
    private String status3Total;//已支付	3
    private String status4Total;//已发货	4
    private String status5Total;//线下付	5


    //市级代理交易数据，商家/普通用户
    private String agentBusinessTotal;//该市级代理下的总商家交易总额
    private String agentUserTotal;//该市级代理下的总用户交易总额
    private String agentFinanceTotal;//市级代理总利润
    private double agentShare;//市级代理分成比例默认4.9%（市级代理：0.5%、、）

    private double agentSingleUserShare;//用户：千分之3%
    private double agentSingleBussinessShare;//个代：3%

    private double agentbusinessShare;//商家：1.1%

    //普通用户注册数据
    private String userRegTotal;//新注册用户
    private String userTotal;//总普通用户

    //TODO 用例场景
    //A商家签约折扣为8折，用户消费总额为100元，在APP上抵扣20元红包，实付80元到结算平台（银联），分成三笔支付
    // 1：商家70元=（100*8折）-（100*1折）平台总费用；
    // 2：平台5.1元=平台总费用*5.1%；
    // 3：市级代理4.9元=平台总费用*4.9%（个代=商家（千分之三） + 用户（百分之三）/ 团代=商家（百分之一）+ 用户（千分之一））；

    //TODO 角色分类
    //1，普通用户：成交量、成交金额、省钱总额
    //      1.1，总成交量、总成交金额、总省钱总额；
    //      1.2，常去商家排名；
    //2，商家：
    //      2.1，总成交量、总成交金额；
    //      2.2，总成交用户、活跃用户、回头率；
    //3，个代：商家（千分之三）成交量、成交金额、分成总额，用户（百分之三）成交量、成交金额、分成总额，是商家和用户的总额提成
    //      3.1，商家总成交量、总成交金额、总分成金额；
    //      3.2，用户总成交量、总成交金额、总分成金额；
    //4，团代：商家（百分之一）成交量、成交金额、分成总额，用户（千分之一）成交量、成交金额、分成总额，是商家和用户的总额提成；
    //      4.1，商家总成交量、总成交金额、总分成金额；
    //      4.2，用户总成交量、总成交金额、总分成金额；
    //5，市级代理：总分成4.9%的0.5%，4.4%是个代和团代之和；
    //      5.1，总成交量、总成交金额、总分成金额；
    //      5.2，商家总成交量、总成交金额；
    //      5.3，用户总成交量、总成交金额；
    //6，平台（admin）：总分成5.1%；
    //      6.1，总成交量、总成交金额、总分成金额；
    //      6.2，各个地区市级代理总成交量、、总成交金额；
    //      6.3，总用户成交量、总成交金额；
    //      6.4，总商家成交量、总成交金额；


}
