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


import com.example.fruit.R;
import com.example.fruit.bean.Car;
import com.example.fruit.bean.User;
import com.example.fruit.ui.activity.UserDetailActivity;
import com.example.fruit.util.SPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> list = new ArrayList<>();
    private Context mActivity;
    private LinearLayout llEmpty;
    private RecyclerView rvUserList;

    public UserAdapter(LinearLayout llEmpty, RecyclerView rvUserList) {
        this.llEmpty = llEmpty;
        this.rvUserList = rvUserList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_user_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        User user = list.get(i);
        if (user != null) {
            String account = (String) SPUtils.get(mActivity, SPUtils.ACCOUNT, "");
            viewHolder.itemView.setVisibility(account.equals(user.getAccount()) ? View.GONE : View.VISIBLE);
            viewHolder.nickName.setText(String.format("nickname:%s", user.getNickName()));
            viewHolder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mActivity, UserDetailActivity.class);
                intent.putExtra("user", user);
                mActivity.startActivity(intent);
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                    dialog.setMessage("Are you sure you want to delete the user?");
                    dialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //删除收藏记录和浏览记录
                            List<Car> brows = DataSupport.where("account = ?", user.getAccount()).find(Car.class);
                            if (brows != null && brows.size() > 0) {
                                for (Car car : brows) {
                                    car.delete();
                                }
                            }
                            list.remove(user);
                            user.delete();
                            notifyDataSetChanged();
                            Toast.makeText(mActivity, "Deleted successfully", Toast.LENGTH_LONG).show();
                            if (list != null && list.size() > 1) {
                                rvUserList.setVisibility(View.VISIBLE);
                                llEmpty.setVisibility(View.GONE);
                            } else {
                                rvUserList.setVisibility(View.GONE);
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

    public void addItem(List<User> listAdd) {
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
        private TextView nickName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.nickName);
        }
    }
}
