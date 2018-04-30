package chat_modular.chat_setup.chat_speech_setup.chat_sounder_setup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.ciacho.aishengdemo.R;
import com.mylhyl.circledialog.CircleDialog;
import java.util.ArrayList;
import java.util.List;

public class SpeechSounderActivity extends AppCompatActivity {
    public static final String[] SOUNDER_NAME={"小燕","小宇","小研","小琪","小峰","小梅","小莉","晓琳","小蓉","小芸",
            "小倩","小坤","小强","小莹","小新","楠楠","老孙"};
    public static final String[] SOUNDER_PARAMS={"xiaoyan","xiaoyu","vixy","xiaoqi","vixf","xiaomei","vixl","xiaolin",
                    "xiaorong","vixyun","xiaoqian","xiaokun","xiaoqiang","vixying","xiaoxin",
                    "nannan","vils"};
    private ListView list_sounder;
    private List<Sounder>dataList;
    private SpeechSounderAdapter speechSounderAdapter;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_speech_sounder);

        iniView();
        iniList();
    }

    private void iniView(){
        list_sounder= (ListView) findViewById(R.id.list_sounder);
        btn_back= (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLastForCancel();
            }
        });
    }

    private void iniList(){
        dataList=new ArrayList<>();
        dataList.add(new Sounder(SOUNDER_NAME[0],"支持中英文(普通话)","青年女声",SOUNDER_PARAMS[0]));
        dataList.add(new Sounder(SOUNDER_NAME[1],"支持中英文(普通话)","青年男声",SOUNDER_PARAMS[1]));
        dataList.add(new Sounder(SOUNDER_NAME[2],"支持中英文(普通话)","青年女声",SOUNDER_PARAMS[2]));
        dataList.add(new Sounder(SOUNDER_NAME[3],"支持中英文(普通话)","青年女声",SOUNDER_PARAMS[3]));
        dataList.add(new Sounder(SOUNDER_NAME[4],"支持中英文(普通话)","青年男声",SOUNDER_PARAMS[4]));

        dataList.add(new Sounder(SOUNDER_NAME[5],"支持中英文(粤语)","青年女声",SOUNDER_PARAMS[5]));
        dataList.add(new Sounder(SOUNDER_NAME[6],"支持中英文(普通话)","青年女声",SOUNDER_PARAMS[6]));
        dataList.add(new Sounder(SOUNDER_NAME[7],"支持中英文(台湾普通话)","青年女声",SOUNDER_PARAMS[7]));

        dataList.add(new Sounder(SOUNDER_NAME[8],"汉语(四川话)","青年女声",SOUNDER_PARAMS[8]));
        dataList.add(new Sounder(SOUNDER_NAME[9],"汉语(东北话)","青年女声",SOUNDER_PARAMS[9]));
        dataList.add(new Sounder(SOUNDER_NAME[10],"汉语(东北话)","青年女声",SOUNDER_PARAMS[10]));

        dataList.add(new Sounder(SOUNDER_NAME[11],"汉语(河南话)","青年男声",SOUNDER_PARAMS[11]));
        dataList.add(new Sounder(SOUNDER_NAME[12],"汉语(湖南话)","青年男声",SOUNDER_PARAMS[12]));
        dataList.add(new Sounder(SOUNDER_NAME[13],"汉语(陕西话)","青年女声",SOUNDER_PARAMS[13]));

        dataList.add(new Sounder(SOUNDER_NAME[14],"汉语(普通话)","童年男声",SOUNDER_PARAMS[14]));
        dataList.add(new Sounder(SOUNDER_NAME[15],"汉语(普通话)","童年女声",SOUNDER_PARAMS[15]));
        dataList.add(new Sounder(SOUNDER_NAME[16],"汉语(普通话)","老年男声",SOUNDER_PARAMS[16]));

        speechSounderAdapter=new SpeechSounderAdapter(this,dataList);
        list_sounder.setAdapter(speechSounderAdapter);
        list_sounder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popCircleDialog(position);
            }
        });
    }

    private void popCircleDialog(final int position){
        CircleDialog.Builder builder=new CircleDialog.Builder(this);
        builder.setTitle("确定设置这个发音人吗？");
        builder.setText("选择发音人:"+SOUNDER_NAME[position]);
        builder.setPositive("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLastForOK(dataList.get(position).getParameter());
            }
        });
        builder.setNegative("取消",null);
        builder.show(getSupportFragmentManager());
    }

    private void backLastForCancel(){
        setResult(RESULT_CANCELED,null);
        finish();
    }

    private void backLastForOK(String data){
        Intent intent=new Intent();
        intent.putExtra("Sounder",data);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backLastForCancel();
        super.onBackPressed();
    }
}
