package com.signity.shopkeeperapp.orders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;

public class ViewPrintAdapter extends PrintDocumentAdapter {

    private PrintedPdfDocument mDocument;
    private Context mContext;
    private View mView;

    public ViewPrintAdapter(Context context, View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                         CancellationSignal cancellationSignal,
                         LayoutResultCallback callback, Bundle extras) {

        mDocument = new PrintedPdfDocument(mContext, newAttributes);

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                .Builder("print_output.pdf")
                .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN);

        PrintDocumentInfo info = builder.build();
        callback.onLayoutFinished(info, true);
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination,
                        CancellationSignal cancellationSignal,
                        WriteResultCallback callback) {

        // Start the page
        int range = 0;
        float newPageHeight = 0;
        for (int j = 0; j < 100; j++) {
            PdfDocument.Page page = mDocument.startPage(j);

            // Create a bitmap and put it a canvas for the view to draw to. Make it the size of the view
            Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            mView.draw(canvas);

            RectF contentRect = new RectF(page.getInfo().getContentRect());
            float pageWidth = contentRect.width();
            float pageHeight = contentRect.height();


            float scale = contentRect.width() / bitmap.getWidth();
            scale = Math.max(scale, contentRect.height() / bitmap.getHeight());
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            float scaleHeight = bitmap.getHeight() * scale;

            Log.e("TAG", "onWrite: " + newPageHeight);
            Log.e("TAG", "onWrite: " + scaleHeight);


            matrix.postTranslate(0, -newPageHeight);
            page.getCanvas().drawBitmap(bitmap, matrix, null);
            newPageHeight += pageHeight;
            range++;

/*
            // how can we fit the Rect src onto this page while maintaining aspect ratio?
            float scale = Math.min(pageWidth / src.width(), pageHeight / src.height());
            float left = pageWidth / 2 - src.width() * scale / 2;
            float top = pageHeight / 2 - src.height() * scale / 2;
            float right = pageWidth / 2 + src.width() * scale / 2;
            float bottom = pageHeight / 2 + src.height() * scale / 2;
            RectF dst = new RectF(left, top, right, bottom);

            pageCanvas.drawBitmap(bitmap, src, dst, null);*/
            mDocument.finishPage(page);

            if (newPageHeight >= scaleHeight) {
                break;
            }
        }

        try {
            mDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            mDocument.close();
            mDocument = null;
        }
        callback.onWriteFinished(new PageRange[]{new PageRange(0, range)});
    }
}