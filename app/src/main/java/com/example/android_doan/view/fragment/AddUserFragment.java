package com.example.android_doan.view.fragment;

import static androidx.navigation.fragment.FragmentKt.findNavController;

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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.RoleModel;
import com.example.android_doan.data.model.request.CreateUserRequest;
import com.example.android_doan.data.repository.RemoteRepository.UserManagementRepository;
import com.example.android_doan.databinding.FragmentAddUserBinding;
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

public class AddUserFragment extends Fragment {
    public static final int REQUEST_CODE = 1000;
    private FragmentAddUserBinding binding;
    private UserManagementViewModel userManagementViewModel;
    private boolean isActive = false;
    private String avatarStr;
    private Uri avatarUri;
    private RoleModel selectedRole;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedDate = Calendar.getInstance();

        userManagementViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<UserManagementRepository>(new UserManagementRepository(), UserManagementViewModel.class)
        ).get(UserManagementViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSpinner();
        setupListener();
        observer();
        handleStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void observer() {
        userManagementViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        userManagementViewModel.getFileLiveData().observe(getViewLifecycleOwner(), fileData -> {
            if (fileData != null) {
                avatarStr = fileData.getFileLink();
                createUser();
            }
        });
    }

    private void setupSpinner() {
        // setup spinner role
        List<RoleModel> roles = new ArrayList<>();
        roles.add(new RoleModel(1, "Admin"));
        roles.add(new RoleModel(2, "Customer"));
        roles.add(new RoleModel(3, "Staff"));

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
    }

    private void setupListener() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (avatarUri != null) {
                    callApiUploadFile();
                } else {
                    createUser();
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
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void uploadAvt() {
        if (requireActivity().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            openFile();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }
    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        activityResultLauncher.launch(intent);
    }

    private void callApiUploadFile() {
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

    private void createUser() {
        binding.switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isActive = b;
            }
        });
        String fullName = binding.etFullName.getText().toString();
        String address = binding.etAddress.getText().toString();
        String email = binding.etEmail.getText().toString();
        String phone = binding.etPhone.getText().toString();
        String birthDay = FormatUtil.formatToIsoDate(binding.tvBirthday.getText().toString());
//        String shoppingAddress = binding.etShoppingAddress.getText().toString();
        String gender = binding.spinnerGender.getSelectedItem().toString();
        String password = binding.etPassword.getText().toString();
        CreateUserRequest request = new CreateUserRequest(email, password, fullName, address, avatarStr, birthDay, gender, phone, selectedRole);
        userManagementViewModel.createUser(request);
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
                            case "createUser":
//                                requireActivity().getSupportFragmentManager().popBackStack();
                                findNavController(this).popBackStack();
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