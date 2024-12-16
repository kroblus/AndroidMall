package com.example.fruit.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.example.fruit.ui.activity.FruitXActivity;
import com.example.fruit.R;
import com.example.fruit.bean.Fruit;
import com.example.fruit.bean.Orders;
import com.example.fruit.bean.User;
import com.example.fruit.util.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Orders> list =new ArrayList<>();
    private Context mActivity;
    private RequestOptions headerRO = new RequestOptions().circleCrop();//圆角变换
    private LinearLayout llEmpty;
    private RecyclerView rvOrderList;
    private ItemListener mItemListener;
    public void setItemListener(ItemListener itemListener){
        this.mItemListener = itemListener;
    }
    public OrderAdapter(LinearLayout llEmpty, RecyclerView rvOrderList){
        this.llEmpty = llEmpty;
        this.rvOrderList =rvOrderList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view= LayoutInflater.from(mActivity).inflate(R.layout.item_rv_order_list,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Orders order = list.get(i);


        User user = DataSupport.where("account = ? ", order.getAccount()).findFirst(User.class);
        if (order != null && user!=null ) {
            viewHolder.nickName.setText(String.format("user:%s",user.getNickName()));
            viewHolder.number.setText(String.format("order:%s",order.getNumber()));
            viewHolder.j.setText(String.format("₽%s",order.getIssuer()));//order.getNumber
            viewHolder.date.setText(order.getDate());

            viewHolder.title.setText(order.getTitle());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, FruitXActivity.class);
                    Fruit fruit = DataSupport.where("title = ?",order.getTitle()).findFirst(Fruit.class);
                    intent.putExtra("fruit",fruit);
                    mActivity.startActivity(intent);
                }
            });
            Boolean isAdmin = (Boolean) SPUtils.get(mActivity,SPUtils.IS_ADMIN,false);
            if (isAdmin){
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                        dialog.setMessage("Are you sure you want to delete this order?");
                        dialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(order);
                                order.delete();
                                notifyDataSetChanged();
                                Toast.makeText(mActivity,"Deleted successfully", Toast.LENGTH_LONG).show();
                                if (list!=null && list.size() > 0){
                                    rvOrderList.setVisibility(View.VISIBLE);
                                    llEmpty.setVisibility(View.GONE);
                                }else {
                                    rvOrderList.setVisibility(View.GONE);
                                    llEmpty.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        return false;
                    }
                });
            }
        }
    }
    public void addItem(List<Orders> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd!=null){
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
        private TextView nickName;
        private TextView date;
        private TextView j;
        private TextView number;
        private TextView title;
      //  private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.nickName);
            date = itemView.findViewById(R.id.date);
            number = itemView.findViewById(R.id.number);
            title = itemView.findViewById(R.id.title);
            j=itemView.findViewById(R.id.j);
           // img=itemView.findViewById(R.id.img);
        }
    }
    public interface ItemListener{
        void ItemClick(Orders order);
    }
}
