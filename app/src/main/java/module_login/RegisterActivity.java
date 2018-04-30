package module_login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ciacho.aishengdemo.R;

import HttpConnect.RegisterConnect;

public class RegisterActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private CardView cvAdd;
    private EditText userNameEdit,passWordEdit,rPasswordEdit;
    private Button submitBt;
    private Handler handler;
    private RegisterConnect registerConnect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar=(Toolbar)findViewById(R.id.register_toolbar);
        userNameEdit=findViewById(R.id.et_username);
        passWordEdit=findViewById(R.id.et_password);
        rPasswordEdit=findViewById(R.id.et_repeatpassword);
        submitBt=findViewById(R.id.bt_go);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        ShowEnterAnimation();
        initView();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                Toast.makeText(RegisterActivity.this,registerConnect.getResponseString(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=userNameEdit.getText().toString();
                String passWord=passWordEdit.getText().toString();
                String rPassWord=rPasswordEdit.getText().toString();
                if(!passWord.equals(rPassWord)){
                    Snackbar.make(view,"两次密码不一致",Snackbar.LENGTH_SHORT)
                            .setAction("清空", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    passWordEdit.setText("");
                                    rPasswordEdit.setText("");
                                }
                            }).show();
                }
                else {
                    registerConnect=new RegisterConnect(handler);
                    registerConnect.SendByHttpClient(userName,passWord);
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    private void initView() {
        fab = findViewById(R.id.fab);
        cvAdd = findViewById(R.id.cv_add);
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }
}
