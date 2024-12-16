package com.example.fruit.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruit.R;
import com.example.fruit.bean.Fruit;
import com.example.fruit.bean.User;
import com.example.fruit.util.SPUtils;
import com.example.fruit.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 开屏页面
 */
public class OpeningActivity extends AppCompatActivity {
    private Activity myActivity;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        //设置页面布局
        setContentView(R.layout.activity_opening);
        try {
            initView();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
    private void initView() throws IOException, JSONException {
        StatusBarUtil.setStatusBar(myActivity,true);//设置当前界面是否是全屏模式（状态栏）
        StatusBarUtil.setStatusBarLightMode(myActivity,true);//状态栏文字颜色
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
                    finish();
                    return;
                }
                Boolean isFirst= (Boolean) SPUtils.get(myActivity,SPUtils.IF_FIRST,true);
                String account= (String) SPUtils.get(myActivity,SPUtils.ACCOUNT,"");
                if (isFirst){//第一次进来  初始化本地数据
                    SPUtils.put(myActivity,SPUtils.IF_FIRST,false);//第一次
                    //初始化数据
                    //获取json数据
                    String rewardJson = "";
                    String rewardJsonLine;
                    //assets文件夹下db.json文件的路径->打开db.json文件
                    BufferedReader bufferedReader = null;
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(myActivity.getAssets().open("db.json")));
                        while (true) {
                            if (!((rewardJsonLine = bufferedReader.readLine()) != null)) break;
                            rewardJson += rewardJsonLine;
                        }
                        JSONObject jsonObject = new JSONObject(rewardJson);
                        JSONArray fruitList = jsonObject.getJSONArray("fruit");//获得列表
                        //把物品列表保存到本地
                        for (int i = 0, length = fruitList.length(); i < length; i++) {
                            JSONObject o = fruitList.getJSONObject(i);
                                            Fruit fruit = new Fruit(o.getInt("typeId"),
                                                    o.getString("title"),
                                                    o.getString("img"),
                                                    o.getString("content"),
                                                    o.getString("issuer"),
                                                    sf.format(new Date())
                                            );
                            fruit.save();//保存到本地
                        }
                        User user = new User("admin","123","管理员","1632957243","四川省内江市东兴区");
                        user.save();
                        User user1 = new User("0","0","普通人","1632957243","四川省南充市仪陇县");
                        user1.save();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                //两秒后跳转到主页面
                Intent intent2 = new Intent();
                if ("".equals(account)) {
                    intent2.setClass(OpeningActivity.this, MainActivity.class);
                }else {
                    intent2.setClass(OpeningActivity.this, MainActivity.class);
                }
                startActivity(intent2);
                finish();
            }
        }, 2000);
    }


    @Override
    public void onBackPressed() {

    }
}
