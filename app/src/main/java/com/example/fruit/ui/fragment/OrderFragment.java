package com.example.fruit.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit.R;
import com.example.fruit.adapter.OrderAdapter;
import com.example.fruit.bean.Orders;
import com.example.fruit.ui.activity.JsActivity;
import com.example.fruit.util.KeyBoardUtil;
import com.example.fruit.util.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 订单
 */
public class OrderFragment extends Fragment {
    private Activity myActivity;
    private LinearLayout llEmpty;
    // private ImageView js;


    private RecyclerView rvOrderList;
    public OrderAdapter mOrderAdapter;
    private Boolean mIsAdmin;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private List<Orders> mOrder = new ArrayList<>();
    private String account;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order,container,false);
        rvOrderList = view.findViewById(R.id.rv_order_list);
        llEmpty = view.findViewById(R.id.ll_empty);
        //  js=view.findViewById(R.id.js);


        etQuery=view.findViewById(R.id.et_query);
        ivSearch=view.findViewById(R.id.iv_search);

        //获取控件
        initView();
        //软键盘搜索
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();//加载数据
            }
        });
        //点击软键盘中的搜索
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtil.hideKeyboard(v);//隐藏软键盘
                    loadData();//加载数据
                    return true;
                }
                return false;
            }
        });
        return view;
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
