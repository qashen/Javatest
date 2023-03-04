package pricing.util;

import java.util.Map;

@Deprecated
public class OnMatch {
    public OnMatch() {
    }

    public void onMatch(Map<String, String> map, String key, String value) {
        String mapValue = map.get(key);
        if (mapValue.equals (value))
        {
            System.out.println ("Match is found");
        }
    }
}
