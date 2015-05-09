package com.syw.weiyu.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//import com.qq.e.appwall.GdtAppwall;
import com.syw.weiyu.R;
import com.syw.weiyu.ui.main.explore.AboutInfoActivity;
import com.syw.weiyu.ui.main.explore.NearByUserActivity;
import com.syw.weiyu.ui.user.ProfileActivity;

public class ExploreActivity extends FragmentActivity {

    private long currentTime=0;
    private long oldTime=0;
    /**
     * 两次返回键退出，间隔2秒
     */
    @Override
    public void onBackPressed() {
        currentTime = System.currentTimeMillis();
        if ((currentTime - oldTime) > 2000 || oldTime == 0) {
            Toast.makeText(this, "再按一次退出", 2000).show();
            oldTime = currentTime;
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_explore);

        final Context ctx = ExploreActivity.this;
//        findViewById(R.id.explore_item_appwall).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Wandoujia app wall
////                Ads.showAppWall(ctx,ctx.getString(R.string.wdj_adid_appwall));
//
//                //GDT app wall
//                new GdtAppwall(ctx,ctx.getString(R.string.gdt_app_id),ctx.getString(R.string.gdt_adid_appwall), false).doShowAppWall();
//            }
//        });

        findViewById(R.id.explore_item_shuoshuo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExploreActivity.this, NearByUserActivity.class);
                startActivity(intent);
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

        TextView tvTitle = (TextView) findViewById(R.id.header_tv_title);
        tvTitle.setText("发现");
    }
}
