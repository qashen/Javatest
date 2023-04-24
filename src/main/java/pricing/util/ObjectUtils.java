package pricing.util;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ObjectUtils {

    private ObjectUtils() {
    }

    public static boolean isEmpty( Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional) {
            return ((Optional<?>) obj).isEmpty();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }

        // else
        return false;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            new BigDecimal(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}