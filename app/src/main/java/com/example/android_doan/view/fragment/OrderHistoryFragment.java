package com.example.android_doan.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android_doan.R;
import com.example.android_doan.adapter.OrderAdapter;
import com.example.android_doan.data.enums.OrderStatusEnum;
import com.example.android_doan.data.model.response.OrderResponse;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.OrderRepository;
import com.example.android_doan.databinding.FragmentOrderHistoryBinding;
import com.example.android_doan.utils.Resource;
import com.example.android_doan.viewmodel.OrderViewModel;
import com.example.android_doan.viewmodel.OrderViewModelFactory;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderHistoryFragment extends Fragment {
    private FragmentOrderHistoryBinding binding;
    private OrderRepository repository;
    private OrderViewModel orderViewModel;
    private RecyclerView rcvOrders;
    private OrderAdapter adapter;
    private List<OrderResponse.OrderData> orders = new ArrayList<>();
    private List<OrderResponse.OrderData> filterOrder = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new OrderRepository();
        orderViewModel = new ViewModelProvider(this, new OrderViewModelFactory(repository)).get(OrderViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRcv();
        getOrders();
        setupListener();
        adapter.setListener(data -> {
            navigateOrderDetailFragment(data, view);
        });

        handleStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding =null;
    }

    private void setupRcv(){
        rcvOrders = binding.rcvOrders;
        adapter = new OrderAdapter(new ArrayList<>());
        rcvOrders.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvOrders.setLayoutManager(linearLayoutManager);
    }

    private void getOrders(){
        orderViewModel.loadNextPage();
        orderViewModel.getOrderLiveData().observe(getViewLifecycleOwner(), orderData -> {
            if (orderData != null){
                orders = orderData;
                binding.chipGroupFilters.check(R.id.chip_all);
                adapter.updateData(orders);
            }
        });


        rcvOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0){
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null){
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        if (orderViewModel.getActionResult().getValue().getStatus() != Resource.Status.LOADING &&
                                (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3) {
                            orderViewModel.loadNextPage();
                            Log.d("lkhai4617", "onScrolled: order history");
                        }
                    }
                }
            }
        });
    }

    private void setupListener(){
        binding.chipGroupFilters.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                int checkedId = checkedIds.get(0);
                handleChipGroupFilter(checkedId);
            }
        });
    }

    private void handleChipGroupFilter(int  checkedId){
        switch (checkedId){
            case R.id.chip_pending:
                filterOrders(OrderStatusEnum.PENDING);
                break;
            case R.id.chip_shipping:
                filterOrders(OrderStatusEnum.SHIPPING);
                break;
            case R.id.chip_completed:
                filterOrders(OrderStatusEnum.COMPLETED);
                break;
            case R.id.chip_canceled:
                filterOrders(OrderStatusEnum.CANCELED);
                break;
            default:
                adapter.updateData(orders);
                break;
        }
    }

    private void filterOrders(OrderStatusEnum status){
        filterOrder.clear();
        for (OrderResponse.OrderData order : orders){
            if (order.getStatus().equals(status.toString())){
                filterOrder.add(order);
            }
        }
        adapter.updateData(filterOrder);
    }

    private void handleStatus(){
        orderViewModel.getActionResult().observe(getViewLifecycleOwner(), actionResult -> {
            if (actionResult != null){
                switch (actionResult.getStatus()){
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(requireContext(), actionResult.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void navigateOrderDetailFragment(OrderResponse.OrderData data, View view){
        Bundle args = new Bundle();
        args.putSerializable(OrderDetailFragment.ORDER_DETAILS, data);

        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_orderHistoryFragment_to_orderDetailFragment, args);
    }
}