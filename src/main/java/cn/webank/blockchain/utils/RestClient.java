package cn.webank.blockchain.utils;

import cn.webank.blockchain.request.http.RetrofitService;
import cn.webank.blockchain.request.http.UniFrontService;
import cn.webank.blockchain.request.http.UniFrontServicePoolInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

/**
 * Created by Junqi Zhang on 2017/5/19.
 */
public class RestClient {
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    private HttpStatus status;

    public RestClient(List<String> uniFrontSvrUrlArr) {
        UniFrontServicePoolInstance.INSTANCE.init(uniFrontSvrUrlArr);
    }

    /*
     * TODO : 有些问题
     */
    public void asyncPost(String json) throws ConnectException {

        RetrofitService<UniFrontService> service = UniFrontServicePoolInstance.INSTANCE.getServicePool().getService();
        Call<String> call = service.getService().callMethod(json);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // tasks available
                } else {
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // something went completely south (like no internet connection)
                // logger.d("Error", t.getMessage());
                if (null != call) {
                    // tasks available
                } else {
                    // error response, no access to resource?
                }
            }
        });
    }

    public String post(String uri, String json) throws ConnectException, IOException {

        try {
            RetrofitService<UniFrontService> service = UniFrontServicePoolInstance.INSTANCE.getServicePool().getService();
            Response<String> response = service.getService().callMethod(json).execute();
            logger.debug("uni front server : " + service.getApiBaseUrl());
            return response.body();
        } catch (ConnectException e) {
            /*
             * 连接不上前置，就会抛ConnectException
             */

            logger.error("{}", e);
            throw e;
        } catch (IOException e) {
            logger.error("{}", e);
            /*
             * TODO : handle IOException
             */
            throw e;
        }
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}