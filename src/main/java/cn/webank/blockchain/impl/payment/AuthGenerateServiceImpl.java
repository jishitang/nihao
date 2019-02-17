package cn.webank.blockchain.impl.payment;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


//TODO : 测试生成随机数性能
public class AuthGenerateServiceImpl implements AuthGenerateService {

    private static final Logger logger = LoggerFactory.getLogger(AuthGenerateServiceImpl.class);

    /**
     * 账号长度
     */
    private int accountLen = 18;

    /**
     * 生成的支付授权码长度
     */
    private int paymentCodeLength = 19;

    /**
     * 验证码的长度
     */
    private int verifyCodeLength = 1;

    /**
     * 授权码总长度
     */
    private int count = 12;

    /**
     * 渠道码
     */
    private String channelCode = "9609";

    /**
     * 境外合作行标识码
     */
    private String issuingBankAuthCode = "001";

    /**
     * 随机位数
     */
    private int randomNumLength;

    /**
     * linux下生成随机码读取的文件
     */
    private String linux_file = "/dev/urandom";

    public AuthGenerateServiceImpl() {
        randomNumLength = paymentCodeLength - verifyCodeLength - channelCode.length() - issuingBankAuthCode.length();
    }

    @Override
    public String generateCode(String userAccount) {
        logger.info("generateCode, userAccount:{}", userAccount);
        String base_code = "";
        try {
            base_code = otherGenerate();
        } catch (Exception e) {
            logger.error("using urandom generate random failed!");
            return null;
//			base_code=baseGenerate(account);
        }
        if (StringUtils.isBlank(base_code)) {
            logger.warn("generateCode failed!");
            return null;
        }
        String verify = getSignCode(base_code, userAccount);
        //根据位数判断校验位是否正确
        if (count == randomNumLength) {
            //不需要校验位
            verify = "";
        } else {
            int len = count - randomNumLength;
            if (StringUtils.isBlank(verify) || verify.length() != len) {
                return null;
            }
        }
        String authCode = channelCode + issuingBankAuthCode + base_code + verify;
        return authCode;
    }

    private String baseGenerate(String userAccount) {
        Long curr = getCurrentRandom();
        Long acc = getAccountRandom(userAccount, curr);
        Long time = getTimeRandom(acc);
        Long memory = getMemoryRandom(time);
        String code = getCode(memory);
        return code;
    }

    private Long getCurrentRandom() {
        Random r = new Random(System.currentTimeMillis());
        return r.nextLong();
    }

    private Long getAccountRandom(String userAccount, Long current) {
        String acc = "";
        if (userAccount.length() > accountLen) {
            acc = userAccount.substring(userAccount.length() - accountLen, userAccount.length());
        } else {
            acc = userAccount;
        }
        Long seek = Long.parseLong(acc) ^ current;
        Random r = new Random(seek);
        return r.nextLong();
    }

    private Long getTimeRandom(Long seek) {
        long jvmTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        Long sek = jvmTime ^ seek;
        Random r = new Random(sek);
        return r.nextLong();
    }

    private Long getMemoryRandom(Long time) {
        long useMemory = Runtime.getRuntime().freeMemory();
        Long seek = useMemory ^ time;
        Random r = new Random(seek);
        return r.nextLong();
    }

    private String getCode(Long seek) {
        Random r = new Random(seek);
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < randomNumLength; i++) {
            code.append(r.nextInt(10));
        }
        return code.toString();
    }

    private byte[] decodeByBase64(byte[] data) throws IOException {
        byte[] decodedBytes = Base64.decodeBase64(data);
        System.out.println("decodedBytes " + new String(decodedBytes));
        return decodedBytes;
//        return (new BASE64Decoder()).decodeBuffer(new ByteArrayInputStream(data));
    }

    private String encodeByBase64(byte[] data) {
        byte[] encodedBytes = Base64.encodeBase64(data);
        return new String(encodedBytes);
//        return (new BASE64Encoder()).encodeBuffer(data);
    }

    private String encryptToMD5(String data) throws NoSuchAlgorithmException {
        return this.encryptToMD5(data, "");
    }

    private String encryptToMD5(String data, String salt) throws NoSuchAlgorithmException {
        byte[] en = null;

        try {
            en = (data + salt).getBytes("UTF-8");
        } catch (UnsupportedEncodingException var6) {
            logger.error("CipherToolsImpl.encryptToMD5.error.UnsupportedEncodingException", var6);
        }

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(en);
        byte[] me = messageDigest.digest();
        return this.encodeByBase64(me);
    }

    private String getSignCode(String authCode, String userAccount) {
        StringBuffer signCode = new StringBuffer();
        try {
            byte[] bytes = null;
            try {
                bytes = this.encryptToMD5(authCode, userAccount).getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("未识别的字符编码:", e);
            }
            if (bytes == null) {
                return "0";
            }
            String md5 = bytes.toString();
            char[] verify = md5.substring(md5.length() - verifyCodeLength, md5.length()).toCharArray();
            for (char aVerify : verify) {
                String tmp = String.valueOf(getCharAscii(aVerify));
                signCode.append(tmp.substring(tmp.length() - 1));
            }

//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < bytes.length; i++) {
//                if ((bytes[i] & 0xff) < 0x10) {
//                    sb.append("0");
//                }
//                sb.append(Long.toString(bytes[i] & 0xff, 16));
//            }
//            String md5 = sb.toString();
//            int n = verifyCodeLength;
//            char[] verify = md5.substring(md5.length() - n, md5.length()).toCharArray();
//            for (int i = 0; i < verify.length; i++) {
//                String tmp = String.valueOf(getCharAscii(verify[i]));
//                signCode.append(tmp.substring(tmp.length() - 1));
//            }
        } catch (NoSuchAlgorithmException e) {
            return signCode.toString();
        }
        return signCode.toString();
    }

    /**
     * linux下生成随机码
     *
     * @return
     * @throws IOException
     */
    private String otherGenerate() throws IOException {
        File file = new File(this.linux_file);
        if (!file.canWrite()) {
            logger.warn("文件:{}没有写权限,将改用另一种方案", this.linux_file);
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] random = new byte[randomNumLength];
        try {
            int result = fi.read(random);
            if (result == -1) {
                return null;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fi.close();
        }
        StringBuffer sb = new StringBuffer();
        //对每个字节模10
        for (int i = 0; i < random.length; i++) {
            byte b = random[i];
            sb.append(b % 10);
        }
        //去除"-"
        return sb.toString().replace("-", "");
    }

    private int getCharAscii(char code) {
        return Character.getNumericValue(code);
//        return code + 0;
    }

    public void setAccountLen(int accountLen) {
        this.accountLen = accountLen;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

}
