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
                listener.onCLickDelete(mListCategory.get(position));
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
                            listener.onClickEdit(categoryModel);
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
