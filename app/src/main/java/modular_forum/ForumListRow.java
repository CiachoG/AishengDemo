package modular_forum;

public class ForumListRow {
    private long PostId;
    private String PostTitle;
    private String UserName;
    private String CommentNum;
    private String PostDate;

    public ForumListRow(String postTitle, String userName, String commentNum, String postDate) {
        PostTitle = postTitle;
        UserName = userName;
        CommentNum = commentNum;
        PostDate = postDate;
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

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }
}
