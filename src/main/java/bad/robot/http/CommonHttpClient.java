package bad.robot.http;

import com.google.code.tempusfugit.temporal.Duration;

/**
 * The common configuration interface for all {@link HttpClient} implementations.
 *
 * Clients may or may not implement the various configuration parameters and they may or may not take affect after actual
 * {@link HttpClient} calls. For example, you might set a timeout using the {@link CommonHttpClient#with(com.google.code.tempusfugit.temporal.Duration)}
 * and then call the {@link HttpClient#get(java.net.URL)} to perform a HTTP GET. The underlying HTTP client implementation
 * should respect the timeout configuration but is free to ignore subsequent calls to set the timeout (ie, implementations
 * may choose to lazy load the underlying client).
 *
 */
public interface CommonHttpClient extends HttpClient {

    CommonHttpClient with(Duration timeout);

    CommonHttpClient with(String username, String password);

}
