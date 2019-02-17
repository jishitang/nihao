package cn.webank.blockchain.protocol;

import lombok.Data;

//import cn.webank.blockchain.spi.common.protocols.response.GetTransList;

/**
 * Created by junqizhang on 01/06/2017.
 */

//import java.util.ArrayList;

@Data
public class GetAccountBalanceResult {
    protected Integer currency;
    protected Integer balance;
}
