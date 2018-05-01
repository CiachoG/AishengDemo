package modular_forum.modular_forum_main;

import java.util.Date;

public class ForumListRow {
    private String PostId;
    private String PostTitle;
    private String LastCommDate;
    private String UserName;
    private String CommentNum;

    public ForumListRow(String postId, String postTitle, String lastCommDate, String userName, String commentNum) {
        PostId = postId;
        PostTitle = postTitle;
        LastCommDate = lastCommDate;
        UserName = userName;
        CommentNum = commentNum;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getPostTitle() {
        return PostTitle;
    }

    public void setPostTitle(String postTitle) {
        PostTitle = postTitle;
    }

    public String getLastCommDate() {
        return LastCommDate;
    }

    public void setLastCommDate(String lastCommDate) {
        LastCommDate = lastCommDate;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCommentNum() {
        return CommentNum;
    }

    public void setCommentNum(String commentNum) {
        CommentNum = commentNum;
    }
}
