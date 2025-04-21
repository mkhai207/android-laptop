package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.databinding.ItemCategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategoryModel> mListCategory;
    private IOnClickCategory listener;

    public void setListener(IOnClickCategory listener){
        this.listener = listener;
    }
    public interface IOnClickCategory{
        void onClickItemCategory();
        void onClickEdit(CategoryModel categoryModel);
        void onCLickDelete(CategoryModel categoryModel);
    }

    public CategoryAdapter(List<CategoryModel> mListCategory) {
        this.mListCategory = mListCategory;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding =
                ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel categoryModel = mListCategory.get(position);
        holder.bind(categoryModel, listener, holder);
    }

    @Override
    public int getItemCount() {
        if (mListCategory != null){
            return mListCategory.size();
        }
        return 0;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        private ItemCategoryBinding binding;
        public CategoryViewHolder(@NonNull ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CategoryModel categoryModel, IOnClickCategory listener, CategoryViewHolder holder){
            if (categoryModel != null){
                binding.tvCode.setText(categoryModel.getCode());
                binding.tvName.setText(categoryModel.getName());

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null){
                            listener.onClickItemCategory();
                        }
                    }
                });

                binding.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null){
                            listener.onClickEdit(categoryModel);
                        }
                    }
                });

                binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null){
                            listener.onCLickDelete(categoryModel);
                        }
                    }
                });
            }
        }
    }

    public void updateData(List<CategoryModel> categoryModels){
        mListCategory.clear();
        mListCategory.addAll(categoryModels);
        notifyDataSetChanged();
    }
}
