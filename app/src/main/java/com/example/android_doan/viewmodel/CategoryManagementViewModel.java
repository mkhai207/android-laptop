package com.example.android_doan.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.data.model.request.CreateCategoryRequest;
import com.example.android_doan.data.model.request.UpdateCategoryRequest;
import com.example.android_doan.data.model.response.Meta;
import com.example.android_doan.data.repository.RemoteRepository.CategoryManagementRepository;
import com.example.android_doan.utils.Resource;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class CategoryManagementViewModel extends ViewModel {
    private CategoryManagementRepository categoryManagementRepository;
    private MutableLiveData<List<CategoryModel>> categoriesLiveData = new MutableLiveData<>();
    private MutableLiveData<CategoryModel> categoryLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource> apiResultLiveData = new MutableLiveData<>(new Resource(Resource.Status.SUCCESS, ""));
    private CompositeDisposable disposables = new CompositeDisposable();

    private int currentPage = 0;
    private int pages = 1;
    private int pageSize = 10;
    private List<CategoryModel> mListCategory = new ArrayList<>();

    public CategoryManagementViewModel(CategoryManagementRepository categoryManagementRepository) {
        this.categoryManagementRepository = categoryManagementRepository;
    }

    public MutableLiveData<Resource> getApiResultLiveData() {
        return apiResultLiveData;
    }

    public MutableLiveData<CategoryModel> getCategoryLiveData() {
        return categoryLiveData;
    }

    public MutableLiveData<List<CategoryModel>> getCategoriesLiveData() {
        return categoriesLiveData;
    }

    public void getAllCategories(int page) {
        if (apiResultLiveData.getValue().getStatus() == Resource.Status.LOADING || page > pages) {
            return;
        }
        apiResultLiveData.setValue(Resource.loading());
        String sort = "";
        Disposable disposable = categoryManagementRepository.getAllCategories(page, pageSize, sort)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        Meta meta = response.getData().getMeta();
                        if (meta != null) {
                            currentPage = meta.getPage();
                            pageSize = meta.getPageSize();
                            pages = meta.getPages();
                        }
                        List<CategoryModel> categories = response.getData().getResult();
                        if (categories != null) {
                            mListCategory.addAll(categories);
                            categoriesLiveData.setValue(mListCategory);
                            apiResultLiveData.setValue(Resource.success("getAllCategories"));
                        }
                        apiResultLiveData.setValue(Resource.error("getAllCategories"));
                    }
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Call api failed");
                                apiResultLiveData.setValue(Resource.error(errorMessage));
                            } catch (Exception e) {
                                apiResultLiveData.setValue(Resource.error("Unknown error"));
                            }
                        } else {
                            apiResultLiveData.setValue(Resource.error("Unknown server error"));
                        }
                    } else {
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void loadNextPage() {
        if (currentPage < pages) {
            Log.d("lkhai4617", "load next page");
            getAllCategories(currentPage + 1);
        }
    }

    public void createCategory(CreateCategoryRequest request) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = categoryManagementRepository.createCategory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 201) {
                        categoryLiveData.setValue(response.getData());
                        apiResultLiveData.setValue(Resource.success("createCategory"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("createCategory"));
                    }
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Call api failed");
                                apiResultLiveData.setValue(Resource.error(errorMessage));
                            } catch (Exception e) {
                                apiResultLiveData.setValue(Resource.error("Unknown error"));
                            }
                        } else {
                            apiResultLiveData.setValue(Resource.error("Unknown server error"));
                        }
                    } else {
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void deleteCategory(int categoryId) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = categoryManagementRepository.deleteCategory(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        apiResultLiveData.setValue(Resource.success("deleteCategory"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("deleteCategory"));
                    }
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Call api failed");
                                apiResultLiveData.setValue(Resource.error(errorMessage));
                            } catch (Exception e) {
                                apiResultLiveData.setValue(Resource.error("Unknown error"));
                            }
                        } else {
                            apiResultLiveData.setValue(Resource.error("Unknown server error"));
                        }
                    } else {
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void updateCategory(UpdateCategoryRequest request) {
        apiResultLiveData.setValue(Resource.loading());
        Disposable disposable = categoryManagementRepository.updateCategory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response != null && response.getStatusCode() == 200) {
                        apiResultLiveData.setValue(Resource.success("updateCategory"));
                    } else {
                        apiResultLiveData.setValue(Resource.error("updateCategory"));
                    }
                }, throwable -> {
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        ResponseBody errorBody = Objects.requireNonNull(httpException.response()).errorBody();
                        if (errorBody != null) {
                            try {
                                String errorJson = errorBody.string();
                                JSONObject jsonObject = new JSONObject(errorJson);
                                String errorMessage = jsonObject.optString("message", "Call api failed");
                                apiResultLiveData.setValue(Resource.error(errorMessage));
                            } catch (Exception e) {
                                apiResultLiveData.setValue(Resource.error("Unknown error"));
                            }
                        } else {
                            apiResultLiveData.setValue(Resource.error("Unknown server error"));
                        }
                    } else {
                        apiResultLiveData.setValue(Resource.error(throwable.getMessage()));
                    }
                });
        disposables.add(disposable);
    }

    public void refresh() {
        currentPage = 0;
        mListCategory.clear();
        categoriesLiveData.setValue(mListCategory);
        loadNextPage();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
