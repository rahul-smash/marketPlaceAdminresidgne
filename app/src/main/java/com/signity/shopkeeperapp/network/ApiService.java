package com.signity.shopkeeperapp.network;

import com.signity.shopkeeperapp.model.CustomersModel;
import com.signity.shopkeeperapp.model.DashBoardModel;
import com.signity.shopkeeperapp.model.EnquiriesModel;
import com.signity.shopkeeperapp.model.GetCustomerDetailModel;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.GetStaffResponse;
import com.signity.shopkeeperapp.model.LoginModel;
import com.signity.shopkeeperapp.model.MobResponse;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.model.OrderItemResponseModel;
import com.signity.shopkeeperapp.model.OtpVerifyModel;
import com.signity.shopkeeperapp.model.ResponseForceUpdate;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.StoresModel;
import com.signity.shopkeeperapp.model.dashboard.StoreDashboardResponse;

import com.signity.shopkeeperapp.model.orders.StoreOrdersReponse;
import com.signity.shopkeeperapp.model.stores.StoresResponse;
import com.signity.shopkeeperapp.model.verify.MobileOtpReponse;
import com.signity.shopkeeperapp.model.verify.OtpVerifyResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

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
    void getStoreOrdersNew(@FieldMap Map<String, String> parameters, Callback<StoreOrdersReponse> response);

    @FormUrlEncoded
    @POST("/storeDashboardOrders")
    void getDashbaordStoreOrders(@FieldMap Map<String, String> parameters, Callback<StoreOrdersReponse> response);

    @FormUrlEncoded
    @POST("/setOrderStatus")
    void setOrderStatus(@FieldMap Map<String, String> parameters, Callback<SetOrdersModel> response);

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

}
