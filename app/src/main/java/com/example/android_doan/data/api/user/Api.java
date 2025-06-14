package com.example.android_doan.data.api.user;

import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.ProductModel;
import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.request.ChangePasswordRequest;
import com.example.android_doan.data.model.request.CreateAddressRequest;
import com.example.android_doan.data.model.request.CreateBrandRequest;
import com.example.android_doan.data.model.request.CreateCategoryRequest;
import com.example.android_doan.data.model.request.CreateProductRequest;
import com.example.android_doan.data.model.request.CreateUserRequest;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.request.UpdateCategoryRequest;
import com.example.android_doan.data.model.request.UpdateOrderRequest;
import com.example.android_doan.data.model.request.UpdateProductRequest;
import com.example.android_doan.data.model.response.AddToCartResponse;
import com.example.android_doan.data.model.response.AddressResponse;
import com.example.android_doan.data.model.response.BrandResponse;
import com.example.android_doan.data.model.response.CreateAddressResponse;
import com.example.android_doan.data.model.response.CreateBrandResponse;
import com.example.android_doan.data.model.response.CreateCategoryResponse;
import com.example.android_doan.data.model.response.CreateProductResponse;
import com.example.android_doan.data.model.response.CreateUserResponse;
import com.example.android_doan.data.model.response.GetAddressResponse;
import com.example.android_doan.data.model.response.GetAllCategoriesResponse;
import com.example.android_doan.data.model.response.GetAllOrderResponse;
import com.example.android_doan.data.model.response.GetAllUserResponse;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.model.response.GetProductResponse;
import com.example.android_doan.data.model.response.OrderAdminResponse;
import com.example.android_doan.data.model.response.OrderResponse;
import com.example.android_doan.data.model.response.ProductResponse;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.AccountResponse;
import com.example.android_doan.data.model.response.UpdateOrderResponse;
import com.example.android_doan.data.model.response.UploadFileResponse;
import com.example.android_doan.data.model.response.UploadMultipleFileResponse;
import com.example.android_doan.data.model.response.UserResponse;
import com.google.android.gms.common.internal.service.Common;

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
    Single<AccountResponse> getAccount();

    @POST("api/v1/auth/logout")
    Single<CommonResponse> logout();

    @GET("api/v1/users/{userId}")
    Single<UserResponse> getUser(@Path("userId") String userId);

    @PUT("api/v1/users")
    Single<UserResponse> updateUser(@Body UserModel user);

    @POST("api/v1/users/change-password")
    Single<CommonResponse> changePassword(@Body ChangePasswordRequest request);

    @GET("api/v1/users")
    Single<GetAllUserResponse> getAllUser(@Query("page") int page,
                                          @Query("size") int size,
                                          @Query("sort") String sort);
    @POST("api/v1/users")
    Single<CreateUserResponse> createUser(@Body CreateUserRequest request);

    @DELETE("api/v1/users/{userId}")
    Single<CommonResponse> deleteUser(@Path("userId") int id);

    // product
    @GET("api/v1/products")
    Single<ProductResponse> getAllProduct(@Query("page") int page,
                                          @Query("size") int size,
                                          @Query("sort") String sort,
                                          @Query("filter") String filter);

    @POST("api/v1/products")
    Single<CreateProductResponse> createProduct(@Body CreateProductRequest request);

    @PUT("api/v1/products")
    Single<CreateProductResponse> updateProduct(@Body UpdateProductRequest request);

    @GET("api/v1/products/{productId}")
    Single<GetProductResponse> getProduct(@Path("productId") String productId);

    // api cart
    @POST("api/v1/carts")
    Single<AddToCartResponse> addToCart(@Body AddToCartRequest request);

    @GET("api/v1/carts")
    Single<GetCartResponse> getCart();

    @PUT("api/v1/carts")
    Single<CommonResponse> updateQuantity(@Body AddToCartRequest request);

    @DELETE("api/v1/carts/{cartId}")
    Single<CommonResponse> removeCartResponse(@Path("cartId") String cartId);

    @DELETE("api/v1/carts/clear")
    Single<CommonResponse> clearCart();

    // api order
    @POST("api/v1/orders")
    Single<OrderResponse> placeOrder(@Body OrderRequest request);

    @GET("api/v1/user-orders/{userId}")
    Single<OrderResponse> getOrder(@Path("userId") String userId,
                                   @Query("page") int page,
                                   @Query("size") int size);

    @GET("api/v1/orders")
    Single<GetAllOrderResponse> getAllOrder(@Query("filter") String filter);

    @PUT("api/v1/admin-orders")
    Single<UpdateOrderResponse> updateOrder(@Body UpdateOrderRequest request);

    // api upload file
    @Multipart
    @POST("api/v1/files")
    Single<UploadFileResponse> uploadFile(@Part("folder") RequestBody folder,
                                          @Part MultipartBody.Part file);

    @Multipart
    @POST("api/v1/files/multiple")
    Single<UploadMultipleFileResponse> uploadMultipleFile(@Part("folder") RequestBody folder,
                                                          @Part List<MultipartBody.Part> files);

    // api category
    @GET("api/v1/categories")
    Single<GetAllCategoriesResponse> getAllCategories(@Query("page") int page,
                                                      @Query("size") int size,
                                                      @Query("sort") String sort);

    @POST("api/v1/categories")
    Single<CreateCategoryResponse> createCategory(@Body CreateCategoryRequest request);

    @DELETE("api/v1/categories/{categoryId}")
    Single<CommonResponse> deleteCategory(@Path("categoryId") int categoryId);

    @PUT("api/v1/categories")
    Single<CreateCategoryResponse> updateCategory(@Body UpdateCategoryRequest request);

    @GET("api/v1/categories")
    Single<GetAllCategoriesResponse> getAllCategoriesNoPage();

    //api brand
    @GET("api/v1/brands")
    Single<BrandResponse> getBrands();
    @POST("api/v1/brands")
    Single<CreateBrandResponse> createBrand(@Body CreateBrandRequest request);

    @PUT("api/v1/brands")
    Single<CreateBrandResponse> updateBrand(@Body BrandModel brandModel);

    @DELETE("api/v1/brands/{brandId}")
    Single<CommonResponse> deleteBrand(@Path("brandId") int id);

    // api address
    @POST("api/v1/addresses")
    Single<CreateAddressResponse> createAddress(@Body CreateAddressRequest request);

    @GET("api/v1/addresses")
    Single<GetAddressResponse> getAddresses(@Query("filter") String filter);

    @PUT("api/v1/addresses")
    Single<CreateAddressResponse> updateAddress(@Body AddressResponse address);

    @DELETE("api/v1/addresses/{addressId}")
    Single<CommonResponse> deleteAddress(@Path("addressId") int addressId);
}
