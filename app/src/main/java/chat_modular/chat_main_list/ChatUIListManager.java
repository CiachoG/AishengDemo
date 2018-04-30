package chat_modular.chat_main_list;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.ciacho.aishengdemo.R;

import java.util.ArrayList;
import java.util.List;
import chat_modular.chat_main.ChatActivity;
import chat_modular.chat_tools.CNCharTransfor;

public class ChatUIListManager {
    private ListView list_chatList;

    private ChatActivity context;

    private List<ChatListRow> chatDataList;
    private ChatUIListAdapter chatUIListAdapter;

    public ChatUIListManager(ChatActivity context) {
        this.context = context;
        signal_addNewRow = false;
        iniData();
        iniEvent();
    }

    private boolean signal_addNewRow;

    private void iniData(){
        chatDataList = new ArrayList<>();
        chatDataList.add(new ChatListRow(ChatListRow.TYPE_OTHER,"你最近好吗"));
    }

    private void iniEvent() {
        list_chatList = (ListView) context.findViewById(R.id.list_chatList);
        list_chatList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
                //有添加新行并为滚动到最后一行
                if (signal_addNewRow && lastVisibleItem != list_chatList.getCount() - 1) {
                    scrollToLastPosition();
                    signal_addNewRow = false;
                }
            }
        });
        chatUIListAdapter = new ChatUIListAdapter(context, chatDataList);
        list_chatList.setAdapter(chatUIListAdapter);
    }

    public void addListSelfRow(String str) {
        ChatListRow row = new ChatListRow(ChatListRow.TYPE_SLEF, str);
        chatDataList.add(row);
        chatUIListAdapter.notifyDataSetChanged();
        signal_addNewRow = true;
    }

    public void addListOtherRow(String str) {
        ChatListRow row = new ChatListRow(ChatListRow.TYPE_OTHER, str);
        chatDataList.add(row);
        chatUIListAdapter.notifyDataSetChanged();
        signal_addNewRow = true;
    }

    public void scrollToLastPosition(){
        list_chatList.smoothScrollToPosition(list_chatList.getCount() - 1);
    }
}
