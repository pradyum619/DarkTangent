package com.sssproducts.darktangent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class homeActivity extends AppCompatActivity {

    private Button logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logoutBtn = (Button) findViewById(R.id.logout_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Paper.book().destroy();
                Intent intent = new Intent(homeActivity.this,loginScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
