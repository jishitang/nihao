/**
 * 
 */
package cn.webank.blockchain.impl.channel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tonychen
 *
 */
@Slf4j
public class ChannelExecutor {

	private static Web3j web3j;
	
	private static ApplicationContext context; 
	
	/**
	 * 初始化web3j对象
	 * 
	 **/
	public void init () throws Exception
	{
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		Service service = context.getBean(Service.class);
		service.run();
		
		ChannelEthereumService channelEthereumService = new ChannelEthereumService();
		channelEthereumService.setChannelService(service);
		web3j = Web3j.build(channelEthereumService, service.getGroupId());
		
		if(null == web3j)
		{
			log.error("Web3j init failed. Please check your config file!");
			throw new RuntimeException("Web3j init failed.");
		}
		log.debug("web3j init successfully.");
	}
	
	public void executeMethod()
	{
		
	}
		 
	
}
