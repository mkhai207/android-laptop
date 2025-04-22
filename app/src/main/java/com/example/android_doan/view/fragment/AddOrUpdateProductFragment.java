package com.example.android_doan.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.adapter.SliderImageAdapter;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.request.CreateProductRequest;
import com.example.android_doan.data.model.request.UpdateProductRequest;
import com.example.android_doan.data.repository.RemoteRepository.ProductManagementRepository;
import com.example.android_doan.databinding.FragmentAddOrUpdateProductBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.utils.RealPathUtil;
import com.example.android_doan.viewmodel.ProductManagementViewModel;
import com.example.android_doan.viewmodel.ProductManagementViewModelFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AddOrUpdateProductFragment extends Fragment {
    private static final int REQUEST_CODE = 1000;
    public static final String PRODUCT_MODEL_KEY = "com.example.android_doan.view.fragment.PRODUCT_MODEL_KEY";
    private FragmentAddOrUpdateProductBinding binding;
    private ProductManagementViewModel productManagementViewModel;
    private ProductModel mProductModel;
    private List<BrandModel> mListBrand = new ArrayList<>();
    private List<CategoryModel> mListCategory = new ArrayList<>();
    private SliderImageAdapter sliderImageAdapter;
    private CategoryModel selectedCategory;
    private BrandModel selectedBrand;
    private Uri thumbnailUri;
    private String thumbnailUrl;
    private List<String> sliderUrls = new ArrayList<>();
    private ArrayAdapter<CategoryModel> categoryAdapter;
    private ArrayAdapter<BrandModel> brandAdapter;


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
                    thumbnailUri = intent.getData();
                    if (thumbnailUri != null){
                        Glide.with(requireContext())
                                .load(thumbnailUri)
                                .error(R.drawable.laptop_logo)
                                .into(binding.ivThumbnail);
                        Log.d("lkhai4617", "onActivityResult: " + thumbnailUri.toString());
                    }
                }
            }
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null){
            mProductModel = (ProductModel) args.getSerializable(PRODUCT_MODEL_KEY);
        }

        ProductManagementRepository productManagementRepository = new ProductManagementRepository();
        productManagementViewModel = new ViewModelProvider(
                this,
                new ProductManagementViewModelFactory(productManagementRepository)
        ).get(ProductManagementViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddOrUpdateProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        setupData();
        observer();
        setupListener();
        handleStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void initUI(){
        productManagementViewModel.getAllCategory();
        productManagementViewModel.getBrands();
        setupSliderImage();
    }

    private void setupData(){
        if (mProductModel != null){
            binding.etName.setText(mProductModel.getName());
            binding.etModel.setText(mProductModel.getModel());
            binding.etCpu.setText(mProductModel.getCpu());
            binding.etRam.setText(String.valueOf(mProductModel.getRam()));
            binding.etMemory.setText(String.valueOf(mProductModel.getMemory()));
            binding.etMemoryType.setText(mProductModel.getMemoryType());
            binding.etGpu.setText(mProductModel.getGpu());
            binding.etScreen.setText(mProductModel.getScreen());
            binding.etPrice.setText(FormatUtil.formatCurrencyVer2(mProductModel.getPrice()));
            binding.etDescription.setText(mProductModel.getDescription());
            if (mProductModel.getThumbnail() != null){
                thumbnailUrl = mProductModel.getThumbnail();
                Glide.with(requireContext())
                        .load("http://192.168.50.2:8080/storage/product/" + mProductModel.getThumbnail())
                        .error(R.drawable.laptop_logo)
                        .into(binding.ivThumbnail);
            }
            switch (mProductModel.getStatus()){
                case "0":
                    binding.switchStatus.setChecked(false);
                    break;
                case "1":
                    binding.switchStatus.setChecked(true);
                    break;
            }
            binding.etWeight.setText(String.valueOf(mProductModel.getWeight()));
            binding.etQuantity.setText(String.valueOf(mProductModel.getQuantity()));
            binding.etColor.setText(mProductModel.getColor());
            binding.etPort.setText(mProductModel.getPort());
            binding.etOs.setText(mProductModel.getOs());
            binding.etTag.setText(mProductModel.getTag());

            //setup slider anh
            if (mProductModel.getSlider() != null){
                sliderUrls = mProductModel.getSlider();
                sliderImageAdapter.updateData(sliderUrls);
            }
        }
    }

    private void observer(){
        productManagementViewModel.getBrandsLiveData().observe(getViewLifecycleOwner(), brands -> {
            if (brands != null){
                mListBrand = brands;
                setupSpinnerBrand();

                if (mProductModel.getBrand() != null){
                    int position = brandAdapter.getPosition(mProductModel.getBrand());
                    binding.spinnerBrand.setSelection(position);
                }
            }
        });

        productManagementViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null){
                mListCategory = categories;
                setupSpinnerCategory();

                if (mProductModel.getCategory() != null){
                    int position = categoryAdapter.getPosition(mProductModel.getCategory());
                    binding.spinnerCategory.setSelection(position);
                }
            }
        });

        productManagementViewModel.getFileLiveData().observe(getViewLifecycleOwner(), fileData -> {
            if (fileData != null){
                thumbnailUrl = fileData.getData().getFileName();
                updateProduct();
            }
        });
    }

    private void setupListener(){
        binding.btnUploadThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    uploadAvt();
                }
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProductModel != null){
                    if (thumbnailUri != null){
                        callApiUploadFile();
                    } else {
                        updateProduct();
                    }
                } else {
                    createProduct();
                }
            }
        });


    }

    private void handleStatus(){
        productManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null){
                switch(apiResult.getStatus()){
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()){
                            case "updateProduct":
                                requireActivity().getSupportFragmentManager().popBackStack();
                                break;
                            case "createProduct":
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

    private void setupSpinnerCategory(){
        categoryAdapter = new ArrayAdapter<CategoryModel>(
                requireContext(), android.R.layout.simple_spinner_item, mListCategory){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(getItem(position).getName());
                return textView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setText(getItem(position).getName());
                return textView;
            }
        };
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(categoryAdapter);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryModel categoryModel = (CategoryModel) adapterView.getSelectedItem();
                selectedCategory = new CategoryModel(categoryModel.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupSpinnerBrand(){
        brandAdapter = new ArrayAdapter<BrandModel>(
                requireContext(), android.R.layout.simple_spinner_item, mListBrand) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(getItem(position).getName());
                return textView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setText(getItem(position).getName());
                return textView;
            }
        };
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBrand.setAdapter(brandAdapter);

        binding.spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BrandModel brandModel = (BrandModel) adapterView.getSelectedItem();
                selectedBrand = new BrandModel(brandModel.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupSliderImage(){
        sliderUrls.clear();
        sliderImageAdapter = new SliderImageAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvSliderImages.setLayoutManager(layoutManager);
        binding.rcvSliderImages.setAdapter(sliderImageAdapter);
    }

    private void updateProduct(){
        String name = binding.etName.getText().toString().trim();
        String model = binding.etModel.getText().toString().trim();
        String cpu = binding.etCpu.getText().toString().trim();
        String ram = binding.etRam.getText().toString().trim();

        String memory = binding.etMemory.getText().toString().trim();
        String memoryType = binding.etMemoryType.getText().toString().trim();
        String gpu = binding.etGpu.getText().toString().trim();
        String screen = binding.etScreen.getText().toString().trim();

        String price = binding.etPrice.getText().toString().replaceAll("[^0-9]", "");;

        String description = binding.etDescription.getText().toString().trim();
        int status = binding.switchStatus.isChecked() ? 1 : 0;

        String weight = binding.etWeight.getText().toString().trim();

        String quantity = binding.etQuantity.getText().toString().trim();

        String color = binding.etColor.getText().toString().trim();
        String port = binding.etPort.getText().toString().trim();
        String os = binding.etOs.getText().toString().trim();
        String tag = binding.etTag.getText().toString().trim();

        String thumbnail = thumbnailUrl;
        List<String> slider = sliderUrls;

        CategoryModel category = selectedCategory;
        BrandModel brand = selectedBrand;

        UpdateProductRequest productRequest = new UpdateProductRequest(
                mProductModel.getId(),
                name,
                model,
                cpu,
                ram,
                memory,
                memoryType,
                gpu,
                screen,
                price,
                description,
                thumbnail,
                status,
                weight,
                quantity,
                tag,
                os,
                color,
                port,
                slider,
                category,
                brand
        );
        productManagementViewModel.updateProduct(productRequest);
    }

    private void createProduct(){

    }

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

    private void callApiUploadFile(){
        String folder = "product";
        RequestBody requestBodyFolder = RequestBody.create(MediaType.parse("multipart/form-data"), folder);
        String realPathAvt = RealPathUtil.getRealPath(requireContext(), thumbnailUri);
        File avatar = new File(realPathAvt);
        RequestBody requestBodyAvt = RequestBody.create(MediaType.parse("multipart/form-data"), avatar);
        MultipartBody.Part multipartBodyAvt = MultipartBody.Part.createFormData("file", avatar.getName(), requestBodyAvt);
        productManagementViewModel.uploadFile(requestBodyFolder, multipartBodyAvt);
    }
}