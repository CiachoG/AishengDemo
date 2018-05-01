package modular_forum.modular_forum_detail;

import java.util.Date;

public class ForumPostDetailListRow {
    private String CommId;
    private String UserId;
    private String UserName;
    private String CommDate;	//转换格式为 yyyy/MM/dd HH:mm:ss
    private String CommContent;

    public String getCommId() {
        return CommId;
    }

    public void setCommId(String commId) {
        CommId = commId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCommDate() {
        return CommDate;
    }

    public void setCommDate(String commDate) {
        CommDate = commDate;
    }

    public String getCommContent() {
        return CommContent;
    }

    public void setCommContent(String commContent) {
        CommContent = commContent;
    }
}
