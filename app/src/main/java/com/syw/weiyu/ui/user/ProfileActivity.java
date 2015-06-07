package com.syw.weiyu.ui.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.syw.weiyu.core.AppException;
import com.syw.weiyu.core.Listener;
import com.syw.weiyu.core.Null;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.R;
import com.syw.weiyu.dao.user.LocalAccountDao;
import com.syw.weiyu.util.Msger;

/**
 * Created by songyouwei on 2015/2/25.
 * 修改资料的Activity
 */
public class ProfileActivity extends LoginBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set back icon
        ImageView imageView = (ImageView) findViewById(R.id.header_left);
        imageView.setImageResource(R.drawable.ic_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //setTitle

        //set progress button
        ActionProcessButton button = (ActionProcessButton) findViewById(R.id.btnSignIn);
        button.setText("修改");
        //set name tv
        EditText nameEditText = (EditText)findViewById(R.id.et_name);
        try {
            Account account = new LocalAccountDao().get();
            nameEditText.setText(account.getName());
        } catch (AppException e) {
            Msger.e(this,e.getMessage());
        }
    }

    /**
     * 修改资料
     * @param userId
     * @param name
     * @param gender
     */
    @Override
    public void doOnClickWork(String userId, String name, String gender) {
        try {
            String id = WeiyuApi.get().getAccount().getId();
            WeiyuApi.get().updateProfile(id, name, gender, new Listener<Null>() {
                @Override
                public void onSuccess(Null data) {
                    Msger.i(ProfileActivity.this,"修改成功");
                    showOnSuccessMsg("修改成功");
                }

                @Override
                public void onFailure(String msg) {
                    Msger.e(ProfileActivity.this,"修改出错:"+msg);
                    showOnErrorMsg("修改出错");
                }
            });
        } catch (AppException e) {
            Msger.e(ProfileActivity.this,"修改出错:"+e.getMessage());
            showOnErrorMsg("修改出错");
        }
    }
}
