package com.example.android_doan.data.api.user;

import com.example.android_doan.data.model.UserModel;
import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.request.ChangePasswordRequest;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.response.AddToCartResponse;
import com.example.android_doan.data.model.response.BrandResponse;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.model.response.OrderResponse;
import com.example.android_doan.data.model.response.ProductResponse;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.AccountResponse;
import com.example.android_doan.data.model.response.UploadFileResponse;
import com.example.android_doan.data.model.response.UserResponse;

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

public interface UserApi {
    @GET("api/v1/auth/account")
    Single<AccountResponse> getAccount();

    @GET("api/v1/products")
    Single<ProductResponse> getAllProduct(@Query("page") int page,
                                          @Query("size") int size,
                                          @Query("sort") String sort,
                                          @Query("filter") String filter);

    @POST("api/v1/carts")
    Single<AddToCartResponse> addToCart(@Body AddToCartRequest request);

    @GET("api/v1/carts")
    Single<GetCartResponse> getCart();

    @PUT("api/v1/carts")
    Single<CommonResponse> updateQuantity(@Body AddToCartRequest request);

    @DELETE("api/v1/carts/{cartId}")
    Single<CommonResponse> removeCartResponse(@Path("cartId") String cartId);

    @POST("api/v1/orders")
    Single<OrderResponse> placeOrder(@Body OrderRequest request);

    @DELETE("api/v1/carts/clear")
    Single<CommonResponse> clearCart();

    @GET("api/v1/user-orders/{userId}")
    Single<OrderResponse> getOrder(@Path("userId") String userId,
                                   @Query("page") int page,
                                   @Query("size") int size);

    @GET("api/v1/brands")
    Single<BrandResponse> getBrands();

    @POST("api/v1/auth/logout")
    Single<CommonResponse> logout();

    @GET("api/v1/users/{userId}")
    Single<UserResponse> getUser(@Path("userId") String userId);

    @PUT("api/v1/users")
    Single<UserResponse> updateUser(@Body UserModel user);

    @POST("api/v1/users/change-password")
    Single<CommonResponse> changePassword(@Body ChangePasswordRequest request);

    @Multipart
    @POST("api/v1/files")
    Single<UploadFileResponse> uploadFile(@Part("folder") RequestBody folder,
                                          @Part MultipartBody.Part file);
}
