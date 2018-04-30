package modular_forum.modular_forum_main;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ciacho.aishengdemo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import main_app.MainApplication;
import modular_dbaccess.SQLDataAccess;

public class ForumPostingActivity extends AppCompatActivity {
    public static final int POST_TITLE_MAXLENGTH=25;
    public static final int POST_CONTENT_MAXLENGTH=250;
    public static final int REQ_POSTING_SUCCESS=0;

    private ImageButton btn_back;
    private EditText edit_postTitle,edit_postContent;
    private TextView text_status;
    private Button btn_posting;

    private MainApplication app;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_frag_forum_posting);

        app= (MainApplication) getApplication();
        mHandler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case REQ_POSTING_SUCCESS:
                        setResult(RESULT_OK);
                        finish();
                        break;
                }
                return false;
            }
        });
        iniView();
        iniEvent();
    }

    private void iniView(){
        btn_back=findViewById(R.id.btn_back);
        edit_postTitle=findViewById(R.id.edit_postTitle);
        edit_postContent=findViewById(R.id.edit_postContent);
        text_status=findViewById(R.id.text_status);
        btn_posting=findViewById(R.id.btn_posting);
    }

    private void iniEvent(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backCancel();
            }
        });
        btn_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发帖
                try{
                    posting();
                }catch (Exception e){
                    Toast.makeText(ForumPostingActivity.this,
                            e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    create table T_Forum_Post(
        Id int primary key,
        PostTitle varchar(25) NOT NULL,		--帖子标题
        PostDate Date NOT NULL,				--帖子日期
        PostContent varchar(250) NOT NULL,	--帖子内容
        UserId int NOT NULL,					--发帖人用户Id
        foreign key(UserId) references T_Global_User(Id)
    );
     */
    private void posting()throws Exception{
        String PostTitle=edit_postTitle.getText().toString();
        if(PostTitle.length()==0)   throw new Exception("帖子标题不能为空");
        if(PostTitle.length()>POST_TITLE_MAXLENGTH)
            throw new Exception("帖子标题太长了");

        String PostDate=transDate(new Date());

        String PostContent=edit_postContent.getText().toString();
        if(PostContent.length()==0)     throw new Exception("帖子内容不能为空");
        if(PostContent.length()>POST_CONTENT_MAXLENGTH)
            throw new Exception("帖子内容太长了");

        String UserId=app.getUserId();
        if(UserId.equals("-1")) throw new Exception("用户登录异常");

        String sql=String.format("insert into T_Forum_Post values(post_seq.nextval"+
                        ",'%s',to_date('%s','yyyy/mm/dd HH24:mi:ss'),'%s',%s)",PostTitle,
                PostDate,PostContent,UserId);

        asyncPosting(sql);
    }

    private void asyncPosting(final String PostingSQL){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int result=SQLDataAccess.update(PostingSQL);
                    if(result==1){
                        mHandler.sendEmptyMessage(REQ_POSTING_SUCCESS);
                    }else
                        throw new Exception("数据库受影响行不等于1");
                } catch (Exception e) {
                    Log.e("发帖异常信息:",e.toString());
                }
            }
        }).start();
    }

    //SQL:yyyy/mm/dd hh:mi:ss
    private SimpleDateFormat dateFormat=null;
    private String transDate(Date date){
        if(dateFormat==null)
            dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date);
    }
    private void backCancel(){
        setResult(RESULT_CANCELED);
        finish();
    }
    @Override
    public void onBackPressed() {
        backCancel();
        super.onBackPressed();
    }
}
