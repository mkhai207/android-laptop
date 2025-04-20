package com.example.android_doan.view.fragment;

import static com.example.android_doan.view.fragment.UpdateUserFragment.USER_MODEL_KEY;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android_doan.R;
import com.example.android_doan.adapter.ProductAdapter;
import com.example.android_doan.adapter.UserAdapter;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.repository.RemoteRepository.UserManagementRepository;
import com.example.android_doan.databinding.FragmentUserManagementBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.utils.GridSpacingItemDecoration;
import com.example.android_doan.viewmodel.UserManagementViewModel;
import com.example.android_doan.viewmodel.UserManagementViewModelFactory;

import java.util.ArrayList;

public class UserManagementFragment extends Fragment {
    private FragmentUserManagementBinding binding;
    private UserManagementViewModel userManagementViewModel;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserManagementRepository userManagementRepository = new UserManagementRepository();
        userManagementViewModel = new ViewModelProvider(
                this, new UserManagementViewModelFactory(userManagementRepository)
                ).get(UserManagementViewModel.class);
        Log.d("lkhai4617", "onCreate: ");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserManagementBinding.inflate(inflater, container, false);

        rcvUser = binding.rcvUser;
        userAdapter = new UserAdapter(new ArrayList<>());
        rcvUser.setAdapter(userAdapter);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvUser.setLayoutManager(linearLayoutManager);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllUser();
        setupListener();
        observer();
        handleStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        userManagementViewModel.refreshUsers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void getAllUser(){
        userManagementViewModel.loadNextPage();
        userManagementViewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()){
                userAdapter.updateData(users);
                Log.d("lkhai4617", "getAllUser: oke " + users.size());
            }
        });

        userManagementViewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), isLoading ->{
            if (isLoading){
                binding.progressBar.setVisibility(View.VISIBLE);
            }else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        rcvUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                        if (userManagementViewModel.getIsLoadingLiveData().getValue() != null && !userManagementViewModel.getIsLoadingLiveData().getValue() &&
                                (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2) {
                            userManagementViewModel.loadNextPage();
                        }
                    }
                }
            }
        });
    }

    private void setupListener(){
        binding.btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment navHostFragment =
                        (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                if (navHostFragment != null) {
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.action_userMManagementFragment_to_addUserFragment);
                }
            }
        });

        userAdapter.setListener(new UserAdapter.IOnClickUser() {
            @Override
            public void onClickItemUser() {

            }

            @Override
            public void onClickEdit(UserModel userModel) {
                Bundle args = new Bundle();
                args.putSerializable(USER_MODEL_KEY, userModel);
                NavHostFragment navHostFragment =
                        (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                if (navHostFragment != null){
                    NavController navController = navHostFragment.getNavController();
                    navController.navigate(R.id.action_userMManagementFragment_to_updateUserFragment, args);
                }
            }

            @Override
            public void onCLickDelete(UserModel userModel) {
                userManagementViewModel.deleteUser(userModel.getId());
            }
        });
    }
    private void observer(){
        userManagementViewModel.getUsersLiveData().observe(getViewLifecycleOwner(), users->{
            userAdapter.updateData(users);
        });
    }

    private void handleStatus(){
        userManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult->{
            if (apiResult != null){
                switch (apiResult.getStatus()){
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()){
                            case "deleteUser":
                                CustomToast.showToast(requireContext(), "Success", 2000);
                                break;
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }
}