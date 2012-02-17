package bad.robot.http;

import jedi.functional.Functor;

import java.util.*;

import static jedi.functional.FunctionalPrimitives.collect;

public class Tuples implements MessageContent {

    private final Map<String, String> tuples = new HashMap<String, String>();

    public static Tuples tuples(String... values) {
        return new Tuples(values);
    }

    private Tuples(String... values) {
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

    @Override
    public String asString() {
        List<String> list = collect(tuples.entrySet(), new Functor<Map.Entry<String, String>, String>() {
            @Override
            public String execute(Map.Entry<String, String> tuple) {
                return tuple.getKey() + "=" + tuple.getValue();
            }
        });
        return Arrays.toString(list.toArray());
    }
}
