package com.syw.weiyu.ui.shuoshuo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.syw.weiyu.R;
import com.syw.weiyu.core.AppException;
import com.syw.weiyu.core.Listener;
import com.syw.weiyu.core.Null;
import com.syw.weiyu.core.WeiyuApi;
import com.syw.weiyu.util.Msger;
import com.syw.weiyu.util.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author: youwei
 * date: 2015-06-13
 * desc:
 */
public class AddShuoshuoActivity extends FragmentActivity implements View.OnClickListener {
    private Activity mContext;
    private ImageView backBtn;
    private ImageView sendBtn;
    private TextView titleTxt;
    private EditText contentET;
    private ImageView picImg;
    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框

    private Uri photoUri;
    /** 使用照相机拍照获取图片 */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /** 使用相册中的图片 */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    /** 获取到的图片路径 */
    private String picPath = "";
//    private static ProgressDialog pd;
//    private String resultStr = "";	// 服务端返回结果集
//    private String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wy_activity_addshuoshuo);
        mContext = AddShuoshuoActivity.this;

        initViews();
    }

    /**
     * 初始化页面控件
     */
    private void initViews() {
        backBtn = (ImageView) findViewById(R.id.header_left);
        sendBtn = (ImageView) findViewById(R.id.header_right);
        sendBtn.setImageResource(R.drawable.wy_ic_send_btn);
        titleTxt = (TextView) findViewById(R.id.header_title);
        titleTxt.setText("散发谣言");
        contentET = (EditText) findViewById(R.id.contentET);
        picImg = (ImageView) findViewById(R.id.picImg);
        backBtn.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        picImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left:// 返回
                onBackPressed();
                break;
            case R.id.header_right:// 发布
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                publicShuoshuo();
                break;
            case R.id.picImg:// 添加图片点击事件
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                // 从页面底部弹出一个窗体，选择拍照还是从相册选择已有图片
                menuWindow = new SelectPicPopupWindow(mContext, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.uploadLayout),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            default:
                break;
        }
    }

    /**
     * 发布说说
     */
    private void publicShuoshuo() {
        String content = contentET.getText().toString();
        try {
            if (isValidPicPath(picPath)) {
                Msger.iStickey(mContext, "正在发送...");
                WeiyuApi.get().publishShuoshuoWithPic(content, picPath, new Listener<Null>() {
                    @Override
                    public void onSuccess(Null data) {
                        Msger.i(mContext, "发送成功");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                            }
                        },1000);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Msger.e(mContext, msg);
                    }
                });
            } else {
                WeiyuApi.get().publishShuoshuo(content, new Listener<Null>() {
                    @Override
                    public void onSuccess(Null data) {
                        Msger.i(mContext, "发送成功");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Msger.e(mContext, msg);
                    }
                });
            }

        } catch (AppException e) {
            Msger.e(mContext, e.getMessage());
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 隐藏弹出窗口
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:// 拍照
                    takePhoto();
                    break;
                case R.id.pickPhotoBtn:// 相册选择图片
                    pickPhoto();
                    break;
                case R.id.cancelBtn:// 取消
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
             * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不使用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        // 如果要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 点击取消按钮
        if(resultCode == RESULT_CANCELED){
            return;
        }

        // 可以使用同一个方法，这里分开写为了防止以后扩展不同的需求
        switch (requestCode) {
            case SELECT_PIC_BY_PICK_PHOTO:// 如果是直接从相册获取
                doPhoto(requestCode, data);
                break;
            case SELECT_PIC_BY_TACK_PHOTO:// 如果是调用相机拍照时
                doPhoto(requestCode, data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode, Intent data) {

        // 从相册取图片，有些手机有异常情况，请注意
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) {
            if (data == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }

        String[] pojo = { MediaStore.MediaColumns.DATA };
        // The method managedQuery() from the type Activity is deprecated
        //Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
        Cursor cursor = mContext.getContentResolver().query(photoUri, pojo, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
            picPath = cursor.getString(columnIndex);

            // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                cursor.close();
            }
        }

        // 如果图片符合要求将其上传到服务器
        if (isValidPicPath(picPath)) {
//            BitmapFactory.Options option = new BitmapFactory.Options();
//            // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图
//            option.inSampleSize = 1;
//            // 根据图片的SDCard路径读出Bitmap
//            Bitmap bm = BitmapFactory.decodeFile(picPath, option);

            final Bitmap bm = BitmapFactory.decodeFile(picPath);

//            // 显示在图片控件上
            picImg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            picImg.setImageBitmap(bm);
        } else {
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isValidPicPath(String picPath) {
        return !StringUtil.isEmpty(picPath) &&
                (picPath.endsWith(".png") ||
                picPath.endsWith(".PNG") ||
                picPath.endsWith(".jpg") ||
                picPath.endsWith(".JPG"));
    }

}
