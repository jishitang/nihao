package cn.webank.blockchain.spi.common.convert;

import cn.webank.blockchain.spi.common.ITransationIdGenerator;
import cn.webank.blockchain.spi.common.StubTransationIdGenerator;
import cn.webank.blockchain.spi.common.dto.BlockChainAddress;
import cn.webank.blockchain.spi.common.dto.PostData;
import cn.webank.blockchain.spi.common.protocols.response.BlockChainRequestMessage;
import cn.webank.common.conf.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

//import org.springframework.beans.factory.annotation.Autowired;

@Component
@Configurable
public class RequestBuilder {
    private static final Logger log = LoggerFactory.getLogger(RequestBuilder.class);
    //	@Autowired
    private ITransationIdGenerator idGenerator;
    //	@Autowired
    private BlockchainBodyParser parser;
    private String keyId; // 每个链都有不同的key_id
    private String orgId; // 请求来源标示，谁发起请求上链，就填谁的标示。需事先分配好
    private String externalWethAccount; // 调用方账号，需事先分配
    private String pwd; // 调用方账号密码，需事先分配
    //    private String bankNo; // 机构标识
    private String appName;// 应用类型
    private String version;
    private String encryptType;

    public RequestBuilder() {
        this.idGenerator = new StubTransationIdGenerator();
        this.parser = new BlockchainBodyParser();
        this.keyId = Config.getKeyId();
        this.orgId = Config.getOrgId();
        this.externalWethAccount = Config.getExternalWethAccount();
        this.pwd = Config.getPwd();
//        this.bankNo = Config.getBankNo();
        this.appName = Config.getAppName();
        this.version = Config.getVersion();
        this.encryptType = Config.getEncryptType();
    }

    public <T> PostData buildRequest(BlockChainAddress bcService, Class<T> clazz, T args) {

        PostData.ReqData.Param param = new PostData.ReqData.Param();
        param.setFunc(bcService.getFunction());
        param.setContract_addr(bcService.getContractAddress());
//		param.setContract_addr(clearingBankContractAddress);

        param.setAddress(externalWethAccount);
        param.setPwd(pwd);
        param.setArgs(toMessage(clazz, args));

        PostData.ReqData reqData = new PostData.ReqData();
        reqData.setMethod(bcService.getMethod());
        reqData.setParam(param);

        PostData postData = new PostData();
        postData.setTx_id(idGenerator.generateUnquieId());
        postData.setEncrypt_type(encryptType);
        postData.setReq_data(reqData);
        postData.setKey_id(keyId);
        postData.setChain_id("0");
        postData.setOrg_app(orgId);
        postData.setVersion(version);
        postData.setBankNo(orgId);
        postData.setAppName(appName);

        BlockChainRequestMessage message = new BlockChainRequestMessage();
        message.setKey_id(keyId);
        message.setOrg_app(orgId);
        message.setVersion(version);
        message.setBankNo(orgId);
        message.setAppName(appName);
        message.setPost_data(postData);

        return postData;
    }

    private <T> Object toMessage(Class<T> clazz, T args) {
        return parser.toMessageObject(clazz, args);
    }
}
