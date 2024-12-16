package com.example.fruit.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;


import com.example.fruit.R;
import com.example.fruit.bean.User;
import com.example.fruit.widget.ActionBar;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;


/**
 * 注册页面
 */
public class RegisterActivity extends Activity {
    private Activity activity;
    private ActionBar mTitleBar;//标题栏
    private EditText etAccount;//手机号
    private EditText etNickName;//昵称
    private EditText etAge;//
    private EditText etEmail;//邮箱
    private EditText etPassword;//密码
    private EditText etPasswordSure;//确认密码

    private Button btnRegister;//注册按钮
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_register);//加载页面
        etAccount =(EditText) findViewById(R.id.et_account);//获取手机号
        etNickName =(EditText) findViewById(R.id.et_nickName);//获取昵称
        etAge =(EditText) findViewById(R.id.et_age);//获取年龄
        etEmail =(EditText) findViewById(R.id.et_email);//获取邮箱
        etPassword=(EditText) findViewById(R.id.et_password);//获取密码
        etPasswordSure=(EditText) findViewById(R.id.et_password_sure);//获取确认密码

        btnRegister=(Button) findViewById(R.id.btn_register);//获取注册按钮
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        mTitleBar.setData(activity,"注册", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
       /* tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到登录页面
                Intent intent=new Intent(activity, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });**/
        //设置注册点击按钮
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭虚拟键盘
                InputMethodManager inputMethodManager= (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                String account= etAccount.getText().toString();
                String nickName= etNickName.getText().toString();
                String age = etAge.getText().toString();
                String email= etEmail.getText().toString();
                String password=etPassword.getText().toString();
                String passwordSure=etPasswordSure.getText().toString();
                if ("".equals(account)){//账号不能为空
                    Toast.makeText(activity,"The account number cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(nickName)){//昵称不能为空
                    Toast.makeText(activity,"Nickname cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(age)){//年龄不能为空
                    Toast.makeText(activity,"Phone number cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(email)){//邮箱不能为空
                    Toast.makeText(activity,"Address cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if ("".equals(password)){//密码为空
                    Toast.makeText(activity,"Password is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password.equals(passwordSure)){//密码不一致
                    Toast.makeText(activity,"Inconsistent passwords", Toast.LENGTH_LONG).show();
                    return;
                }
                User user = DataSupport.where("account = ?", account).findFirst(User.class);
                if (user == null) {
                    user = new User (account,password,nickName, String.valueOf(age),email);
                    user.save();//保存用户信息
                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(activity, "Successful registration", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(activity, "This account already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
