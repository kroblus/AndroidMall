package com.example.fruit.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fruit.R;
import com.example.fruit.bean.Browse;
import com.example.fruit.bean.Car;
import com.example.fruit.bean.Fruit;
import com.example.fruit.util.SPUtils;
import com.example.fruit.widget.ActionBar;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 水果明细信息
 */
public class FruitDetailActivity extends AppCompatActivity {
    private Activity mActivity;
    private ImageView ivImg;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvContent;
    private TextView tvIssuer;
    private Button btnCollect;
    private Button btnCancel;
    private ActionBar mActionBar;//标题栏
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_fruit_detail);
        ivImg = findViewById(R.id.img);
        tvTitle = findViewById(R.id.title);
        tvDate = findViewById(R.id.date);
        tvContent = findViewById(R.id.content);
        tvIssuer = findViewById(R.id.issuer);
        btnCollect = findViewById(R.id.btn_collect);
        btnCancel = findViewById(R.id.btn_cancel);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(mActivity, "Product Details", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        Fruit fruit = (Fruit) getIntent().getSerializableExtra("fruit");
        tvTitle.setText(fruit.getTitle());
        tvDate.setText(String.format("Availability:%s", fruit.getDate()));
        tvContent.setText(fruit.getContent());
        tvIssuer.setText(String.format("₽ %s", fruit.getIssuer()));
        Glide.with(mActivity)
                .asBitmap()
                .skipMemoryCache(true)
                .load(fruit.getImg())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivImg);
        String account = (String) SPUtils.get(mActivity, SPUtils.ACCOUNT, "");
        //浏览记录
        Browse browse = DataSupport.where("account = ? and title = ?", account, fruit.getTitle()).findFirst(Browse.class);//浏览记录
        if (browse == null) {//不存在该条浏览记录  新增记录
            Browse browse1 = new Browse(account, fruit.getTitle());
            browse1.save();
        }

        Boolean isAdmin = (Boolean) SPUtils.get(mActivity, SPUtils.IS_ADMIN, false);
        if (!isAdmin) {
            Car order = DataSupport.where("account = ? and title = ?", account, fruit.getTitle()).findFirst(Car.class);
            btnCollect.setVisibility(order != null ? View.GONE : View.VISIBLE);
            btnCancel.setVisibility(order != null ? View.VISIBLE : View.GONE);
        }
        //收藏
        btnCollect.setOnClickListener(v -> {
            Car car = new Car(fruit.getIssuer(), account, fruit.getTitle(), "S" + System.currentTimeMillis(), account, sf.format(new Date()));
            car.save();
            Toast.makeText(mActivity, "Add to Cart Success", Toast.LENGTH_SHORT).show();
            btnCollect.setVisibility(View.GONE);
            btnCancel.setVisibility(View.VISIBLE);
        });
        //Cancel收藏
        btnCancel.setOnClickListener(v -> {
            Car order = DataSupport.where("account = ? and title = ?", account, fruit.getTitle()).findFirst(Car.class);
            order.delete();
            Toast.makeText(mActivity, "Removed from cart", Toast.LENGTH_SHORT).show();
            btnCollect.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.GONE);
        });
    }
}
