package com.example.android_doan.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android_doan.R;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.databinding.ItemProductAdminBinding;
import com.example.android_doan.utils.FormatUtil;
import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ProductViewHolder>{
    private List<ProductModel> mListProduct;

    private IOnClickProduct listener;

    public interface IOnClickProduct{
        void onClickItemProduct();
        void onClickEdit(ProductModel productModel);
        void onCLickDelete(ProductModel productModel);
    }

    public void setListener(IOnClickProduct listener) {
        this.listener = listener;
    }
    public ProductAdminAdapter(List<ProductModel> mListProduct){
        this.mListProduct = mListProduct;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductAdminBinding binding = ItemProductAdminBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null){
            return mListProduct.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = mListProduct.get(position);
        holder.bind(productModel, holder, listener);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemProductAdminBinding binding;
        public ProductViewHolder(ItemProductAdminBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductModel productModel, ProductViewHolder holder, IOnClickProduct listener){
            if (productModel.getThumbnail() != null){
                // "http://192.168.50.2:8080/storage/product/" +
                Glide.with(holder.itemView.getContext())
                        .load(productModel.getThumbnail())
                        .error(R.drawable.ic_user)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("lkhai4617", "Glide load failed: " + (e != null ? e.getMessage() : "Unknown error"));
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("lkhai4617", "Glide load success");
                                return false;
                            }
                        })
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
            }
        }
    }

    public void updateData(List<ProductModel> newProducts){
        mListProduct.clear();
        mListProduct.addAll(newProducts);
        notifyDataSetChanged();
    }

    private void showPopupMenu(View anchor, ProductModel productModel) {
        PopupMenu popupMenu = new PopupMenu(anchor.getContext(), anchor);
        popupMenu.inflate(R.menu.popup_product_admin_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()){
                case R.id.action_edit:
                    if (listener != null){
                        listener.onClickEdit(productModel);
                    }
                    return true;
                case R.id.action_delete:
                    if (listener != null){
                        listener.onCLickDelete(productModel);
                    }
                    return true;
            }
            return false;
        });
        popupMenu.show();
    }
}
