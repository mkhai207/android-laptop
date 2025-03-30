package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.data.model.response.OrderResponse;
import com.example.android_doan.databinding.ItemOrderBinding;
import com.example.android_doan.utils.DateFormatUtil;

import java.math.BigDecimal;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private List<OrderResponse.OrderData> mListData;

    public OrderAdapter(List<OrderResponse.OrderData> mListData) {
        this.mListData = mListData;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderResponse.OrderData data = mListData.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        if (mListData != null) {
            return mListData.size();
        }
        return 0;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        private ItemOrderBinding binding;
        public OrderViewHolder(@NonNull ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderResponse.OrderData order){
            if (order.getCreatedAt() != null){
                String date = DateFormatUtil.formatIsoDate(order.getCreatedAt());
                binding.tvOrderDate.setText(date);
            }
            binding.tvOrderId.setText(String.valueOf(order.getId()));
            int quantity = order.getOrderDetails().size();
            binding.tvQuantity.setText(String.valueOf(quantity));
            String total = BigDecimal.valueOf(order.getTotalMoney()).toString();
            binding.tvTotal.setText(total);
            if (order.getStatus() != null){
                binding.tvStatus.setText(order.getStatus());
            }
        }
    }

    public void updateData(List<OrderResponse.OrderData> newData){
        mListData.clear();
        mListData.addAll(newData);
        notifyDataSetChanged();
    }
}
