package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.databinding.ItemCartBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<GetCartResponse.Data> mList;

    private IOnClickItemCart listener;

    public interface IOnClickItemCart{
        void onClickAdjustQuantity(AddToCartRequest request);
        void onClickRemove(String productId);
    }

    public void setListener(IOnClickItemCart listener){
        this.listener = listener;
    }

    public CartAdapter(List<GetCartResponse.Data> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        GetCartResponse.Data cartItem = mList.get(position);
        holder.bind(cartItem, holder, listener);
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private ItemCartBinding binding;

        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(GetCartResponse.Data itemCart, CartViewHolder holder, IOnClickItemCart listener){
            if (itemCart == null){
                return;
            }
            Glide.with(holder.itemView.getContext())
                    .load("http://192.168.50.2:8080/storage/product/" + itemCart.getProduct().getThumbnail())
                    .error(R.drawable.logo)
                    .into(binding.imgProduct);

            binding.tvProductName.setText(itemCart.getProduct().getName());
            binding.tvQuantity.setText(String.valueOf(itemCart.getQuantity()));
            binding.tvPrice.setText(String.valueOf(itemCart.getQuantity() * itemCart.getProduct().getPrice()));

            binding.ivIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = itemCart.getProduct().getQuantity();
                    int cartQuantity = Integer.parseInt(binding.tvQuantity.getText().toString());
                    if (cartQuantity < quantity){
                        // cap nhat ui
                        binding.tvQuantity.setText(String.valueOf(cartQuantity + 1));
                        int curQuantity = Integer.parseInt(binding.tvQuantity.getText().toString());
                        binding.tvPrice.setText(String.valueOf(curQuantity * itemCart.getProduct().getPrice()));

                        if (listener != null){
                            AddToCartRequest request = new AddToCartRequest(binding.tvQuantity.getText().toString(), itemCart.getProduct().getId());
                            listener.onClickAdjustQuantity(request);
                        }
                    }
                }
            });

            binding.ivDecrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int cartQuantity = Integer.parseInt(binding.tvQuantity.getText().toString());
                    if (cartQuantity > 1){
                        // cap nhat ui
                        binding.tvQuantity.setText(String.valueOf(cartQuantity - 1));
                        int curQuantity = Integer.parseInt(binding.tvQuantity.getText().toString());
                        binding.tvPrice.setText(String.valueOf(curQuantity * itemCart.getProduct().getPrice()));

                        if (listener != null){
                            AddToCartRequest request = new AddToCartRequest(binding.tvQuantity.getText().toString(), itemCart.getProduct().getId());
                            listener.onClickAdjustQuantity(request);
                        }
                    }
                }
            });

            binding.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view, itemCart.getProduct().getId());
                }
            });
        }
    }

    public void updateItemCart(List<GetCartResponse.Data> newData){
        this.mList.clear();
        this.mList.addAll(newData);
        notifyDataSetChanged();
    }

    private void showPopupMenu(View anchor, String productId) {
        PopupMenu popupMenu = new PopupMenu(anchor.getContext(), anchor);
        popupMenu.inflate(R.menu.cart_item_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.remove) {
                if (listener != null) {
                    listener.onClickRemove(productId);
                }
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
}
