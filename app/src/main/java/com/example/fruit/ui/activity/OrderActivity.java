package com.example.fruit.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fruit.R;
import com.example.fruit.adapter.CarAdapter;
import com.example.fruit.adapter.OrderAdapter;
import com.example.fruit.bean.Orders;
import com.example.fruit.util.SPUtils;
import com.example.fruit.widget.ActionBar;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private ActionBar mTitleBar;//标题栏
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvOrderList;
    public OrderAdapter mOrderAdapter;
    private Boolean mIsAdmin;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private List<Orders> mOrder = new ArrayList<>();
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        myActivity = this;
        rvOrderList =findViewById(R.id.rv_order_list);
        llEmpty =findViewById(R.id.ll_empty);
        etQuery=findViewById(R.id.et_query);
        ivSearch=findViewById(R.id.iv_search);
        mTitleBar = (ActionBar) findViewById(R.id.myActionBar);
        mTitleBar.setData(myActivity,"My Orders", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });  initView();
        ivSearch.setOnClickListener(v -> {
                loadData();//加载数据
        });
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

    }
    private void initView() {

        mIsAdmin = (Boolean) SPUtils.get(myActivity, SPUtils.IS_ADMIN, false);
        account = (String) SPUtils.get(myActivity, SPUtils.ACCOUNT, "");
        LinearLayoutManager layoutManager=new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvOrderList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        mOrderAdapter =new OrderAdapter(llEmpty,rvOrderList);
        //=2.3、设置recyclerView的适配器
        rvOrderList.setAdapter(mOrderAdapter);
        loadData();//加载数据




    }
    /**
     * 加载数据
     */
    private void loadData(){
        String content=etQuery.getText().toString();//获取搜索内容
        if ("".equals(content) && !mIsAdmin){
            mOrder = DataSupport.where("account = ? " ,account).find(Orders.class);//查询全部
        }else {
            mOrder =DataSupport.where("number like ? and account != ?" ,"%"+content+"%","admin").find(Orders.class);//通过标题模糊查询留言
        }
        Collections.reverse(mOrder);
        if (mOrder !=null && mOrder.size()>0){
            rvOrderList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mOrderAdapter.addItem(mOrder);
        }else {
            rvOrderList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            loadData();
        }
    }

}