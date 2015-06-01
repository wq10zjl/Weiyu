package com.syw.weiyu.dao.shuoshuo;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.AppException;
import com.syw.weiyu.api.Listener;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.Comment;
import com.syw.weiyu.dao.user.AccountDao;

import java.util.List;

/**
 * author: youwei
 * date: 2015-05-20
 * desc:
 */
public class CommentDao {
    /**
     * 添加评论
     * @param content
     * @param listener
     */
    public void addComment(long ssId,String content, final Listener<Comment> listener) {
        Account account = null;
        try {
            account = new AccountDao().get();
        } catch (AppException e) {
            listener.onFailure(e.getMessage());
        }
        final Comment comment = new Comment();
        comment.setSsId(ssId);
        comment.setUserId(account.getId());
        comment.setUserName(account.getName());
        comment.setUserGender(account.getGender());
        comment.setContent(content);
        comment.setTimestamp(System.currentTimeMillis());
        comment.save(AppContext.getCtx(), new SaveListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess(comment);
            }

            @Override
            public void onFailure(int i, String s) {
                listener.onFailure("评论出错:"+s);
            }
        });
    }

    /**
     * 获取评论列表
     * @param ssId 说说ID
     * @return
     * @throws AppException
     */
    public void getComments(long ssId,final Listener<List<Comment>> listener) {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.order("-timestamp");
        query.findObjects(AppContext.getCtx(), new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                listener.onSuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                listener.onFailure("获取评论出错:"+s);
            }
        });

//        String url = AppConstants.url_list_poi;
//
//        AjaxParams params = LBSCloud.getInitializedParams(AppConstants.geotable_id_comment);
//        params.put("q","");
//        //按时间|距离排序，优先显示时间靠前的
//        params.put("sortby","timestamp:-1|distance:1");
//        params.put("ssId",ssId+","+ssId);
//        //get
//        FinalHttp http = new FinalHttp();
//        http.get(url, params, new AjaxCallBack<String>() {
//            @Override
//            public void onSuccess(String s) {
//                super.onSuccess(s);
//                try {
//                    List<Comment> comments = parseCommentsFromJson(s);
//                    listener.onCallback(Listener.CallbackType.onSuccess, comments, null);
//                } catch (AppException e) {
//                    listener.onCallback(Listener.CallbackType.onFailure, null, e.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t, int errorNo, String strMsg) {
//                super.onFailure(t, errorNo, strMsg);
//                listener.onCallback(Listener.CallbackType.onFailure, null, strMsg);
//            }
//        });
    }
}
