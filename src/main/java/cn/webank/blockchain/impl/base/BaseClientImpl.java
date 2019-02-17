package cn.webank.blockchain.impl.base;

import cn.webank.blockchain.constants.BlockchainException;
import cn.webank.blockchain.constants.DirectRouteMsgType;
import cn.webank.blockchain.constants.ErrorCode;
import cn.webank.blockchain.constants.WeTimeoutException;
import cn.webank.blockchain.impl.directroutemsg.OnNotifyCallback;
import cn.webank.blockchain.protocol.DirectRouteBaseMsgArgs;
import cn.webank.blockchain.request.http.PingTask;
import cn.webank.blockchain.request.http.PingTaskUtil;
import cn.webank.blockchain.spi.common.BlockChainCallService;
import cn.webank.blockchain.spi.common.StubTransationIdGenerator;
import cn.webank.blockchain.spi.common.convert.RequestBuilder;
import cn.webank.blockchain.spi.common.convert.ResponseReader;
import cn.webank.blockchain.spi.common.dto.BlockChainAddress;
import cn.webank.blockchain.spi.common.dto.IArgs;
import cn.webank.blockchain.spi.common.dto.PostData;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.spi.common.protocols.response.TransactionReceiptResult;
import cn.webank.blockchain.utils.JsonMapper;
import cn.webank.service.ChannelConnectionsInstance;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.channel.dto.ChannelRequest;
import org.fisco.bcos.channel.dto.ChannelResponse;
import cn.webank.common.conf.Config;
import cn.webank.directroute.callback.DirectRouteCallback;
import cn.webank.directroute.protocol.WePopDirectPathRequestBody;
import cn.webank.directroute.tool.DirectRouteBodyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;

/**
 * Created by junqizhang on 2017/5/19.
 */
@Component
public class BaseClientImpl {

    /*
     * 链上链下最大超时时间
     * unit : millisecond
     */
    public static final int MAX_DIRECT_ROUTE_REQUEST_TIMEOUT = 50000;
    /*
     * 普通链上链下消息的超时时间
     */
    public static final int defaultDirectRouteRequestTimeout = Config.getDefaultDirectRouteRequestTimeout();
    /*
     * 链上链下且需要写交易的超时时间
     */
    public static final int directRouteTransactionTimeout = Config.getDirectRouteTransactionTimeout();
    /*
     * 读区块链的超时时间
     */
    public static final int queryBlockChainTimeout = Config.getQueryBlockChainTimeout();
    /*
     * 写区块链的超时时间
     */
    public static final int writeBlockChainTimeout = Config.getWriteBlockChainTimeout();
    protected static final Logger logger = LoggerFactory.getLogger(BaseClientImpl.class);
    protected StubTransationIdGenerator idGenerator = new StubTransationIdGenerator();

    protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
    //    @Autowired
    protected RequestBuilder ethbuilder;
    //    @Autowired
    protected ResponseReader responseReader;
    //    @Autowired
    protected BlockChainCallService callService;
    //    @Value("${bank.blockchain.issuingBank.contractaddress}")
    protected static String issuingBankContractAddress; // 发卡行逻辑合约地址
    //    @Value("${bank.blockchain.clearingBank.contractaddress}")
    protected static String clearingBankContractAddress; // 清算行逻辑合约地址
    //    @Value("${bank.blockchain.acquirerBank.contractaddress}")
    protected static String acquirerBankContractAddress; // 收单逻辑合约地址
    protected Service directThroughLineService;
    protected OnNotifyCallback onNotifyCallback;
    private PingTask pingTask = PingTaskUtil.INSTANCE.getPingTask();
//    private Gson gson = new Gson();

    static
    {
    	 issuingBankContractAddress = Config.getIssuingBankContractAddress();
         clearingBankContractAddress = Config.getClearingBankContractAddress();
         acquirerBankContractAddress = Config.getAcquirerBankContractAddress();
    }
    
    public BaseClientImpl() {
        this.ethbuilder = new RequestBuilder();
        this.responseReader = new ResponseReader();
        this.callService = new BlockChainCallService();
        this.directThroughLineService = ChannelConnectionsInstance.INSTANCE.getDirectThroughLineService();
        this.onNotifyCallback = ChannelConnectionsInstance.INSTANCE.getOnNotifyCallback();

//        PingTask pingTask = new PingTask(Config.getPingTaskInterval());
//        pingTask.schedule();
//        PingTaskUtil.INSTANCE.setPingTask(pingTask);
    }

    protected String generateNewMessageId() {
        return this.directThroughLineService.newSeq();
    }

