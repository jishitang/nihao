package cn.webank.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * How to run health check:
 * java -cp 'conf/:apps/*:lib/*' cn.webank.demo.HealthCheckApp DirectMsg WB
 */
public class HealthCheckApp {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        try {
            if (args.length < 2) {
                System.out.println("please input organise id , Proper Usage is: java -cp 'conf/:apps/*:lib/*' cn.webank.demo.HealthCheckApp DirectMsg WB");
                System.out.println("command optinos : DirectMsg, getExchangeRate");
                System.exit(0);
            }
            // 传入目标orgId
            String command = args[0];
            Application application = new Application();

            switch (command) {
                case "DirectMsg": {
                    /*
                     * demo : ./wepop_util.sh DirectMsg BOCHK
                     */
                    String toOrgId = args[1];
                    application.testCheckDirectRouteMsgHealth(toOrgId);
                }
                break;
                case "getExchangeRate": {
                    application.testGetExchangeRate();
                }
                break;
                case "setCheckCodeTag": {
                    String reqeustId = args[1];
//                    application.testSetCheckCodeTag(reqeustId);
                }
                break;
//                case "setCheckCodeAndGenerateFile": {
//                    String reqeustId = args[1];
//                    String filePath = args[2];
//                    application.testSetCheckCodeTag(reqeustId, filePath);
//                }
//                break;
                case "notify": {
                    Integer lastCheckCode = Integer.valueOf(args[1]);
                    String lastCheckCodeDate = args[2];
                    application.testNotifySetCheckStatus(lastCheckCode, lastCheckCodeDate);
                }
                break;
                case "generateClearingTransFile": {
                    /*
                     * demo : ./wepop_util.sh generateClearingTransFile 18755 /tmp/list2
                     */
                    System.out.println("arg 1 : " + args[1]);
                    System.out.println("arg 2 : " + args[2]);

                    Integer lastCheckCode = Integer.valueOf(args[1]);
                    String filePath = args[2];
                    application.testGenerateClearingTransFile(lastCheckCode, filePath);
                }
                break;
                default: {
                    System.out.println("support command : DirectMsg getExchangeRate setCheckCodeAndGenerateFile notify generateClearingTransFile");
                }
            }

            System.exit(0);
        } catch (Exception e) {
            log.error("Exception in main", e);
            System.exit(0);
        }
    }
}
