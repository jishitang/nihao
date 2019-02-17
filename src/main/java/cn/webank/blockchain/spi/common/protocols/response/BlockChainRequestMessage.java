package cn.webank.blockchain.spi.common.protocols.response;

import cn.webank.blockchain.spi.common.dto.PostData;
import lombok.Data;

/**
 * 区块链请求基类
 */
@Data
public class BlockChainRequestMessage {

    /**
     * 每个链都有不同的key_id
     */
    private String key_id;

    /**
     * 每个链都有不同的chain_id
     */
    private String chain_id;

    /**
     * 请求来源标示，谁发起请求上链，就填谁的标示。需事先分配好
     */
    private String org_app;

    /**
     * 协议版本号，默认1.0.0
     */
    private String version;

    /**
     * 机构标识
     */
    private String bankNo;

    /**
     * 应用类型
     */
    private String appName;

    private PostData post_data;


}