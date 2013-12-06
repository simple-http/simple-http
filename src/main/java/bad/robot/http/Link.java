package bad.robot.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @see <a href="http://www.rfc-editor.org/rfc/rfc5988.txt">http://www.rfc-editor.org/rfc/rfc5988.txt</a>
 */
public class Link {

    private final Map<String, URL> links = new HashMap<>();

    public Link(String link) throws MalformedURLException {
        StringTokenizer tokenizer = new StringTokenizer(link, ",");
        while (tokenizer.hasMoreElements()) {
            String relativeLink = (String) tokenizer.nextElement();
            String href = findHref(relativeLink);
            String relation = findRelation(relativeLink);
            links.put(relation, new URL(href));
        }
    }

    public URL next() {
        return links.get("next");
    }

    public URL previous() {
        if (!links.containsKey("prev"))
        return links.get("previous");
        return links.get("prev");
    }

    public URL first() {
        return links.get("first");
    }

    public URL last() {
        return links.get("last");
    }

    private String findRelation(String link) {
        String token = "rel=\"";
        int start = link.indexOf(token);
        int end = link.indexOf("\"", start + token.length());
        return link.substring(start + token.length(), end).toLowerCase();
    }

    private String findHref(String link) {
        return link.substring(link.indexOf("<") + 1, link.indexOf(">;"));
    }

}
