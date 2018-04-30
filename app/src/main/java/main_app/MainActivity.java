package main_app;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ciacho.aishengdemo.R;
import modular_forum.FragmentForum;

public class MainActivity extends AppCompatActivity {
    private FragmentForum fragmentForum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        iniFragment();
    }

    private void iniFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(fragmentForum==null)
            fragmentForum=new FragmentForum();
        transaction.replace(R.id.layout_frameSet,fragmentForum);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case FragmentForum.REQ_POSTING:
                    fragmentForum.onActivityResult(requestCode,resultCode,data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
