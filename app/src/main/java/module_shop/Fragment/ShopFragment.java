package module_shop.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ciacho.aishengdemo.R;

@SuppressLint("ValidFragment")
public class ShopFragment extends Fragment {

    public static ShopFragment getInstance() {
        ShopFragment sf = new ShopFragment();
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shop_fragment_layout, null);

        return v;
    }
}