package com.example.android_doan.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_doan.adapter.SortAdapter;
import com.example.android_doan.data.model.SortOption;
import com.example.android_doan.databinding.FragmentSortBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class SortBottomSheetFragment extends BottomSheetDialogFragment {
    private FragmentSortBottomSheetBinding binding;
    private RecyclerView rcvSortOption;
    private SortAdapter sortAdapter;
    private IOnCLickItemSortDialog listener;

    public static SortBottomSheetFragment newInstance(IOnCLickItemSortDialog listener){
        SortBottomSheetFragment fragment = new SortBottomSheetFragment();
        fragment.listener = listener;
        return fragment;
    }

    public interface IOnCLickItemSortDialog{
        void onClickItemSortDialog(SortOption sortOption);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSortBottomSheetBinding.inflate(inflater, container, false);
        rcvSortOption = binding.rcvSortOptions;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void setupData(){
        List<SortOption> sortOptions = new ArrayList<>();
        sortOptions.add(new SortOption("sold,desc", "Phổ biến"));
        sortOptions.add(new SortOption("createdAt,desc", "Hàng mới"));
        sortOptions.add(new SortOption("price,desc", "Giá: Từ cao đến thấp"));
        sortOptions.add(new SortOption("price", "Giá: Từ thấp đến cao"));

        sortAdapter = new SortAdapter(sortOptions, new SortAdapter.IOnClickItemSort() {
            @Override
            public void onClickItemSort(SortOption sortOption) {
                if (listener != null){
                    listener.onClickItemSortDialog(sortOption);
                    dismiss();
                }
            }
        });

        rcvSortOption.setAdapter(sortAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvSortOption.setLayoutManager(linearLayoutManager);

    }
}
