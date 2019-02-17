package cn.webank.blockchain.request.http;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junqi Zhang on 27/08/2017.
 */
public class UniFrontServicePool {

    private static int index = 0;

    @Getter
    private List<RetrofitService<UniFrontService>> allUniFrontService = new ArrayList<>();

    public UniFrontServicePool(List<String> uniFrontHostList) {

        for (int i = 0; i < uniFrontHostList.size(); i++) {
            RetrofitService<UniFrontService> retrofitService = new RetrofitService<>(uniFrontHostList.get(i), UniFrontService.class);
            allUniFrontService.add(retrofitService);
        }
    }

    public RetrofitService<UniFrontService> getService() {

        int i = index++ % allUniFrontService.size();
        RetrofitService<UniFrontService> service = allUniFrontService.get(i);
        if (service.isHealth()) {
            return service;
        } else {
            for (RetrofitService<UniFrontService> serviceTemp : allUniFrontService) {
                if (serviceTemp.isHealth()) {
                    return serviceTemp;
                }
            }
        }
        return allUniFrontService.get(0);
    }

    public RetrofitService<UniFrontService> getService(int index) {
        return allUniFrontService.get(index);
    }

    public int size() {
        return allUniFrontService.size();
    }
}
