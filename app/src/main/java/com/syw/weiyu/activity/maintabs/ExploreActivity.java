package com.syw.weiyu.activity.maintabs;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.qq.e.appwall.GdtAppwall;
import com.syw.weiyu.R;
import com.syw.weiyu.activity.AboutInfoActivity;
import com.syw.weiyu.activity.ProfileActivity;

public class ExploreActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_explore);

        final Context ctx = ExploreActivity.this;
        findViewById(R.id.explore_item_appwall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Wandoujia app wall
//                Ads.showAppWall(ctx,ctx.getString(R.string.wdj_adid_appwall));

                //GDT app wall
                new GdtAppwall(ctx,ctx.getString(R.string.gdt_app_id),ctx.getString(R.string.gdt_adid_appwall), false).doShowAppWall();
            }
        });

        findViewById(R.id.explore_item_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExploreActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.explore_item_aboutinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExploreActivity.this, AboutInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
