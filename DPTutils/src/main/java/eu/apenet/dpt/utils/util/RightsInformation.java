package eu.apenet.dpt.utils.util;

public class RightsInformation {
    private String abbreviation;
    private String name;
    private String url;
    private String description;
    private String extra;

    @Override
    public String toString() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getExtra() {
        return extra;
    }
}
