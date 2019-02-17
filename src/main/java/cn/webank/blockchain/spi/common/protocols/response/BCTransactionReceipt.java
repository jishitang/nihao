package cn.webank.blockchain.spi.common.protocols.response;

import lombok.Data;

/**
 * 区块链请求基类
 */
@Data
public class BCTransactionReceipt {
    protected ResponseData<TransactionReceiptResult> resp_data;
    /**
     * 响应码
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String message;
    private String encrypt_type;
    private String txid;
}
