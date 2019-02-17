package cn.webank.blockchain.spi.common.protocols.response;

import lombok.Data;

/**
 * Created by junqizhang on 2017/5/22.
 */
@Data
public class ResponseStructOld<T, F> {
    protected T result;
    protected F responseStruct;
}
