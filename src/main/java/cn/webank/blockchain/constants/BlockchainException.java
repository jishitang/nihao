package cn.webank.blockchain.constants;

import lombok.Getter;

/**
 * Created by junqizhang on 20/06/2017.
 */
public class BlockchainException extends Exception {

    @Getter
    private Integer errorCode;

    @Getter
    private String rawResponse;

    public BlockchainException(Integer errorCode, String rawResponse, String message) {
        super(message);
        this.errorCode = errorCode;
        this.rawResponse = rawResponse;
    }
}
