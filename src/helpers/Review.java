package helpers;

public class Review {
    private Long id;
    private String rating;
    private String comment;

    public Review() {
        this.id = null;
        this.rating = null;
        this.comment = null;
    }

    public Review(long id, String rating, String comment) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
