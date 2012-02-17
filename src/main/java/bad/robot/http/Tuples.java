package bad.robot.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tuples implements MessageContent {

    private final Map<String, String> tuples = new HashMap<String, String>();

    public Tuples(String... values) {
        if (values.length % 2 != 0)
            throw new IllegalArgumentException("should be a even number of arguments, received" + values.length);
        toMap(values);
    }

    private void toMap(String[] values) {
        for (int i = 0; i < values.length; i += 2)
            tuples.put(values[i], values[i + 1]);
    }

    public <T> List<T> transform(Transform<Map.Entry<String, String>, T> transform) {
        List<T> pairs = new ArrayList<T>();
        for (Map.Entry<String, String> tuple : tuples.entrySet())
            pairs.add(transform.call(tuple));
        return pairs;
    }

}