    private ResponseStruct<TransactionReceiptResult> updateResponseStruct(
            ResponseStruct<TransactionReceiptResult> responseStruct,
            Integer errorCode,
            Integer subErrorCode,
            String message
    ) {
        if (ErrorCode.ContractError.FRONT_SERVER_DETECT_API_SERVICE_DOWN.equals(subErrorCode)) {

            responseStruct.setErrorCode(ErrorCode.API_SERVICE_NOT_FOUND);
        } else {
            responseStruct.setErrorCode(errorCode);
        }
        responseStruct.setMessage(message);
//        responseStruct.setRawResponse();

        TransactionReceiptResult result = new TransactionReceiptResult();
        result.setStatus(subErrorCode);
        responseStruct.setResult(result);

        return responseStruct;
    }

    public <F extends IArgs> ResponseStruct<TransactionReceiptResult> writeBlockChainAndGetResult(
            BlockChainAddress bcUrl,
            Class<F> reqClazz,
            F arguments) {

        logger.info("write blockchain request : {}", arguments);
        ResponseStruct<TransactionReceiptResult> responseStruct = new ResponseStruct<>();
        try {
            responseStruct = callService.writeBlockChainAndGetResult(
                    bcUrl,
                    reqClazz,
                    arguments,
                    TransactionReceiptResult.class
            );
            logger.info("write blockchain response : {}", responseStruct);

            /*
             * TODO : need test here
             */
            if (null == responseStruct) {
                //3次超时会返回null
                responseStruct = new ResponseStruct<>();
                responseStruct.setErrorCode(ErrorCode.BLOCK_CHAIN_READ_TIMEOUT_REACH_LIMIT);
                logger.error("transactionQueryResult is null!");
            }
        } catch (WeTimeoutException e) {
            responseStruct.setErrorCode(e.getErrorCode());
            responseStruct.setMessage("WeTimeoutException : " + e.getMessage());
        } catch (BlockchainException e) {

            responseStruct = updateResponseStruct(responseStruct, ErrorCode.BLOCK_CHAIN_WRITE_TX_RETURN_ERROR, e.getErrorCode(), e.getMessage());

        } catch (IOException e) {
            responseStruct.setErrorCode(ErrorCode.UNI_FRONT_SERVER_NOT_FOUND);
            responseStruct.setMessage("BlockchainException : " + e.getMessage());
        } catch (Exception e) {
            responseStruct.setErrorCode(ErrorCode.BLOCK_CHAIN_WRITE_TX_UNKNOWN);
            responseStruct.setMessage("BlockchainException : " + e.getMessage());
        }
//        catch (ConnectException e) {
//            responseStruct.setErrorCode(ErrorCode.UNI_FRONT_SERVER_RESPONSE_TIMEOUT);
//            responseStruct.setMessage("ConnectException : " + e.getMessage());
//        }
        return responseStruct;
    }

    public <F extends IArgs, T> ResponseStruct<T> query(
            BlockChainAddress bcService,
            Class<F> clazz,
            F args,
            Class<T> resultClazz) {

        ResponseStruct<T> responseStruct;
        try {
            PostData bcMessage = ethbuilder.buildRequest(
                    bcService,
                    clazz,
                    args
            );
            logger.info("query blockchain request : {}", bcMessage);
            //check
//            String requestJson = gson.toJson(bcMessage);
            String requestJson = jsonMapper.toJson(bcMessage);
            String respJson = callService.postHttpRequest(requestJson);
            logger.info("query blockchain response :" + respJson);

            if (null == respJson) {
                //TODO :
            }
            responseStruct = responseReader.readResult(respJson, resultClazz);
            logger.debug("query blockchain response struct = {} ", responseStruct);
        }
        /*
         * TODO : 需新增处理
         */
//        catch (WeTimeoutException e) {
//            responseStruct = new ResponseStruct<>();
////            responseStruct.setErrorCode(ErrorCode.UNI_FRONT_SERVER_NOT_FOUND);
//            responseStruct.setErrorCode(e.getErrorCode());
//            responseStruct.setMessage("ResourceAccessException : " + e.getMessage());
//        }
        catch (ResourceAccessException | IOException e) {
            responseStruct = new ResponseStruct<>();
            responseStruct.setErrorCode(ErrorCode.UNI_FRONT_SERVER_NOT_FOUND);
            responseStruct.setMessage("Exception : " + e.getMessage());
            logger.error("exception : {} ", e);
        } catch (Exception e) {
            responseStruct = new ResponseStruct<>();
            responseStruct.setErrorCode(ErrorCode.UNI_FRONT_SERVER_REQUEST_UNKNOWN);
            responseStruct.setMessage("Exception : " + e.getMessage());
            logger.error("exception : {} ", e);
        }

        return responseStruct;
    }

