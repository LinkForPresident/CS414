package GameServer.test;

import java.util.HashMap;
import java.util.Map;

public class ServerUtils {
    public HashMap<String, String> convertJsonStringToMap(String json) {
        HashMap<String, String> map = new HashMap<String, String>();
        json = json.replaceAll("\"","");
        json = json.replaceAll("\\{","");
        json = json.replaceAll("\\}","");

        String[] list = json.split(", ");
        for(String elem : list) {
            String[] subElem = elem.split(":");
            map.put(subElem[0].trim(), subElem[1].trim());
        }
        return map;
    }
}
