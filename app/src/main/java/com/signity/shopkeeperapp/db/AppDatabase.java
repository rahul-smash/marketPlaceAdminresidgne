package com.signity.shopkeeperapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.signity.shopkeeperapp.model.CustomerDetailModel;
import com.signity.shopkeeperapp.model.DashBoardModelDetail;
import com.signity.shopkeeperapp.model.OrdersListModel;
import com.signity.shopkeeperapp.model.UserModel;
import com.signity.shopkeeperapp.util.Constant;
import com.signity.shopkeeperapp.util.GsonHelper;
import com.signity.shopkeeperapp.util.Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by rajesh on 27/4/16.
 */
public class AppDatabase {

    private final String DB_VALUEZ_NAME = "dbValuezAdmin";
    private final int DB_VERSION = 1;

    private DBHelper opener;
    private SQLiteDatabase db;
    Context context;
    GsonHelper gsonHelper;
    private String TAG = AppDatabase.class.getSimpleName();


    public AppDatabase(Context context) {
        this.context = context;
        this.opener = new DBHelper(context);
        db = opener.getWritableDatabase();
        gsonHelper = new GsonHelper();
    }


    /*Set Dashboard data*/


    /*
    CREATE TABLE IF NOT EXISTS dashboard (
    store_id TEXT UNIQUE PRIMARY KEY NOT NULL,
   due_orders INTEGER DEFAULT (0),
   active_orders INTEGER DEFAULT (0),
   customers INTEGER DEFAULT (0),
   outstanding TEXT,
   store TEXT
    );
      */

    public void setDashBoard(DashBoardModelDetail dashBoardModelDetail) {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);

