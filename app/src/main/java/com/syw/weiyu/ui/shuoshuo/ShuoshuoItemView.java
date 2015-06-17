package com.syw.weiyu.ui.shuoshuo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * author: songyouwei
 * date: 2015-06-18
 * desc:
 */
public class ShuoshuoItemView extends LinearLayout implements Target {
    public ShuoshuoItemView(Context context) {
        super(context);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
