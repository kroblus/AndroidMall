package com.example.fruit.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.fruit.R;
import com.example.fruit.bean.User;
import com.example.fruit.util.SPUtils;
import com.example.fruit.widget.ActionBar;

import org.litepal.crud.DataSupport;


/**
 * 个人信息
 */
public class PersonActivity extends AppCompatActivity {
    private Activity mActivity;
    private ActionBar mTitleBar;//标题栏
    private TextView tvAccount;
    private TextView etNickName;
    private TextView etAge;
    private TextView etEmail;
    private Button btnSave;//保存
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        mActivity = this;
        tvAccount = findViewById(R.id.tv_account);
        etNickName = findViewById(R.id.tv_nickName);
        etAge = findViewById(R.id.tv_age);
        etEmail = findViewById(R.id.tv_email);
        btnSave = findViewById(R.id.btn_save);
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        mTitleBar.setData(mActivity,"Profile", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        initView();
    }

    private void initView() {
        String account = (String) SPUtils.get(mActivity,"account","");
        User user = DataSupport.where("account = ?", account).findFirst(User.class);
        if (user != null) {
            tvAccount.setText(user.getAccount());
            etNickName.setText(user.getNickName());
            etAge.setText(String.valueOf(user.getAge()));
            etEmail.setText(user.getEmail());
        }
        //保存
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = tvAccount.getText().toString();
                String nickName = etNickName.getText().toString();
                String age = etAge.getText().toString();
                String email = etEmail.getText().toString();
                User user1 = DataSupport.where("account = ?",account).findFirst(User.class);
                if ("".equals(nickName)) {
                    Toast.makeText(mActivity,"Nickname cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(age)) {
                    Toast.makeText(mActivity,"Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(email)) {
                    Toast.makeText(mActivity,"Address cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                user1.setNickName(nickName);
                user1.setAge(String.valueOf(age));
                user1.setEmail(email);
                user1.save();
                Toast.makeText(mActivity,"Save Successful", Toast.LENGTH_SHORT).show();
                finish();//关闭页面
            }
        });
       /* btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.Instance.getMainActivity().finish();
                SPUtils.remove(mActivity,"account");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });*/
    }
}
