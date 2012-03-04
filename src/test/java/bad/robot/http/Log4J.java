package bad.robot.http;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.hamcrest.Matcher;

import java.io.StringWriter;
import java.util.UUID;

public class Log4J {

    private final StringWriter writer = new StringWriter();
    private final Logger logger;
    private final String uuid = UUID.randomUUID().toString();

    public static Log4J appendTo(Logger logger) {
        return new Log4J(logger, Level.ALL);
    }

    public static Log4J appendTo(Logger logger, Level level) {
        return new Log4J(logger, level);
    }

    Log4J(Logger logger, Level level) {
        this.logger = logger;
        WriterAppender appender = new WriterAppender(new SimpleLayout(), writer);
        appender.setName(uuid);
        logger.addAppender(appender);
        logger.setLevel(level);
    }

    public void clean() {
        logger.removeAppender(uuid);
    }

    public void assertThat(Matcher<String> matcher) {
        org.junit.Assert.assertThat(writer.toString(), matcher);
    }

    public boolean matches(Matcher<String> matcher) {
        return matcher.matches(writer.toString());
    }

    public String toString() {
        return writer.toString();
    }
}
