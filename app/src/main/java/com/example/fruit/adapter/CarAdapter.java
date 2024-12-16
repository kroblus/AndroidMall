package com.example.fruit.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.fruit.R;
import com.example.fruit.bean.Car;
import com.example.fruit.bean.Fruit;
import com.example.fruit.bean.User;
import com.example.fruit.ui.activity.FruitYActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

//购物车适配器

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private List<Car> list = new ArrayList<>();
    private Context mActivity;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    private LinearLayout llEmpty;
    private RecyclerView rvOrderList;
    private ItemListener mItemListener;


    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    public CarAdapter(LinearLayout llEmpty, RecyclerView rvOrderList) {
        this.llEmpty = llEmpty;
        this.rvOrderList = rvOrderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_car_list, viewGroup, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Car car = list.get(i);


        User user = DataSupport.where("account = ? ", car.getAccount()).findFirst(User.class);
        if (car != null && user != null) {

            viewHolder.j.setText(String.format("₽%s", car.getIssuer()));//order.getNumber

            viewHolder.title.setText(car.getTitle());
            viewHolder.itemView.setOnClickListener(v -> {
            });

            viewHolder.zf.setOnClickListener(v -> {
                if (!viewHolder.cb.isChecked()) {
                    Toast.makeText(mActivity, "You haven't selected a product yet!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(mActivity, FruitYActivity.class);
                    Fruit fruit = DataSupport.where("title = ?", car.getTitle()).findFirst(Fruit.class);
                    intent.putExtra("fruit", fruit);
                    mActivity.startActivity(intent);
                    return;
                }
            });

            viewHolder.itemView.setOnLongClickListener(v -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                dialog.setMessage("Are you sure you want to delete this product?");
                //消息框确认按钮
                dialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(car);
                        car.delete();
                        notifyDataSetChanged();//刷新ListView与数据库同步
                        Toast.makeText(mActivity, "Deleted successfully", Toast.LENGTH_LONG).show();
                        if (list != null && list.size() > 0) {
                            rvOrderList.setVisibility(View.VISIBLE);
                            llEmpty.setVisibility(View.GONE);
                        } else {
                            rvOrderList.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });
                //消息框Cancel按钮
                dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            });
        }
    }

    public void addItem(List<Car> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd != null) {
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Button zf;
        private TextView j;
        private LinearLayout ll_img;
        private TextView title;
        private CheckBox cb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            zf = itemView.findViewById(R.id.zf);
            ll_img = itemView.findViewById(R.id.ll_img);
            title = itemView.findViewById(R.id.title);
            j = itemView.findViewById(R.id.j);
            cb = itemView.findViewById(R.id.cb);
        }
    }

    public interface ItemListener {
        void ItemClick(Car car);
    }
}
