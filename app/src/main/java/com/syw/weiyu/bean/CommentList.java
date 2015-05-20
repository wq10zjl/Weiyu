package com.syw.weiyu.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: youwei
 * date: 2015-05-20
 * desc:
 */
public class CommentList {
    private List<Comment> commentList = new ArrayList<>();

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
