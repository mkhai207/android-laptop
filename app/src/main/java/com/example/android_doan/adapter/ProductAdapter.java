package com.example.android_doan.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android_doan.R;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.databinding.ItemProductBinding;
import com.example.android_doan.utils.FormatUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{
    private Context context;
    private List<ProductModel> mListProduct;

    private IOnClickProduct listener = null;

    public ProductAdapter(Context context, List<ProductModel> mListProduct){
        this.mListProduct = mListProduct;
        this.context = context;
    }

    public void setListener(IOnClickProduct listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding itemProductBinding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(itemProductBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel productModel = mListProduct.get(position);
        holder.bind(productModel, listener);
    }

    @Override
    public int getItemCount() {
        return mListProduct.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;
        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductModel productModel, IOnClickProduct listener){
            if (productModel.getThumbnail() != null && !productModel.getThumbnail().isEmpty()){
                String accessToken = DataLocalManager.getAccessToken();
                if (accessToken == null){
                    return;
                }
                GlideUrl glideUrl = new GlideUrl("http://192.168.50.2:8080/" + productModel.getThumbnail(), ()-> {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + accessToken);
                    return headers;
                });

                Glide.with(context)
                        .load("http://192.168.50.2:8080/storage/product/" + productModel.getThumbnail())
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
            }
            binding.tvNameOfLaptop.setText(productModel.getName());
            String price = FormatUtil.formatCurrency(productModel.getPrice());
            binding.tvPriceOfLaptop.setText(price);
            binding.tvQuantitySoldOfLaptop.setText(String.valueOf(productModel.getSold()));

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onClickProduct(productModel);
                    }
                }
            });
        }
    }

    public void updateProduct(List<ProductModel> newProducts){
        mListProduct.clear();
        mListProduct.addAll(newProducts);
        this.notifyDataSetChanged();
    }

    public interface IOnClickProduct{
        void onClickProduct(ProductModel productModel);
    }
}
