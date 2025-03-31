package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.data.model.response.OrderResponse;
import com.example.android_doan.databinding.ItemOrderBinding;
import com.example.android_doan.utils.FormatUtil;

import java.math.BigDecimal;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private List<OrderResponse.OrderData> mListData;
    private IOnClickDetailOrder listener;
    public interface IOnClickDetailOrder{
        void onClickDetailOrder(OrderResponse.OrderData data);
    }

    public void setListener(IOnClickDetailOrder listener){
        this.listener = listener;
    }

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
        holder.bind(data, listener);
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

        public void bind(OrderResponse.OrderData order, IOnClickDetailOrder listener){
            if (order.getCreatedAt() != null){
                String date = FormatUtil.formatIsoDate(order.getCreatedAt());
                binding.tvOrderDate.setText(date);
            }
            binding.tvOrderId.setText(String.valueOf(order.getId()));
            int quantity = order.getOrderDetails().size();
            binding.tvQuantity.setText(String.valueOf(quantity));
            String total = FormatUtil.formatCurrency(order.getTotalMoney());
            binding.tvTotal.setText(total);
            if (order.getStatus() != null){
                binding.tvStatus.setText(order.getStatus());
            }

            binding.btnDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onClickDetailOrder(order);
                    }
                }
            });
        }
    }

    public void updateData(List<OrderResponse.OrderData> newData){
        mListData.clear();
        mListData.addAll(newData);
        notifyDataSetChanged();
    }
}
