package com.signity.shopkeeperapp.network;

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
import com.signity.shopkeeperapp.model.Product.StoreAttributes;
import com.signity.shopkeeperapp.model.ResponseForceUpdate;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.StoresModel;
import com.signity.shopkeeperapp.model.category.AddCategoryResponse;
import com.signity.shopkeeperapp.model.dashboard.StoreDashboardResponse;
import com.signity.shopkeeperapp.model.image.ImageUploadResponse;
import com.signity.shopkeeperapp.model.notification.NotificationModel;
import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.model.productStatus.ProductStatus;
import com.signity.shopkeeperapp.model.stores.StoresResponse;
import com.signity.shopkeeperapp.model.verify.MobileOtpReponse;
import com.signity.shopkeeperapp.model.verify.OtpVerifyResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
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
    @POST("/getCustomer")
    void getCustomerDetail(@FieldMap Map<String, String> parameters, Callback<GetCustomerDetailModel> response);

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
    @POST("/setProductStatus")
    void setProductStatus(@FieldMap Map<String, Object> parameters, Callback<ProductStatus> response);

    @POST("/getStoreAttributes")
    void getStoreAttributes(Callback<StoreAttributes> response);
}
