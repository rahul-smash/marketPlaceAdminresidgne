package com.signity.shopkeeperapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.signity.shopkeeperapp.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 * Created by Rajinder on 30/9/15.
 */
public class Util {

    static Context currentContext;

    public static String ReadFromfile(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
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
                returnString.append(line);
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
        return returnString.toString();
    }

    public static boolean checkIntenetConnection(Context context) {
        currentContext = context;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null)
            return info.isConnected();
        else
            return false;

/*        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null){
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return  false;*/

    }

    public static void savePreferenceValue(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREF, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String loadPreferenceValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREF, context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void saveCurrency(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREF, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value != null && !(value.isEmpty())) {
            editor.putString(Constant.STORE_CURRENCY, value);
        } else {
            editor.putString(Constant.STORE_CURRENCY, context.getString(R.string.text_rs));
        }
        editor.commit();
    }

    public static String getCurrency(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREF, context.MODE_PRIVATE);
        return sharedPreferences.getString(Constant.STORE_CURRENCY, "");
    }


    public static String unescapeJavaString(String currency) {
        StringBuilder sb = null;
        try {
            sb = new StringBuilder(currency.length());
            for (int i = 0; i < currency.length(); i++) {
                char ch = currency.charAt(i);
                if (ch == '\\') {
                    char nextChar = (i == currency.length() - 1) ? '\\' : currency
                            .charAt(i + 1);
// Octal escape?
                    if (nextChar >= '0' && nextChar <= '7') {
                        String code = "" + nextChar;
                        i++;
                        if ((i < currency.length() - 1) && currency.charAt(i + 1) >= '0'
                                && currency.charAt(i + 1) <= '7') {
                            code += currency.charAt(i + 1);
                            i++;
                            if ((i < currency.length() - 1) && currency.charAt(i + 1) >= '0'
                                    && currency.charAt(i + 1) <= '7') {
                                code += currency.charAt(i + 1);
                                i++;
                            }
                        }
                        sb.append((char) Integer.parseInt(code, 8));
                        continue;
                    }
                    switch (nextChar) {
                        case '\\':
                            ch = '\\';
                            break;
                        case 'b':
                            ch = '\b';
                            break;
                        case 'f':
                            ch = '\f';
                            break;
                        case 'n':
                            ch = '\n';
                            break;
                        case 'r':
                            ch = '\r';
                            break;
                        case 't':
                            ch = '\t';
                            break;
                        case '\"':
                            ch = '\"';
                            break;
                        case '\'':
                            ch = '\'';
                            break;
// Hex Unicode: u????
                        case 'u':
                            if (i >= currency.length() - 5) {
                                ch = 'u';
                                break;
                            }
                            int code = Integer.parseInt(
                                    "" + currency.charAt(i + 2) + currency.charAt(i + 3)
                                            + currency.charAt(i + 4) + currency.charAt(i + 5), 16);
                            sb.append(Character.toChars(code));
                            i += 5;
                            continue;
                    }
                    i++;
                }
                sb.append(ch);
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        if (sb != null) {
            return sb.toString();
        } else {
            return "";
        }

    }

    public static String getDoubleValue(Object value) {
        String s = "0.00";
        DecimalFormat f = new DecimalFormat("#0.00");
        Double localDouble = new Double(0.00);
        if (value instanceof String) {
            try {
                localDouble = Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        if (value instanceof Double) {
            localDouble = (Double) value;
        }

        try {
            s = f.format(localDouble);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
