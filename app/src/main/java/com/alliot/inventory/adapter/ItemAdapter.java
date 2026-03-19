package com.alliot.inventory.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alliot.inventory.R;
import com.alliot.inventory.model.Item;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item, View sharedElement);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Item> newItems) {
        List<Item> newList = newItems != null ? new ArrayList<>(newItems) : new ArrayList<>();
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ItemDiffCallback(this.items, newList));
        this.items = newList;
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid_cell, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private final ImageView ivItemImage;
        private final TextView tvItemName;
        private final TextView tvItemSku;
        private final TextView tvAvailableQty;
        private final View statusIndicator;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardItem);
            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemSku = itemView.findViewById(R.id.tvItemSku);
            tvAvailableQty = itemView.findViewById(R.id.tvAvailableQty);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }

        void bind(Item item) {
            android.content.Context context = itemView.getContext();

            // Name
            tvItemName.setText(item.getName() != null ? item.getName()
                    : context.getString(R.string.no_name));

            // SKU with label
            String sku = item.getInternalSku() != null ? item.getInternalSku()
                    : context.getString(R.string.not_available);
            tvItemSku.setText(String.format(context.getString(R.string.sku_format), sku));

            // Available quantity
            double qty = item.getAvailableQuantity();
            tvAvailableQty.setText(String.format(Locale.getDefault(),
                    context.getString(R.string.available_format), qty));

            // Active/inactive status
            statusIndicator.setBackgroundResource(
                    item.isActive() ? R.drawable.status_active : R.drawable.status_inactive
            );

            String imageUrl = item.getMediumImageUrl();
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_placeholder)
                            .error(R.drawable.ic_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerInside())
                    .into(ivItemImage);

            ivItemImage.setTransitionName("item_image_" + item.getId());

            cardView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(item, ivItemImage);
            });
        }
    }

    static class ItemDiffCallback extends DiffUtil.Callback {
        private final List<Item> oldList;
        private final List<Item> newList;

        ItemDiffCallback(List<Item> oldList, List<Item> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() { return oldList.size(); }

        @Override
        public int getNewListSize() { return newList.size(); }

        @Override
        public boolean areItemsTheSame(int oldPos, int newPos) {
            return oldList.get(oldPos).getId() == newList.get(newPos).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldPos, int newPos) {
            Item oldItem = oldList.get(oldPos);
            Item newItem = newList.get(newPos);
            return oldItem.getId() == newItem.getId()
                    && oldItem.getQuantity() == newItem.getQuantity()
                    && oldItem.isActive() == newItem.isActive();
        }
    }
}

