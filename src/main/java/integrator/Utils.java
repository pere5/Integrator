package integrator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Per Eriksson on 2017-01-29.
 */
public class Utils {
    public static String prettyList(List list) {
        return Arrays.toString(list.toArray());
    }

    public static String prettyMap(Map map) {
        return Arrays.toString(map.entrySet().toArray());
    }

    public static String prettySet(Set set) {
        return Arrays.toString(set.toArray());
    }
}
