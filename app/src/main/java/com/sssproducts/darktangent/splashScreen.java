package com.sssproducts.darktangent;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sssproducts.darktangent.Model.Users;
import com.sssproducts.darktangent.Prevalent.Prevalent;

import io.paperdb.Paper;

public class splashScreen extends AppCompatActivity {

    Handler handler;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loadingBar = new ProgressDialog(this);

        handler=new Handler();
        Paper.init(this);
        String userPhoneKey = Paper.book().read(Prevalent.userPhoneKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if (userPhoneKey != "" && userPasswordKey != "")
        {
            if (!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey))
            {
                loadingBar.setTitle("Logging In...");
                loadingBar.setMessage("Please Wait");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                AllowAccess(userPhoneKey, userPasswordKey);
            }
            else
            {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent=new Intent(splashScreen.this,loginScreen.class);
                        startActivity(intent);
                        finish();
                    }
                },3000);
            }

        }
        else
        {
            handler.postDelayed(new Runnable() {
                 public void run() {
                    Intent intent=new Intent(splashScreen.this,loginScreen.class);
                    startActivity(intent);
                    finish();
                }
            },3000);
        }

    }

    private void AllowAccess(final String phone,final String password) {

        final String parentDbname = "Users";
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbname).child(phone).exists()){

                    Users usersdata = dataSnapshot.child(parentDbname).child(phone).getValue(Users.class);

                    if (usersdata.getPhone().equals(phone))
                    {
                        if (usersdata.getPassword().equals(password))
                        {
                            Toast.makeText(splashScreen.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(splashScreen.this,homeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(splashScreen.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {

                    Toast.makeText(splashScreen.this, "Number not registered", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
}
