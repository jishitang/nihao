package cn.webank.blockchain.spi.common.protocols.response;

import cn.webank.blockchain.spi.common.dto.BlockchainTxResult;
import lombok.Data;

/**
 * 上链结果查询回包的Result的Object
 */
@Data
public class TransactionReceiptResult implements BlockchainTxResult {
    private Integer status;
    private long balance;
    private Integer check_code;
    private long tx_amt_1;
    private int currency_1;
    private long tx_amt_2;
    private int currency_2;
    private int accrualAcquirer;
    private int accrualIssuing;
    private String func;
    private String txHash;
}
