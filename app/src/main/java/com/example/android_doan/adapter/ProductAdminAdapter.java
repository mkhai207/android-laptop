package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.databinding.ItemProductAdminBinding;
import com.example.android_doan.utils.FormatUtil;

import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductViewHolder> {
    private List<ProductModel> mListProduct;

    private IOnClickProduct listener;

    public ProductAdminAdapter(List<ProductModel> mListProduct) {
        this.mListProduct = mListProduct;
    }

    public void setListener(IOnClickProduct listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductAdminBinding binding = ItemProductAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null) {
            return mListProduct.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = mListProduct.get(position);
        holder.bind(productModel, holder, listener);
    }

    public void updateData(List<ProductModel> newProducts) {
        mListProduct.clear();
        mListProduct.addAll(newProducts);
        notifyDataSetChanged();
    }

    private void showPopupMenu(View anchor, ProductModel productModel) {
        PopupMenu popupMenu = new PopupMenu(anchor.getContext(), anchor);
        popupMenu.inflate(R.menu.popup_product_admin_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_edit:
                    if (listener != null) {
                        listener.onClickEdit(productModel);
                    }
                    return true;
                case R.id.action_delete:
                    if (listener != null) {
                        listener.onCLickDelete(productModel);
                    }
                    return true;
            }
            return false;
        });
        popupMenu.show();
    }

    public interface IOnClickProduct {
        void onClickEdit(ProductModel productModel);

        void onCLickDelete(ProductModel productModel);

        void onClickSelect(ProductModel productModel);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemProductAdminBinding binding;

        public ProductViewHolder(ItemProductAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductModel productModel, ProductViewHolder holder, IOnClickProduct listener) {
            if (productModel.getThumbnail() != null) {
                // "http://192.168.50.2:8080/storage/product/" +
                Glide.with(holder.itemView.getContext())
                        .load(productModel.getThumbnail())
                        .error(R.drawable.ic_user)
                        .into(binding.imgLaptop);
                binding.tvNameOfLaptop.setText(productModel.getName());
                binding.tvPriceOfLaptop.setText(FormatUtil.formatCurrency(productModel.getPrice()));
                binding.tvSoldOfLaptop.setText(String.valueOf(productModel.getSold()));

                binding.ivMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupMenu(view, productModel);
                    }
                });

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClickSelect(productModel);
                        }
                    }
                });
            }
        }
    }
}
