package club.swdev.webapp.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Link implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String url;

    public Link() {
    }

    public Link(String name, String url) {
        Objects.requireNonNull(name, "Link name cannot be null");
        this.name = name;
        this.url = (url == null) ? "" : url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Link link = (Link) obj;

        if (!name.equals(link.name)) return false;
        return Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Link(" + name + ',' + url + ')';
    }
}