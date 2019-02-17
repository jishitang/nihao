package cn.webank.blockchain.result;

import cn.webank.blockchain.constants.RecordAccountsPayableResultType;
import lombok.Data;

/**
 * Created by junqizhang on 13/06/2017.
 */
@Data
public class RecordAccountsPayableResult {

    /*
     * 错误信息
     */
    protected String message;
    /*
     * 记账的result类型： 成功，失败和未知
     */
    private RecordAccountsPayableResultType resultType;
    /*
     * 错误码
     */
    private Integer errorCode;
}
