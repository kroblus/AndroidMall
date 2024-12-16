package com.example.fruit.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fruit.R;
import com.example.fruit.bean.Fruit;
import com.example.fruit.util.SPUtils;

import java.util.ArrayList;
import java.util.List;


public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private List<Fruit> list = new ArrayList<>();
    private Context mActivity;
    private ItemListener mItemListener;
    private LinearLayout llEmpty;
    private RecyclerView rvfruitList;

    public void setItemListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

    public FruitAdapter(LinearLayout llEmpty, RecyclerView rvfruitList) {
        this.llEmpty = llEmpty;
        this.rvfruitList = rvfruitList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mActivity = viewGroup.getContext();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_rv_fruit_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Fruit fruit = list.get(i);
        if (fruit != null) {
            viewHolder.title.setText(fruit.getTitle());
            viewHolder.author_name.setText(String.format("₽%s", fruit.getIssuer()));

            Glide.with(mActivity)
                    .asBitmap()
                    .load(fruit.getImg())
                    .error(R.drawable.ic_error)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewHolder.img);
            Boolean isAdmin = (Boolean) SPUtils.get(mActivity, SPUtils.IS_ADMIN, false);
            viewHolder.itemView.setOnClickListener(v -> {
                if (mItemListener != null) {
                    mItemListener.ItemClick(fruit);
                }
            });
            if (isAdmin) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
                        dialog.setMessage("Are you sure you want to remove the fruit?");
                        dialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(fruit);
                                fruit.delete();
                                notifyDataSetChanged();
                                Toast.makeText(mActivity, "Deleted successfully", Toast.LENGTH_LONG).show();
                                if (list != null && list.size() > 0) {
                                    rvfruitList.setVisibility(View.VISIBLE);
                                    llEmpty.setVisibility(View.GONE);
                                } else {
                                    rvfruitList.setVisibility(View.GONE);
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(List<Fruit> listAdd) {
        //如果是加载第一页，需要先清空数据列表
        this.list.clear();
        if (listAdd != null) {
            //添加数据
            this.list.addAll(listAdd);
        }
        //通知RecyclerView进行改变--整体
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author_name;

        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author_name = itemView.findViewById(R.id.author_name);

            img = itemView.findViewById(R.id.img);
        }
    }

    public interface ItemListener {
        void ItemClick(Fruit fruit);
    }
}
