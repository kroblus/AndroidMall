package com.example.fruit.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fruit.R;
import com.example.fruit.bean.Fruit;
import com.example.fruit.widget.ActionBar;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 添加页面
 */
public class AddFruitActivity extends AppCompatActivity {
    private ActionBar mActionBar;//标题栏
    private Activity myActivity;
    private EditText etTitle;//标题
    private EditText etIssuer;//发布单位
    private EditText etImg;//图片
    private Spinner spType;//类型
    private EditText etContent;//内容
    private ImageView ivImg;//图片
    SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Fruit mfruit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_fruit_add);
        etTitle = findViewById(R.id.title);
        etIssuer = findViewById(R.id.issuer);
        spType = findViewById(R.id.type);
        etImg = findViewById(R.id.img);
        etContent = findViewById(R.id.content);
        ivImg = findViewById(R.id.iv_img);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(myActivity,"Edit Fruit Information", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
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
        mfruit = (Fruit) getIntent().getSerializableExtra("fruit");
        if (mfruit !=null){
            etTitle.setText(mfruit.getTitle());
            spType.setSelection(mfruit.getTypeId());
            etImg.setText(mfruit.getImg());
            etIssuer.setText(mfruit.getIssuer());
            etContent.setText(mfruit.getContent());
            spType.setSelection(mfruit.getTypeId(),true);
            Glide.with(myActivity)
                    .asBitmap()
                    .load(mfruit.getImg())
                    .error(R.drawable.ic_error)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivImg);
        }
        ivImg.setVisibility(mfruit ==null? View.GONE: View.VISIBLE);
    }

    public void save(View view){
        String title = etTitle.getText().toString();
        String issuer = etIssuer.getText().toString();
        String img = etImg.getText().toString();
        String content = etContent.getText().toString();
        Integer typeId =  spType.getSelectedItemPosition();
        if ("".equals(title)) {
            Toast.makeText(myActivity,"Title cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(issuer)) {
            Toast.makeText(myActivity,"Price cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(img)) {
            Toast.makeText(myActivity,"Image address cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(content)) {
            Toast.makeText(myActivity,"Description cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        Fruit fruit = null;
        if (!title.equals(mfruit != null? mfruit.getTitle():"")){
            fruit = DataSupport.where("title = ?",title).findFirst(Fruit.class);
        }
        if (fruit == null ){
            if (mfruit != null){
                fruit = DataSupport.where("title = ?", mfruit.getTitle()).findFirst(Fruit.class);
                fruit.setTypeId(typeId);
                fruit.setTitle(title);
                fruit.setIssuer(issuer);
                fruit.setImg(img);
                fruit.setContent(content);
            }else {
                fruit = new Fruit(typeId,title,img,content,issuer,sf.format(new Date()));
            }
            fruit.save();
            setResult(RESULT_OK);
            finish();
            Toast.makeText(myActivity,"Save Successful", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(myActivity,"This title already exists", Toast.LENGTH_LONG).show();
        }
    }
}
