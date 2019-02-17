package cn.webank.blockchain.spi.common.convert;

import cn.webank.blockchain.constants.ErrorCode;
import cn.webank.blockchain.spi.common.protocols.response.BCResponseMessage;
import cn.webank.blockchain.spi.common.protocols.response.HashData;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.utils.JsonMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

//import org.springframework.beans.factory.annotation.Autowired;

@Component
@Slf4j
public class ResponseReader {

    private BlockchainBodyParser parser;

    public ResponseReader() {
        this.parser = new BlockchainBodyParser();
    }

    public <T> ResponseStruct<T> readResult(String responseAsJson, Class<T> resultClazz) {
        log.debug("==> read result from response : resp={} ,targetclazz={} ", responseAsJson, resultClazz);
        try {
            JsonMapper jsonMapper = new JsonMapper(Include.NON_DEFAULT);
            BCResponseMessage<T> response = jsonMapper.getMapper().readValue(responseAsJson, new TypeReference<BCResponseMessage<T>>() {
            });

            ResponseStruct<T> responseStruct = new ResponseStruct<>();
            responseStruct.setRawResponse(responseAsJson);
            if (null == response) {
                responseStruct.setErrorCode(ErrorCode.BLOCK_CHAIN_READ_TX_RETURN_NULL);
                responseStruct.setMessage(ErrorCode.BLOCK_CHAIN_READ_TX_RETURN_NULL.toString());
            } else if (null == response.getResp_data() || null == response.getResp_data().getData()) {
                responseStruct.setErrorCode(response.getCode());
                responseStruct.setMessage(response.getMessage());
            } else if (0 == response.getCode()) {

                HashData<T> data = response.getResp_data().getData();

                JsonNode jsonNode = jsonMapper.getMapper().readTree(responseAsJson).findPath("resp_data").findPath("data")
                        .findPath("result");
                String bodyJson = String.valueOf(jsonNode);
                T domainObject = this.parser.toDomainObject(resultClazz, bodyJson);

                // probably domainObject is null
                if (null == domainObject) {
                    responseStruct.setErrorCode(ErrorCode.BLOCK_CHAIN_READ_TX_RETURN_NULL);
                } else {
                    responseStruct.setResult(domainObject);
                }

                responseStruct.setMessage(data.getMsg());
                responseStruct.setErrorCode(data.getCode());

            } else {
                responseStruct.setErrorCode(response.getCode());
                responseStruct.setMessage(response.getMessage());
            }
            return responseStruct;
//			HashData<T> data = response.getResp_data().getData();
//
//			// 0 代表区块链交易返回成功
//			Integer respCode = data.getCode();
//			String respMsg = data.getMsg();
//
//			JsonNode jsonNode = jsonMapper.getMapper().readTree(responseAsJson).findPath("resp_data").findPath("data")
//			.findPath("result");
//
//			String bodyJson  = String.valueOf(jsonNode);
//			T domainObject = parser.toDomainObject(resultClazz, bodyJson);
//
//			log.debug("==> read result from response : result={} " , domainObject);

//			HashData<T> dataT = data;
//
//			response.setResp_data();
//			BCResponseMessage<T> responseT =
//			return BlockChainResponseVO.<T>of(respCode, respMsg, domainObject);

        } catch (IOException e) {
            throw Throwables.propagate(e);
        }

    }

}
