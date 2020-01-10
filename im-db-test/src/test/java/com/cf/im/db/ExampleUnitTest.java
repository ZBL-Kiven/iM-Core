package com.cf.im.db;

import android.database.Cursor;

import androidx.room.util.DBUtil;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.alibaba.fastjson.JSON;
import com.cf.im.db.databases.DB;
import com.cf.im.db.domain.MemberBean;
import com.cf.im.db.databases.AppDatabase;

import org.junit.Test;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void test() {

        String content = "{\n" +
                "            \"inactive\": false,\n" +
                "            \"role\": \"owner\",\n" +
                "            \"updated\": \"2019-12-17T10:53:13Z\",\n" +
                "            \"uid\": 1,\n" +
                "            \"name\": \"aohan.yang\",\n" +
                "            \"type\": \"normal\",\n" +
                "            \"created\": \"2019-12-17T10:53:13Z\",\n" +
                "            \"hidden\": false,\n" +
                "            \"team_id\": 1,\n" +
                "            \"profile\": {\n" +
                "                \"email\": \"aohan.yang@cityfruit.io\",\n" +
                "                \"gender\": \"male\",\n" +
                "                \"title\": \"male\"\n" +
                "            }\n" +
                "        }";

        MemberBean bean = JSON.parseObject(content,MemberBean.class);
        System.out.println();


    }

}