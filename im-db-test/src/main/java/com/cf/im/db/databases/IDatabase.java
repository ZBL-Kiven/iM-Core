package com.cf.im.db.databases;

import com.cf.im.db.dao.MemberDao;
import com.cf.im.db.dao.MessageDao;

import java.util.concurrent.ExecutorService;

public interface IDatabase {

    ExecutorService getReadExecutor();

    ExecutorService getWriteExecutor();

    MessageDao getMessageDao();

    MemberDao getMemberDao();

}
