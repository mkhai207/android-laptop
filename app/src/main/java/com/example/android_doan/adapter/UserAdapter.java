package com.example.android_doan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.databinding.ItemUserBinding;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserModel> mListUser;
    private IOnClickUser listener;

    public UserAdapter(List<UserModel> mListUser) {
        this.mListUser = mListUser;
    }

    public void setListener(IOnClickUser listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel userModel = mListUser.get(position);
        holder.bind(userModel, listener, holder);
    }

    @Override
    public int getItemCount() {
        if (mListUser != null) {
            return mListUser.size();
        }
        return 0;
    }

    public void updateData(List<UserModel> users) {
        mListUser.clear();
        mListUser.addAll(users);
        notifyDataSetChanged();
    }

    public interface IOnClickUser {
        void onClickItemUser();

        void onClickEdit(UserModel userModel);

        void onCLickDelete(UserModel userModel);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private ItemUserBinding binding;

        public UserViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(UserModel userModel, IOnClickUser listener, UserViewHolder holder) {
            if (userModel != null) {
                Glide.with(holder.itemView.getContext())
                        .load(userModel.getAvatar())
                        .error(R.drawable.ic_user)
                        .into(binding.ivAvatar);
                binding.tvFullName.setText(userModel.getFullName());
                binding.tvPhone.setText(userModel.getPhone());

                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onClickItemUser();
                        }
                    }
                });

                binding.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onClickEdit(userModel);
                        }
                    }
                });

                binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onCLickDelete(userModel);
                        }
                    }
                });
            }
        }
    }
}
