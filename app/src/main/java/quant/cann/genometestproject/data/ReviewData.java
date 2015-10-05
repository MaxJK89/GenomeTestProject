package quant.cann.genometestproject.data;

/**
 * Created by angboty on 10/3/2015.
 */
public class ReviewData {

    public String authorName;
    public String rating;
    public String comment;
    public String authorNameUrl;

    public ReviewData(String authorName, String rating, String comment, String authorNameUrl) {
        this.authorName = authorName;
        this.rating = rating;
        this.comment = comment;
        this.authorNameUrl = authorNameUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
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

    public String getAuthorNameUrl() {
        return authorNameUrl;
    }

    public void setAuthorNameUrl(String authorNameUrl) {
        this.authorNameUrl = authorNameUrl;
    }


}
