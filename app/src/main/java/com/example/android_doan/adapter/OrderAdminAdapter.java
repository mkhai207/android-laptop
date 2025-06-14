package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.data.model.response.OrderAdminResponse;
import com.example.android_doan.databinding.ItemOrderAdminBinding;
import com.example.android_doan.utils.FormatUtil;

import java.util.List;

public class OrderAdminAdapter extends RecyclerView.Adapter<OrderAdminAdapter.OrderAdminViewHolder> {
    private List<OrderAdminResponse> mListOrder;
    private IOnClickOrder listener;

    public OrderAdminAdapter(List<OrderAdminResponse> mListOrder) {
        this.mListOrder = mListOrder;
    }

    public void setListener(IOnClickOrder listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderAdminBinding binding = ItemOrderAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderAdminViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdminViewHolder holder, int position) {
        OrderAdminResponse orderAdminResponse = mListOrder.get(position);
        holder.bind(orderAdminResponse, listener);
    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    public void updateData(List<OrderAdminResponse> orders) {
        mListOrder.clear();
        mListOrder.addAll(orders);
        notifyDataSetChanged();
    }

    public interface IOnClickOrder {
        void onClickUpdateStatus(OrderAdminResponse orderAdminResponse);

        void onClickViewDetail(OrderAdminResponse orderAdminResponse);
    }

    public static class OrderAdminViewHolder extends RecyclerView.ViewHolder {
        private ItemOrderAdminBinding binding;

        public OrderAdminViewHolder(@NonNull ItemOrderAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderAdminResponse order, IOnClickOrder listener) {
            if (order != null) {
                binding.tvStatus.setText(order.getStatus().toUpperCase());
                binding.tvOrderDate.setText(FormatUtil.formatIsoDate(order.getCreatedAt()));
                binding.tvOrderId.setText(String.valueOf(order.getId()));
                binding.tvQuantity.setText(String.valueOf(order.getOrderDetails().size()));
                binding.tvTotal.setText(FormatUtil.formatCurrency(order.getTotalMoney()));
                if (order.getAddress() != null) {
                    binding.tvRecipientName.setText(order.getAddress().getRecipientName() != null ? order.getAddress().getRecipientName() : "");
                    binding.tvPhoneNumber.setText(order.getAddress().getPhoneNumber() != null ? order.getAddress().getPhoneNumber() : "");
                    String address = order.getAddress().getStreet() + ", " + order.getAddress().getWard() + ", " + order.getAddress().getDistrict() + ", " + order.getAddress().getCity();
                    binding.tvShippingAddress.setText(address != null ? address : "");
                } else {
                    binding.tvRecipientName.setText(order.getName());
                    binding.tvPhoneNumber.setText(order.getPhone());
                    binding.tvShippingAddress.setText(order.getShippingAddress());
                }

                binding.btnUpdateStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onClickUpdateStatus(order);
                        }
                    }
                });

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClickViewDetail(order);
                        }
                    }
                });
            }
        }
    }
}
