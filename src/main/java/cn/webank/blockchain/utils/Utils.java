package cn.webank.blockchain.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Junqi Zhang on 11/11/2017.
 */
public class Utils {

    public static String getMapHeader(LinkedHashMap map) {
        String header = "";
        for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
            header += key.toString();
            if (it.hasNext()) {
                header += "|";
            }
        }
        return header;
    }

    public static String getMapContent(LinkedHashMap map) {
        String content = "";
        for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
            content += map.get(key).toString();
            if (it.hasNext()) {
                content += "|";
            }
        }
        return content;
    }

}
