package com.signity.shopkeeperapp.network;

import com.signity.shopkeeperapp.model.CustomersModel;
import com.signity.shopkeeperapp.model.DashBoardModel;
import com.signity.shopkeeperapp.model.EnquiriesModel;
import com.signity.shopkeeperapp.model.GetCustomerDetailModel;
import com.signity.shopkeeperapp.model.GetOrdersModel;
import com.signity.shopkeeperapp.model.GetStaffResponse;
import com.signity.shopkeeperapp.model.MobResponse;
import com.signity.shopkeeperapp.model.MobResponseLogin;
import com.signity.shopkeeperapp.model.OtpVerifyModel;
import com.signity.shopkeeperapp.model.SetOrdersModel;
import com.signity.shopkeeperapp.model.StoresModel;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
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
    @POST("/setDueOrderStatus")
    void setOrderStatus(@FieldMap Map<String, String> parameters, Callback<SetOrdersModel> response);


    @FormUrlEncoded
    @POST("/setOrderStatus")
    void setOrderStatusForAll(@FieldMap Map<String, String> parameters, Callback<SetOrdersModel> response);

    @FormUrlEncoded
    @POST("/mobileVerification")
    void moblieVerification(@FieldMap Map<String, String> parameters, Callback<MobResponseLogin> response);

    @FormUrlEncoded
    @POST("/verifyOtp")
    void otpVerify(@FieldMap Map<String, String> parameters, Callback<OtpVerifyModel> response);

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
    @POST("/storeStaff")
    void getstoreStaff(@FieldMap Map<String, String> parameters, Callback<GetStaffResponse> response);


}
