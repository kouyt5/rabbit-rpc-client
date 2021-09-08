package com.chenc.amqptest.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Shuffle {

    private static Set<String> idSet = Collections.synchronizedSet(new HashSet<String>());
    private static Map<String, String> idMap = Collections.synchronizedMap(new HashMap<String, String>());

    public static String getId(String index) {
        if (index == "1") {
            while (true) {
                if (idMap.containsKey("1")) {
                    return idMap.get("1");
                }
            }
        }
        // 获取到2
        while (true) {
            if (idMap.containsKey("1")) {
                return idMap.get("1");
            }
        }
    }

    public static void setId(String index, String id){
        System.out.println("put a id"+id);
        idMap.put(index, id);
    }

}
