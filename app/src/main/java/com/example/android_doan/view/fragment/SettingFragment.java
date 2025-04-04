package com.example.android_doan.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android_doan.R;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.ChangePasswordRequest;
import com.example.android_doan.data.repository.LocalRepository.DataLocalManager;
import com.example.android_doan.data.repository.RemoteRepository.SettingRepository;
import com.example.android_doan.databinding.FragmentSettingBinding;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.utils.RealPathUtil;
import com.example.android_doan.view.activity.LoginActivity;
import com.example.android_doan.viewmodel.SettingViewModel;
import com.example.android_doan.viewmodel.SettingViewModelFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SettingFragment extends Fragment {
    public static final int REQUEST_CODE = 1000;
    private FragmentSettingBinding binding;
    private SettingViewModel settingViewModel;
    private List<String> genders;
    private Uri avtUri;
    private String avatarStr = "";
    private Calendar selectedDate;

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean isGranted) {
            if(isGranted){
                openFile();
            } else{
                Toast.makeText(requireContext(), "Bạn không có quyền truy cập ảnh!", Toast.LENGTH_LONG).show();
            }
        }
    });

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK){
                Intent intent = o.getData();
                if (intent != null) {
                    avtUri = intent.getData();
                    if (avtUri != null){
                        Glide.with(requireContext())
                                .load(avtUri)
                                .error(R.drawable.ic_user)
                                .into(binding.ivAvatar);
                        Log.d("lkhai4617", "onActivityResult: " + avtUri.toString());
                    }
                }
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedDate = Calendar.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        SettingRepository repository = new SettingRepository();
        settingViewModel = new ViewModelProvider(requireActivity(), new SettingViewModelFactory(repository)).get(SettingViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        loadData();
        observer();
        setupListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    private void initUI(){
        Spinner spinnerGender = binding.spinnerGender;
        genders = new ArrayList<>();
        genders.add("MALE");
        genders.add("FEMALE");
        genders.add("OTHER");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);


        binding.edtBirthday.setKeyListener(null);
        binding.edtBirthday.setOnClickListener(v -> showDatePickerDialog());
    }

    private void loadData(){
        String userId = DataLocalManager.getUserId();
        if (userId == null || userId.isEmpty()){
            return;
        }
        settingViewModel.getUser(userId);
    }

    private void observer(){
        settingViewModel.getUserInfo().observe(getViewLifecycleOwner(), userResponse -> {
            if (userResponse != null && userResponse.getData() != null) {
                bindData(userResponse.getData());
            }
        });

        settingViewModel.getActionResult().observe(getViewLifecycleOwner(), actionResult ->{
            if (actionResult!= null){
                switch (actionResult.getStatus()){
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), actionResult.getMessage(), Toast.LENGTH_SHORT).show();
                        if (DataLocalManager.getAccessToken()== null){
                            Intent intent = new Intent(requireContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(requireContext(), actionResult.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        settingViewModel.getFileData().observe(getViewLifecycleOwner(), data ->{
            if (data != null){
                avatarStr = data.getData().getFileName();

                int userId = Integer.parseInt(DataLocalManager.getUserId());
                String fullname = binding.edtFullName.getText().toString();
                String address = binding.edtAddress.getText().toString();
                String birthday = FormatUtil.formatToIsoDate(binding.edtBirthday.getText().toString());
                String gender = binding.spinnerGender.getSelectedItem().toString();
                String phone = binding.edtPhone.getText().toString();
                String shoppingAddress = binding.edtShoppingAddress.getText().toString();

                UserModel userModel = new UserModel(userId, true, avatarStr, fullname, address, phone, gender, birthday, shoppingAddress);
                Log.d("lkhai4617", "onClick: " + avatarStr);
                settingViewModel.updateUser(userModel);
            }
        });
    }

    private void bindData(UserModel userModel){
        if (userModel.getAvatar() != null){
            avatarStr = userModel.getAvatar();
            String accessToken = DataLocalManager.getAccessToken();

            GlideUrl glideUrl = new GlideUrl("http://192.168.50.2:8080/storage/avatar/" + userModel.getAvatar(), ()-> {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            });

            Glide.with(requireContext())
                    .load(glideUrl)
                    .error(R.drawable.ic_user)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("lkhai4617", "Glide load failed: " + (e != null ? e.getMessage() : "Unknown error"));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d("lkhai4617", "Glide load success");
                            return false;
                        }
                    })
                    .into(binding.ivAvatar);
        }

        if (userModel.getFullName() != null) {
            binding.edtFullName.setText(userModel.getFullName());
        }

        if (userModel.getAddress() != null){
            binding.edtAddress.setText(userModel.getAddress());
        }

        if (userModel.getBirthday() != null){
            binding.edtBirthday.setText(FormatUtil.formatIsoDate(userModel.getBirthday()));
        }

        if (userModel.getGender() != null){
            int position = genders.indexOf(userModel.getGender());
            if (position != -1){
                binding.spinnerGender.setSelection(position);
            }
        }

        if (userModel.getPhone() != null){
            binding.edtPhone.setText(userModel.getPhone());
        }

        if (userModel.getShoppingAddress() != null){
            binding.edtShoppingAddress.setText(userModel.getShoppingAddress());
        }
    }

    private void setupListener(){
        binding.tvChangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = binding.tvChangeAccount.getText().toString();
                if (str.equals(requireContext().getResources().getString(R.string.change))){
                    binding.tvChangeAccount.setText(requireContext().getResources().getString(R.string.save));

                    binding.edtFullName.setEnabled(true);
                    binding.edtAddress.setEnabled(true);
                    binding.edtBirthday.setEnabled(true);
                    binding.spinnerGender.setEnabled(true);
                    binding.edtPhone.setEnabled(true);
                    binding.edtShoppingAddress.setEnabled(true);
                    binding.ivEdit.setVisibility(View.VISIBLE);
                } else {
                    binding.tvChangeAccount.setText(requireContext().getResources().getString(R.string.change));

                    binding.edtFullName.setEnabled(false);
                    binding.edtAddress.setEnabled(false);
                    binding.edtBirthday.setEnabled(false);
                    binding.spinnerGender.setEnabled(false);
                    binding.edtPhone.setEnabled(false);
                    binding.edtShoppingAddress.setEnabled(false);
                    binding.ivEdit.setVisibility(View.GONE);

                    if (avtUri != null){
                        callApiUploadFile();
                    } {
                        updateUser();
                    }
                }

            }
        });

        binding.tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordBottomSheetFragment fragment = ChangePasswordBottomSheetFragment.newInstance(new ChangePasswordBottomSheetFragment.IOnCLickSavePassword() {
                    @Override
                    public void onClickSavePassword(String oldPassword, String newPassword) {
                        settingViewModel.changePassword(new ChangePasswordRequest(oldPassword, newPassword));
                    }
                });
                fragment.show(requireActivity().getSupportFragmentManager(), "change_password_bottom_sheet_fragment");
            }
        });

        binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    uploadAvt();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void uploadAvt(){
        if (requireActivity().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
            openFile();
        } else {
            String[] permissions = {Manifest.permission.READ_MEDIA_IMAGES};
            requireActivity().requestPermissions(permissions, REQUEST_CODE);
        }
    }

    private void openFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        activityResultLauncher.launch(intent);
    }

    private void showDatePickerDialog() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, yearSelected, monthOfYear, dayOfMonth) -> {
                    selectedDate.set(yearSelected, monthOfYear, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String formattedDate = sdf.format(selectedDate.getTime());
                    binding.edtBirthday.setText(formattedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void callApiUploadFile(){
        String folder = "avatar";
        RequestBody requestBodyFolder = RequestBody.create(MediaType.parse("multipart/form-data"), folder);
        String realPathAvt = RealPathUtil.getRealPath(requireContext(), avtUri);
        File avatar = new File(realPathAvt);
        RequestBody requestBodyAvt = RequestBody.create(MediaType.parse("multipart/form-data"), avatar);
        MultipartBody.Part multipartBodyAvt = MultipartBody.Part.createFormData("file", avatar.getName(), requestBodyAvt);
        settingViewModel.uploadFile(requestBodyFolder, multipartBodyAvt);
    }

    private void updateUser(){
        int userId = Integer.parseInt(DataLocalManager.getUserId());
        String fullname = binding.edtFullName.getText().toString();
        String address = binding.edtAddress.getText().toString();
        String birthday = FormatUtil.formatToIsoDate(binding.edtBirthday.getText().toString());
        String gender = binding.spinnerGender.getSelectedItem().toString();
        String phone = binding.edtPhone.getText().toString();
        String shoppingAddress = binding.edtShoppingAddress.getText().toString();

        UserModel userModel = new UserModel(userId, true, avatarStr, fullname, address, phone, gender, birthday, shoppingAddress);
        Log.d("lkhai4617", "onClick: " + avatarStr);
        settingViewModel.updateUser(userModel);
    }
}