package modular_forum;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import modular_forum.modular_forum_detail.ForumPostDetailActivity;
import com.baoyz.widget.PullRefreshLayout;
import com.example.ciacho.aishengdemo.R;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import main_app.MainApplication;
import modular_dbaccess.SQLDataAccess;
import modular_forum.modular_forum_main.ForumListAdapter;
import modular_forum.modular_forum_main.ForumListRow;
import modular_forum.modular_forum_main.ForumPostingActivity;

public class FragmentForum extends Fragment {
    public static final int CODE_UPDATELIST=0,CODE_LOADERROR=1;
    public static final int REQ_POSTING=0;
    public static final int PAGE_LENGTH=10;

    private View mCacheView;    //缓存视图
    private TextView text_loading;
    private PullRefreshLayout layout_pullRefreshLayout;
    private ListView list_forum;
    private ImageButton imgbtn_addNewForum;
    private boolean hasNewData;

    private ProgressBar progBar_loadingPage;
    private TextView text_noLoadingMore;
    private int lastVisibleIndex;
    private boolean isLoading;  //是否正在进行分页查询

    private List<ForumListRow> dataList;
    private ForumListAdapter forumListAdapter;
    private Handler mHandler;

    private int nowAllPage;    //第nowAllPage等待加载

    private MainApplication app;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app= (MainApplication) getContext().getApplicationContext();
        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch(msg.what){
                    case CODE_UPDATELIST:   //数据加载完成，更新UI
                        isLoading=false;
                        if(hasNewData){ //有新的数据需要加载
                            nowAllPage++;
                            forumListAdapter.notifyDataSetChanged();

                            imgbtn_addNewForum.setVisibility(View.VISIBLE);
                            list_forum.setVisibility(View.VISIBLE);
                            text_loading.setVisibility(View.GONE);

                            progBar_loadingPage.setVisibility(View.GONE);
                            text_noLoadingMore.setVisibility(View.VISIBLE);
                        }else{      //已经到底了
                            imgbtn_addNewForum.setVisibility(View.VISIBLE);
                            list_forum.setVisibility(View.VISIBLE);
                            text_loading.setVisibility(View.GONE);

                            progBar_loadingPage.setVisibility(View.GONE);
                            text_noLoadingMore.setVisibility(View.VISIBLE);
                        }
                        break;
                    case CODE_LOADERROR:    //数据加载失败
                        isLoading=false;
                        imgbtn_addNewForum.setVisibility(View.GONE);
                        list_forum.setVisibility(View.GONE);
                        text_loading.setVisibility(View.VISIBLE);
                        text_loading.setText("加载出现错误....");
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if(mCacheView==null){
            mCacheView=inflater.inflate(R.layout.layout_frag_forum,container,false);

            iniView();
            iniEvent();

            dataList=new ArrayList<>();
            forumListAdapter=new ForumListAdapter(this.getContext(),dataList);

            View view=inflater.inflate(R.layout.layout_frag_forum_list_footer,null,false);
            progBar_loadingPage=view.findViewById(R.id.progBar_loadingPage);
            progBar_loadingPage.setVisibility(View.VISIBLE);
            text_noLoadingMore=view.findViewById(R.id.text_noLoadingMore);
            text_noLoadingMore.setVisibility(View.GONE);

            hasNewData=true;
            lastVisibleIndex=0;
            isLoading=false;
            list_forum.addFooterView(view);
            list_forum.setAdapter(forumListAdapter);

            nowAllPage=1;
            asyncLoadData(nowAllPage);
        }
        return mCacheView;
    }

    private void iniView(){
        if(mCacheView==null)    return;

        layout_pullRefreshLayout=mCacheView.findViewById(R.id.layout_pullRefreshLayout);
        list_forum=mCacheView.findViewById(R.id.list_forum);
        text_loading=mCacheView.findViewById(R.id.text_loading);

        imgbtn_addNewForum=mCacheView.findViewById(R.id.imgbtn_addNewForum);

        text_loading.setVisibility(View.VISIBLE);
        list_forum.setVisibility(View.GONE);
        imgbtn_addNewForum.setVisibility(View.GONE);
    }

