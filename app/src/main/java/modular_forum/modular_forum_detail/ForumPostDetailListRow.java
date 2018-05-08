package modular_forum.modular_forum_detail;

public class ForumPostDetailListRow {
    private String commDate;	//转换格式为 yyyy/MM/dd HH:mm:ss
    private int commId;
    private String commText;
    private String userName;
    private String userId;
    private String userHeaderUrl;

    public String getCommDate() {
        return commDate;
    }

    public void setCommDate(String commDate) {
        this.commDate = commDate;
    }

    public int getCommId() {
        return commId;
    }

    public void setCommId(int commId) {
        this.commId = commId;
    }

    public String getCommText() {
        return commText;
    }

    public void setCommText(String commText) {
        this.commText = commText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserHeaderUrl() {
        return userHeaderUrl;
    }

    public void setUserHeaderUrl(String userHeaderUrl) {
        this.userHeaderUrl = userHeaderUrl;
    }
}
