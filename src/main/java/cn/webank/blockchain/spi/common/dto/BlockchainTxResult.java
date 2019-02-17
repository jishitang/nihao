package cn.webank.blockchain.spi.common.dto;

/**
 * 结果
 */
public interface BlockchainTxResult {
    String getTxHash();

    void setTxHash(String txHash);
}
