package cn.webank.blockchain.spi.common;

import cn.webank.blockchain.constants.BlockchainException;
import cn.webank.blockchain.constants.ErrorCode;
import cn.webank.blockchain.constants.WeTimeoutException;
import cn.webank.blockchain.protocol.GetTransactionReceiptArgs;
import cn.webank.blockchain.spi.common.convert.RequestBuilder;
import cn.webank.blockchain.spi.common.convert.ResponseReader;
import cn.webank.blockchain.spi.common.dto.BlockChainAddress;
import cn.webank.blockchain.spi.common.dto.IArgs;
import cn.webank.blockchain.spi.common.dto.BlockchainTxResult;
import cn.webank.blockchain.spi.common.dto.PostData;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.spi.common.vo.BlockChainTicketVO;
import cn.webank.blockchain.utils.JsonMapper;
import cn.webank.blockchain.utils.RestClient;
import cn.webank.common.conf.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

@Component
@Slf4j
public class BlockChainCallService implements IBlockChainCallService {

    public static final String BLOCKCHAIN_SCENARIOID = "01";
    public static final BlockChainAddress SERVICE_TXN_RESULT_QUERY = BlockChainAddress.of(
            "0x31d8e1066a6bf8f45f6ef050862c86d505802903",
            "dealTransactionReceipt",
            "getTransactionReceipt");
    //    private Gson gson = new Gson();
    protected JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
    RestClient httpClient;
    private RequestBuilder msgBuilder;
    private ResponseReader responseReader;

    public BlockChainCallService() {
        this.msgBuilder = new RequestBuilder();
        this.responseReader = new ResponseReader();
        List<String> uniFrontSvrUrlArr = Config.getUniFrontSvrUrl();
        this.httpClient = new RestClient(uniFrontSvrUrlArr);
    }

    public <F extends IArgs, T extends BlockchainTxResult> ResponseStruct<T> writeBlockChainAndGetResult(
            BlockChainAddress bcUrl,
            Class<F> reqClazz,
            F arguments,
            Class<T> resultClazz
    ) throws ConnectException, WeTimeoutException, BlockchainException, IOException {

        PostData writeQuery = msgBuilder.buildRequest(
                bcUrl,
                reqClazz,
                arguments
        );
        log.debug("tx write request : ", writeQuery);
        ResponseStruct<String> writeResponseStruct = requestUniFront(writeQuery, String.class);
        log.debug("tx write response : ", writeResponseStruct.toString());

        if (0 != writeResponseStruct.getErrorCode()) {
            log.error("writeTxResult failed");
            throw new BlockchainException(writeResponseStruct.getErrorCode(), "", writeResponseStruct.getMessage());

        } else if (null == writeResponseStruct || null == writeResponseStruct.getResult()) {
            log.error("response is invalid!");
            throw new BlockchainException(
                    ErrorCode.BLOCK_CHAIN_WRITE_TX_RETURN_NULL,
                    "", "write transaction Result is null!"
            );
        }
        String hashValue = writeResponseStruct.getResult();
        Integer sleepTime = Config.getQueryBlockchainTxSuccInterval();

        BlockChainTicketVO ticket = BlockChainTicketVO.of(hashValue, sleepTime);
        PostData readQuery = msgBuilder.buildRequest(
                SERVICE_TXN_RESULT_QUERY,
                GetTransactionReceiptArgs.class,
                new GetTransactionReceiptArgs(ticket.getHash())
        );

        log.debug("tx read request : ", readQuery);
        ResponseStruct<T> readResponseStruct = queryResultByTicket(readQuery, resultClazz, ticket);
        readResponseStruct.getResult().setTxHash(hashValue);
        log.debug("tx read response : ", readResponseStruct);
        return readResponseStruct;
    }

