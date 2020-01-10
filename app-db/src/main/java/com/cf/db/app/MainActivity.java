package com.cf.db.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.cf.im.db.databases.AppDatabase;
import com.cf.im.db.domain.MemberBean;
import com.cf.im.db.repository.MemberRepository;

import java.util.List;

public class MainActivity extends FragmentActivity {

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            for (int i = 0; i < 15; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("线程:Handler" + Thread.currentThread().hashCode() + "\t" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String content = "{\n" +
                "            \"inactive\": false,\n" +
                "            \"role\": \"owner\",\n" +
                "            \"updated\": \"2019-12-17T10:53:13Z\",\n" +
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

        MemberRepository repository = new MemberRepository();

        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
                if (i % 10 == 0) {
                    handler.sendEmptyMessage(1);
                }
                System.out.println("线程:" + Thread.currentThread().hashCode() + "\t" + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
