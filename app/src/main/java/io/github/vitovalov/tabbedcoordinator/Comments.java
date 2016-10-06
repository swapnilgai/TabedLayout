package io.github.vitovalov.tabbedcoordinator;

/**
 * Created by swapnil on 5/29/16.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comments {

    @JsonIgnoreProperties(ignoreUnknown = true)
    private int commentId;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String userID; // ref to signup entity
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String userName; // ref to signup entity
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String userImage; // ref to signup entity
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String commentText;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private boolean isImage;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String voteUp;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String voteDown;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private int eventId;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public String getVoteUp() {
        return voteUp;
    }

    public void setVoteUp(String voteUp) {
        this.voteUp = voteUp;
    }

    public String getVoteDown() {
        return voteDown;
    }

    public void setVoteDown(String voteDown) {
        this.voteDown = voteDown;
    }

}
