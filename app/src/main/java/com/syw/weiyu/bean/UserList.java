package com.syw.weiyu.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: youwei
 * date: 2015-06-01
 * desc:
 */
public class UserList {
    private int total;
    private List<User> Users = new ArrayList<>();

    public UserList(int total, List<User> Users) {
        this.total = total;
        this.Users = Users;
    }

    public List<User> getUsers() {
        return Users;
    }

    public void setUsers(List<User> Users) {
        this.Users = Users;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
