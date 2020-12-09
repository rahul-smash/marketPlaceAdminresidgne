package com.signity.shopkeeperapp.network;

import com.signity.shopkeeperapp.model.AddAddressModel;
import com.signity.shopkeeperapp.model.Categories.GetCategoryResponse;
import com.signity.shopkeeperapp.model.CategoryStatus.CategoryStatus;
import com.signity.shopkeeperapp.model.CustomersModel;
import com.signity.shopkeeperapp.model.DashBoardModel;
import com.signity.shopkeeperapp.model.DeleteCategory.DeleteCategories;
import com.signity.shopkeeperapp.model.EnquiriesModel;
import com.signity.shopkeeperapp.model.GetCustomerDetailModel;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.GetStaffResponse;
import com.signity.shopkeeperapp.model.LoginModel;
import com.signity.shopkeeperapp.model.MobResponse;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.model.OrderItemResponseModel;
import com.signity.shopkeeperapp.model.OtpVerifyModel;
import com.signity.shopkeeperapp.model.Product.GetProductResponse;
import com.signity.shopkeeperapp.model.Product.ProductDetailResponse;
import com.signity.shopkeeperapp.model.Product.StoreAttributes;
import com.signity.shopkeeperapp.model.ResponseForceUpdate;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.StoresModel;
import com.signity.shopkeeperapp.model.category.AddCategoryResponse;
import com.signity.shopkeeperapp.model.category.CategoryDetailResponse;
import com.signity.shopkeeperapp.model.creatives.CreativeModel;
import com.signity.shopkeeperapp.model.creatives.SharedPostModel;
import com.signity.shopkeeperapp.model.customers.AreaCodesResp;
import com.signity.shopkeeperapp.model.customers.CustomerDataResponse;
import com.signity.shopkeeperapp.model.customers.addCustomer.AddCustomerResponse;
import com.signity.shopkeeperapp.model.customers.detail.CustomerDetailResponse;
import com.signity.shopkeeperapp.model.dashboard.StoreDashboardResponse;
import com.signity.shopkeeperapp.model.dashboard.StoreVersionDTO;
import com.signity.shopkeeperapp.model.image.ImageUploadResponse;
import com.signity.shopkeeperapp.model.market.facebookPost.FacebookPostResponse;
import com.signity.shopkeeperapp.model.market.industry.IndustryRegistration;
import com.signity.shopkeeperapp.model.market.videoCreative.VideoCreative;
import com.signity.shopkeeperapp.model.notification.NotificationModel;
import com.signity.shopkeeperapp.model.orders.CustomerData;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.model.orders.checkout.OrderCalculationResponse;
import com.signity.shopkeeperapp.model.orders.delivery.DeliverySlotDTO;
import com.signity.shopkeeperapp.model.orders.loyalty.LoyaltyPointsResponse;
import com.signity.shopkeeperapp.model.orders.offers.ApplyCouponDTO;
import com.signity.shopkeeperapp.model.orders.offers.StoreOffersResponse;
import com.signity.shopkeeperapp.model.orders.storeAddress.StoreAddressDTO;
import com.signity.shopkeeperapp.model.productStatus.ProductStatus;
import com.signity.shopkeeperapp.model.runner.AddRunnerApiResponse;
import com.signity.shopkeeperapp.model.runner.CommonResponse;
import com.signity.shopkeeperapp.model.runner.RunnerDetailResponse;
import com.signity.shopkeeperapp.model.runner.RunnersResponseDTO;
import com.signity.shopkeeperapp.model.stores.StoresResponse;
import com.signity.shopkeeperapp.model.verify.EmailVerifyResponse;
import com.signity.shopkeeperapp.model.verify.MobileOtpReponse;
import com.signity.shopkeeperapp.model.verify.OtpVerifyResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by rajesh on 5/10/15.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("/storedashboard")
    void getDashBoard(@FieldMap Map<String, String> parameters, Callback<DashBoardModel> response);

    @FormUrlEncoded
    @POST("/storeCustomers")
    void getCustomers(@FieldMap Map<String, String> parameters, Callback<CustomersModel> response);

    @FormUrlEncoded
    @POST("/storeCustomers")
    void getCustomersNew(@FieldMap Map<String, Object> parameters, Callback<CustomerDataResponse> response);

    @FormUrlEncoded
    @POST("/getCustomer")
    void getCustomerDetail(@FieldMap Map<String, String> parameters, Callback<GetCustomerDetailModel> response);

    @FormUrlEncoded
    @POST("/getCustomer")
    void getCustomerDetailNew(@FieldMap Map<String, Object> parameters, Callback<CustomerDetailResponse> response);

    @FormUrlEncoded
    @POST("/storeorders")
    void getStoreOrders(@FieldMap Map<String, String> parameters, Callback<GetOrdersModel> response);


    @FormUrlEncoded
    @POST("/storeDashboardOrders")
    void getStoreOrdersNew(@FieldMap Map<String, Object> parameters, Callback<StoreOrdersReponse> response);

    @FormUrlEncoded
    @POST("/getStoreNotification")
    void getStoreNotification(@FieldMap Map<String, Object> parameters, Callback<NotificationModel> response);

    @FormUrlEncoded
    @POST("/storeDashboardOrders")
    void getDashbaordStoreOrders(@FieldMap Map<String, Object> parameters, Callback<StoreOrdersReponse> response);

    @FormUrlEncoded
    @POST("/setOrderStatus")
    void setOrderStatus(@FieldMap Map<String, String> parameters, Callback<SetOrdersModel> response);

    @FormUrlEncoded
    @POST("/orderDetail")
    void getOrderDetail(@FieldMap Map<String, String> parameters, Callback<StoreOrdersReponse> response);

    @FormUrlEncoded
    @POST("/setOrderStatus")
    void rejectOrder(@FieldMap Map<String, String> parameters, Callback<SetOrdersModel> response);

    @FormUrlEncoded
    @POST("/setOrderItemStatus")
    void setOrderItemStatus(@FieldMap Map<String, String> parameters, Callback<OrderItemResponseModel> response);

    @FormUrlEncoded
    @POST("/setOrderStatus")
    void setOrderStatusForAll(@FieldMap Map<String, String> parameters, Callback<SetOrdersModel> response);

    @FormUrlEncoded
    @POST("/mobileVerification")
    void moblieVerification(@FieldMap Map<String, String> parameters, Callback<MobResponseLogin> response);

    @FormUrlEncoded
    @POST("/mobileVerification")
    void moblieVerificationNew(@FieldMap Map<String, String> parameters, Callback<MobileOtpReponse> response);

    @FormUrlEncoded
    @POST("/validateStaff")
    void validStaff(@FieldMap Map<String, String> parameters, Callback<MobResponseLogin> response);

    @FormUrlEncoded
    @POST("/verifyOtp")
    void otpVerify(@FieldMap Map<String, String> parameters, Callback<OtpVerifyModel> response);

    @FormUrlEncoded
    @POST("/verifyOtp")
    void otpVerifyNew(@FieldMap Map<String, String> parameters, Callback<OtpVerifyResponse> response);

    @FormUrlEncoded
    @POST("/setStoreStatus")
    void setStoreStatus(@FieldMap Map<String, String> parameters, Callback<MobResponse> response);

    @FormUrlEncoded
    @POST("/storeEnquires")
    void getStoreEnquiries(@FieldMap Map<String, String> parameters, Callback<EnquiriesModel> response);

    @FormUrlEncoded
    @POST("/adminStores")
    void getAdminStores(@FieldMap Map<String, String> parameters, Callback<StoresModel> response);

    @FormUrlEncoded
    @POST("/adminStores")
    void getAdminStoresNew(@FieldMap Map<String, String> parameters, Callback<StoresResponse> response);

    @FormUrlEncoded
    @POST("/storeStaff")
    void getstoreStaff(@FieldMap Map<String, String> parameters, Callback<GetStaffResponse> response);

    @FormUrlEncoded
    @POST("/userLogin")
    void loginVerification(@FieldMap Map<String, String> parameters, Callback<LoginModel> response);

    @FormUrlEncoded
    @POST("/userLogin")
    void loginVerificationNew(@FieldMap Map<String, String> parameters, Callback<EmailVerifyResponse> response);

    @FormUrlEncoded
    @POST("/logout")
    void logout(@FieldMap Map<String, String> parameters, Callback<LoginModel> response);

    @FormUrlEncoded
    @POST("/changePassword")
    void changePassword(@FieldMap Map<String, String> parameters, Callback<LoginModel> response);

    @FormUrlEncoded
    @POST("/forgetPassword")
    void forgetPassword(@FieldMap Map<String, String> parameters, Callback<LoginModel> response);

    @GET("/forceDownload")
    void forceDownload(Callback<ResponseForceUpdate> response);

    @FormUrlEncoded
    @POST("/storedashboard")
    void storeDashboard(@FieldMap Map<String, Integer> parameters, Callback<StoreDashboardResponse> response);

    @FormUrlEncoded
    @POST("/getCategories")
    void getCategories(@FieldMap Map<String, Object> parameters, Callback<GetCategoryResponse> response);

    @FormUrlEncoded
    @POST("/getAllProducts")
    void getAllProducts(@FieldMap Map<String, Object> parameters, Callback<GetProductResponse> response);

    @Multipart
    @POST("/uploadImages")
    void uploadImage(@Part("image") TypedFile file, Callback<ImageUploadResponse> response);

    @FormUrlEncoded
    @POST("/setCategory")
    void addCategory(@FieldMap Map<String, String> parameters, Callback<AddCategoryResponse> response);

    @FormUrlEncoded
    @POST("/setCategoryStatus")
    void setCategoryStatus(@FieldMap Map<String, Object> parameters, Callback<CategoryStatus> response);

    @FormUrlEncoded
    @POST("/delCategory")
    void delCategory(@FieldMap Map<String, Object> parameters, Callback<DeleteCategories> response);

    @FormUrlEncoded
    @POST("/delproduct")
    void delProduct(@FieldMap Map<String, Object> parameters, Callback<DeleteCategories> response);

    @FormUrlEncoded
    @POST("/setProductStatus")
    void setProductStatus(@FieldMap Map<String, Object> parameters, Callback<ProductStatus> response);

    @GET("/getStoreAttributes")
    void getStoreAttributes(Callback<StoreAttributes> response);

    @FormUrlEncoded
    @POST("/addProduct")
    void addProduct(@FieldMap Map<String, String> parameters, Callback<CategoryStatus> response);

    @POST("/publish_store")
    void publish(Callback<CategoryStatus> response);

    @FormUrlEncoded
    @POST("/productDetail")
    void getProductById(@FieldMap Map<String, String> map, Callback<ProductDetailResponse> response);

    @FormUrlEncoded
    @POST("/categoryDetail")
    void getCategoryById(@FieldMap Map<String, String> map, Callback<CategoryDetailResponse> response);

    @FormUrlEncoded
    @POST("/editCategory")
    void editCategory(@FieldMap Map<String, String> map, Callback<AddCategoryResponse> responseCallback);

    @FormUrlEncoded
    @POST("/editProduct")
    void editProduct(@FieldMap Map<String, String> productData, Callback<CategoryStatus> responseCallback);

    @FormUrlEncoded
    @POST("/addCustomer")
    void addCustomer(@FieldMap Map<String, Object> param, Callback<AddCustomerResponse> responseCallback);

    @GET("/getAreaList")
    void getAreaCodes(Callback<AreaCodesResp> responseCallback);

    @FormUrlEncoded
    @POST("/getBestProducts")
    void getBestSelling(@FieldMap Map<String, Object> param, Callback<GetProductResponse> responseCallback);

    @FormUrlEncoded
    @POST("/getCustomerByPhone")
    void checkNumber(@FieldMap Map<String, Object> param, Callback<CustomerData> response);

    @GET("/storeOffers")
    void getCoupons(Callback<StoreOffersResponse> response);

    @FormUrlEncoded
    @POST("/getLoyalityPoints")
    void getLoyalityPoints(@FieldMap Map<String, String> param, Callback<LoyaltyPointsResponse> response);

    @FormUrlEncoded
    @POST("/multiple_tax_calculation_new")
    void calculateOrder(@FieldMap Map<String, Object> param, Callback<OrderCalculationResponse> response);

    @FormUrlEncoded
    @POST("/deliveryAddress")
    void addAddressForDelivery(@FieldMap Map<String, Object> param, Callback<AddAddressModel> response);

    @FormUrlEncoded
    @POST("/placeOrder")
    void placeOrder(@FieldMap Map<String, Object> param, Callback<AddAddressModel> response);

    @FormUrlEncoded
    @POST("/pickupPlaceOrder")
    void placeOrderPickDine(@FieldMap Map<String, Object> param, Callback<AddAddressModel> response);

    @FormUrlEncoded
    @POST("/validateAllCoupons")
    void applyCoupon(@FieldMap Map<String, Object> param, Callback<ApplyCouponDTO> response);

    @FormUrlEncoded
    @POST("/storeappversions")
    void storeAppVersion(@FieldMap Map<String, Object> param, Callback<StoreVersionDTO> response);

    @GET("/storePickupAddress")
    void getStoreAddress(Callback<StoreAddressDTO> response);

    @GET("/deliveryTimeSlot")
    void getDeliverySlots(Callback<DeliverySlotDTO> response);

    @GET("/tags")
    void getCreatives(@Query("brand") long brandId, @Query("package") int packageId, @Query("valueapp_store_id") String valueappStoreId, Callback<List<CreativeModel>> response);

    @GET("/frames")
    void getFrames(@Query("valueapp_store_id") String valueappStoreId, @Query("brand") long brandId, @Query("package") int packageId, Callback<List<CreativeModel>> response);

    @GET("/tags/{id}")
    void getCreativesById(@Query("valueapp_store_id") String valueappStoreId, @Path("id") long id, @Query("brand") long brandId, Callback<CreativeModel> response);

    @GET("/frames/{id}")
    void getFramesById(@Query("valueapp_store_id") String valueappStoreId, @Path("id") long id, Callback<CreativeModel> creativeModelCallback);

    @Multipart
    @POST("/{id}/photos")
    void postFacebook(@Path("id") String pageId,
                      @Query("message") String message,
                      @Query("access_token") String accessToken,
                      @Part("") MultipartBody.Part files,
                      Callback<FacebookPostResponse> responseCallback);

    @Multipart
    @POST("/{id}/photos")
    void postScheduleFacebook(@Path("id") String pageId,
                              @Query("published") boolean value,
                              @Query("message") String message,
                              @Query("access_token") String accessToken,
                              @Query("scheduled_publish_time") int time,
                              @Part("") MultipartBody.Part files,
                              Callback<FacebookPostResponse> responseCallback);

    @GET("/token")
    void getTwilioToken(@Query("identity") String identity, Callback<String> responseCallback);

    @GET("/getRunnerList")
    void getRunners(Callback<RunnersResponseDTO> responseDTOCallback);

    @FormUrlEncoded
    @POST("/getRunnerList")
    void getRunnerById(@FieldMap Map<String, Object> param, Callback<RunnerDetailResponse> callback);

    @FormUrlEncoded
    @POST("/addRunner")
    void addRunner(@FieldMap Map<String, Object> param, Callback<AddRunnerApiResponse> callback);

    @FormUrlEncoded
    @POST("/deleteRunner")
    void deleteRunner(@FieldMap Map<String, String> params, Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/assignRunnerToOrder")
    void assignRunner(@FieldMap Map<String, String> param, Callback<CommonResponse> callback);

    @FormUrlEncoded
    @POST("/setRunnerStatus")
    void changeRunnerStatus(@FieldMap Map<String, String> params, Callback<CommonResponse> callback);

    @GET("/videos")
    void getVideoCreatives(@Query("valueapp_store_id") String valueappStoreId, @Query("brand") long brandId, @Query("package") int packageId, Callback<List<VideoCreative>> listCallback);

    @Multipart
    @POST("/{id}/videos")
    void postFacebookVideo(@Path("id") String pageId,
                           @Query("title") String title,
                           @Query("file_url") String videoUrl,
                           @Query("access_token") String accessToken,
                           @Part("") MultipartBody.Part files,
                           Callback<FacebookPostResponse> responseCallback);

    @Multipart
    @POST("/{id}/videos")
    void postScheduleFacebookVideo(@Path("id") String pageId,
                                   @Query("title") String title,
                                   @Query("file_url") String videoUrl,
                                   @Query("access_token") String accessToken,
                                   @Part("") MultipartBody.Part files,
                                   @Query("published") boolean isPublished,
                                   @Query("scheduled_publish_time") int time, Callback<FacebookPostResponse> video_scheduled);


    @FormUrlEncoded
    @POST("/stores/register")
    void registerStore(@FieldMap Map<String, String> param, Callback<IndustryRegistration> responseCallback);

    @FormUrlEncoded
    @POST("/sharedcreatives")
    void saveSharedData(@FieldMap Map<String, Object> param, Callback<CommonResponse> responseCallback);

    @GET("/sharedcreatives")
    void getSharedPosts(@Query("valueapp_store_id") String storeId, @Query("_limit") int limit, @Query("_start") int start, Callback<SharedPostModel> sharedPostModelCallback);
}
