package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.data.model.SortOption;
import com.example.android_doan.databinding.ItemSortBottomSheetBinding;

import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.SortOptionViewHolder>{
    private List<SortOption> mSortOption;
    private IOnClickItemSort listener;

    public interface IOnClickItemSort{
        void onClickItemSort(SortOption sortOption);
    }
    public SortAdapter(List<SortOption> mSortOption, IOnClickItemSort listener){
        this.mSortOption = mSortOption;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SortOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSortBottomSheetBinding binding = ItemSortBottomSheetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SortOptionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SortOptionViewHolder holder, int position) {
        SortOption sortOption = mSortOption.get(position);
        holder.bind(sortOption, listener);
    }

    @Override
    public int getItemCount() {
        if (mSortOption != null){
            return mSortOption.size();
        }
        return 0;
    }

    public static class SortOptionViewHolder extends RecyclerView.ViewHolder{
        private ItemSortBottomSheetBinding binding;
        public SortOptionViewHolder(@NonNull ItemSortBottomSheetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(SortOption sortOption, IOnClickItemSort listener){
            if (sortOption != null){
                binding.tvSortOption.setText(sortOption.getName());
            }
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        listener.onClickItemSort(sortOption);
                    }
                }
            });
        }
    }
}
