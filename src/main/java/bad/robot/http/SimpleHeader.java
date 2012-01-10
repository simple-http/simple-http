package bad.robot.http;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class SimpleHeader implements Header {

    private final String name;
    private final String value;

    private SimpleHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static SimpleHeader header(String name, String value) {
        return new SimpleHeader(name, value);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object that) {
        return reflectionEquals(this, that);
    }

    @Override
    public String toString() {
        return "SimpleHeader{name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
