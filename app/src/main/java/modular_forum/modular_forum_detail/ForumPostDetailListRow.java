package modular_forum.modular_forum_detail;

import java.util.Date;

public class ForumPostDetailListRow {
    private String userName="";    //评论或楼主用户名
    private String contentText=""; //帖子内容或评论文本
    private Date rowDate;     //发表评论或帖子发帖时间

    private String postTitle="";   //帖子标题
    private int commId;          //评论Id
    private int userId;             //评论的用户Id

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public Date getRowDate() {
        return rowDate;
    }

    public void setRowDate(Date rowDate) {
        this.rowDate = rowDate;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public int getCommId() {
        return commId;
    }

    public void setCommId(int commId) {
        this.commId = commId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
