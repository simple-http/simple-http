package bad.robot.http;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class StringMessageContent implements MessageContent {

    private final String content;

    public StringMessageContent(String content) {
        this.content = content;
    }

    @Override
    public String asString() {
        return content;
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
        return "StringMessageBody{content=" + content + '}';
    }
}
