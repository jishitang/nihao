package cn.webank.blockchain.protocol;


import lombok.Data;

/**
 * 查询交易列表时，区块链返回的结构
 */
@Data
public class GetClearingTransResultListElement {
    private String date; // yyyyMMdd
    private String biz_no;//交易流水号
    private String org_biz_no; // 仅冲账交易需要
    private String from_org;//发卡行的标示
    private String from_userid;//发卡行的用户标示
    private String to_org;        //收单行的标示
    private String to_userid;//收单行的商户标示
    private String to_username;//收单行的商户名称
    private int tx_type; // 1：收单行交易 13：消费 前缀2：发卡行交易 21充值，22：提现，31：退货，
    //    private int tx_status;////订单状态，1：处理成功， 2：退货完成  3：交易已被冲正
    private int is_strike; // 是否冲账，1：冲账； 0：非冲账
    private long tx_amt_1;//
    private int currency_1;//
    private int share_1;//
    private int share_2;//
    private int share_3;//
    //    private long tx_amt_2;//
    private int currency_2;//
    private int exchange;
    private String check_code;
    private long tx_time;//时间戳，精确到毫秒
}
