package com.example.android_doan.data.api.user;

import com.example.android_doan.base.BasePagingResponse;
import com.example.android_doan.base.BaseResponse;
import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.CategoryModel;
import com.example.android_doan.data.model.FileData;
import com.example.android_doan.data.model.OrderData;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.request.ChangePasswordRequest;
import com.example.android_doan.data.model.request.CreateAddressRequest;
import com.example.android_doan.data.model.request.CreateAdminOrderRequest;
import com.example.android_doan.data.model.request.CreateBrandRequest;
import com.example.android_doan.data.model.request.CreateCategoryRequest;
import com.example.android_doan.data.model.request.CreateProductRequest;
import com.example.android_doan.data.model.request.CreateUserRequest;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.request.UpdateCategoryRequest;
import com.example.android_doan.data.model.request.UpdateOrderRequest;
import com.example.android_doan.data.model.request.UpdateProductRequest;
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.model.response.OrderAdminResponse;
import com.example.android_doan.data.model.response.User;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    // api user
    @GET("api/v1/auth/account")
    Single<BaseResponse<User>> getAccount();

    @POST("api/v1/auth/logout")
    Single<BaseResponse<String>> logout();

    @GET("api/v1/users/{userId}")
    Single<BaseResponse<UserModel>> getUser(@Path("userId") String userId);

    @PUT("api/v1/users")
    Single<BaseResponse<UserModel>> updateUser(@Body UserModel user);

    @POST("api/v1/users/change-password")
    Single<BaseResponse<String>> changePassword(@Body ChangePasswordRequest request);

    @GET("api/v1/users")
    Single<BaseResponse<BasePagingResponse<UserModel>>> getAllUser(@Query("page") int page,
                                                                   @Query("size") int size,
                                                                   @Query("sort") String sort);

    @POST("api/v1/users")
    Single<BaseResponse<UserModel>> createUser(@Body CreateUserRequest request);

    @DELETE("api/v1/users/{userId}")
    Single<BaseResponse<String>> deleteUser(@Path("userId") int id);

    // product
    @GET("api/v1/products")
    Single<BaseResponse<BasePagingResponse<ProductModel>>> getAllProduct(@Query("page") int page,
                                                                         @Query("size") int size,
                                                                         @Query("sort") String sort,
                                                                         @Query("filter") String filter);

    @POST("api/v1/products")
    Single<BaseResponse<ProductModel>> createProduct(@Body CreateProductRequest request);

    @PUT("api/v1/products")
    Single<BaseResponse<ProductModel>> updateProduct(@Body UpdateProductRequest request);

    @GET("api/v1/products/{productId}")
    Single<BaseResponse<ProductModel>> getProduct(@Path("productId") String productId);

    // api cart
    @POST("api/v1/carts")
    Single<BaseResponse<String>> addToCart(@Body AddToCartRequest request);

    @GET("api/v1/carts")
    Single<GetCartResponse> getCart();

    @PUT("api/v1/carts")
    Single<BaseResponse<String>> updateQuantity(@Body AddToCartRequest request);

    @DELETE("api/v1/carts/{cartId}")
    Single<BaseResponse<String>> removeCartResponse(@Path("cartId") String cartId);

    @DELETE("api/v1/carts/clear")
    Single<BaseResponse<String>> clearCart();

    // api order
    @POST("api/v1/orders")
    Single<BaseResponse<BasePagingResponse<OrderData>>> placeOrder(@Body OrderRequest request);

    @GET("api/v1/user-orders/{userId}")
    Single<BaseResponse<BasePagingResponse<OrderData>>> getOrder(@Path("userId") String userId,
                                                                 @Query("page") int page,
                                                                 @Query("size") int size);

    @GET("api/v1/orders")
    Single<BaseResponse<BasePagingResponse<OrderAdminResponse>>> getAllOrder(@Query("filter") String filter);

    @PUT("api/v1/admin-orders")
    Single<BaseResponse<OrderAdminResponse>> updateOrder(@Body UpdateOrderRequest request);

    @PUT("api/v1/admin-orders")
    Single<BaseResponse<OrderAdminResponse>> createOrder(@Body CreateAdminOrderRequest request);

    // api upload file
    @Multipart
    @POST("api/v1/files")
    Single<BaseResponse<FileData>> uploadFile(@Part("folder") RequestBody folder,
                                              @Part MultipartBody.Part file);

    @Multipart
    @POST("api/v1/files/multiple")
    Single<BaseResponse<List<FileData>>> uploadMultipleFile(@Part("folder") RequestBody folder,
                                                            @Part List<MultipartBody.Part> files);

    // api category

    @GET("api/v1/categories")
    Single<BaseResponse<BasePagingResponse<CategoryModel>>> getAllCategories(@Query("page") int page,
                                                                             @Query("size") int size,
                                                                             @Query("sort") String sort);

    @POST("api/v1/categories")
    Single<BaseResponse<CategoryModel>> createCategory(@Body CreateCategoryRequest request);

    @DELETE("api/v1/categories/{categoryId}")
    Single<BaseResponse<String>> deleteCategory(@Path("categoryId") int categoryId);

    @PUT("api/v1/categories")
    Single<BaseResponse<CategoryModel>> updateCategory(@Body UpdateCategoryRequest request);

    @GET("api/v1/categories")
    Single<BaseResponse<BasePagingResponse<CategoryModel>>> getAllCategoriesNoPage();

    //api brand
    @GET("api/v1/brands")
    Single<BaseResponse<BasePagingResponse<BrandModel>>> getBrands(@Query("page") int page,
                                                                   @Query("size") int size,
                                                                   @Query("sort") String sort);

    @POST("api/v1/brands")
    Single<BaseResponse<BrandModel>> createBrand(@Body CreateBrandRequest request);

    @PUT("api/v1/brands")
    Single<BaseResponse<BrandModel>> updateBrand(@Body BrandModel brandModel);

    @DELETE("api/v1/brands/{brandId}")
    Single<BaseResponse<String>> deleteBrand(@Path("brandId") int id);

    // api address
    @POST("api/v1/addresses")
    Single<BaseResponse<AddressResponse>> createAddress(@Body CreateAddressRequest request);

    @GET("api/v1/addresses")
    Single<BaseResponse<BasePagingResponse<AddressResponse>>> getAddresses(@Query("filter") String filter);

    @PUT("api/v1/addresses")
    Single<BaseResponse<AddressResponse>> updateAddress(@Body AddressResponse address);

    @DELETE("api/v1/addresses/{addressId}")
    Single<BaseResponse<String>> deleteAddress(@Path("addressId") int addressId);
}
