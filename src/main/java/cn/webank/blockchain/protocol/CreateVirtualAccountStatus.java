package cn.webank.blockchain.protocol;

import lombok.Data;

/**
 * Created by junqizhang on 13/06/2017.
 */
@Data
public class CreateVirtualAccountStatus {

    /*
     * result 为0表明创建账户成功
     */
    public static final Integer STATUS_CREATE_VIRTUAL_ACCOUNT_SUCC = 0;

    /*
     * 创建账户是否成功
     */
    boolean isCreateVirtualAccountResultSucc = false;
}
