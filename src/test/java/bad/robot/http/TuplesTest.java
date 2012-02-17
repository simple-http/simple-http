package bad.robot.http;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TuplesTest {
    
    @Test (expected = IllegalArgumentException.class)
    public void shouldAllowOnlyPairs() {
        new Tuples("name");        
    }

    @Test
    public void shouldAllowNoArguments() {
        new Tuples();
    }

    @Test
    public void shouldAllowRetrievalViaTransformation() {
        Tuples tuples = new Tuples("name", "value", "cheese", "ham");
        List<String> values = tuples.transform(new Transform<Map.Entry<String, String>, String>() {
            @Override
            public String call(Map.Entry<String, String> tuple) {
                return tuple.getKey() + "=" + tuple.getValue();
            }
        });
        assertThat(values.get(0), is("name=value"));
        assertThat(values.get(1), is("cheese=ham"));
    }

}
