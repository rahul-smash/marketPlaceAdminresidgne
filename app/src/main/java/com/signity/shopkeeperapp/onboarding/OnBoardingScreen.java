package com.signity.shopkeeperapp.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.signity.shopkeeperapp.R;

/**
 * OnBoardingScreen
 *
 * @author Ketan Tetry
 */
public class OnBoardingScreen extends Fragment {
    public static final String SCREEN_ID = "SCREEN_ID";
    private ImageView imageViewTop;
    private ImageView imageViewBackground;
    private TextView textViewTitle;
    private TextView textViewSuburb;
    private int id;

    public static OnBoardingScreen getInstance(Bundle bundle) {
        OnBoardingScreen fragment = new OnBoardingScreen();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_on_boarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        getExtras();
        setUp();
    }

    private void initViews(View view) {
        imageViewTop = view.findViewById(R.id.iv_on_boarding);
        imageViewBackground = view.findViewById(R.id.iv_background_on_boarding);
        textViewTitle = view.findViewById(R.id.tv_title_on_boarding);
        textViewSuburb = view.findViewById(R.id.tv_suburb_on_boarding);
    }

    private void getExtras() {
        if (getArguments() != null) {
            id = getArguments().getInt(SCREEN_ID, 0);
        }
    }

    private void setUp() {
        OnBoarding type = OnBoarding.values()[id];

        imageViewBackground.setImageDrawable(ResourcesCompat.getDrawable(getContext().getResources(), type.getBackground(), null));
        imageViewTop.setImageDrawable(ResourcesCompat.getDrawable(getContext().getResources(), type.getMainImage(), null));

        textViewTitle.setText(type.getTitle());
        textViewSuburb.setText(type.getSuburb());
    }

    public enum OnBoarding {

        SCREEN1(R.drawable.onboard1, R.drawable.graphic1, R.string.heading1, R.string.blurb1),
        SCREEN2(R.drawable.onboard2, R.drawable.graphic2, R.string.heading2, R.string.blurb2),
        SCREEN3(R.drawable.onboard3, R.drawable.graphic3, R.string.heading3, R.string.blurb3),
        SCREEN4(R.drawable.onboard4, R.drawable.graphic4, R.string.heading4, R.string.blurb4);

        @DrawableRes
        private int background;
        @DrawableRes
        private int mainImage;
        @StringRes
        private int title;
        @StringRes
        private int suburb;

        OnBoarding(int background, int mainImage, int title, int suburb) {
            this.background = background;
            this.mainImage = mainImage;
            this.title = title;
            this.suburb = suburb;
        }

        public int getBackground() {
            return background;
        }

        public int getMainImage() {
            return mainImage;
        }

        public int getTitle() {
            return title;
        }

        public int getSuburb() {
            return suburb;
        }
    }
}
