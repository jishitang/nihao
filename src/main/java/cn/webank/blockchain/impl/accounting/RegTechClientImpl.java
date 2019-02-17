package cn.webank.blockchain.impl.accounting;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.collections4.CollectionUtils;

import cn.webank.blockchain.api.accounting.RegTechClient;
import cn.webank.blockchain.impl.base.BaseIssuingBankClientImpl;
import cn.webank.blockchain.protocol.GetBlockByNumberResult;
import cn.webank.blockchain.protocol.GetBlockNumberResult;
import cn.webank.blockchain.protocol.GetCCTransListArgs;
import cn.webank.blockchain.protocol.GetCCTransListResult;
import cn.webank.blockchain.protocol.PageArgs;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.spi.common.protocols.response.TransactionQueryResult;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.DynamicArray;
import org.fisco.bcos.web3j.abi.datatypes.generated.Int256;
import org.fisco.bcos.web3j.abi.datatypes.generated.Uint256;
import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameter;
import org.fisco.bcos.web3j.protocol.core.DefaultBlockParameterNumber;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.BcosBlock;;
import org.fisco.bcos.web3j.protocol.core.methods.response.BcosBlock.Block;

public class RegTechClientImpl extends BaseIssuingBankClientImpl implements RegTechClient {

    @Override
    public ResponseStruct<TransactionQueryResult> getCheckCodeList(String orgId, PageArgs args) {

		RemoteCall<List>  f = acquirerBank.getCheckCodeList(BigInteger.valueOf(args.getOffset()),
				BigInteger.valueOf(args.getNum()));
    	
    	List<BigInteger> resultList = null;
    	TransactionQueryResult result = new TransactionQueryResult();
    	
    	try {
    		resultList = f.send();
    		
		} catch ( Exception e) {
			logger.error("Method [getCheckCodeList] execute with exception:{}",e);
			return null;
		}
		ArrayList<Integer>codeList = new ArrayList<Integer>();
    		for(int i = 0; i < resultList.size(); i++)
    		{
    			codeList.add(resultList.get(i).intValue());
    		}
    		result.setCheckCodeList(codeList);
    	ResponseStruct<TransactionQueryResult> respData = new ResponseStruct<TransactionQueryResult>();
    	respData.setResult(result);
    	
    	respData.setErrorCode(0);
        logger.info("response :" + respData);
        return respData;

    }

    @Override
    public ResponseStruct<GetBlockNumberResult> getBlockNumber(String orgId) {

        int blockNum = web3j.getBlockNumberCache().intValue();
        
        GetBlockNumberResult result = new GetBlockNumberResult();
        
        result.setData(blockNum);
    	
        ResponseStruct<GetBlockNumberResult> respData = new ResponseStruct<GetBlockNumberResult> ();
        respData.setErrorCode(0);
        respData.setResult(result);
        
        return respData;
    }

    @Override
    public ResponseStruct<GetBlockByNumberResult> getBlockByNumber(String orgId, Integer blockHeight) {

        DefaultBlockParameter param = new DefaultBlockParameterNumber(blockHeight);
        BcosBlock block =null;
        try {
			block = web3j.getBlockByNumber(param, true).send();
		} catch (IOException e) {
			logger.error("Method [getBlockByNumber] execute with exception:{}",e);
			return null;
		}
        Block blockInfo = block.getResult();
        
        GetBlockByNumberResult result = new GetBlockByNumberResult();
        result.setBlockHash(blockInfo.getHash());
        result.setTimestamp(blockInfo.getTimestamp().toString());
        result.setTransactionsCount(CollectionUtils.isEmpty(blockInfo.getTransactions())?0:blockInfo.getTransactions().size());
        
        ResponseStruct<GetBlockByNumberResult> respData = new ResponseStruct<GetBlockByNumberResult>();
        
        respData.setResult(result);
        respData.setErrorCode(0);

        logger.info("response :" + respData);
        return respData;
    }

    public ResponseStruct<GetCCTransListResult> getCCTransList(GetCCTransListArgs getCCTransListArgs) {
		RemoteCall<List> f = acquirerBank.getCCTransList(BigInteger.valueOf(getCCTransListArgs.getStartOffSet()),
				BigInteger.valueOf(getCCTransListArgs.getTransNums()));
    	List addresses = null;
    	try {
    		addresses = f.send();
		} catch (Exception e) {
			logger.error("Method [getCCTransList] execute with exception:{}",e);
			return null;
		}
    	List<Address>addressList = new ArrayList<Address>();
    	for(int i =0; i < addresses.size(); i++)
		{
			addressList.add(i, new Address(addresses.get(i).toString()));
		}
    	GetCCTransListResult result = new GetCCTransListResult();
    	result.setAddressList(addressList);
    	ResponseStruct<GetCCTransListResult> response = new ResponseStruct<GetCCTransListResult>();
    	response.setResult(result);
    	response.setErrorCode(0);
    	
        logger.info("response :" + response);
        return response;
    }

}
