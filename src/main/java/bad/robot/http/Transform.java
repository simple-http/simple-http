package bad.robot.http;

public interface Transform<FROM, TO> {
    TO call(FROM value);
}
