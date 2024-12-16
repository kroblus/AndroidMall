package com.example.fruit.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fruit.R;
import com.example.fruit.adapter.UserAdapter;
import com.example.fruit.bean.User;
import com.example.fruit.util.KeyBoardUtil;
import com.example.fruit.widget.ActionBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;


/**
     * 用户管理
     */
    public class ManageActivity extends AppCompatActivity {
        private Activity myActivity;
        private ActionBar mTitleBar;//标题栏
        private LinearLayout llEmpty;
        private RecyclerView rvUserList;
        public UserAdapter mUserAdapter;
        private FloatingActionButton btnAdd;
        private EditText etQuery;//搜索内容
        private ImageView ivSearch;//搜索图标
        private List<User> mUsers;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        myActivity = this;
        rvUserList = findViewById(R.id.rv_user_list);
        llEmpty = findViewById(R.id.ll_empty);
        etQuery=findViewById(R.id.et_query);
        ivSearch=findViewById(R.id.iv_search);
        mTitleBar = (ActionBar)findViewById(R.id.myActionBar);
        mTitleBar.setData(myActivity,"User Management",R.drawable.ic_back, 0, 0,getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        initView();
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();//加载数据
            }
        });
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

    }
    private void initView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvUserList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        mUserAdapter=new UserAdapter(llEmpty, rvUserList);
        //=2.3、设置recyclerView的适配器
        rvUserList.setAdapter(mUserAdapter);
        loadData();
    }

    /**
     * 加载数据
     */
    private void loadData(){
        String content=etQuery.getText().toString();//获取搜索内容
        if ("".equals(content)){
            mUsers= DataSupport.where("account != ?","admin").find(User.class);//查询全部
        }else {
            mUsers=DataSupport.where("account like ?","%"+content+"%").find(User.class);//通过账号模糊查询用户
        }
        Collections.reverse(mUsers);
        if (mUsers !=null && mUsers.size()>0){
            rvUserList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            mUserAdapter.addItem(mUsers);
        }else {
            rvUserList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            initView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    }