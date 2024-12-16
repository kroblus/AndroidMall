package com.example.fruit.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruit.R;
import com.example.fruit.bean.User;
import com.example.fruit.widget.ActionBar;

import org.litepal.crud.DataSupport;


/**
 * 用户明细
 */
public class UserDetailActivity extends AppCompatActivity {
    private ActionBar mActionBar;//标题栏
    private Activity mActivity;
    private TextView account;
    private EditText nickName;
    private EditText age;
    private EditText email;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        mActivity = this;
        account = findViewById(R.id.account);
        nickName = findViewById(R.id.nickName);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(mActivity,"Profile", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        mUser = (User) getIntent().getSerializableExtra("user");
        if (mUser != null) {
            account.setText(mUser.getAccount());
            nickName.setText(mUser.getNickName());
            age.setText(String.valueOf(mUser.getAge()));
            email.setText(mUser.getEmail());
        }
    }

    //保存
    public void save(View view){
        User user = DataSupport.where("account = ?",mUser.getAccount()).findFirst(User.class);
        String nickNameStr = nickName.getText().toString();
        String ageStr = age.getText().toString();
        String emailStr = email.getText().toString();
        if ("".equals(nickNameStr)) {
            Toast.makeText(mActivity,"Nickname cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(ageStr)) {
            Toast.makeText(mActivity,"Phone number cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(emailStr)) {
            Toast.makeText(mActivity,"The address cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (user != null) {
            user.setNickName(nickNameStr);
            user.setAge(String.valueOf(ageStr));
            user.setEmail(emailStr);
            user.save();
            Toast.makeText(mActivity,"Save Successful", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(mActivity,"Save Fail", Toast.LENGTH_SHORT).show();
        }

    }
}
