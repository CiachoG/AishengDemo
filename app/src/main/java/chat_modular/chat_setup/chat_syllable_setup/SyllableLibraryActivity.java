package chat_modular.chat_setup.chat_syllable_setup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.ciacho.aishengdemo.R;

import java.util.ArrayList;
import java.util.List;

public class SyllableLibraryActivity extends AppCompatActivity {
    private ListView list_syllLib;
    private ImageButton btn_back;

    private SyllableLibAdapter syllableLibAdapter;
    private List<SyllableLibRow> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_syll_lib);

        iniData();
        iniView();
    }

    private void iniData(){
        dataList=new ArrayList<>();
        Bundle bundle=getIntent().getExtras();
        for(String key:bundle.keySet()){
            dataList.add(new SyllableLibRow(key,bundle.getString(key)));
        }
        syllableLibAdapter=new SyllableLibAdapter(this,dataList);
    }

    private void iniView(){
        btn_back= (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list_syllLib= (ListView) findViewById(R.id.list_syllLib);
        list_syllLib.setAdapter(syllableLibAdapter);
    }


}
