package cn.webank.demo;

import cn.webank.blockchain.protocol.CheckDirectRouteMsgHealthArgs;

public class DemoTest {

	public static void main(String[] args) {
		 CheckDirectRouteMsgHealthArgs arg = new CheckDirectRouteMsgHealthArgs();
	     arg.setMessage("good");
	     System.out.println(arg.getMessage());
	}

}
