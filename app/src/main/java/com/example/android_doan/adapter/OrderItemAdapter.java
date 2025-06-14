package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.data.model.OrderItem;
import com.example.android_doan.databinding.ItemAdminOrderItemBinding;
import com.example.android_doan.utils.FormatUtil;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<OrderItem> mList;
    private IOnClickOrderItem listener;

    public OrderItemAdapter(List<OrderItem> mList) {
        this.mList = mList;
    }

    public void setListener(IOnClickOrderItem listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminOrderItemBinding binding = ItemAdminOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem orderItem = mList.get(position);
        holder.bind(orderItem, holder, listener);
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public void updateData(List<OrderItem> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public List<OrderItem> getList() {
        return mList;
    }

    public interface IOnClickOrderItem {
        void onClickIncreaseQuantity(OrderItem orderItem);

        void onClickDecreaseQuantity(OrderItem orderItem);
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private ItemAdminOrderItemBinding binding;

        public OrderItemViewHolder(@NonNull ItemAdminOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderItem orderItem, OrderItemViewHolder holder, IOnClickOrderItem listener) {
            if (orderItem != null) {
                Glide.with(holder.itemView.getContext())
                        .load(orderItem.getProductThumbnail())
                        .error(R.drawable.logo)
                        .into(binding.ivProduct);
                binding.productName.setText(orderItem.getProductName());
                binding.productQuantity.setText(String.valueOf(orderItem.getQuantity()));
                binding.productPrice.setText(FormatUtil.formatCurrency(orderItem.getPrice()));

                binding.btnIncrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClickIncreaseQuantity(orderItem);
                        }
                    }
                });

                binding.btnDecrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null && orderItem.getQuantity() > 1) {
                            listener.onClickDecreaseQuantity(orderItem);
                        }
                    }
                });
            }
        }
    }
}
