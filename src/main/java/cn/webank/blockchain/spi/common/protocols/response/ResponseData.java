package cn.webank.blockchain.spi.common.protocols.response;

import lombok.Data;

@Data
public class ResponseData<T> {

    /**
     * 和请求包一致
     */
    protected String version;
    /**
     * 和请求包一致
     */
    protected String method;

    protected HashData<T> data;
}
