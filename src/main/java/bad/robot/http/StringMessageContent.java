package bad.robot.http;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

/**
 * hashCode and equals intentionally reflect equality on String so this message body can pose as a String.
 */
public class StringMessageContent implements MessageContent {

    private final String content;

    public StringMessageContent(String content) {
        this.content = content;
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(content);
    }

    @Override
    public boolean equals(Object that) {
        return reflectionEquals(content, that);
    }

    @Override
    public String toString() {
        return "StringMessageBody{content=" + content + '}';
    }


}
