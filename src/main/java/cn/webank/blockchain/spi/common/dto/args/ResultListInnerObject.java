package cn.webank.blockchain.spi.common.dto.args;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

import java.math.BigDecimal;

@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class ResultListInnerObject implements IArgs {
    @IndexField(index = 0)
    InnerObject1 obj1;
    @IndexField(index = 1)
    InnerObject2 obj2;
    @IndexField(index = 2)
    String f3;


    @Data
    @BlockChainDTO(bindType = BindTypeEnum.List)
    public static class InnerObject1 {
        @IndexField(index = 0)
        int a;
        @IndexField(index = 1)
        String b;

    }

    @Data
    @BlockChainDTO(bindType = BindTypeEnum.Object)
    public static class InnerObject2 {
        BigDecimal c;
        String d;
    }

}



