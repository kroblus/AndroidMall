package com.example.fruit.widget;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.ColorUtils;

import com.example.fruit.R;


/**
 * 自定义ActionBar
 */
public final class ActionBar extends LinearLayout {

    private LinearLayout llActionbarRoot;//自定义ActionBar根节点
    private View vStatusBar;//状态栏位置
    private ImageView ivLeft;//左边图标
    private TextView tvLeft;//左边
    private TextView tvTitle;//中间标题
    private ImageView ivRight;//右边图标
    private Button btnRight;//右边按钮
    private TextView tvRight;//右边文字
    public ActionBar(Context context) {
        this(context, null);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title){
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左侧文本
     * @param text
     */
    public void setLeftText(String text){
        if (!TextUtils.isEmpty(text)) {
            tvLeft.setText(text);
            tvLeft.setVisibility(View.VISIBLE);
        } else {
            tvLeft.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左侧图标
     * @param ico
     */
    public void setLeftIco(int ico){
        if (ico!=0) {
            ivLeft.setImageResource(ico);
            ivLeft.setVisibility(View.VISIBLE);
        } else {
            ivLeft.setVisibility(View.GONE);
        }
    }


    /**
     * 设置右侧文本
     * @param text
     */
    public void setRightText(String text){
        if (!TextUtils.isEmpty(text)) {
            tvRight.setText(text);
            tvRight.setVisibility(View.VISIBLE);
        } else {
            tvRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右侧btn
     * @param text
     */
    public void setRightBtn(String text){
        if (!TextUtils.isEmpty(text)) {
            btnRight.setText(text);
            btnRight.setVisibility(View.VISIBLE);
        } else {
            btnRight.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右侧图标
     * @param ico
     */
    public void setRightIco(int ico){
        if (ico!=0) {
            ivRight.setImageResource(ico);
            ivRight.setVisibility(View.VISIBLE);
        } else {
            ivRight.setVisibility(View.GONE);
        }
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);//设置横向布局
        View contentView = inflate(getContext(), R.layout.widget_actionbar, this);
        //获取控件
        llActionbarRoot = (LinearLayout)contentView.findViewById(R.id.ll_actionbar_root);
        vStatusBar = contentView.findViewById(R.id.v_statusbar);
        ivLeft = (ImageView)contentView.findViewById(R.id.iv_actionbar_left);
        tvLeft=(TextView) contentView.findViewById(R.id.tv_actionbar_left);
        tvTitle =(TextView) contentView.findViewById(R.id.tv_actionbar_title);
        ivRight =(ImageView) contentView.findViewById(R.id.iv_actionbar_right);
        btnRight=(Button) contentView.findViewById(R.id.btn_actionbar_right);
        tvRight=(TextView) contentView.findViewById(R.id.tv_actionbar_right);
    }

    /**
     * 设置透明度
     *
     * @param transAlpha{Integer} 0-255 之间
     */
    public void setTranslucent(int transAlpha) {
        //设置透明度
        llActionbarRoot.setBackgroundColor(ColorUtils.setAlphaComponent(getResources().getColor(R.color.colorPrimary), transAlpha));
        tvTitle.setAlpha(transAlpha);
        ivLeft.setAlpha(transAlpha);
        ivRight.setAlpha(transAlpha);
    }
    /**
     * 设置数据
     *
     * @param strTitle   标题
     * @param resIdLeft  左边图标资源
     * @param resIdRight 右边图标资源
     * @param intColor   内容颜色 0为白色 1为黑色
     * @param backgroundColor   背景颜色
     * @param listener   点击事件监听
     */
    public void setData(String strTitle, int resIdLeft, int resIdRight, int intColor, int backgroundColor, final ActionBarClickListener listener) {
        String textColor=intColor==0?"#FFFFFF":"#000000";
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
            tvTitle.setTextColor(Color.parseColor(textColor));
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (resIdLeft == 0) {
            ivLeft.setVisibility(View.GONE);
        } else {
            ivLeft.setBackgroundResource(resIdLeft);
            ivLeft.setVisibility(View.VISIBLE);
        }
        if (resIdRight == 0) {
            ivRight.setVisibility(View.GONE);
        } else {
            ivRight.setBackgroundResource(resIdRight);
            ivRight.setVisibility(View.VISIBLE);
        }

        if (backgroundColor==0){
            llActionbarRoot.setBackgroundResource(0);
        }else {
            llActionbarRoot.setBackgroundColor(backgroundColor);//设置标题栏背景颜色
        }
        if (listener != null) {
            ivLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick();
                }
            });
            ivRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick();
                }
            });
        }
    }
    public void setData(Activity context, String strTitle, int resIdLeft, int resIdRight, int intColor, int backgroundColor, final ActionBarClickListener listener) {
        String textColor=intColor==0?"#FFFFFF":"#000000";
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
            tvTitle.setTextColor(Color.parseColor(textColor));
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (resIdLeft == 0) {
            ivLeft.setVisibility(View.GONE);
        } else {
            ivLeft.setBackgroundResource(resIdLeft);
            ivLeft.setVisibility(View.VISIBLE);
        }
        if (resIdRight == 0) {
            ivRight.setVisibility(View.GONE);
        } else {
            ivRight.setBackgroundResource(resIdRight);
            ivRight.setVisibility(View.VISIBLE);
        }

        if (backgroundColor==0){
            llActionbarRoot.setBackgroundResource(0);
        }else {
            llActionbarRoot.setBackgroundColor(backgroundColor);//设置标题栏背景颜色
        }
        if (listener != null) {
            ivLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick();
                }
            });
            ivRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick();
                }
            });
        }
    }

    public void setData(String strTitle, int resIdLeft, String strRight, int intColor, int backgroundColor, final ActionBarClickListener listener) {
        String textColor=intColor==0?"#FFFFFF":"#000000";
        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
            tvTitle.setTextColor(Color.parseColor(textColor));
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (resIdLeft == 0) {
            ivLeft.setVisibility(View.GONE);
        } else {
            ivLeft.setBackgroundResource(resIdLeft);
            ivLeft.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(strRight)) {
            btnRight.setText(strRight);
            btnRight.setVisibility(View.VISIBLE);
        } else {
            btnRight.setVisibility(View.GONE);
        }
        if (backgroundColor==0){
            llActionbarRoot.setBackgroundResource(0);
        }else {
            llActionbarRoot.setBackgroundColor(backgroundColor);//设置标题栏背景颜色
        }
        if (listener != null) {
            if(ivLeft.getVisibility()==VISIBLE){
                ivLeft.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onLeftClick();
                    }
                });
            }else if(tvLeft.getVisibility()==VISIBLE){
                tvLeft.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onLeftClick();
                    }
                });
            }


            if(btnRight.getVisibility()==VISIBLE){
                btnRight.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onRightClick();
                    }
                });
            }else if(tvRight.getVisibility()==VISIBLE){
                tvRight.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onRightClick();
                    }
                });
            }

        }
    }
    public interface ActionBarClickListener {
        //左边点击
        void onLeftClick();
        //右边点击
        void onRightClick();
    }
}
