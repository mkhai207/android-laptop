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
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.databinding.ItemAddressBinding;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    private List<AddressResponse> mListAddress;
    private IOnClickAddress listener;
    public interface IOnClickAddress{
        void onClickEdit(AddressResponse addressResponse);
        void onClickDelete(AddressResponse addressResponse);
        void onClickSelect(AddressResponse addressResponse);
    }

    public void setListener(IOnClickAddress listener){
        this.listener = listener;
    }
    public AddressAdapter(List<AddressResponse> mListAddress) {
        this.mListAddress = mListAddress;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressBinding binding = ItemAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        AddressResponse addressResponse = mListAddress.get(position);
        holder.bind(addressResponse, listener);
    }

    @Override
    public int getItemCount() {
        if (mListAddress != null){
            return mListAddress.size();
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
                listener.onClickDelete(mListAddress.get(position));
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

    public static class AddressViewHolder extends RecyclerView.ViewHolder{
        private ItemAddressBinding binding;
        public AddressViewHolder(@NonNull ItemAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AddressResponse addressModel, IOnClickAddress listener){
            if (addressModel != null){
                binding.tvFullName.setText(addressModel.getRecipientName());
                binding.tvNumPhone.setText(addressModel.getPhoneNumber());
                String address = addressModel.getStreet() + ", " + addressModel.getWard() + ", " + addressModel.getDistrict() + ", " + addressModel.getCity();
                binding.tvAddress.setText(address);

                if (addressModel.isDefault()){
                    binding.tvIsDefault.setVisibility(View.VISIBLE);
                } else {
                    binding.tvIsDefault.setVisibility(View.GONE);
                }

                binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (listener != null){
                            listener.onClickEdit(addressModel);
                            return true;
                        }
                        return false;
                    }
                });

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null){
                            listener.onClickSelect(addressModel);
                        }
                    }
                });
            }
        }
    }

    public void updateData(List<AddressResponse> addresses){
        mListAddress.clear();
        mListAddress.addAll(addresses);
        notifyDataSetChanged();
    }
}
