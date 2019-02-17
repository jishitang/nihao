package cn.webank.blockchain.spi.common.dto;

import cn.webank.blockchain.spi.common.protocols.response.ResponseData;
import lombok.Data;

/**
 * 区块链返回报文
 */
@Data
public class BCResponseTicket {

    /**
     * 响应码
     */
    private Integer code;

    private String message;

    private String encrypt_type;

    private String txid;

    private ResponseData<String> resp_data;

    /**
     * 时间间隔
     * <pre>
     * "隔这段时间后再上链查询交易执行结果，如果已经查询到交易结果后，则不用再查询了。最多查询3次。
     * 单位毫秒"
     * </pre>
     */
    private Integer sleep_time;

}
