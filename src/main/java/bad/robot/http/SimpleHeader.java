package bad.robot.http;

public class SimpleHeader implements Header {

    private final String name;
    private final String value;

    SimpleHeader(String name, String value) {
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
}
