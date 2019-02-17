package cn.webank.blockchain.protocol;

import lombok.Data;

/**
 * Created by junqizhang on 13/06/2017.
 */
@Data
public class IsAccountExistResult {

    /*
     * result 为0账户存在
     */
    public static final String STATUS_ACCOUNT_EXIST = "0";

    /*
     * 账户是否存在
     */
    boolean isAccountExist = false;
}
