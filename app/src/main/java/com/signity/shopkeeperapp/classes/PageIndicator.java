package com.signity.shopkeeperapp.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.signity.shopkeeperapp.R;


/**
 * Created by root on 18/12/15.
 */
public class PageIndicator extends View {


    int totalNoOfDots;
    int activeDot;
    int dotSpacing;
    int horizontalSpace = 5;
    Bitmap activeDotBitmap;
    Bitmap normalDotBitmap;
    int x = 0;
    Drawable filled, blank;

    private Paint paint;

    public PageIndicator(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activeDotBitmap = drawableToBitmap(getResources().getDrawable(R.drawable.indicator_selected));
        normalDotBitmap = drawableToBitmap(getResources().getDrawable(R.drawable.indicator_unselected));
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activeDotBitmap = drawableToBitmap(getResources().getDrawable(R.drawable.indicator_selected));
        normalDotBitmap = drawableToBitmap(getResources().getDrawable(R.drawable.indicator_unselected));

    }

    public PageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activeDotBitmap = drawableToBitmap(getResources().getDrawable(R.drawable.indicator_selected));
        normalDotBitmap = drawableToBitmap(getResources().getDrawable(R.drawable.indicator_unselected));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDot(canvas);
        super.onDraw(canvas);

    }


    private void drawDot(Canvas canvas) {
        for (int i = 0; i < totalNoOfDots; i++) {
            if (i == activeDot) {
                canvas.drawBitmap(activeDotBitmap, x, 0, paint);
                x = x + activeDotBitmap.getWidth() + horizontalSpace + dotSpacing;
            } else {
                canvas.drawBitmap(normalDotBitmap, x, 0, paint);
                x = x + normalDotBitmap.getWidth() + horizontalSpace + dotSpacing;
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (totalNoOfDots - 1) * (normalDotBitmap.getWidth() + horizontalSpace + getDotSpacing()) + (activeDotBitmap.getWidth());
        width = resolveSize(width, widthMeasureSpec);
        int height = activeDotBitmap.getHeight();
        height = resolveSize(height, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public void refersh() {
        x = 0;
        invalidate();
    }


    public int getTotalNoOfDots() {
        return totalNoOfDots;
    }

    public void setTotalNoOfDots(int totalNoOfDots) {
        this.totalNoOfDots = totalNoOfDots;
        x = 0;
        invalidate();
        requestLayout();
    }

    public int getActiveDot() {
        return activeDot;
    }

    public void setActiveDot(int activeDot) {
        this.activeDot = activeDot;
        x = 0;
        invalidate();
    }

    public int getDotSpacing() {
        return dotSpacing;
    }

    public void setDotSpacing(int dotSpacing) {
        this.dotSpacing = dotSpacing;
        x = 0;
        invalidate();
    }


    public Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
