package com.example.android_doan.adapter;

import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.databinding.ItemImageBinding;

import java.util.ArrayList;
import java.util.List;

public class SliderImageAdapter extends RecyclerView.Adapter<SliderImageAdapter.ImageViewHolder>{
    private List<String> mListImage;

    public SliderImageAdapter(List<String> mListImage) {
        this.mListImage = mListImage;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImageBinding binding = ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String url = mListImage.get(position);
        holder.bind(url, holder);
    }

    @Override
    public int getItemCount() {
        if (mListImage != null){
            return mListImage.size();
        }
        return 0;
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        private ItemImageBinding binding;
        public ImageViewHolder(@NonNull ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String url, ImageViewHolder holder){
            if (url != null){
                Glide.with(holder.itemView.getContext())
                        .load(url)
                        .error(R.drawable.laptop_logo)
                        .into(binding.ivProduct);
            }
        }
    }

    public void updateData(List<String> strings){
        mListImage.clear();
        mListImage.addAll(strings);
        notifyDataSetChanged();
    }

    public void updateDataUri(List<Uri> uris){
        List<String> strings = new ArrayList<>();
        for (Uri uri: uris){
            strings.add(uri.toString());
        }
        mListImage.addAll(strings);
        notifyDataSetChanged();
    }
}
