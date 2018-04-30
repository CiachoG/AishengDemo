package modular_forum.modular_forum_main;

import java.util.Date;

public class ForumListRow {
    private long PostId;
    private String PostTitle;
    private Date LastCommDate;
    private String UserName;
    private String CommentNum;

    public ForumListRow(long postId, String postTitle, Date lastCommDate, String userName, String commentNum) {
        PostId = postId;
        PostTitle = postTitle;
        LastCommDate = lastCommDate;
        UserName = userName;
        CommentNum = commentNum;
    }

    public long getPostId() {
        return PostId;
    }

    public void setPostId(long postId) {
        PostId = postId;
    }

    public String getPostTitle() {
        return PostTitle;
    }

    public void setPostTitle(String postTitle) {
        PostTitle = postTitle;
    }

    public Date getLastCommDate() {
        return LastCommDate;
    }

    public void setLastCommDate(Date lastCommDate) {
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
