package com.signity.shopkeeperapp.enquiries;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.signity.shopkeeperapp.R;
import com.signity.shopkeeperapp.util.AnimUtil;

import static android.content.Intent.ACTION_DIAL;

/**
 * Created by rajesh on 24/12/15.
 */
public class EnquiriesDetailFragment extends Fragment {


    String name;
    String message;
    String city;
    String email;
    String phoneNumber;
    String bookingDate;

    TextView mCustomerFullName, mCustomerAddress, mCustomerEmail, mCustomerContact, mCustomerMessage,mCustomerBookingDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = getArguments().getString("name");
        message = getArguments().getString("message");
        city = getArguments().getString("city");
        email = getArguments().getString("email");
        phoneNumber = getArguments().getString("phone");
        bookingDate = getArguments().getString("booking");
    }

    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                EnquiriesDetailFragment.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_enquiries_detail, container, false);
        initialization(rootView);
        handleBackButton(rootView);
        handleCallButton(rootView);
        setHeader(rootView);
        return rootView;
    }

    private void initialization(View rootView) {

        mCustomerFullName = (TextView) rootView.findViewById(R.id.txtName);
        mCustomerAddress = (TextView) rootView.findViewById(R.id.txtAddress);
        mCustomerEmail = (TextView) rootView.findViewById(R.id.txtEmail);
        mCustomerContact = (TextView) rootView.findViewById(R.id.txtContact);
        mCustomerBookingDate = (TextView) rootView.findViewById(R.id.txtBookingDate);

        mCustomerMessage = (TextView) rootView.findViewById(R.id.txtMessageVal);
    }

    private void handleCallButton(View rootView) {

        ((Button) rootView.findViewById(R.id.btnCall)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAlert();
            }
        });
    }

    public void handleBackButton(View rootView) {
        ((Button) rootView.findViewById(R.id.backButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                AnimUtil.slideFromLeftAnim(getActivity());

            }
        });
    }

    public void setHeader(View rootView) {
        if (name.equals(null) || name.equalsIgnoreCase("")) {
            ((TextView) rootView.findViewById(R.id.textTitle)).setText("Guest User");
        } else {
            ((TextView) rootView.findViewById(R.id.textTitle)).setText(name);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (name.equals(null) || name.equalsIgnoreCase("")) {
            mCustomerFullName.setText("Guest User");
        } else {
            mCustomerFullName.setText(name);
        }

        if (city.equals(null) || city.equalsIgnoreCase("")) {
            mCustomerAddress.setText("");
        } else {
            mCustomerAddress.setText("" + city);
        }

        if (email.equals(null) || email.equalsIgnoreCase("")) {
            mCustomerEmail.setText("");
        } else {
            mCustomerEmail.setText(email);
        }

        if (phoneNumber.equals(null) || phoneNumber.equalsIgnoreCase("")) {
            mCustomerContact.setText("");
        } else {
            mCustomerContact.setText(phoneNumber);
        }

        if (message.equals(null) || message.equalsIgnoreCase("")) {
            mCustomerMessage.setText("");
        } else {
            mCustomerMessage.setText(message);
        }

        if (bookingDate.equals(null) || bookingDate.equalsIgnoreCase("")) {
            mCustomerBookingDate.setText("");
        } else {
            mCustomerBookingDate.setText(bookingDate);
        }


    }


    private void callAlert() {

        androidx.appcompat.app.AlertDialog.Builder adb = new androidx.appcompat.app.AlertDialog.Builder(getActivity());


        adb.setTitle("Call " + phoneNumber + " ?");


        adb.setIcon(R.drawable.ic_launcher);


        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                actionCall();


            }
        });


        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }
    private void actionCall() {

        try {
            PackageManager pm = getContext().getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                Intent intent = new Intent(ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
                AnimUtil.slideFromRightAnim(getActivity());
            } else {
                Toast.makeText(getActivity(), "Your device is not supporting any calling feature", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  /*  private void actionCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
        AnimUtil.slideFromLeftAnim(getActivity());
    }*/

}
