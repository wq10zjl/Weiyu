package com.syw.weiyu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.LBS.LocSDK;
import com.syw.weiyu.R;
import com.syw.weiyu.entity.MLocation;
import com.syw.weiyu.entity.User;
import com.syw.weiyu.util.ACache;

import io.rong.imkit.RongIM;


public class TestsActivity extends Activity {
    // 用来实现 UI 线程的更新。
    Handler mHandler;
    //显示Location的TextView
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mHandler = new Handler();
        textView = (TextView)findViewById(R.id.textView);
    }

    private void printLocationOnTextView(BDLocation location) {
        if (location == null)
            return ;
        final StringBuffer sb = new StringBuffer(256);
        sb.append("time : ");
        sb.append(location.getTime());
        sb.append("\nerror code : ");
        sb.append(location.getLocType());
        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());
        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());
        sb.append("\nradius : ");
        sb.append(location.getRadius());
        if (location.getLocType() == BDLocation.TypeGpsLocation){
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());
            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());
        }
        Log.i("info", sb.toString());
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                textView.setText(sb.toString());
            }
        });
    }

    public void locate(View view) {
        textView.setText("Locating...");
        LocSDK.getInstance(this).locate(new LocSDK.OnLocateCompleteListener() {
            MLocation mLocation;
            @Override
            public void onSuccess(BDLocation location) {
                mLocation = new MLocation(location);
                AppContext.getInstance().setLocation(mLocation);
                ACache.get(TestsActivity.this).put(AppContext.LOCATION,mLocation);
                textView.setText(mLocation.toString());
            }

            @Override
            public void onFailure() {
                mLocation = new MLocation(TestsActivity.this);
                AppContext.getInstance().setLocation(mLocation);
                ACache.get(TestsActivity.this).put(AppContext.LOCATION, mLocation);
                textView.setText(mLocation.toString());
            }
        });
    }

    public void chat(View view) {
        mHandler.post(new Runnable() {
                          @Override
                          public void run() {
                              String userId = "songyouwei";
                              String title = "自问自答";

                              RongIM.getInstance().startPrivateChat(TestsActivity.this, userId, title);
                          }
                      }
        );
    }

    public void conversations(View view) {
        RongIM.getInstance().startConversationList(TestsActivity.this);
    }

    public void openPagerSlidingTabs(View view) {
//        Intent intent = new Intent(MainActivity.this, SlidingTabsActivity.class);
//        startActivity(intent);
    }

    public void datasave(View view) {
        User user = new User();
        user.setUserId("songyouwei");
        user.setName("哎呦喂");
        try {
//            DataAccessor.getInstance(this).saveData(user);
//            DataAccessor.getInstance(this).saveData(location);
            ACache.get(this).put("user",user);
        } catch (Exception e) {
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Toast.makeText(this,"OK",Toast.LENGTH_SHORT).show();
    }

    public void dataget(View view) {
        try {
            textView.setText(ACache.get(this).getAsObject("user").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void launcher(View v) {
        Intent intent = new Intent(TestsActivity.this, LauncherActivity.class);
        startActivity(intent);
    }

    public void maintabs(View v) {
        Intent intent = new Intent(TestsActivity.this, MainTabsActivity.class);
        startActivity(intent);
    }

}