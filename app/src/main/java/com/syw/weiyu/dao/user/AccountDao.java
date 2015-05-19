package com.syw.weiyu.dao.user;

import android.content.Context;
import com.syw.weiyu.AppContext;
import com.syw.weiyu.AppException;
import com.syw.weiyu.bean.Account;
import com.syw.weiyu.bean.User;
import net.tsz.afinal.FinalDb;

/**
 * author: youwei
 * date: 2015-05-19
 * desc: 账户数据的存取，包含用户信息，Token，和位置信息
 */
public class AccountDao {

    /**
     * 获取当前账户
     * @return
     * @throws AppException
     */
    public Account get() throws AppException {
        Context ctx = AppContext.getCtx();
        Account account = FinalDb.create(ctx).findAll(Account.class).get(0);
        if (account != null) return account;
        else throw new AppException("暂无账户");
    }

    public void set(Account account) throws AppException {
        FinalDb.create(AppContext.getCtx()).save(account);
    }
}
