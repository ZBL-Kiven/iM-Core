package com.cf.im.db.databases;

import com.cf.im.db.dao.DialogDao;
import com.cf.im.db.dao.MemberDao;
import com.cf.im.db.dao.impl.MessageDaoImpl;

import java.util.concurrent.ExecutorService;

public interface IDatabase {

    ExecutorService getReadExecutor();

    ExecutorService getWriteExecutor();

    MessageDaoImpl getMessageDao();

    MemberDao getMemberDao();

    DialogDao getDialogDao();

    void exit();
}
