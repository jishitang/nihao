package cn.webank.blockchain.constants;

/**
 * Created by junqizhang on 12/06/2017.
 */
public enum CurrencyType {

    CURRENCY_TYPE_ERROR(-1), CURRENCY_TYPE_CNY(1), CURRENCY_TYPE_HKD(13);

    private Integer value;

    CurrencyType(Integer index) {
        this.value = index;
    }

    static public CurrencyType getType(Object index) {
        return CURRENCY_TYPE_ERROR;
    }

    static public CurrencyType getType(Integer index) {
        if (null == index) {
            return CURRENCY_TYPE_ERROR;
        }
        switch (index) {
            case 1:
                return CURRENCY_TYPE_CNY;
            case 13:
                return CURRENCY_TYPE_HKD;
            default:
                return CURRENCY_TYPE_ERROR;
        }
    }

    public Integer getValue() {
        return this.value;
    }
}
