package Classes;

public class ImagePost extends Posts {
    private String name;
    private String description;
    private String location;

    public ImagePost(String userID, String postSecurity, String name, String description, String location) {
        super(userID, postSecurity);

        if (Validations.isEmpty(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (Validations.isEmpty(description)) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        this.name = name;
        this.description = description;
        this.location = location;
    }

    // Getters
    public String getName() {
        return name;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}