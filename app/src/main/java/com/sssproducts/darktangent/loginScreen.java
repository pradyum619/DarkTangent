package com.sssproducts.darktangent;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.sssproducts.darktangent.Model.Users;
import com.sssproducts.darktangent.Prevalent.Prevalent;

import java.util.HashMap;

import io.paperdb.Paper;


public class loginScreen extends AppCompatActivity {

    private TextView newuser;
    private Button login_btn;
    private EditText input_pass, input_phn;
    private ProgressDialog loadingBar;
    private String parentDbname = "Users";
    private CheckBox chkBoxRememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        newuser = (TextView) findViewById(R.id.register_txt);
        login_btn = (Button) findViewById(R.id.login_button);
        input_phn = (EditText) findViewById(R.id.username_input);
        input_pass = (EditText) findViewById(R.id.password_input);
        loadingBar = new ProgressDialog(this);
        chkBoxRememberMe = (CheckBox) findViewById(R.id.remember_me_chkb);

        Paper.init(this);

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(loginScreen.this,register_screen.class);
                startActivity(intent);
            }

        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();

            }
        });
    }

    private void LoginUser() {

        String pass = input_pass.getText().toString();
        String phn = input_phn.getText().toString();


        if (TextUtils.isEmpty(phn)){

            Toast.makeText(this, "Please Enter PhoneNumber", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(pass)){

            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Logging In...");
            loadingBar.setMessage("Please Wait While we check Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phn, pass);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {

        if (chkBoxRememberMe.isChecked()){

            Paper.book().write(Prevalent.userPhoneKey, phone);
            Paper.book().write(Prevalent.userPasswordKey, password);
        }

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
                            Toast.makeText(loginScreen.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(loginScreen.this,homeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            Toast.makeText(loginScreen.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {

                    Toast.makeText(loginScreen.this, "Number not registered", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
}
