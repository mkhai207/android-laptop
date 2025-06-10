package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.data.model.OrderDetail;
import com.example.android_doan.databinding.ItemOrderDetailBinding;
import com.example.android_doan.utils.FormatUtil;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private List<OrderDetail> mList;

    public OrderDetailAdapter(List<OrderDetail> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = ItemOrderDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderDetail orderDetail = mList.get(position);
        holder.bind(orderDetail, holder);
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public void updateData(List<OrderDetail> newData) {
        mList.clear();
        mList.addAll(newData);
        notifyDataSetChanged();
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        private ItemOrderDetailBinding binding;

        public OrderDetailViewHolder(@NonNull ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderDetail orderDetail, OrderDetailViewHolder holder) {
            if (orderDetail != null) {
                Glide.with(holder.itemView.getContext())
                        .load(orderDetail.getProductThumbnail())
                        .error(R.drawable.logo)
                        .into(binding.ivProductImage);

                binding.tvProductName.setText(orderDetail.getProductName());
                binding.tvProductCode.setText(String.valueOf(orderDetail.getId()));
                binding.tvQuantity.setText(String.valueOf(orderDetail.getQuantity()));
                String price = FormatUtil.formatCurrency(orderDetail.getPrice());
                binding.tvPrice.setText(price);
            }
        }
    }
}
