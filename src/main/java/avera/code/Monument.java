package avera.code;

public class Monument
{
    private String name;
    private String location;
    private String category;
    private String httpLink;
    private double latitude;
    private double longitude;
    private String regionMonument;
    private String stateMonument;
    private String description;

    public Monument(String name, String description, String location, String category, String httpLink, double latitude, double longitude, String regionMonument, String stateMonument)
    {
        this.name = name;
        this.description = description;
        this.location = location;
        this.category = category;
        this.httpLink = httpLink;
        this.latitude = latitude;
        this.longitude = longitude;
        this.regionMonument = regionMonument;
        this.stateMonument = stateMonument;
    }

    public Monument()
    {
        this.name = null;
        this.location = null;
        this.category = null;
        this.httpLink = null;
        this.latitude = 0;
        this.longitude = 0;
        this.regionMonument = null;
        this.stateMonument = null;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setHttpLink(String httpLink)
    {
        this.httpLink = httpLink;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public void setRegionMonument(String region)
    {
        this.regionMonument = region;
    }

    public void setStateMonument(String stateMonument)
    {
        this.stateMonument = stateMonument;
    }

    public String getName()
    {
        return this.name;
    }

    public String getLocation()
    {
        return this.location;
    }

    public String getCategory()
    {
        return this.category;
    }

    public String getHttpLink()
    {
        return this.httpLink;
    }

    public double getLatitude()
    {
        return this.latitude;
    }

    public double getLongitude()
    {
        return this.longitude;
    }

    public String getRegionMonument()
    {
        return this.regionMonument;
    }

    public String getStateMonument()
    {
        return this.stateMonument;
    }

    public String getDescription()
    {
        return this.description;
    }

    @Override
    public String toString() {
        return "Monument{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", category='" + category + '\'' +
                ", httpLink='" + httpLink + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", regionMonument='" + regionMonument + '\'' +
                ", stateMonument='" + stateMonument + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
