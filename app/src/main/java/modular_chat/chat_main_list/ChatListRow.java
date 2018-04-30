package modular_chat.chat_main_list;


public class ChatListRow {
    public static final int TYPE_SLEF=0,TYPE_OTHER=1;
    private int chat_type;      //交流类别
    private String chat_content;    //交流内容

    public ChatListRow(int chat_type, String chat_content) {
        this.chat_type = chat_type;
        this.chat_content = chat_content;
    }

    public int getChatType() {
        return chat_type;
    }

    public void setChatType(int chat_type) {
        this.chat_type = chat_type;
    }

    public String getChatContent() {
        return chat_content;
    }

    public void setChatContent(String chat_content) {
        this.chat_content = chat_content;
    }
}
