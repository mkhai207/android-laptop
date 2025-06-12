package com.example.android_doan.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.android_doan.R;
import com.example.android_doan.adapter.SliderImageAdapter;
import com.example.android_doan.base.BaseViewModelFactory;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.data.model.FileData;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.request.CreateProductRequest;
import com.example.android_doan.data.model.request.UpdateProductRequest;
import com.example.android_doan.data.repository.RemoteRepository.ProductManagementRepository;
import com.example.android_doan.databinding.FragmentAddOrUpdateProductBinding;
import com.example.android_doan.utils.CustomToast;
import com.example.android_doan.utils.FormatUtil;
import com.example.android_doan.utils.RealPathUtil;
import com.example.android_doan.viewmodel.ProductManagementViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AddOrUpdateProductFragment extends Fragment {
    public static final String PRODUCT_MODEL_KEY = "com.example.android_doan.view.fragment.PRODUCT_MODEL_KEY";
    private static final int REQUEST_CODE = 1000;
    private static final int REQUEST_CODE_SLIDER = 1001;
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
    private List<Uri> sliderUris = new ArrayList<>();
    private ArrayAdapter<CategoryModel> categoryAdapter;
    private ArrayAdapter<BrandModel> brandAdapter;
    private Boolean thumbnailUploadResult = null;
    private Boolean sliderUploadResult = null;
    private int status;
    private boolean hasProcessedProduct;
    private ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if (o.getResultCode() == Activity.RESULT_OK) {
                                Intent intent = o.getData();
                                if (intent != null) {
                                    thumbnailUri = intent.getData();
                                    if (thumbnailUri != null) {
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
    private ActivityResultLauncher<Intent> activityResultLauncherSlider = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            List<Uri> sliderUriTmp = new ArrayList<>();
                            if (data.getClipData() != null) {
                                ClipData clipData = data.getClipData();
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    Uri uri = clipData.getItemAt(i).getUri();
                                    sliderUriTmp.add(uri);
                                }
                            } else if (data.getData() != null) {
                                sliderUriTmp.add(data.getData());
                                Log.d("lkhai4617", "onActivityResult: " + sliderUris.size());
                            }
                            sliderUris.addAll(sliderUriTmp);
                            sliderImageAdapter.updateDataUri(sliderUriTmp);
                        }
                    }
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mProductModel = (ProductModel) args.getSerializable(PRODUCT_MODEL_KEY);
        }

        productManagementViewModel = new ViewModelProvider(
                this,
                new BaseViewModelFactory<ProductManagementRepository>(new ProductManagementRepository(), ProductManagementViewModel.class)
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

    private void initUI() {
        productManagementViewModel.getAllCategory();
        productManagementViewModel.getBrands();
        setupSliderImage();

        binding.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                status = b ? 1 : 0;
            }
        });

        if (mProductModel == null) {
            binding.switchStatus.setVisibility(View.GONE);
        }
    }

    private void setupData() {
        if (mProductModel != null) {
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
            if (mProductModel.getThumbnail() != null) {
                thumbnailUrl = mProductModel.getThumbnail();
                Glide.with(requireContext())
                        .load(mProductModel.getThumbnail())
                        .error(R.drawable.laptop_logo)
                        .into(binding.ivThumbnail);
            }
            binding.switchStatus.setChecked(mProductModel.getStatus());
            binding.etWeight.setText(String.valueOf(mProductModel.getWeight()));
            binding.etQuantity.setText(String.valueOf(mProductModel.getQuantity()));
            binding.etColor.setText(mProductModel.getColor());
            binding.etPort.setText(mProductModel.getPort());
            binding.etOs.setText(mProductModel.getOs());
            binding.etTag.setText(mProductModel.getTag());

            //setup slider anh
            if (mProductModel.getSlider() != null) {
                sliderUrls = mProductModel.getSlider();
                sliderImageAdapter.updateData(sliderUrls);
            }
        }
    }

    private void observer() {
        productManagementViewModel.getBrandsLiveData().observe(getViewLifecycleOwner(), brands -> {
            if (brands != null) {
                mListBrand = brands;
                setupSpinnerBrand();

                if (mProductModel != null && mProductModel.getBrand() != null) {
                    int position = brandAdapter.getPosition(mProductModel.getBrand());
                    binding.spinnerBrand.setSelection(position);
                }
            }
        });

        productManagementViewModel.getCategoriesLiveData().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                mListCategory = categories;
                setupSpinnerCategory();

                if (mProductModel != null && mProductModel.getCategory() != null) {
                    int position = categoryAdapter.getPosition(mProductModel.getCategory());
                    binding.spinnerCategory.setSelection(position);
                }
            }
        });

        productManagementViewModel.getFileLiveData().observe(getViewLifecycleOwner(), fileData -> {
            if (fileData != null) {
                thumbnailUrl = fileData.getFileLink();
                thumbnailUploadResult = true;
                checkUploadStatus();
            }
        });

        productManagementViewModel.getFilesLiveData().observe(getViewLifecycleOwner(), filesData -> {
            if (filesData != null) {
                for (FileData fileData : filesData) {
                    sliderUrls.add(fileData.getFileLink());
                }
                sliderUploadResult = true;
                checkUploadStatus();
            }
        });

    }

    private void setupListener() {
        binding.btnUploadThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    uploadFile();
                }
            }
        });

        binding.btnAddSliderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    uploadMultipleFile();
                }
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedUpload();
            }
        });


    }

    private void handleStatus() {
        productManagementViewModel.getApiResultLiveData().observe(getViewLifecycleOwner(), apiResult -> {
            if (apiResult != null) {
                switch (apiResult.getStatus()) {
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        switch (apiResult.getMessage()) {
                            case "updateProduct":
                            case "createProduct":
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

    private void setupSpinnerCategory() {
        categoryAdapter = new ArrayAdapter<CategoryModel>(
                requireContext(), android.R.layout.simple_spinner_item, mListCategory) {
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

    private void setupSpinnerBrand() {
        brandAdapter = new ArrayAdapter<BrandModel>(
                requireContext(), android.R.layout.simple_spinner_item, mListBrand) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setText(Objects.requireNonNull(getItem(position)).getName());
                return textView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
                textView.setText(Objects.requireNonNull(getItem(position)).getName());
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
                // TODO document why this method is empty
            }
        });
    }

    private void setupSliderImage() {
        sliderUrls.clear();
        sliderImageAdapter = new SliderImageAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvSliderImages.setLayoutManager(layoutManager);
        binding.rcvSliderImages.setAdapter(sliderImageAdapter);
    }

    private CreateProductRequest getProductRequestData() {
        return new CreateProductRequest(
                binding.etName.getText().toString().trim(),
                binding.etModel.getText().toString().trim(),
                binding.etCpu.getText().toString().trim(),
                binding.etRam.getText().toString().trim(),
                binding.etMemory.getText().toString().trim(),
                binding.etMemoryType.getText().toString().toUpperCase(Locale.ROOT).trim(),
                binding.etGpu.getText().toString().trim(),
                binding.etScreen.getText().toString().trim(),
                binding.etPrice.getText().toString().replaceAll("[^0-9]", ""),
                binding.etDescription.getText().toString().trim(),
                thumbnailUrl,
                binding.etWeight.getText().toString().trim(),
                binding.etQuantity.getText().toString().trim(),
                binding.etColor.getText().toString().trim(),
                binding.etPort.getText().toString().trim(),
                binding.etOs.getText().toString().trim(),
                binding.etTag.getText().toString().trim(),
                sliderUrls,
                selectedCategory,
                selectedBrand
        );
    }

    private void updateProduct() {
//        String name = binding.etName.getText().toString().trim();
//        String model = binding.etModel.getText().toString().trim();
//        String cpu = binding.etCpu.getText().toString().trim();
//        String ram = binding.etRam.getText().toString().trim();
//
//        String memory = binding.etMemory.getText().toString().trim();
//        String memoryType = binding.etMemoryType.getText().toString().trim();
//        String gpu = binding.etGpu.getText().toString().trim();
//        String screen = binding.etScreen.getText().toString().trim();
//
//        String price = binding.etPrice.getText().toString().replaceAll("[^0-9]", "");
//
//        String description = binding.etDescription.getText().toString().trim();
//
//        String weight = binding.etWeight.getText().toString().trim();
//
//        String quantity = binding.etQuantity.getText().toString().trim();
//
//        String color = binding.etColor.getText().toString().trim();
//        String port = binding.etPort.getText().toString().trim();
//        String os = binding.etOs.getText().toString().trim();
//        String tag = binding.etTag.getText().toString().trim();
//
//        String thumbnail = thumbnailUrl;
//        List<String> slider = sliderUrls;
//
//        CategoryModel category = selectedCategory;
//        BrandModel brand = selectedBrand;
//
//        UpdateProductRequest productRequest = new UpdateProductRequest(
//                mProductModel.getId(),
//                name,
//                model,
//                cpu,
//                ram,
//                memory,
//                memoryType,
//                gpu,
//                screen,
//                price,
//                description,
//                thumbnail,
//                status,
//                weight,
//                quantity,
//                tag,
//                os,
//                color,
//                port,
//                slider,
//                category,
//                brand
//        );
        CreateProductRequest data = getProductRequestData();
        UpdateProductRequest request = new UpdateProductRequest(
                mProductModel.getId(),
                data.getName(), data.getModel(), data.getCpu(), data.getRam(), data.getMemory(), data.getMemoryType(),
                data.getGpu(), data.getScreen(), data.getPrice(), data.getDescription(), data.getThumbnail(),
                status,
                data.getWeight(), data.getQuantity(), data.getTag(), data.getOs(), data.getColor(), data.getPort(),
                data.getSlider(), data.getCategory(), data.getBrand()
        );
        productManagementViewModel.updateProduct(request);
    }

    private void createProduct() {
//        String name = binding.etName.getText().toString().trim();
//        String model = binding.etModel.getText().toString().trim();
//        String cpu = binding.etCpu.getText().toString().trim();
//        String ram = binding.etRam.getText().toString().trim();
//
//        String memory = binding.etMemory.getText().toString().trim();
//        String memoryType = binding.etMemoryType.getText().toString().trim();
//        String gpu = binding.etGpu.getText().toString().trim();
//        String screen = binding.etScreen.getText().toString().trim();
//
//        String price = binding.etPrice.getText().toString().replaceAll("[^0-9]", "");
//        ;
//
//        String description = binding.etDescription.getText().toString().trim();
//        String weight = binding.etWeight.getText().toString().trim();
//
//        String quantity = binding.etQuantity.getText().toString().trim();
//
//        String color = binding.etColor.getText().toString().trim();
//        String port = binding.etPort.getText().toString().trim();
//        String os = binding.etOs.getText().toString().trim();
//        String tag = binding.etTag.getText().toString().trim();
//
//        String thumbnail = thumbnailUrl;
//        List<String> slider = sliderUrls;
//
//        CategoryModel category = selectedCategory;
//        BrandModel brand = selectedBrand;

//        this.name = name;
//        this.model = model;
//        this.cpu = cpu;
//        this.ram = ram;
//        this.memory = memory;
//        this.memoryType = memoryType;
//        this.gpu = gpu;
//        this.screen = screen;
//        this.price = price;
//        this.description = description;
//        this.thumbnail = thumbnail;
//        this.weight = weight;
//        this.quantity = quantity;
//        this.color = color;
//        this.port = port;
//        this.os = os;
//        this.tag = tag;
//        this.slider = slider;
//        this.category = category;
//        this.brand = brand;

//        CreateProductRequest createProductRequest = new CreateProductRequest(
//                name,
//                model,
//                cpu,
//                ram,
//                memory,
//                memoryType,
//                gpu,
//                screen,
//                price,
//                description,
//                thumbnail,
//                weight,
//                quantity,
//                color,
//                port,
//                os,
//                tag,
//                slider,
//                category,
//                brand
//        );
        CreateProductRequest createProductRequest = getProductRequestData();

        productManagementViewModel.createProduct(createProductRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void uploadFile() {
        if (requireActivity().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            openFile();
        } else {
//            String[] permissions = {Manifest.permission.READ_MEDIA_IMAGES};
//            requireActivity().requestPermissions(permissions, REQUEST_CODE);
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }
    }

    private void openFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        activityResultLauncher.launch(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void uploadMultipleFile() {
        if (requireActivity().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            openFilesForSlider();
        } else {
//            String[] permissions = {Manifest.permission.READ_MEDIA_IMAGES};
//            requireActivity().requestPermissions(permissions, REQUEST_CODE_SLIDER);
            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        }
    }

    private void openFilesForSlider() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        activityResultLauncherSlider.launch(intent);
    }

    private void callApiUploadFile() {
        String folder = "product";
        RequestBody requestBodyFolder = RequestBody.create(MediaType.parse("text/plain"), folder);

        String realPathAvt = RealPathUtil.getRealPath(requireContext(), thumbnailUri);
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
        productManagementViewModel.uploadFile(requestBodyFolder, multipartBodyAvt);
    }

    private void callApiUploadMultipleFile(List<Uri> sliderUris) {
        String folder = "product";
        RequestBody requestBodyFolder = RequestBody.create(MediaType.parse("text/plain"), folder);

        List<MultipartBody.Part> imageParts = new ArrayList<>();

        for (Uri uri : sliderUris) {
            String realPath = RealPathUtil.getRealPath(requireContext(), uri);
            File imageFile = new File(realPath);

            String mediaType;
            String fileName = imageFile.getName().toLowerCase();
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                mediaType = "image/jpeg";
            } else if (fileName.endsWith(".png")) {
                mediaType = "image/png";
            } else {
                Toast.makeText(requireContext(), "Unsupported file format: " + fileName, Toast.LENGTH_SHORT).show();
                continue;
            }

            RequestBody requestBodyImage = RequestBody.create(MediaType.parse(mediaType), imageFile);

            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("files", imageFile.getName(), requestBodyImage);
            imageParts.add(imagePart);
        }

        if (imageParts.isEmpty()) {
            Toast.makeText(requireContext(), "No valid images to upload", Toast.LENGTH_SHORT).show();
            return;
        }

        productManagementViewModel.uploadMultipleFile(requestBodyFolder, imageParts);
    }

    private void proceedUpload() {
        hasProcessedProduct = false;
        if (thumbnailUri != null) {
            callApiUploadFile();
        }
        if (!sliderUris.isEmpty()) {
            callApiUploadMultipleFile(sliderUris);
        }

        if (thumbnailUri == null && sliderUris.isEmpty()) {
            checkUploadStatus();
        }
    }

    private void checkUploadStatus() {
        if (hasProcessedProduct) {
            return;
        }

        boolean canProceed = true;

        if (thumbnailUri != null) {
            if (thumbnailUploadResult == null) {
                canProceed = false;
            } else if (Boolean.FALSE.equals(thumbnailUploadResult)) {
                canProceed = false;
                CustomToast.showToast(requireContext(), "Lỗi upload ảnh", Toast.LENGTH_SHORT);
            }
        }

        if (!sliderUris.isEmpty()) {
            if (sliderUploadResult == null) {
                canProceed = false;
            } else if (Boolean.FALSE.equals(sliderUploadResult)) {
                canProceed = false;
                Toast.makeText(requireContext(), "Lỗi upload ảnh slider", Toast.LENGTH_SHORT).show();
            }
        }

        if (canProceed) {
            hasProcessedProduct = true;

            if (thumbnailUri != null && sliderUris.isEmpty()) {
                Log.d("lkhai4617", "checkUploadStatus: Only thumbnail uploaded");
            } else if (thumbnailUri == null && !sliderUris.isEmpty()) {
                Log.d("lkhai4617", "checkUploadStatus: Only slider uploaded");
            } else if (thumbnailUri != null) {
                Log.d("lkhai4617", "checkUploadStatus: Both thumbnail and slider uploaded");
            } else {
                Log.d("lkhai4617", "checkUploadStatus: No upload required");
            }

            if (mProductModel != null) {
                updateProduct();
            } else {
                createProduct();
            }

            thumbnailUploadResult = null;
            sliderUploadResult = null;
            thumbnailUri = null;
            sliderUris.clear();
        }
    }
}