        try {
            ContentValues values = new ContentValues();
            values.put("store_id", storeId);
            values.put("due_orders", dashBoardModelDetail.getDueOrders());
            values.put("active_orders", dashBoardModelDetail.getActiveOrders());
            values.put("customers", dashBoardModelDetail.getCustomers());
            values.put("outstanding", String.valueOf(dashBoardModelDetail.getOutstanding()));
            values.put("store", gsonHelper.getStore(dashBoardModelDetail.getStore()));

            if (isDashBoardExist(storeId)) {
                long l = db.update("dashboard", values, "store_id=?", new String[]{storeId});
                Log.i(TAG, "--------Dashboard--------UPDATED------------");
            } else {
                long l = db.insert("dashboard", null, values);
                Log.i(TAG, "--------Dashboard--------CREATED------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isDashBoardExist(String storeId) {
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US,
                    "SELECT * FROM dashboard where store_id=%s", storeId);
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                return true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public DashBoardModelDetail getDashBoard() {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        DashBoardModelDetail dashBoardModelDetail = null;

        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US,
                    "SELECT * FROM dashboard where store_id=%s", storeId);
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                dashBoardModelDetail = new DashBoardModelDetail();
                dashBoardModelDetail.setDueOrders(cursor.getInt(1));
                dashBoardModelDetail.setActiveOrders(cursor.getInt(2));
                dashBoardModelDetail.setCustomers(cursor.getInt(3));
                dashBoardModelDetail.setOutstanding(Double.parseDouble(cursor.getString(4)));
                dashBoardModelDetail.setStore(gsonHelper.getStore(cursor.getString(5)));
                Log.e("Dashboard", dashBoardModelDetail.toString());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null)
                cursor.close();
        }
        return dashBoardModelDetail;
    }

    /* Customer data operation*/

    /*
    * CREATE TABLE IF NOT EXISTS customers (
    id TEXT UNIQUE PRIMARY KEY NOT NULL,
    store_id TEXT,
    full_name TEXT,
    phone TEXT,
    status TEXT,
    email TEXT,
    area TEXT,
    total_orders INTEGER DEFAULT (0),
    active_orders INTEGER DEFAULT (0),
    due_amount TEXT,
    paid_amount TEXT
    );
    * */


    public void setAllCustomers(List<UserModel> customers) {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);

        for (UserModel customer : customers) {
            try {

                ContentValues values = new ContentValues();
                values.put("id", customer.getId());
                values.put("store_id", storeId);
                values.put("full_name", customer.getFullName());
                values.put("phone", customer.getPhone());
                values.put("status", customer.getStatus());
                values.put("email", customer.getEmail());
                values.put("area", customer.getArea());

                if (isCustomerExit(customer.getId())) {
                    long l = db.update("customers", values, "id=? AND store_id=?", new String[]{customer.getId(), storeId});
                    Log.i(TAG, "--------Customer--------UPDATED------------");
                } else {
                    long l = db.insert("customers", null, values);
                    Log.i(TAG, "--------Customer--------CREATED------------");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public List<UserModel> getAllCustomer() {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        List<UserModel> listCustomers = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US, "SELECT * FROM customers where store_id=%s", storeId);
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                UserModel category = new UserModel();
                category.setId(cursor.getString(0));
                category.setFullName(cursor.getString(2));
                category.setPhone(cursor.getString(3));
                category.setStatus(cursor.getString(4));
                category.setEmail(cursor.getString(5));
                category.setArea(cursor.getString(6));
                cursor.moveToNext();
                listCustomers.add(category);
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        return listCustomers;
    }

    public void updateCustomer(CustomerDetailModel customerDetailModel) {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        UserModel userModel = null;
        try {
            userModel = customerDetailModel.getCustomers().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (userModel != null) {
                if (isCustomerExit(userModel.getId())) {
                    ContentValues values = new ContentValues();
                    values.put("id", userModel.getId());
                    values.put("total_orders", customerDetailModel.getTotalOrders());
                    values.put("active_orders", customerDetailModel.getActiveOrders());
                    values.put("due_amount", customerDetailModel.getDueAmount());
                    values.put("paid_amount", customerDetailModel.getPaidAmount());
                    long l = db.update("customers", values, "id=? AND store_id=?", new String[]{userModel.getId(), storeId});
                    Log.i(TAG, "--------Customer--------UPDATED------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CustomerDetailModel getCustomerDetailModel(String custId) {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        CustomerDetailModel customerDetailModel = null;

        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US,
                    "SELECT * FROM customers where store_id=%s AND id=%s", storeId, custId);
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                customerDetailModel = new CustomerDetailModel();
                customerDetailModel.setTotalOrders(cursor.getInt(7));
                customerDetailModel.setActiveOrders(cursor.getInt(8));
                customerDetailModel.setDueAmount(cursor.getString(9));
                customerDetailModel.setPaidAmount(cursor.getString(10));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null)
                cursor.close();
        }
        return customerDetailModel;

    }


    public boolean isCustomerExit(String id) {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US,
                    "SELECT * FROM customers where store_id=%s AND id=%s", storeId, id);
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                return true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

    public UserModel getCustomer(String id) {
        UserModel customer = null;
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US,
                    "SELECT * FROM customers where store_id=%s AND id=%s", storeId, id);
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                customer = new UserModel();
                customer.setId(cursor.getString(0));
                customer.setFullName(cursor.getString(2));
                customer.setPhone(cursor.getString(3));
                customer.setStatus(cursor.getString(4));
                customer.setEmail(cursor.getString(5));
                customer.setArea(cursor.getString(6));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null)
                cursor.close();
        }
        return customer;

    }



     /*--------------------- Customer data operation  ENDS HERE--------------------*/


    /*------------------------Order Operation starts here------------------------------*/

    /* DB SCHema*/
    /*
            CREATE TABLE IF NOT EXISTS orders (
            order_id TEXT UNIQUE PRIMARY KEY NOT NULL,
            store_id TEXT,
            user_id TEXT,
            status TEXT,
            customer_name TEXT,
            phone TEXT,
            time TEXT,
            note TEXT,
            discount TEXT,
            total TEXT,
            checkout TEXT,
            shipping_charges TEXT,
            coupon_code TEXT,
            address TEXT,
            total_amount TEXT,
            items TEXT
            );
    */

    public void setListOrders(List<OrdersListModel> orders) {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);

        for (OrdersListModel order : orders) {
            try {
                ContentValues values = new ContentValues();
                values.put("order_id", order.getOrderId());
                values.put("store_id", storeId);
                values.put("user_id", order.getUserId());
                values.put("status", order.getStatus());
                values.put("customer_name", order.getCustomerName());
                values.put("phone", order.getPhone());
                values.put("time", order.getTime());
                values.put("note", order.getNote());
                values.put("discount", order.getDiscount());
                values.put("total", order.getTotal());
                values.put("checkout", String.valueOf(order.getCheckout()));
                values.put("shipping_charges", String.valueOf(order.getShippingCharges()));
                values.put("address", order.getAddress());
                values.put("total_amount", String.valueOf(order.getTotalAmount()));
                values.put("items", gsonHelper.getItems(order.getItems()));

                if (isOrderExist(order.getOrderId())) {
                    long l = db.update("orders", values, "order_id=? AND store_id=?", new String[]{order.getOrderId(), storeId});
                    Log.i(TAG, "--------Order " + order.getOrderId() + "--------UPDATED------------");
                } else {
                    long l = db.insert("orders", null, values);
                    Log.i(TAG, "--------Order " + order.getOrderId() + "--------Created------------");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<OrdersListModel> getDueOrders() {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        List<OrdersListModel> orderList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US, "SELECT * FROM orders where store_id=%s AND status=%s", storeId, "0");
            cursor = db.rawQuery(sql, null);
            orderList = getOrders(cursor);
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        return orderList;
    }
    public List<OrdersListModel> getProcessingOrders() {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        List<OrdersListModel> orderList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US, "SELECT * FROM orders where store_id=%s AND status=%s", storeId, "1");
            cursor = db.rawQuery(sql, null);
            orderList = getOrders(cursor);
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        return orderList;
    }
    public List<OrdersListModel> getRejctedOrders() {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        List<OrdersListModel> orderList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US, "SELECT * FROM orders where store_id=%s AND status=%s", storeId, "2");
            cursor = db.rawQuery(sql, null);
            orderList = getOrders(cursor);
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        return orderList;
    }
    public List<OrdersListModel> getShippingOrders() {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        List<OrdersListModel> orderList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US, "SELECT * FROM orders where store_id=%s AND status=%s", storeId, "4");
            cursor = db.rawQuery(sql, null);
            orderList = getOrders(cursor);
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        return orderList;
    }
    public List<OrdersListModel> getDeliverOrders() {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        List<OrdersListModel> orderList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US, "SELECT * FROM orders where store_id=%s AND status=%s", storeId, "5");
            cursor = db.rawQuery(sql, null);
            orderList = getOrders(cursor);
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        return orderList;
    }
    public List<OrdersListModel> getCancelOrders() {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        List<OrdersListModel> orderList = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US, "SELECT * FROM orders where store_id=%s AND status=%s", storeId, "6");
            cursor = db.rawQuery(sql, null);
            orderList = getOrders(cursor);
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        return orderList;
    }