    private void iniEvent(){
        list_forum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(), ForumPostDetailActivity.class);
                long PostId=dataList.get(position).getPostId();
                Bundle bundle=new Bundle();
                bundle.putLong("POSTID",PostId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        list_forum.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&
                        lastVisibleIndex==forumListAdapter.getCount()){
                    //上拉加载
                    if(app.getUserId().equals("-1")) {
                        Toast.makeText(getContext(),"请先登录，来查看更多...",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(!isLoading){     //未在加载状态中
                        progBar_loadingPage.setVisibility(View.VISIBLE);
                        text_noLoadingMore.setVisibility(View.GONE);

                        asyncLoadData(nowAllPage);
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

        imgbtn_addNewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(app.getUserId().equals("-1")){
                    Toast.makeText(getContext(),"你当前没有权限发帖",Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent=new Intent(getContext(), ForumPostingActivity.class);
                getActivity().startActivityForResult(intent,REQ_POSTING);
            }
        });

        layout_pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        layout_pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout_pullRefreshLayout.setRefreshing(false);

                nowAllPage=1;
                dataList.clear();
                asyncLoadData(nowAllPage);
            }
        });
    }

    //异步加载数据
    public void asyncLoadData(final int loadingPage){
        isLoading=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                syncLoadForumDataPage(loadingPage);
            }
        }).start();
    }

    private void syncLoadForumDataPage(int page){
        try {
            String uniqueViewName="T_Temp_Result_"+app.getUserId();
            String createView_sql="create or replace view "+uniqueViewName+" as " +
                    "select T_Forum_Post.Id PostId,PostTitle,to_char(T_Temp_LastCommDate.LastCommDate,'yyyy/mm/dd HH24:mi:ss') LastCommDate, UserName,CommentNum " +
                    "from T_Forum_Post,T_Global_User, " +
                    "((select T_Forum_Comment.PostId CommPostId,count(*) CommentNum from T_Forum_Comment group by T_Forum_Comment.PostId) " +
                    "union (select Id PostId,0 CommentNum from T_Forum_Post where Id not in(select PostId from T_Forum_Comment)))   T_Temp_CommNum, " +
                    "((select T_Forum_Post.Id PostId,max(T_Forum_Comment.CommDate) LastCommDate  from T_Forum_Post,T_Forum_Comment  where T_Forum_Post.Id=T_Forum_Comment.PostId  group by T_Forum_Post.Id) " +
                    "union (select Id PostId,PostDate LastCommDate from T_Forum_Post where Id not in(select PostId from T_Forum_Comment)))T_Temp_LastCommDate " +
                    "where T_Forum_Post.UserId=T_Global_User.Id and T_Forum_Post.Id=T_Temp_CommNum.CommPostId and T_Temp_LastCommDate.PostId=T_Forum_Post.Id "+
                    "order by LastCommDate desc ";

            String query_sql="select * from "+uniqueViewName+" " +
                    "where  rownum<="+(page*PAGE_LENGTH)+" and PostId not in (select PostId from "+uniqueViewName+" where rownum<= "+((page-1)*PAGE_LENGTH)+ " )";

            String dropView_sql="drop view "+uniqueViewName;

            Method method=FragmentForum.class.getMethod("solveResultSet",ResultSet.class);
            SQLDataAccess.query(new String[]{createView_sql,query_sql,dropView_sql},
                    1,method,this);//先查询，后处理

            //处理完成没有出现异常信息
            mHandler.sendEmptyMessage(CODE_UPDATELIST);
        } catch (Exception e) {
            //抛出异常
            mHandler.sendEmptyMessage(CODE_LOADERROR);
            Log.e("帖子列表数据读取异常信息:",e.toString());
        }
    }

    //处理解析结果集变为列表,返回是否有结果
    public void solveResultSet(ResultSet rs)throws SQLException{
        int len=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        while(rs.next()){
            long PostId=rs.getInt(1);
            String PostTitle=rs.getString(2);

            Date LastCommDate= null;
            try{
                String dateStr=rs.getString(3);
                LastCommDate = sdf.parse(dateStr);
            }catch(ParseException e) {
                Log.e("日期转换错误:",e.toString());
            }
            String UserName=rs.getString(4);
            int CommentNum=rs.getInt(5);

            ForumListRow item=new ForumListRow
                    (PostId,PostTitle,LastCommDate,UserName,String.valueOf(CommentNum));

            dataList.add(item);
            len++;
        }
        hasNewData=(len>=1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //发帖成功后，论坛帖子界面要做的事情
        nowAllPage=1;
        dataList.clear();
        asyncLoadData(nowAllPage);
        Toast.makeText(getContext(),"发帖成功",Toast.LENGTH_SHORT).show();
    }
}
