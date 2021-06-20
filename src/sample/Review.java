package sample;

public class Review {
    private int rating;
    private String comment;

    public Review() {
        this.rating = 0;
        this.comment = null;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
