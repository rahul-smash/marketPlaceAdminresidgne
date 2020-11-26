package com.signity.shopkeeperapp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.signity.shopkeeperapp.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rajinder on 30/9/15.
 */
public class Util {

    private static final String TAG = "Util";
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

    public static String getPriceWithCurrency(double price, String currency) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance(currency));
        return format.format(price);
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


    public static DisplayMetrics getDisplayMatric(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(metrics);
        Log.e("Dimension", metrics.widthPixels + "*" + metrics.heightPixels);
        return metrics;
    }


    @SuppressLint("NewApi")
    private static int getSoftButtonsBarHeight(Context context) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static String getCurrencySymbol(String currencyCode) {
        try {
            Currency currency = Currency.getInstance(currencyCode);
            return currency.getSymbol();
        } catch (Exception e) {
            return currencyCode;
        }
    }

    public static String getOrderTime(String time) {
        String output = "";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            Date date = inputFormat.parse(time);
            if (date != null) {
                output = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static String getOrderDate(String format) {
        String output = "";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(format);
            if (date != null) {
                output = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }


    public static String getDeliverySlotDate(String format) {
        String output = "";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(format);
            if (date != null) {
                output = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static String getDeliverySlotDate1(String format) {
        String output = "";

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = inputFormat.parse(format);
            if (date != null) {
                output = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static boolean checkValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        boolean isValid = false;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        isValid = matcher.matches();
        return isValid;
    }

    public static String getTimeFrom(String format) {
        String output = "";

        SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            Date date = inputFormat.parse(format);
            if (date != null) {
                output = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static File saveImageFilePng(Bitmap bitmap, String filename, File storage) {
        bitmap.setHasAlpha(true);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        File file = new File(storage, filename.concat(".png"));

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File saveImageFile(Bitmap bitmap, String filename, File storage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File file = new File(storage, filename.concat(".jpg"));
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File saveImageFile(Bitmap bitmap, String filename, File storage, int quality) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bytes);
        File file = new File(storage, filename.concat(".jpg"));
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getDateFrom(String format) {
        String output = "";

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(format);
            if (date != null) {
                output = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static File getUserImage(String folder, String imageType) {
        return new File(getUserFileDirectory(folder), imageType.concat(".jpg"));
    }

    public static File getUserFileDirectory(String folder) {
        File file = new File(createDefaultStorage(), folder);
        if (!file.exists() && file.mkdir()) {
            Log.d(TAG, "getUserFileDirectory: " + folder + " Created");
        }
        return file;
    }

    public static File createDefaultStorage() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.APP_TITLE);
        if (!dir.exists() && dir.mkdirs()) {
            Log.d(TAG, "createDefaultStorage: " + "Directory Created");
        }
        return dir;
    }

    public static Bitmap decodeBitmap(File imageFile, int targetW, int targetH) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
    }
}
