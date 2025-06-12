package com.example.android_doan.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.RoleModel;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.repository.RemoteRepository.UserManagementRepository;
import com.example.android_doan.databinding.FragmentUpdateUserBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.utils.RealPathUtil;
import com.example.android_doan.viewmodel.UserManagementViewModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateUserFragment extends Fragment {
    public static final String USER_MODEL_KEY = "com.example.android_doan.view.fragment.USER_MODEL_KEY";
    public static final int REQUEST_CODE = 1000;
    private UserModel mUserModel;
    private UserManagementViewModel userManagementViewModel;
    private FragmentUpdateUserBinding binding;
    private RoleModel selectedRole;
    private String avatarStr;
    private Uri avatarUri;
    private boolean isActive;
    private Calendar selectedDate;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == Activity.RESULT_OK) {
                Intent intent = o.getData();
                if (intent != null) {
                    avatarUri = intent.getData();
                    if (avatarUri != null) {
                        Glide.with(requireContext())
                                .load(avatarUri)
                                .error(R.drawable.ic_user)
                                .into(binding.imgAvatar);
                        Log.d("lkhai4617", "onActivityResult: " + avatarUri.toString());
                    }
                }
            }
        }
    });
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean isGranted) {
            if (isGranted) {
                openFile();
            } else {
                Toast.makeText(requireContext(), "Bạn không có quyền truy cập ảnh!", Toast.LENGTH_LONG).show();
            }
        }
    });

    public static UpdateUserFragment newInstance(UserModel userModel) {
        UpdateUserFragment fragment = new UpdateUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_MODEL_KEY, userModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (selectedDate == null) {
            selectedDate = Calendar.getInstance();
        }

        Bundle args = getArguments();
        if (args != null) {
            mUserModel = (UserModel) args.getSerializable(USER_MODEL_KEY);
        }

        userManagementViewModel = new ViewModelProvider(
                this, new BaseViewModelFactory<>(new UserManagementRepository(), UserManagementViewModel.class)
        ).get(UserManagementViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        setupListener();
        observer();
        handleStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void initUI() {
        // setup spinner role
        List<RoleModel> roles = new ArrayList<>();
        roles.add(new RoleModel(1, "Admin role"));
        roles.add(new RoleModel(2, "Customer role"));
        roles.add(new RoleModel(3, "Nhân viên"));

        ArrayAdapter<RoleModel> adapterRole = new ArrayAdapter<RoleModel>(requireContext(), android.R.layout.simple_spinner_item, roles) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(getItem(position).getName());
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setText(getItem(position).getName());
                return textView;
            }
        };

        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRole.setAdapter(adapterRole);

        binding.spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRole = (RoleModel) adapterView.getSelectedItem();
                Log.d("lkhai4617", "onItemSelected: " + selectedRole.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // setup spinner gender
        List<String> genders = new ArrayList<>();
        genders.add("MALE");
        genders.add("FEMALE");
        genders.add("OTHER");

        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, genders);

        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGender.setAdapter(adapterGender);
        Log.d("lkhai4617", "initUI: " + mUserModel);
        if (mUserModel != null) {
            Glide.with(requireContext())
                    .load(mUserModel.getAvatar())
                    .error(R.drawable.ic_user)
                    .into(binding.imgAvatar);
            binding.etFullName.setText(mUserModel.getFullName());
            binding.etEmail.setText(mUserModel.getEmail());
            binding.etAddress.setText(mUserModel.getAddress());

            if (mUserModel.getBirthday() != null) {
                String birthDay = FormatUtil.formatIsoDate(mUserModel.getBirthday());
                if (birthDay != null) {
                    binding.tvBirthday.setText(birthDay);
                }
            }

            int positionGender = adapterGender.getPosition(mUserModel.getGender());
            binding.spinnerGender.setSelection(positionGender);
            binding.etPhone.setText(mUserModel.getPhone());

//            binding.etShoppingAddress.setText(mUserModel.getShoppingAddress());

            int positionRole = adapterRole.getPosition(mUserModel.getRole());
            binding.spinnerRole.setSelection(positionRole);
            isActive = mUserModel.isActive();
            binding.switchActive.setChecked(isActive);
        }
    }

    private void setupListener() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (avatarUri != null) {
                    callApiUploadFile();
                } else {
                    updateUser();
                }
            }
        });

        binding.btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    uploadAvt();
                }
            }
        });

        binding.tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // lang nghe thay doi switch compat
        binding.switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isActive = b;
                Log.d("lkhai4617", "onCheckedChanged: " + isActive);
            }
        });
    }

    private void observer() {
        userManagementViewModel.getFileLiveData().observe(getViewLifecycleOwner(), fileData -> {
            if (fileData != null) {
                avatarStr = fileData.getFileLink();
                updateUser();
            }
        });

        userManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), actionResult -> {
            if (actionResult != null) {
                switch (actionResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (actionResult.getMessage()) {
                            case "updateUser":
                                requireActivity().getSupportFragmentManager().popBackStack();
                                break;
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), actionResult.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void callApiUploadFile() {
//        String folder = "avatar";
//        RequestBody requestBodyFolder = RequestBody.create(MediaType.parse("multipart/form-data"), folder);
//        String realPathAvt = RealPathUtil.getRealPath(requireContext(), avatarUri);
//        File avatar = new File(realPathAvt);
//        RequestBody requestBodyAvt = RequestBody.create(MediaType.parse("multipart/form-data"), avatar);
//        MultipartBody.Part multipartBodyAvt = MultipartBody.Part.createFormData("file", avatar.getName(), requestBodyAvt);
//        userManagementViewModel.uploadFile(requestBodyFolder, multipartBodyAvt);

        String folder = "avatar";
        RequestBody requestBodyFolder = RequestBody.create(MediaType.parse("text/plain"), folder);

        String realPathAvt = RealPathUtil.getRealPath(requireContext(), avatarUri);
        File avatar = new File(realPathAvt);

        String mediaType;
        String fileName = avatar.getName().toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            mediaType = "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            mediaType = "image/png";
        } else {
            Toast.makeText(requireContext(), "Unsupported file format", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestBodyAvt = RequestBody.create(MediaType.parse(mediaType), avatar);
        MultipartBody.Part multipartBodyAvt = MultipartBody.Part.createFormData("file", avatar.getName(), requestBodyAvt);
        userManagementViewModel.uploadFile(requestBodyFolder, multipartBodyAvt);
    }

    private void updateUser() {
        int userId = mUserModel.getId();
        String fullName = binding.etFullName.getText().toString();
        String address = binding.etAddress.getText().toString();
        String birthday = FormatUtil.formatToIsoDate(binding.tvBirthday.getText().toString());
        String gender = binding.spinnerGender.getSelectedItem().toString();
        String phone = binding.etPhone.getText().toString();
//        String shoppingAddress = binding.etShoppingAddress.getText().toString();

        UserModel userModel = new UserModel(userId, isActive, avatarStr, fullName, address, phone, gender, birthday);
        userManagementViewModel.updateUser(userModel);
    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        activityResultLauncher.launch(intent);
    }

    private void uploadAvt() {
        if (requireActivity().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            openFile();
        } else {
            String[] permissions = {Manifest.permission.READ_MEDIA_IMAGES};
            requireActivity().requestPermissions(permissions, REQUEST_CODE);
        }
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
                    binding.tvBirthday.setText(formattedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void handleStatus() {
        userManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null) {
                switch (apiResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()) {
                            case "updateUser":
                                CustomToast.showToast(requireContext(), "Thành công", Toast.LENGTH_SHORT);
                                requireActivity().getSupportFragmentManager().popBackStack();
                                break;
                        }
                        break;
                    case ERROR:
                        binding.progressBar.setVisibility(View.GONE);
                        CustomToast.showToast(requireContext(), apiResult.getMessage(), 2000);
                        break;
                }
            }
        });
    }
}