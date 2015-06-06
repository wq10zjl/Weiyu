package com.syw.weiyu.dao.user;

import android.content.Context;
import android.support.annotation.NonNull;
import com.syw.weiyu.core.AppContext;
import com.syw.weiyu.core.AppException;
import com.syw.weiyu.bean.Account;
import net.tsz.afinal.FinalDb;

import java.util.List;

/**
 * author: youwei
 * date: 2015-05-19
 * desc: 本地账户数据的存取，DB&Cache，包含用户信息，Token信息
 */
public class LocalAccountDao {

    /**
     * 获取本地的当前账户
     * @return
     * @throws AppException 暂无账户
     */
    public Account get() throws AppException {
        Account account = (Account) AppContext.getCache(AppContext.KEY_ACCOUNT);
        if (account == null) {
            Context ctx = AppContext.getCtx();
            FinalDb finalDb = FinalDb.create(ctx);
            List<Account> accounts = finalDb.findAll(Account.class);
            if (accounts != null && accounts.size() > 0) account = accounts.get(0);
            else throw new AppException("暂无本地账户，无法继续操作");
        }
        return account;
    }

    /**
     * 设置本地的账户，会清空之前的账户
     * @param account
     */
    public void set(@NonNull Account account) {
        Context ctx = AppContext.getCtx();
        FinalDb finalDb = FinalDb.create(ctx);
        finalDb.deleteAll(Account.class);
        finalDb.save(account);
        AppContext.putCache(AppContext.KEY_ACCOUNT, account);
    }
}