    private <T> ResponseStruct<T> queryResultByTicket(
            PostData msgQuery,
            Class<T> resultClazz,
            BlockChainTicketVO ticket
    ) throws ConnectException, WeTimeoutException, IOException {

        int repeatNum = Config.getWriteBlockchainRepeatNum();

        for (int i = 0; i < repeatNum; i++) {
            try {
                Thread.sleep(ticket.getSleepTime());
            } catch (InterruptedException e) {
                log.error("FATAL ERROR", e);
            }
            log.info("queryResultByTicket index : {}", i);
            ResponseStruct<T> responseStruct = requestUniFront(msgQuery, resultClazz);
            log.info("result: {}", responseStruct);

            if (ErrorCode.ContractError.BLOCK_CHAIN_TX_NOT_CONFIRM_YET.equals(responseStruct.getErrorCode())) {
                continue;
            }
//            if (responseStruct.getErrorCode().equals(ErrorCode.BLOCK_CHAIN_TX_NOT_CONFIRM_YET)) {
//                continue;
//            }
            return responseStruct;
        }
        return null;
    }

    /*
     * TODO : make this function private
     */
    public String postHttpRequest(String requestJson) throws ConnectException, IOException {
        String respJson = httpClient.post("", requestJson);
        log.info("response ：" + respJson);
        return respJson;
    }

    private <T> ResponseStruct<T> requestUniFront(
            PostData msgQuery,
            Class<T> resultClazz
    ) throws ConnectException, WeTimeoutException, IOException {
        try {
            log.debug("protocol: msgQuery , msg={} ", msgQuery);
            String requestJson = jsonMapper.toJson(msgQuery);

            String respJson = this.postHttpRequest(requestJson);
            log.info("response: {}", respJson);
            return responseReader.readResult(respJson, resultClazz);

        } catch (ResourceAccessException e) {
            log.error("ResourceAccessException : " + e.getMessage() + " | baseEx : " + e.getCause().getClass());
            throw new WeTimeoutException(e);
        } catch (ConnectException e) {
            log.info("ConnectException : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info("Exception : " + e.getMessage());
            throw e;
            //查询可重试
        }
    }

    //	public <F extends IArgs,T> ResponseStruct<T> query(
//			BlockChainAddress bcService,
//			Class<F> clazz,
//			F args,
//			Class<T> resultClazz
//	)

    /**
     * @param
     * @return
     */
//	private <T>T requestUniFront(
//	private <T> ResponseStruct<T> requestUniFront(
//			PostData msgQuery,
//			Class<T> resultClazz
//	) {
//		try {
//            log.debug("protocol: msgQuery , msg={} " , msgQuery);
//            String json = gson.toJson(msgQuery);
//			String respJson = this.postHttpRequest(json);
//            log.info("result: {}", respJson);
//
////            T result = gson.fromJson(respJson, resultClazz);
////            return result;
//			ResponseStruct<T> respData = responseReader.readResult(respJson, resultClazz);
//            return respData;
//		} catch (ResourceAccessException e) {
//			log.error("ResourceAccessException : " + e.getMessage());
//			throw e;
//		} catch (Exception e) {
//			log.info("Exception : " + e.getMessage());
//			throw e;
//			//查询可重试
//		}
//	}


//	/**
//	 *
//	 * 交易请求,并获取Ticket
//	 * @param msg
//	 */
//	private BlockChainTicketVO sendCommandAndGetTicket(PostData msg) {
//		try {
//			log.debug("==> sendCommandAndGetTicket , msg={} " , msg);
//
//			ResponseVO<BCResponseTicket> resp = rmbSender.syncSend(BCResponseTicket.class,null, BLOCKCHAIN_SERVICEID, BLOCKCHAIN_SCENARIOID, msg);
//			if (resp.isSuccess()) {
//				String hashValue = resp.getDomain().get().getResp_data().getData().getResult();
//				Integer sleep_time = resp.getDomain().get().getSleep_time();
//
//				log.debug("<== sendCommandAndGetTicket , hashValue={} , sleep_time={}" , hashValue, sleep_time);
//
//				return BlockChainTicketVO.of(hashValue,sleep_time);
//			}else{
//				throw new RuntimeException("请求失败");
//			}
//		} catch (RMBInvokeException e) {
//			//调用失败, 重发
//			throw Throwables.propagate(e);
//		} catch (RMBTimeoutException e) {
//			//TODO 接口保持幂等,可以重试
//			throw Throwables.propagate(e);
//		}
//	}

//	@Override
    public <F extends IArgs, T> void doCallAndInvokeLater(BlockChainAddress bcUrl, Class<F> reqClazz, F arguments,
                                                          Class<T> resultClazz, IBlockchainCallback<T> callback) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("异步调用返回 暂未支持");
    }
}
