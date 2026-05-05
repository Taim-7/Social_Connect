package Classes;

public class TextPost extends Posts {
    private String text;

    public TextPost(String userID, String postSecurity, String text) {
        super(userID, postSecurity);

        if (text.length() > 300) {
            throw new IllegalArgumentException("Text too long (max 300)");
        }

        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text.length() > 300) {
            throw new IllegalArgumentException("Text too long (max 300)");
        }

        this.text = text;
    }
}