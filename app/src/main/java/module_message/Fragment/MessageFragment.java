package module_message.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ciacho.aishengdemo.R;

@SuppressLint("ValidFragment")
public class MessageFragment extends Fragment {

    public static MessageFragment getInstance() {
        MessageFragment sf = new MessageFragment();
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_layout, null);

        return v;
    }
}