package modular_chat.chat_setup.chat_quick_response_setup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ciacho.aishengdemo.R;
import com.githang.statusbar.StatusBarCompat;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import java.util.ArrayList;
import java.util.List;

import modular_chat.chat_main.ChatOrderManager;
import modular_chat.chat_main.QuickResponser;

//快速应答的设置活动页面
public class QuickResponseActivity extends AppCompatActivity {
    private ListView list_quickResponse;
    private List<QuickResponser.Unit> dataList;     //最新的实时快速应答词库

    private QuickResponseAdapter quickResponseAdapter;
    private ImageButton btn_back,btn_addNewRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_quick_response);

        iniStatusBar();
        iniData();
        iniView();
        iniEvent();
    }

    private void iniStatusBar(){
        StatusBarCompat.setStatusBarColor(this, Color.WHITE, true);
    }

    private void iniData(){     //读取上一个活动的快速应答词条数据
        dataList=new ArrayList<>();
        Bundle bundle=getIntent().getExtras();
        for(String key:bundle.keySet())
            dataList.add(new QuickResponser.Unit(key,bundle.getString(key)));
    }

    private void iniView(){
        btn_back= (ImageButton) findViewById(R.id.btn_back);
        btn_addNewRow= (ImageButton) findViewById(R.id.btn_addNewRow);
        list_quickResponse= (ListView) findViewById(R.id.list_quickResponse);
        quickResponseAdapter=new QuickResponseAdapter(this,dataList);
        list_quickResponse.setAdapter(quickResponseAdapter);
    }

    private void iniEvent(){
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLastActivityData();
            }
        });
        btn_addNewRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popSysDialog("新建一个词条", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String code=edit_quick_code.getText().toString();
                        if(isSysOrderCode(code)){
                            Toast.makeText(QuickResponseActivity.this,"词条编码不能与系统指令编码相同",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(code.length()%2!=0&&isUnique(code,null)){ //奇数位,唯一数字
                            dataList.add(new QuickResponser.
                                    Unit(code,edit_quick_content.getText().toString()));
                            quickResponseAdapter.notifyDataSetChanged();
                            Toast.makeText(QuickResponseActivity.this,"新建成功",Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(QuickResponseActivity.this,"词条编码需奇数位唯一纯数字",Toast.LENGTH_SHORT).show();
                    }
                },null,null);
            }
        });
        list_quickResponse.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                popBottomMenu(position);
                return false;
            }
        });
    }



    //长按词条列表弹出底部菜单
    private static final String[] items={"删除","编辑"};
    private void popBottomMenu(final int pos){  //目标操作的行索引
        CircleDialog.Builder builder=new CircleDialog.Builder(this);
        builder.setCanceledOnTouchOutside(true);
        builder.setCancelable(true);

        builder.configDialog(new ConfigDialog() {
            @Override
            public void onConfig(DialogParams params) {
                params.animStyle = R.style.dialogWindowAnim;
            }
        });
        builder.setTitle("词条设置");
        builder.setItems(items, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (position){
                    case 0:     //删除
                        popSysDeleteEnsureDialog(pos);
                        break;
                    case 1:     //编辑
                        final String edit_code=dataList.get(pos).getCode();
                        String content=dataList.get(pos).getContent();

                        //弹出系统对话框用以编辑词条
                        popSysDialog("编辑一个词条", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //确定编辑完成，获取词条的编码
                                String code=edit_quick_code.getText().toString();
                                if(isSysOrderCode(code)){
                                    Toast.makeText(QuickResponseActivity.this,"词条编码不能与系统指令编码相同",Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if(code.length()%2!=0&&isUnique(code,edit_code)){ //奇数位,唯一数字
                                    dataList.get(pos).setCode(code);
                                    dataList.get(pos).setContent(edit_quick_content.getText().toString());
                                    Toast.makeText(QuickResponseActivity.this,"编辑成功",Toast.LENGTH_SHORT).show();
                                }else
                                    Toast.makeText(QuickResponseActivity.this,"词条编码需奇数位唯一纯数字",Toast.LENGTH_SHORT).show();
                            }
                        },edit_code,content);
                        break;
                }
            }
        });
        builder.setNegative("取消",null);
        builder.show();
    }

    //弹出系统的新建和编辑词条对话框
    private EditText edit_quick_code,edit_quick_content;
    private void popSysDialog(String title,DialogInterface.OnClickListener listener,String code,String content){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title);

        View view= LayoutInflater.from(this).inflate(R.layout.layout_quick_dialog_add,null,false);
        edit_quick_code= (EditText) view.findViewById(R.id.edit_quick_code);
        edit_quick_content= (EditText) view.findViewById(R.id.edit_quick_content);

        if(code!=null)      edit_quick_code.setText(code);
        if(content!=null)   edit_quick_content.setText(content);

        builder.setView(view);
        builder.setPositiveButton("确定",listener);
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private void popSysDeleteEnsureDialog(final int click_pos){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("确定要删除该词条吗");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataList.remove(click_pos);
                quickResponseAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    //不包括自身的唯一性
    private boolean isUnique(String code,String self){
        for(int i=0;i<dataList.size();++i){
            if(self!=null&&self.equals(dataList.get(i).getCode()))
                continue;

            if(dataList.get(i).getCode().equals(code))
                return false;
        }
        return true;
    }

    private void backLastActivityData(){
        Bundle bundle=new Bundle();
        for(int i=0;i<dataList.size();++i)
            bundle.putString(dataList.get(i).getCode(),dataList.get(i).getContent());

        Intent intent=new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();
    }

    private boolean isSysOrderCode(String code){
        for(int i = 0; i< ChatOrderManager.ARRAY_SYSORDER.length; ++i)
            if(ChatOrderManager.ARRAY_SYSORDER[i].equals(code))
                return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        backLastActivityData();
        super.onBackPressed();
    }
}
