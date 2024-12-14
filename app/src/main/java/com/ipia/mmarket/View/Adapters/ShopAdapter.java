package com.ipia.mmarket.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ipia.mmarket.R;
import com.ipia.mmarket.Models.Item;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {

    Context context;
    ArrayList<Item> itemList;
    private OnItemClickListener onItemClickListener;

    public ShopAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ShopViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.tvHeading.setText(item.getItemName());
        holder.tvDescription.setText(item.getItemDescription());
        holder.tvPrice.setText(String.valueOf(item.getItemPrice()) + " KM");
        holder.tvUser.setText(item.getUserName());

        Glide.with(context)
                .load(item.getImageUrl())
                .into(holder.titleImage);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (item.getUserId().equals(currentUserId)) {
            holder.deleteItemButton.setVisibility(View.VISIBLE);
            holder.deleteItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(item.getItemId());
                }
            });
        } else {
            holder.deleteItemButton.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView titleImage;
        TextView tvHeading;
        TextView tvDescription;
        TextView tvUser;
        TextView tvPrice;
        FloatingActionButton deleteItemButton;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            titleImage = itemView.findViewById(R.id.selling_item);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            deleteItemButton = itemView.findViewById(R.id.delete_item_button);
        }
    }

    public void updateItemList(ArrayList<Item> newList) {
        itemList = newList;
        notifyDataSetChanged();
    }

    private void deleteItem(String itemId) {
        DatabaseReference itemDb = FirebaseDatabase.getInstance("https://mmarket-ipia-rma2-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Items")
                .child(itemId);

        itemDb.removeValue();
    }
}


