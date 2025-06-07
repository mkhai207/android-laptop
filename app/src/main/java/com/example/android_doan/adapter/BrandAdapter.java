package com.example.android_doan.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.R;
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

    public void attachSwipeToDelete(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            private final ColorDrawable background = new ColorDrawable(Color.RED);
            private final Drawable deleteIcon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.ic_delete);
            private final int iconMargin = 32;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                listener.onCLickDelete(mListBrand.get(position));
                recyclerView.getAdapter().notifyItemChanged(position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getBottom() - itemView.getTop();

                // Draw red background
                background.setBounds(
                        itemView.getRight() + (int) dX,
                        itemView.getTop(),
                        itemView.getRight(),
                        itemView.getBottom()
                );
                background.draw(c);

                // Draw delete icon
                if (deleteIcon != null) {
                    int iconTop = itemView.getTop() + (itemHeight - deleteIcon.getIntrinsicHeight()) / 2;
                    int iconBottom = iconTop + deleteIcon.getIntrinsicHeight();
                    int iconLeft = itemView.getRight() - iconMargin - deleteIcon.getIntrinsicWidth();
                    int iconRight = itemView.getRight() - iconMargin;

                    deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    deleteIcon.draw(c);
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
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

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null){
                            listener.onClickEdit(brandModel);
                        }
                    }
                });

//                binding.btnDelete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (listener != null){
//                            listener.onCLickDelete(brandModel);
//                        }
//                    }
//                });
            }
        }
    }

    public void updateData(List<BrandModel> brands){
        mListBrand.clear();
        mListBrand.addAll(brands);
        notifyDataSetChanged();
    }
}
