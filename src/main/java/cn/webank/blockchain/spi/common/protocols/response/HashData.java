package cn.webank.blockchain.spi.common.protocols.response;

import lombok.Data;

/**
 * Created by gmzer on 2017/5/22.
 */
@Data
public class HashData<T> {
    /**
     * 交易执行返回码
     */
    protected Integer code;
    protected String msg;
    protected T result;
}
