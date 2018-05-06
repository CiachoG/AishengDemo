package modular_forum.modular_forum_main;

public class ForumListRow {
    private String postId;
    private String postTitle;
    private String lastCommDate;
    private String userName;
    private String commentNum;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getLastCommDate() {
        return lastCommDate;
    }

    public void setLastCommDate(String lastCommDate) {
        this.lastCommDate = lastCommDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }
}
