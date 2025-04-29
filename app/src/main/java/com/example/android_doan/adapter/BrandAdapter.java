package com.example.android_doan.adapter;

import android.text.style.AlignmentSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.databinding.ItemBrandBinding;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder>{
    private List<BrandModel> mListBrand;

    public BrandAdapter(List<BrandModel> mListBrand) {
        this.mListBrand = mListBrand;
    }

    private IOnClickBrand listener;

    public void setListener(IOnClickBrand listener){
        this.listener = listener;
    }
    public interface IOnClickBrand{
        void onClickItemBrand();
        void onClickEdit(BrandModel brandModel);
        void onCLickDelete(BrandModel brandModel);
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBrandBinding binding = ItemBrandBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BrandViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        BrandModel brandModel = mListBrand.get(position);
        holder.bind(brandModel, listener);
    }

    @Override
    public int getItemCount() {
        if (mListBrand != null){
            return mListBrand.size();
        }
        return 0;
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder{
        private ItemBrandBinding binding;
        public BrandViewHolder(@NonNull ItemBrandBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BrandModel brandModel, IOnClickBrand listener){
            if (brandModel != null) {
                binding.tvBrandName.setText(brandModel.getName());

                binding.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null){
                            listener.onClickEdit(brandModel);
                        }
                    }
                });

                binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null){
                            listener.onCLickDelete(brandModel);
                        }
                    }
                });
            }
        }
    }

    public void updateData(List<BrandModel> brands){
        mListBrand.clear();
        mListBrand.addAll(brands);
        notifyDataSetChanged();
    }
}
