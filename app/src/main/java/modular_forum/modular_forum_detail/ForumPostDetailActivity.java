package modular_forum.modular_forum_detail;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ciacho.aishengdemo.Quantity;
import com.example.ciacho.aishengdemo.R;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import main_app.MainApplication;
import modular_forum.ForumDataLoader;

public class ForumPostDetailActivity extends AppCompatActivity {
    private static final int CODE_UPDATELIST=0,CODE_LOADERROR=1,CODE_LOADTOP=2,
            CODE_COMMENT_SUCCESS=3,CODE_COMMENT_ERROR=4;

    private ImageButton btn_back;
    private TextView text_loading;
    private ListView list_forumDetailList;
    private View topView;
    private CircleImageView imgView_headport;
    private String topImgUrl;

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
        nowAllPage=0;
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
                            loadTopContentImgHeader();
                            list_forumDetailList.setAdapter(forumPostDetailAdapter);
                            //成功显示顶层帖子主内容
                        }
                        break;
                    case CODE_UPDATELIST:
                        isLoading=false;
                        if(hasNewData){
                            nowAllPage++;
                        }else{
                            progBar_loadingPage.setVisibility(View.GONE);
                            text_noLoadingMore.setVisibility(View.VISIBLE);
                        }
                        forumPostDetailAdapter.notifyDataSetChanged();
                        break;
                    case CODE_LOADERROR:
                        isLoading=false;
                        list_forumDetailList.setVisibility(View.GONE);
                        text_loading.setVisibility(View.VISIBLE);
                        text_loading.setText("加载出错.....");
                        break;
                    case CODE_COMMENT_SUCCESS:
                        isLoading=false;
                        Toast.makeText(ForumPostDetailActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                        edit_commText.setText("");
                        btn_commSend.setClickable(true);
                        list_forumDetailList.requestFocus();
                        break;
                    case CODE_COMMENT_ERROR:
                        isLoading=false;
                        btn_commSend.setClickable(true);
                        Toast.makeText(ForumPostDetailActivity.this,"评论失败",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        asyncLoadDetailData(); //活动创建开始默认加载顶层内容
        iniStatusBar();
        iniView();
        iniEvent();
    }

    private void iniStatusBar(){
        StatusBarCompat.setStatusBarColor(this, Color.WHITE, true);
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
        imgView_headport=topView.findViewById(R.id.imgView_headport);

        text_postTitle.setText(item.getPostTitle());
        text_userName.setText(item.getUserName());
        text_postTime.setText(item.getPostDate());
        text_postContent.setText(item.getPostContent());
        topImgUrl=item.getUserHeaderUrl();
    }

    private void loadTopContentImgHeader(){
        Glide.with(this)
                .load(Quantity.SERVER_URL+topImgUrl)
                .animate(R.anim.anim_alpha)
                .placeholder(R.drawable.img_headport_default)
                .error(R.drawable.img_headport_default)     //加载失败后放置的默认图像
                .diskCacheStrategy(DiskCacheStrategy.RESULT)    //只缓存最终结果图像
                .into(imgView_headport);
    }

    private void syncLoadCommentData(int page){
        List<ForumPostDetailListRow>resultList=ForumDataLoader.
                loadingPostDetailCommData(PostId,page);

        if(resultList!=null){
            hasNewData=(resultList.size()>0);
            if(resultList.size()>0){
                dataList.addAll(resultList);
            }
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

        btn_commSend.setClickable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String CommText=edit_commText.getText().toString();
                String UserId=((MainApplication)getApplication()).getUserId();
                String result=ForumDataLoader.sendCommentData
                        (CommText,ForumDataLoader.DATE_FORMAT.format(new Date()),UserId,PostId);

                if(result.equals("true")){
                    nowAllPage=0;
                    isLoading=true;
                    for(int i=dataList.size()-1;i>=1;--i)
                        dataList.remove(i);

                    syncLoadCommentData(nowAllPage);
                    mHandler.sendEmptyMessage(CODE_COMMENT_SUCCESS);
                }else{
                    mHandler.sendEmptyMessage(CODE_COMMENT_ERROR);
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        Glide.get(this).clearMemory();  //清除内存缓存
        super.onDestroy();
    }
}
