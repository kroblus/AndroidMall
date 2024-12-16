package com.example.fruit.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruit.R;
import com.example.fruit.widget.ActionBar;

/**
 * 结算
 */
public class JsActivity extends AppCompatActivity implements View.OnClickListener{
    Button btn_ZF;
    private Activity myActivity;
    private ActionBar mTitleBar;//标题栏
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js);
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        mTitleBar.setData(myActivity,"Payment", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        final RadioButton rab_weixin = (RadioButton)findViewById(R.id.rab_wenxin);
        final RadioButton rab_zhifubao = (RadioButton)findViewById(R.id.rab_zhifubao);
        final RadioButton rab_yinhangka = (RadioButton)findViewById(R.id.rab_yinhangka);
        final TextView tv_zf = (TextView)findViewById(R.id.tv_zf);

        View.OnClickListener nanJianTingQi = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_zf.setText("Your chosen payment is:"+"\n\n"+"     WeiXin");
            }
        };
        View.OnClickListener nan2JianTingQi = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_zf.setText("Your chosen payment is:"+"\n\n"+"     Alipay(Not supported)");
            }
        };

        View.OnClickListener nvJianTingQi = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_zf.setText("Your chosen payment is:"+"\n\n"+"     Card(Not supported)");

            }
        };
        //3、将两个监听器分别配置给两个按钮
        rab_weixin.setOnClickListener(nanJianTingQi);
        rab_zhifubao.setOnClickListener(nan2JianTingQi);
        rab_yinhangka.setOnClickListener(nvJianTingQi);



        initView();

    }
    public void initView() { }

    public void onClick(View v) {
          Toast.makeText(JsActivity.this, "Sorry, we only support WeChat payment.", Toast.LENGTH_LONG).show();
          Intent intent = new Intent(JsActivity.this, WxActivity.class);
         startActivity(intent);
    }



}


