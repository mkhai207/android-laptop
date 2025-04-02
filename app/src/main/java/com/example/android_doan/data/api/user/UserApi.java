package com.example.android_doan.data.api.user;

import com.example.android_doan.data.model.request.AddToCartRequest;
import com.example.android_doan.data.model.request.OrderRequest;
import com.example.android_doan.data.model.response.AddToCartResponse;
import com.example.android_doan.data.model.response.BrandResponse;
import com.example.android_doan.data.model.response.GetCartResponse;
import com.example.android_doan.data.model.response.OrderResponse;
import com.example.android_doan.data.model.response.ProductResponse;
import com.example.android_doan.data.model.response.CommonResponse;
import com.example.android_doan.data.model.response.UserResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @GET("api/v1/auth/account")
    Single<UserResponse> getAccount();

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


}
