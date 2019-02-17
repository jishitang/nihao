package cn.webank.blockchain.spi.common.protocols.response;

import lombok.Data;

import javax.annotation.Nullable;

/**
 * 区块链请求基类
 */
@Data
public class BCResponseMessage<T> {
    /**
     * 响应码
     */
    protected Integer code;
    /**
     * 响应信息
     */
    @Nullable
    protected String message;

    protected String encrypt_type;

    protected String txid;

    protected ResponseData<T> resp_data;
}
