package personal_moudel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperTextView;
import com.example.ciacho.aishengdemo.R;

import login_moudel.LoginActivity;
import personal_moudel.app.AboutActivity;

@SuppressLint("ValidFragment")
public class PersonalFragment extends Fragment {
    private SuperTextView sText_about;
    private SuperTextView sText_Tx;
    public static PersonalFragment getInstance() {
        PersonalFragment sf = new PersonalFragment();
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.personal_fragment_layout, null);
        sText_about=v.findViewById(R.id.about_aisheng);
        sText_Tx=v.findViewById(R.id.per_srcname);
        sText_Tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        sText_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),AboutActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}