package cn.webank.blockchain.spi.common.protocols.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;

/**
 * Created by junqizhang on 2017/5/22.
 */
@Data
//@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClearingStatusResponse extends HashData<Integer> {

    private String rawResponse;

    public ClearingStatusResponse(HashData<Integer> baseObj, String rawResponse) {
        this.code = baseObj.getCode();
        this.msg = baseObj.getMsg();
        this.result = baseObj.getResult();
        this.rawResponse = rawResponse;
    }
}
