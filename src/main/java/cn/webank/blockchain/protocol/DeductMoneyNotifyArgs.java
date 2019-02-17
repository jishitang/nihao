package cn.webank.blockchain.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by junqizhang on 08/07/2017.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeductMoneyNotifyArgs extends DirectRouteBaseMsgArgs {
//    public class DeductMoneyNotifyArgs extends PushNotifyArgs {

    /*
    * 聚合支付服务商id
    */
    protected String merchantSvcId;

    /*
     * 聚合支付服务商name
     */
    protected String merchantSvcName;

    /*
     * 支付授权码
     */
    private String paymentAuthCode;

    /*
     * 商户id
     */
    private String merchantId;

    /*
     * 商户名字
     */
    private String merchantName;

    /*
     * 收单行订单id (即WeBank订单id)
     */
    private String abBizNo;

    /*
     * 聚合支付服务商订单id
     */
    private String msBizNo;

    /*
     * 订单的人民币金额，精度到分
     */
    private Integer rmbAmt;

    /*
     * 额外信息
     */
    private String extraData;

//    public DeductMoneyNotifyArgs(ChannelPush channelPush) {
//        super.clone(channelPush);
//    }
}
