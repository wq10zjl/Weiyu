package com.syw.weiyu.ui.shuoshuo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bmob.v3.update.BmobUpdateAgent;
import com.paging.listview.PagingListView;
import com.syw.weiyu.core.AppException;
import com.syw.weiyu.core.Listener;
import com.syw.weiyu.core.Null;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.R;
import com.syw.weiyu.bean.ShuoshuoList;
import com.syw.weiyu.ui.adapter.ShuoshuosAdapter;

import com.syw.weiyu.util.Msger;

import com.syw.weiyu.util.StringUtil;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * author: youwei
 * date: 2015-05-11
 * desc: 说说（首页）
 */
public class ShuoshuoActivity extends FragmentActivity {

    int pageIndex = 0;
    int totalPage = 0;

    PagingListView listView;
    ShuoshuosAdapter adapter;

    PtrClassicFrameLayout mPtrFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_shuoshuo);
        //检查自动更新，默认仅wifi
        BmobUpdateAgent.update(this);

        initViews();
        initUltraPullToRefresh();
        initListView();
    }


    @Override
    protected void onResume() {
        super.onResume();

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    ShuoshuoList list = WeiyuApi.get().getCachedNearbyShuoshuo();
                    adapter.set(list.getShuoshuos());
                } catch (AppException e) {
                    mPtrFrame.autoRefresh();
                }
            }
        }.sendEmptyMessageDelayed(1, 500);

        //create banner ad
        if (listView.getHeaderViewsCount() == 0) {
            listView.addHeaderView(WeiyuApi.get().getBannerAdView(this, null));
        }
    }

    @Override
    protected void onDestroy() {
        WeiyuApi.get().onBannerDestory();
        super.onDestroy();
    }

    private void initViews() {
        ImageView btnAdd = (ImageView) findViewById(R.id.header_right);
        btnAdd.setImageResource(R.drawable.ic_action_new);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = getAddShuoshuoAlertDialog();
                dialog.show();
            }
        });
//        ImageView btnMessage = (ImageView) findViewById(R.id.header_left);
//        btnMessage.setImageResource();

                ((TextView) findViewById(R.id.header_title)).setText("说说");
    }


    /**
     * 初始化下拉刷新框架
     */
    private void initUltraPullToRefresh() {
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                WeiyuApi.get().getNearbyShuoshuo(pageIndex, new Listener<ShuoshuoList>() {
                    @Override
                    public void onSuccess(ShuoshuoList data) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        adapter.set(data.getShuoshuos());
                        //set totalPage
                        totalPage = (int)Math.ceil((double)data.getTotal()/(double)data.getShuoshuos().size());
                        //set has more page
                        listView.setHasMoreItems(pageIndex < totalPage);
                    }

                    @Override
                    public void onFailure(String msg) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        Msger.e(ShuoshuoActivity.this, msg);
                    }
                });
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    /**
     * 初始化列表视图
     */
    private void initListView() {
        listView = (PagingListView) findViewById(R.id.lv_shuoshuo);
        adapter = new ShuoshuosAdapter(this);
        listView.setAdapter(adapter);
        listView.setHasMoreItems(false);

        listView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                WeiyuApi.get().getNearbyShuoshuo(++pageIndex, new Listener<ShuoshuoList>() {
                    @Override
                    public void onSuccess(ShuoshuoList data) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        adapter.append(data.getShuoshuos());
                        //set has more page
                        listView.onFinishLoading(pageIndex < totalPage, null);
                    }

                    @Override
                    public void onFailure(String msg) {
                        //结束下拉刷新
                        mPtrFrame.refreshComplete();
                        Msger.e(ShuoshuoActivity.this, msg);
                        pageIndex--;
                    }
                });
            }
        });
    }

    private AlertDialog getAddShuoshuoAlertDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ShuoshuoActivity.this,AlertDialog.THEME_HOLO_LIGHT);
        // Get the layout inflater
        final LayoutInflater inflater = ShuoshuoActivity.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.wy_dialog_addshuoshuo, null);
        final EditText contentET = (EditText)view.findViewById(R.id.et_shuoshuo_content);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String content = contentET.getText().toString();
                        if (StringUtil.isEmpty(content)) return;
                        try {
                            WeiyuApi.get().publishShuoshuo(content, new Listener<Null>() {
                                @Override
                                public void onSuccess(Null data) {
                                    Msger.i(ShuoshuoActivity.this, "发送成功");
                                }

                                @Override
                                public void onFailure(String msg) {
                                    Msger.e(ShuoshuoActivity.this, msg);
                                }
                            });
                        } catch (AppException e) {
                            Msger.e(ShuoshuoActivity.this, e.getMessage());
                        }

                        new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                mPtrFrame.autoRefresh();
                            }
                        }.sendEmptyMessageDelayed(1, 1000);
                    }
                })
                .setNegativeButton("取消", null);
        return builder.create();
    }


    /**
     * 两次返回键退出，间隔2秒
     */
//    private long currentTime=0;
//    private long oldTime=0;
//    @Override
//    public void onBackPressed() {
//        currentTime = System.currentTimeMillis();
//        if ((currentTime - oldTime) > 2000 || oldTime == 0) {
//            Toast.makeText(this, "再按一次退出", 2000).show();
//            oldTime = currentTime;
//        } else {
//            finish();
//        }
//    }
}