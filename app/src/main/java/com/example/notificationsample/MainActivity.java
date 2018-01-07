package com.example.notificationsample;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListener();
    }

    protected void setListener() {
        findViewById(R.id.btn_notify).setOnClickListener(view ->jumpNotify()); //显示通知栏
    }

    private void jumpNotify() {
        Intent intent=new Intent(this,NotificationActivity.class);
        startActivity(intent);
    }
}
