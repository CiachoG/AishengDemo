package modular_forum.modular_forum_main;

import android.graphics.Color;
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
import com.githang.statusbar.StatusBarCompat;

import java.util.Date;
import main_app.MainApplication;
import modular_forum.ForumDataLoader;

public class ForumPostingActivity extends AppCompatActivity {
    public static final int POST_TITLE_MAXLENGTH=25;
    public static final int POST_CONTENT_MAXLENGTH=250;
    public static final int REQ_POSTING_SUCCESS=0,REQ_POSTING_ERROR=1;

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
                    case REQ_POSTING_ERROR:
                        Bundle bundle=message.getData();
                        Toast.makeText(ForumPostingActivity.this,"发帖失败"+bundle.get("DATA")
                                ,Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        iniStatusBar();
        iniView();
        iniEvent();
    }

    private void iniStatusBar(){
        StatusBarCompat.setStatusBarColor(this, Color.WHITE, true);
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            posting();
                        }catch (Exception e){
                            Message msg=mHandler.obtainMessage();
                            msg.what=REQ_POSTING_ERROR;
                            Bundle bundle=msg.getData();
                            bundle.putString("DATA",e.getMessage());
                            mHandler.sendMessage(msg);
                            Log.e("提交发帖数据错误:",e.toString());
                        }
                    }
                }).start();
            }
        });
    }

    private void posting()throws Exception{
        String PostTitle=edit_postTitle.getText().toString();
        if(PostTitle.length()==0)   throw new Exception("帖子标题不能为空");
        if(PostTitle.length()>POST_TITLE_MAXLENGTH)
            throw new Exception("帖子标题太长了");

        String PostDate= ForumDataLoader.DATE_FORMAT.format(new Date());
        String PostContent=edit_postContent.getText().toString();
        if(PostContent.length()==0)     throw new Exception("帖子内容不能为空");
        if(PostContent.length()>POST_CONTENT_MAXLENGTH)
            throw new Exception("帖子内容太长了");

        String UserId=app.getUserId();
        if(UserId.equals("-1")) throw new Exception("用户登录异常");

        String result=ForumDataLoader.sendPostData(PostTitle,PostDate,PostContent,UserId);
        if(result.equals("true")){
            mHandler.sendEmptyMessage(REQ_POSTING_SUCCESS);
        }else{
            throw new Exception(result);
        }
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
