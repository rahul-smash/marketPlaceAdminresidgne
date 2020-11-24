package com.signity.shopkeeperapp.market;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.base.BaseDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by Ketan Tetry on 20/12/19.
 */
public class FacebookDialog extends BaseDialogFragment {

    public static final String TAG = FacebookDialog.class.getSimpleName();

    private TextView textViewDate;

    private TextView textViewHours;

    private TextView scheduleBtn;

    private int yearInt, dayInt, monthInt, hourOfDayInt, minuteInt;
    private PostCallback callback;
    private int time;
    private long timeStamp;
    private Calendar calendar;

    public static FacebookDialog getInstance(Bundle bundle) {
        FacebookDialog dialog = new FacebookDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_facebook_post;
    }

    @Override
    protected void setUp() {

        getExtra();

        calendar = Calendar.getInstance();
        if (timeStamp != 0) {
            calendar.setTimeInMillis(timeStamp * 1000);
        }

        yearInt = calendar.get(Calendar.YEAR);
        monthInt = calendar.get(Calendar.MONTH);
        dayInt = calendar.get(Calendar.DATE);
        hourOfDayInt = calendar.get(Calendar.HOUR_OF_DAY);
        minuteInt = calendar.get(Calendar.MINUTE);

//        textViewDate.setText(timeStamp == 0 ? CommonUtils.getCurrentDate() : CommonUtils.getFacebookDate(timeStamp));
//        textViewHours.setText(timeStamp == 0 ? CommonUtils.getCurrentTime() : CommonUtils.getFacebookTime(timeStamp));
        scheduleBtn.setText(timeStamp == 0 ? "Schedule Later" : "Re-Schedule");
    }

    private void getExtra() {
        if (getArguments() != null) {
            timeStamp = getArguments().getLong("time");
        }
    }

    void onClickDate() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getBaseActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yearInt = year;
                monthInt = month;
                dayInt = dayOfMonth;

                String date = String.format(Locale.getDefault(), "%d %d %d", dayOfMonth, month + 1, year);
                Log.d(TAG, "onDateSet: " + date);
//                textViewDate.setText(CommonUtils.getDateFrom(date));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        calendar.add(Calendar.DATE, 180);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    void onClickTime() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourOfDayInt = hourOfDay;
                minuteInt = minute;

//                textViewHours.setText(CommonUtils.getTimeFrom(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)));

                getTime();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        timePickerDialog.show();
    }

    private boolean getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearInt, monthInt, dayInt, hourOfDayInt, minuteInt, 0);
        calendar.setTimeZone(TimeZone.getDefault());
        long startTime = calendar.getTimeInMillis();

        Date date = new Date();
        int diff = Math.round((startTime - date.getTime()) / 60000);

        if (diff <= 10) {
            Toast.makeText(getBaseActivity(), "Scheduled posts need to be shared between 10 minutes and 6 months from when you create them.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            time = (int) (startTime / 1000);
            return true;
        }
    }

    public void setCallback(PostCallback callback) {
        this.callback = callback;
    }

    public void onClickPostNow() {
        if (callback != null) {
            callback.onPostNow();
            dismiss();
        }
    }

    public void onClickSchedule() {
        if (callback != null && getTime()) {
            dismiss();
            callback.onSchedulePost(time);
        } else {
            Toast.makeText(getBaseActivity(), "Scheduled posts need to be shared between 10 minutes and 6 months from when you create them.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickClose() {
        dismiss();
    }

    enum Months {
        Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
    }

    public interface PostCallback {
        void onPostNow();

        void onSchedulePost(int time);
    }
}
