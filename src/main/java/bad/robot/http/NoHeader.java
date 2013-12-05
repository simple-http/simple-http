package bad.robot.http;

public class NoHeader implements Header {
    @Override
    public String name() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String value() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object that) {
        return that.getClass().equals(this.getClass());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