//    order_id TEXT UNIQUE PRIMARY KEY NOT NULL,
//    store_id TEXT,
//    user_id TEXT,
//    status TEXT,
//    customer_name TEXT,
//    phone TEXT,
//    time TEXT,
//    note TEXT,
//    discount TEXT,
//    total TEXT,
//    checkout TEXT,
//    shipping_charges TEXT,
//    coupon_code TEXT,
//    address TEXT,
//    total_amount TEXT,
//    items TEXT

    public List<OrdersListModel> getOrders(Cursor cursor) {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        List<OrdersListModel> listOrders = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                OrdersListModel order = new OrdersListModel();
                order.setOrderId(cursor.getString(0));
                order.setUserId(cursor.getString(2));
                order.setStatus(cursor.getString(3));
                order.setCustomerName(cursor.getString(4));
                order.setPhone(cursor.getString(5));
                order.setTime(cursor.getString(6));
                order.setNote(cursor.getString(7));
                order.setDiscount(Double.parseDouble(cursor.getString(8)));
                order.setTotal(Double.parseDouble(cursor.getString(9)));
                order.setCheckout(Double.parseDouble(cursor.getString(10)));
                order.setShippingCharges(Double.parseDouble(cursor.getString(11)));
                order.setCouponCode(cursor.getString(12));
                order.setAddress(cursor.getString(13));
                order.setTotalAmount(Double.parseDouble(cursor.getString(14)));
                order.setItems(gsonHelper.getItems(cursor.getString(15)));
                listOrders.add(order);
                cursor.moveToNext();
            }
            if (cursor != null)
                cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) cursor.close();
        }
        return listOrders;
    }


    public boolean isOrderExist(String id) {
        String storeId = Util.loadPreferenceValue(context, Constant.STORE_ID);
        Cursor cursor = null;
        try {
            String sql = String.format(Locale.US,
                    "SELECT * FROM orders where store_id=%s AND order_id=%s", storeId, id);
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                return true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null)
                cursor.close();
        }
        return false;
    }


    /*Database Helper and creation*/
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_VALUEZ_NAME, null, DB_VERSION);
        }

        // onCreate is called once if database not exists.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(this.stringFromAssets("sql/dashboard.ddl"));
            db.execSQL(this.stringFromAssets("sql/customers.ddl"));
            db.execSQL(this.stringFromAssets("sql/orders.ddl"));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("Grocers", "------Database Version : Old Version:" + oldVersion
                    + "  New Version:" + newVersion + "------------");
            // some of the store element are added so better to drop the store table and  recreate this

        }

        public String stringFromAssets(String fileName) {
            StringBuilder ReturnString = new StringBuilder();
            InputStream fIn = null;
            InputStreamReader isr = null;
            BufferedReader input = null;
            try {
                fIn = context.getResources().getAssets()
                        .open(fileName, Context.MODE_PRIVATE);
                isr = new InputStreamReader(fIn);
                input = new BufferedReader(isr);
                String line = "";
                while ((line = input.readLine()) != null) {
                    ReturnString.append(line);
                }
            } catch (Exception e) {
                e.getMessage();
            } finally {
                try {
                    if (isr != null)
                        isr.close();
                    if (fIn != null)
                        fIn.close();
                    if (input != null)
                        input.close();
                } catch (Exception e2) {
                    e2.getMessage();
                }
            }
            return ReturnString.toString();
        }


    }

}
