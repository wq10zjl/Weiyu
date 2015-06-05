package com.syw.weiyu.dao.shuoshuo;

import com.syw.weiyu.core.Listener;
import com.syw.weiyu.bean.Comment;

import java.util.List;

/**
 * author: youwei
 * date: 2015-06-03
 * desc:
 */
public interface CommentDao {
    void addComment(long ssId,String content, Listener<Comment> listener);
    void getComments(long ssId,Listener<List<Comment>> listener);
}
