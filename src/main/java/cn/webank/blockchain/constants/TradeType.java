package cn.webank.blockchain.constants;

/**
 * Created by junqizhang on 12/06/2017.
 */
public enum TradeType {

    TRADE_TYPE_ERROR(-1),

    /*
     * 主扫模式1， 商户出示的二维码代表的是商户的id
     */
    TYPE_MERCHANT_ID_QRCODE(1),

    /*
     * 主扫模式2， 商户出示的二维码代表的是商户提前生成的订单
     */
    TYPE_MERCHANT_ORDER_QRCODE(2),

    /*
     * 被扫模式， 用户app上展示出支付授权码
     */
    TYPE_PAYMENT_AUTH_CODE(3);

    private Integer value;

    TradeType(Integer index) {
        this.value = index;
    }

    static public TradeType getType(Object index) {
        return TRADE_TYPE_ERROR;
    }

    static public TradeType getType(Integer index) {
        if (null == index) {
            return TRADE_TYPE_ERROR;
        }
        switch (index) {
            case 1:
                return TYPE_MERCHANT_ID_QRCODE;
            case 2:
                return TYPE_MERCHANT_ORDER_QRCODE;
            case 3:
                return TYPE_PAYMENT_AUTH_CODE;
            default:
                return TRADE_TYPE_ERROR;
        }
    }

    public Integer getValue() {
        return this.value;
    }
}
