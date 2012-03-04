package bad.robot.http.listener;

import bad.robot.http.HttpClient;
import bad.robot.http.HttpResponse;
import com.google.code.tempusfugit.temporal.Clock;
import com.google.code.tempusfugit.temporal.StopWatch;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TimedHttpClient implements InvocationHandler {

    private final HttpClient delegate;
    private final Logger log;
    private final Clock clock;

    public static HttpClient timedHttpClient(HttpClient delegate, Clock clock, Class<?> logger) {
        verify(logger);
        return (HttpClient) Proxy.newProxyInstance(delegate.getClass().getClassLoader(),
                new Class[]{HttpClient.class}, new TimedHttpClient(delegate, clock, Logger.getLogger(logger)));
    }

    private static void verify(Class<?> logger) {
        if (logger.getName().equals(Object.class.getName()))
            throw new IllegalArgumentException("please carefully select a logger class and make sure it's configured in log4j.xml");
    }

    private TimedHttpClient(HttpClient delegate, Clock clock, Logger logger) {
        this.log = logger;
        this.clock = clock;
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StopWatch stopWatch = StopWatch.start(clock);
        Object result = null;
        try {
            result = method.invoke(delegate, args);
            return result;
        } finally {
            if (log.isInfoEnabled())
                log.info(message(method, args, stopWatch, result));
        }
    }

    private String message(Method method, Object[] args, StopWatch stopWatch, Object result) {
        return new StringBuilder()
            .append(method.getName().toUpperCase())
            .append(" ")
            .append(args[0].toString())
            .append(" was ")
            .append(response(result))
            .append(", took ")
            .append(stopWatch.markAndGetTotalElapsedTime())
            .toString();
    }

    private static StringBuilder response(Object result) {
        StringBuilder builder = new StringBuilder();
        if (result instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) result;
            return builder.append(response.getStatusCode()).append(" (").append(response.getStatusMessage()).append(")");
        }
        return builder;
    }
}
