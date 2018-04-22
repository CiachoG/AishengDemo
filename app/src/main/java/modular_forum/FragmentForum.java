package modular_forum;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ciacho.aishengdemo.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentForum extends Fragment {
    public static final int CODE_UPDATELIST=0;
    private View mCacheView;    //缓存视图
    private ListView list_forum;
    TextView text_loading;

    private List<ForumListRow> dataList;
    private ForumListAdapter forumListAdapter;
    private Handler mHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch(msg.what){
                    case CODE_UPDATELIST:
                        forumListAdapter.notifyDataSetChanged();
                        list_forum.setVisibility(View.VISIBLE);
                        text_loading.setVisibility(View.GONE);
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

            list_forum= (ListView) mCacheView.findViewById(R.id.list_forum);
            text_loading= (TextView) mCacheView.findViewById(R.id.text_loading);

            list_forum.setVisibility(View.GONE);
            text_loading.setVisibility(View.VISIBLE);

            dataList=new ArrayList<>();
            forumListAdapter=new ForumListAdapter(this.getContext(),dataList);
            list_forum.setAdapter(forumListAdapter);
            list_forum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                }
            });
            asyncLoadData();
        }
        return mCacheView;
    }

    //异步加载数据
    private void asyncLoadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                iniData();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mHandler.sendEmptyMessage(CODE_UPDATELIST);
            }
        }).start();
    }

    private void iniData(){
        dataList.add(new ForumListRow("我是个无声的女孩。聋人的美女们，愿意做我交朋友吗？",
                "oggNue","23","2018-3-23"));

        dataList.add(new ForumListRow("如果真心相爱，真的不分聋哑和正常人",
                "533543","11","2018-2-23"));

        dataList.add(new ForumListRow("我发表了一篇图片贴，大伙来看看吧~",
                "AgeChanger","3","2018-1-17"));

        dataList.add(new ForumListRow("网红聋哑外卖小哥 打开聋哑人就业新窗口",
                "其实还行","4","昨天"));

        dataList.add(new ForumListRow("迷路聋哑人从青岛走到诸城热心人报警民警帮他找到家",
                "JiaoYY","45","前天"));

        dataList.add(new ForumListRow("“无声”面包店网络走红 17名90后员工均为聋哑人",
                "Glossy","12","16:09"));

        dataList.add(new ForumListRow("湖北竹山公安救助一走失聋哑人",
                "兮兮郁郁","34","2018-1-29"));

        dataList.add(new ForumListRow("无声有爱 商丘妇科医院助力聋哑人就医",
                "磨毛","12","17:08"));

        dataList.add(new ForumListRow("一位聋哑人的回家路(六):家是最温暖的地方 他想用心呵护它",
                "1C","15","12:56"));

        dataList.add(new ForumListRow("“OK手势”暖人心:聋哑人办理银行卡 邮储营业员手语指导开户",
                "xiaQW","7","12:34"));

        dataList.add(new ForumListRow("河南郑州:聋哑母亲剖宫产 护士全程写字沟通",
                "是日天晴","678","昨天"));

        dataList.add(new ForumListRow("厉害了!3D打印机械手会打手语 能帮助聋哑人沟通",
                "风铃ing","12","4:08"));
    }
}
