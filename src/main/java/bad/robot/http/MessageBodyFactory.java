package bad.robot.http;

public interface MessageBodyFactory<T> {
    T create();
}
