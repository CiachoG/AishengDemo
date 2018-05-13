package modular_chat.chat_main_list;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AbsListView;
import android.widget.ListView;
import com.example.ciacho.aishengdemo.R;
import java.util.ArrayList;
import java.util.List;


import modular_chat.chat_main.ChatActivity;

public class ChatUIListManager {
    public static final int CODE_UPDATE_UIROW=0;

    private ListView list_chatList;

    private ChatActivity context;

    private List<ChatListRow> chatDataList;
    private ChatUIListAdapter chatUIListAdapter;
    private ChatTuringRobot chatTuringRobot;

    private Handler mHandler;

    public ChatUIListManager(ChatActivity context) {
        this.context = context;

        iniData();
        iniEvent();
    }

    private boolean signal_addNewRow;

    private void iniData(){
        chatDataList = new ArrayList<>();
        signal_addNewRow = false;
    }

    private void iniEvent() {
        list_chatList = context.findViewById(R.id.list_chatList);
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
        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case CODE_UPDATE_UIROW:
                        Bundle bundle=message.getData();
                        String solvedText=bundle.getString("DATA");
                        addListSelfRow(solvedText);
                        context.getSpeechSynthesizeExecutor().startSpeechSynthesize(solvedText);
                        break;
                }
                return false;
            }
        });
        chatTuringRobot=new ChatTuringRobot(context,mHandler);
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

        if(context.isTuring())
            chatTuringRobot.work(str);
    }

    public void scrollToLastPosition(){
        list_chatList.smoothScrollToPosition(list_chatList.getCount() - 1);
    }
}
