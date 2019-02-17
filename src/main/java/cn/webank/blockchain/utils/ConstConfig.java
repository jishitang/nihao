package cn.webank.blockchain.utils;

import java.util.HashMap;
import java.util.Map;


public class ConstConfig {
    /**
     * 和客户端之间的错误码
     */
    public static final Integer RET_SUCCEED = 0; //成功
    public static final int ERR_LOGIN_PWD = 980000; //登錄賬號或者密碼錯誤
    public static final int ERR_LOGIN_SIG_EXP = 980001; //登录态失效，请重新登陆
    public static final int ERR_NO_RECORDS = 980002;//沒有更多的記錄了

    public static final int ERR_INNER = 999999; //服務內部錯誤
    public static final int ERR_SYS_BUSY = 999998; //系統繁忙，請稍後再試
    public static final int ERR_FREQ_TOO_HIGH = 990001; //請求頻次過大
    public static final int ERR_TIMEOUT = 990002; //請求超時
    public static final int ERR_PARA_EXCEPTION = 990003; //請求參數異常
    public static final int ERR_IP_LIMITED = 990004; //IP受限
    public static final int ERR_NODE_EXCEPTION = 990005; //服務節點異常
    public static final int ERR_ACCOUNT_NOT_EXIST = 990006; //帳號不存在
    public static final int ERR_PAYMENT_PWD = 990007; //支付密碼錯誤，請稍後重試！
    public static final int ERR_CHARGE_FAIL = 990008; //扣費失敗
    public static final int ERR_LACK_BALANCE = 990009; //錢包余額不足
    public static final int ERR_RECHARGE_FAIL = 990010; //充值失敗
    public static final int ERR_WITHDRAW_FAIL = 990011; //提現失敗
    public static final int ERR_NO_OPR_AUTH = 990013; //無操作權限
    public static final int ERR_UNKNOWN = 990014; //未知異常
    public static final int ERR_ACCOUNT_EXIST = 990015; //帳號已存在
    public static final int ERR_TRANSACTIONS_NOT_EXIST = 990023; //不存在的交易
    public static final int ERR_TRANSACTIONS_OPR = 990025; //交易失敗
    public static final int ERR_BALANCE_NOT_ENOUGH = 990027; //該銀行卡餘額不足
    public static final int ERR_BANK_CARD_NOT_AVAILABLE = 990028; //該銀行卡不可用

    public static final Map<Integer, String> errorMap = new HashMap<Integer, String>();
    /**
     * 货币的数字和字符串转换
     */
    public static final Map<String, String> CURRENCY_MAPPING = new HashMap<String, String>();

    ;
    /**
     * 验证类型 1:手势验证码；2:输入支付密码
     */
    public static final int VERIFY_TYPE_GESTURECODE = 1;
    public static final int VERIFY_TYPE_TRANS_PWD = 2;
    /**
     * 其他配置
     */
    public static final String CHAIN_HTTP_URL_CONFIG_NAME = "frontURL";//区块链HTTP的URL
    public static final String userConfigFile = "src/main/resources/user.json";
    public static final int INT_AMOUNT_DIVISOR = 100;//精确到分整数类型的金额转化为实际金额的除数
    public static final String OPENAPI_TYPE_DEPOSIT = "0";//Open Api 请求类型 0-deposit 1 withdraw 2 strike 3 consume
    public static final String OPENAPI_TYPE_WITHDRAW = "1";
    public static final String OPENAPI_TYPE_STRIKE = "2";
    public static final String OPENAPI_TYPE_CONSUME = "13";
    /**
     * 区块链接口相关错误码
     */
    public static int SUC_CODE = 0;//成功代码
    public static int DOING_CODE = 2012;//交易处理中

    static {
        errorMap.put(RET_SUCCEED, "成功");
        errorMap.put(ERR_LOGIN_SIG_EXP, "登陸已過期，請重新登陸");
        errorMap.put(ERR_NO_RECORDS, "沒有更多的記錄了");
        errorMap.put(ERR_LOGIN_PWD, "登錄賬號或者密碼錯誤");
        errorMap.put(ERR_INNER, "服務內部錯誤");
        errorMap.put(ERR_SYS_BUSY, "系統繁忙，請稍後再試");
        errorMap.put(ERR_FREQ_TOO_HIGH, "請求頻次過大");
        errorMap.put(ERR_TIMEOUT, "請求超時");
        errorMap.put(ERR_PARA_EXCEPTION, "請求參數異常");
        errorMap.put(ERR_IP_LIMITED, "IP受限");
        errorMap.put(ERR_NODE_EXCEPTION, "服務節點異常");
        errorMap.put(ERR_ACCOUNT_NOT_EXIST, "帳號不存在");
        errorMap.put(ERR_PAYMENT_PWD, "支付密碼錯誤，請稍後重試！");
        errorMap.put(ERR_CHARGE_FAIL, "扣費失敗");
        errorMap.put(ERR_LACK_BALANCE, "錢包余額不足");
        errorMap.put(ERR_RECHARGE_FAIL, "充值失敗");
        errorMap.put(ERR_WITHDRAW_FAIL, "提現失敗");
        errorMap.put(ERR_NO_OPR_AUTH, "無操作權限");
        errorMap.put(ERR_UNKNOWN, "未知異常");
        errorMap.put(ERR_ACCOUNT_EXIST, "帳號已存在");
        errorMap.put(ERR_TRANSACTIONS_NOT_EXIST, "不存在的交易");
        errorMap.put(ERR_TRANSACTIONS_OPR, "交易失敗");
        errorMap.put(ERR_BALANCE_NOT_ENOUGH, "該銀行卡餘額不足");
        errorMap.put(ERR_BANK_CARD_NOT_AVAILABLE, "該銀行卡不可用");
    }

    static {
        CURRENCY_MAPPING.put("1", "CNY");
        CURRENCY_MAPPING.put("12", "GBP");
        CURRENCY_MAPPING.put("13", "HKD");
        CURRENCY_MAPPING.put("14", "USD");
        CURRENCY_MAPPING.put("15", "CHF");
        CURRENCY_MAPPING.put("18", "SGD");
        CURRENCY_MAPPING.put("81", "MOP");
        Map<String, String> tempMap = new HashMap<>();
        for (Map.Entry<String, String> entry : CURRENCY_MAPPING.entrySet()) {
            tempMap.put(entry.getValue(), entry.getKey());
        }
    }
}
