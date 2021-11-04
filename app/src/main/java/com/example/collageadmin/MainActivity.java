package com.example.collageadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout gotoNoticeActivity;
    private RelativeLayout goto_gallery_activity;
    private RelativeLayout goto_pdf_activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gotoActivity();
        gotoNoticeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),NoticeActivity.class);
                startActivity(intent);
            }
        });

        goto_gallery_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryImage=new Intent(MainActivity.this,UploadImage.class);
                startActivity(galleryImage);
            }
        });
        goto_pdf_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,UploadPDFActivity.class);
                startActivity(intent);
            }
        });
    }

    private void gotoActivity() {

        gotoNoticeActivity=findViewById(R.id.Goto_notice_activity);
        goto_gallery_activity=findViewById(R.id.gallery_view);
        goto_pdf_activity=findViewById(R.id.pdf_relativeLayout);

    }
}