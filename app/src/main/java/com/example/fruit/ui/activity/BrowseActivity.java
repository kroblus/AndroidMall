package com.example.fruit.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruit.R;
import com.example.fruit.adapter.BrowseAdapter;
import com.example.fruit.bean.Browse;
import com.example.fruit.bean.Fruit;
import com.example.fruit.util.SPUtils;
import com.example.fruit.widget.ActionBar;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * 浏览记录
 */
public class BrowseActivity extends AppCompatActivity {
    private Activity myActivity;
    private ActionBar mTitleBar;//标题栏
    private LinearLayout llEmpty;
    private RecyclerView rvBrowseList;
    private BrowseAdapter mBrowseAdapter;
    private List<Browse> mBrowses;
    private String account;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        myActivity = this;
        rvBrowseList = findViewById(R.id.rv_collect_list);
        llEmpty = findViewById(R.id.ll_empty);
        mTitleBar = (ActionBar)findViewById(R.id.myActionBar);
        mTitleBar.setData(myActivity,"Browsing History", R.drawable.ic_back, 0, 0,getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
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
        account = (String) SPUtils.get(myActivity, SPUtils.ACCOUNT,"");
        LinearLayoutManager layoutManager=new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvBrowseList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        mBrowseAdapter=new BrowseAdapter(llEmpty,rvBrowseList);
        //=2.3、设置recyclerView的适配器
        rvBrowseList.setAdapter(mBrowseAdapter);
        loadData();//加载数据
        mBrowseAdapter.setItemListener(new BrowseAdapter.ItemListener() {
            @Override
            public void ItemClick(Browse collect) {
                Intent intent = new Intent(myActivity, FruitDetailActivity.class);;
                Fruit news = DataSupport.where("title = ?",collect.getTitle()).findFirst(Fruit.class);
                intent.putExtra("fruit",news);
                startActivityForResult(intent,100);
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData(){
        mBrowses = DataSupport.where("account = ?",account).find(Browse.class);//查询浏览记录
        if (mBrowses !=null && mBrowses.size()>0){
            rvBrowseList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mBrowseAdapter.addItem(mBrowses);
        }else {
            rvBrowseList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
