package com.syw.weiyu.ui.shuoshuo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.syw.weiyu.R;

/**
 * author: songyouwei
 * date: 2015-06-17
 * desc:
 */
public class ImgViewerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_imgviewer);
        String imgUrl = getIntent().getStringExtra("imgUrl");
        ImageView img = (ImageView)this.findViewById(R.id.large_image );
        Picasso.with(this).load(imgUrl).into(img);
        img.setOnClickListener(new View.OnClickListener() { // 点击返回
            public void onClick(View paramView) {
                onBackPressed();
            }
        });
    }
}
