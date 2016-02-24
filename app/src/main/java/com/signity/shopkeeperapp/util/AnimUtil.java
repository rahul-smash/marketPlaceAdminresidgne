package com.signity.shopkeeperapp.util;

import android.app.Activity;

import com.signity.shopkeeperapp.R;


public class AnimUtil {

    public static void slideFromLeftAnim(Activity context) {
        context.overridePendingTransition(R.anim.left_to_center_slide,
                R.anim.center_to_right_slide);
    }

    public static void slideFromRightAnim(Activity context) {
        context.overridePendingTransition(R.anim.right_to_center_slide,
                R.anim.center_to_left_slide);
    }

    public static void slideUpAnim(Activity context) {
        context.overridePendingTransition(R.anim.slide_up_activity,
                R.anim.no_change);
    }

    public static void slidDownAnim(Activity context) {
        context.overridePendingTransition(R.anim.no_change,
                R.anim.slide_down_activity);
    }

}
