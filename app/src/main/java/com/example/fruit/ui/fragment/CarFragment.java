package com.example.fruit.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit.R;
import com.example.fruit.adapter.CarAdapter;
import com.example.fruit.bean.Car;
import com.example.fruit.ui.activity.JsActivity;
import com.example.fruit.util.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 订单
 */
public class CarFragment extends Fragment {
    private Activity myActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvOrderList;
    public CarAdapter mCarAdapter;
    private Boolean mIsAdmin;
    private boolean iscbAll = false;
    private List<Car> mCar = new ArrayList<>();
    private String account;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_car,container,false);
        rvOrderList = view.findViewById(R.id.rv_order_list);
        llEmpty = view.findViewById(R.id.ll_empty);
        //获取控件
        initView();
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
        mCarAdapter =new CarAdapter(llEmpty,rvOrderList);
        //=2.3、设置recyclerView的适配器
        rvOrderList.setAdapter(mCarAdapter);
        loadData();//加载数据
    }

    /**
     * 加载数据
     */
    private void loadData(){
        mCar = DataSupport.where("account = ? " ,account).find(Car.class);//查询全部
        Collections.reverse(mCar);
        if (mCar !=null && mCar.size()>0){
            rvOrderList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mCarAdapter.addItem(mCar);
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
