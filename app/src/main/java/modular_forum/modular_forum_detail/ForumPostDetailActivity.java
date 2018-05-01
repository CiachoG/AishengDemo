package modular_forum.modular_forum_detail;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ciacho.aishengdemo.R;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import main_app.MainApplication;
import modular_dbaccess.SQLDataAccess;
import modular_forum.ForumDataLoader;
import modular_forum.ForumDataTimeTool;

public class ForumPostDetailActivity extends AppCompatActivity {
    private static final int CODE_UPDATELIST=0,CODE_LOADERROR=1,CODE_LOADTOP=2,CODE_SEND_COMMENT=3;
    private static final int PAGE_SIZE=10;

    private ImageButton btn_back;
    private TextView text_loading;
    private ListView list_forumDetailList;
    private View topView;

    private Handler mHandler;
    private String PostId;

    private List<ForumPostDetailListRow> dataList;
    private ForumPostDetailAdapter forumPostDetailAdapter;
    private View view_listFooter;
    private ProgressBar progBar_loadingPage;
    private TextView text_noLoadingMore;

    private int lastVisibleIndex;
    private int nowAllPage;
    private boolean hasNewData,isLoading;

    private EditText edit_commText;
    private ImageButton btn_commSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_frag_forum_detail);

        Bundle bundle=getIntent().getExtras();
        PostId=bundle.getString("POSTID");

        isLoading=false;
        nowAllPage=1;
        dataList=new ArrayList<>();
        forumPostDetailAdapter=null;
        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case CODE_LOADTOP:  //成功加载顶层数据
                        list_forumDetailList.setAdapter(forumPostDetailAdapter);
                        list_forumDetailList.setVisibility(View.VISIBLE);
                        text_loading.setVisibility(View.GONE);

                        if(forumPostDetailAdapter!=null){
                            list_forumDetailList.setAdapter(forumPostDetailAdapter);
                            //成功显示顶层帖子主内容

                            nowAllPage++;
                        }
                        break;
                    case CODE_UPDATELIST:
                        isLoading=false;
                        if(hasNewData){
                            nowAllPage++;
                            forumPostDetailAdapter.notifyDataSetChanged();
                        }else{
                            progBar_loadingPage.setVisibility(View.GONE);
                            text_noLoadingMore.setVisibility(View.VISIBLE);
                        }
                        break;
                    case CODE_LOADERROR:
                        isLoading=false;
                        list_forumDetailList.setVisibility(View.GONE);
                        text_loading.setVisibility(View.VISIBLE);
                        text_loading.setText("加载出错.....");
                        break;
                    case CODE_SEND_COMMENT:
                        Toast.makeText(ForumPostDetailActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                        edit_commText.setText("");
                        list_forumDetailList.requestFocus();
                        break;
                }
                return false;
            }
        });

        asyncLoadDetailData(); //活动创建开始默认加载顶层内容
        iniView();
        iniEvent();
    }

    private void iniView(){
        text_loading=findViewById(R.id.text_loading);
        text_loading.setVisibility(View.VISIBLE);
        list_forumDetailList=findViewById(R.id.list_forumDetailList);
        list_forumDetailList.setVisibility(View.GONE);
        btn_back=findViewById(R.id.btn_back);

        view_listFooter=LayoutInflater.from(this).inflate(R.layout.layout_frag_forum_list_footer,null,false);
        progBar_loadingPage=view_listFooter.findViewById(R.id.progBar_loadingPage);
        progBar_loadingPage.setVisibility(View.GONE);
        text_noLoadingMore=view_listFooter.findViewById(R.id.text_noLoadingMore);
        text_noLoadingMore.setVisibility(View.GONE);

        list_forumDetailList.addFooterView(view_listFooter);

        edit_commText=findViewById(R.id.edit_commText);
        btn_commSend=findViewById(R.id.btn_commSend);
    }

    private void iniEvent(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list_forumDetailList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&
                        lastVisibleIndex==forumPostDetailAdapter.getCount()){

                    progBar_loadingPage.setVisibility(View.VISIBLE);
                    text_noLoadingMore.setVisibility(View.GONE);
                    if(!isLoading){

                        isLoading=true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                syncLoadCommentData(nowAllPage);
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem
                    , int visibleItemCount, int totalItemCount) {
                //位置从0开始
                lastVisibleIndex=firstVisibleItem+visibleItemCount-1;
            }
        });
        btn_commSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment();
            }
        });
    }

    private void asyncLoadDetailData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                isLoading=true;
                boolean result=syncLoadTopData();
                if(result) {      //顶层内容成功加载
                    syncLoadCommentData(nowAllPage);
                }else isLoading=false;
            }
        }).start();
    }

    //同步加载顶层数据
    public boolean syncLoadTopData(){
        ForumPostDetailTopRow topRow= ForumDataLoader.loadingPostDetailTopData(PostId);

        if(topRow!=null){
            dataList.clear();
            dataList.add(new ForumPostDetailListRow()); //加入意义的数据行

            loadTopView(topRow);
            forumPostDetailAdapter=new ForumPostDetailAdapter(this,dataList,topView);
            mHandler.sendEmptyMessage(CODE_LOADTOP);
            return true;
        }else{
            mHandler.sendEmptyMessage(CODE_LOADERROR);
            return false;
        }
    }

    //生成固定的topView
    private void loadTopView(ForumPostDetailTopRow item){
        TextView text_postTitle,text_userName,text_postTime,text_postContent;

        topView= LayoutInflater.from(this).inflate(R.layout.layout_frag_forum_detail_top,null,false);
        text_postTitle=topView.findViewById(R.id.text_postTitle);
        text_userName=topView.findViewById(R.id.text_userName);
        text_postTime=topView.findViewById(R.id.text_postTime);
        text_postContent=topView.findViewById(R.id.text_postContent);

        text_postTitle.setText(item.getPostTitle());
        text_userName.setText(item.getUserName());
        text_postTime.setText(item.getPostDate());
        text_postContent.setText(item.getPostContent());
    }

    private void syncLoadCommentData(int page){
        List<ForumPostDetailListRow>resultList=ForumDataLoader.
                loadingPostDetailCommData(PostId,page);

        if(resultList!=null){
            hasNewData=(resultList.size()>0);
            mHandler.sendEmptyMessage(CODE_UPDATELIST);
        }else{
            mHandler.sendEmptyMessage(CODE_LOADERROR);
        }
    }

    private void comment(){
        String content=edit_commText.getText().toString();
        if(content.length()==0){
            Toast.makeText(this,"不能发表空评论",Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String CommText=edit_commText.getText().toString();
                String UserId=((MainApplication)getApplication()).getUserId();

                String result=ForumDataLoader.sendCommentData
                        (CommText,ForumDataLoader.DATE_FORMAT.format(new Date()),UserId,PostId);

                if(result.equals("OK")){
                    nowAllPage=1;
                    isLoading=true;
                    for(int i=dataList.size()-1;i>=1;--i)
                        dataList.remove(i);

                    syncLoadCommentData(nowAllPage);
                    mHandler.sendEmptyMessage(CODE_SEND_COMMENT);
                }
            }
        }).start();
    }
}
