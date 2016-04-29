package com.signity.shopkeeperapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.signity.shopkeeperapp.util.GsonHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by rajesh on 27/4/16.
 */
public class AppDatabase {

    private  final String DB_VALUEZ_NAME="dbValuezAdmin";
    private  final int DB_VERSION=1;

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
