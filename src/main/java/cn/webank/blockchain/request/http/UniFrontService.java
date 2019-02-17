package cn.webank.blockchain.request.http;

import retrofit2.Call;
import retrofit2.http.*;


/**
 * Created by Junqi Zhang on 26/08/2017.
 */
public interface UniFrontService {

    @GET("wepop/issuingbank/ping")
    Call<String> ping(@Query("id") String id);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("wepop/issuingbank/callmethod")
    Call<String> callMethod(@Body String regMsg);
}
