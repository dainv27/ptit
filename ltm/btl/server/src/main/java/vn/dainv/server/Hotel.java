package vn.dainv.server;

public class Hotel {
    private final String id;
    private String name;
    private int stars;
    private String description;

    public Hotel(String id, String name, int stars, String description) {
        this.id = id;
        this.name = name;
        this.stars = stars;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