    public void registerCallback(DirectRouteCallback directRouteCallback) {
        this.onNotifyCallback.setDirectRouteCallback(directRouteCallback);
    }

//    public void registerCallback(DirectRouteCallback directRouteCallback) {
//        this.onNotifyCallback.setDirectRouteCallback(directRouteCallback);
//    }

    private <T> ResponseStruct<T> buildResponseStruct(Class<T> resultClass,
                                                      ChannelResponse response,
                                                      T msgBodyObj,
                                                      Integer errorCode,
                                                      String errorMessage
    ) {
        try {
            if (null == msgBodyObj) {
                msgBodyObj = resultClass.newInstance();
//                msgBodyObj.setErrorCode(response.getErrorCode());
            }
        } catch (Exception e) {
            logger.error("{}", e);
            msgBodyObj = null;
        }
        ResponseStruct<T> responseStruct = new ResponseStruct<>();
        responseStruct.setErrorCode(errorCode);
        responseStruct.setMessage(errorMessage);
        responseStruct.setResult(msgBodyObj);
        return responseStruct;
    }

    protected <T, F extends DirectRouteBaseMsgArgs> ResponseStruct<T> getImpl(
            String fromOrgId,
            String toOrgId,
            F arg,
            Class<F> argsClass,
            Class<T> resultClass,
            DirectRouteMsgType msgType,
            int timeOut
    ) {
        if (timeOut > MAX_DIRECT_ROUTE_REQUEST_TIMEOUT || timeOut < 0) {
            timeOut = MAX_DIRECT_ROUTE_REQUEST_TIMEOUT;
            logger.error("invalid timeOut : {}", timeOut);
        }
        String errorMessage;
        arg.setFromOrgId(fromOrgId);
        arg.setToOrgId(toOrgId);

        ChannelRequest request = new ChannelRequest();
        request.setFromOrg(fromOrgId);
        request.setToOrg(toOrgId);
        request.setTimeout(timeOut);
        request.setToTopic(toOrgId);
//        request.setMessageID((int) idGenerator.generateUniqueIdLong());
        request.setMessageID(generateNewMessageId());

        String msgBody = jsonMapper.toJson(arg);
        WePopDirectPathRequestBody wePopDirectPathRequestBody = new WePopDirectPathRequestBody();
        wePopDirectPathRequestBody.setMsgType(msgType);
        wePopDirectPathRequestBody.setMsgBody(msgBody);
        String wePopDirectPathRequestBodyStr = jsonMapper.toJson(wePopDirectPathRequestBody);
        logger.info("direct route request, seq : {}, body ：{}", request.getMessageID(), wePopDirectPathRequestBodyStr);
        request.setContent(wePopDirectPathRequestBodyStr);

        ChannelResponse response = directThroughLineService.sendChannelMessage2(request);
        logger.info("direct route response, seq : {}, errorCode : {}, body : {}",
                response.getMessageID(),
                response.getErrorCode(),
                response.getContent()
        );

        if (102 == response.getErrorCode()) {
            /*
             * return 102 if timeout
             */
            errorMessage = "direct route msg timeout, sendChannelMessage timeout, sub errorCode : " + +response.getErrorCode();
            ResponseStruct<T> responseStruct = buildResponseStruct(
                    resultClass,
                    response,
                    null,
                    ErrorCode.DIRECT_ROUTE_REQUEST_TIMEOUT,
                    errorMessage
            );
            logger.error(errorMessage);
            return responseStruct;
        } else if (0 != response.getErrorCode()) {
            /*
             # TODO : 处理链上联系错误码
             */
            errorMessage = "direct route msg error, sub errorCode : "
                    + response.getErrorCode()
                    + "; sendChannelMessage error : "
                    + response.getErrorMessage();
            ResponseStruct<T> responseStruct = buildResponseStruct(
                    resultClass,
                    response,
                    null,
                    ErrorCode.DIRECT_ROUTE_MSG_BASE_ERROR,
                    errorMessage
            );
            logger.error(errorMessage);
            return responseStruct;
        }
        T msgBodyObj = DirectRouteBodyParser.deserialize(response.getContent(), resultClass);
        ResponseStruct<T> responseStruct = buildResponseStruct(
                resultClass,
                response,
                msgBodyObj,
                response.getErrorCode(),
                response.getErrorMessage()
        );
//        logger.info("responseStruct  ：" + responseStruct);
        return responseStruct;
    }
}
