package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.databinding.ItemProductPhotoBinding;

import java.util.List;

public class ProductDetailViewPager2Adapter extends RecyclerView.Adapter<ProductDetailViewPager2Adapter.PhotoViewHolder>{
    private List<String> mListPhoto;

    public ProductDetailViewPager2Adapter(List<String> mListPhoto){
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductPhotoBinding binding = ItemProductPhotoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PhotoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String photoUrl = mListPhoto.get(position);
        holder.bind(photoUrl, holder);
    }

    @Override
    public int getItemCount() {
        if (mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder{
        ItemProductPhotoBinding binding;
        public PhotoViewHolder(ItemProductPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String photoUrl, PhotoViewHolder holder){
            // "http://192.168.50.2:8080/storage/product/"
            Glide.with(holder.itemView.getContext())
                    .load(photoUrl)
                    .error(R.drawable.ic_user)
                    .into(binding.imgPhoto);
        }
    }
}
