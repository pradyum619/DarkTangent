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

import java.util.HashMap;

public class register_screen extends AppCompatActivity {

    TextView alreadyreg;
    private Button register_btn;
    private EditText input_uname, input_pass, input_phn;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        alreadyreg = (TextView) findViewById(R.id.registered_txt);
        register_btn = (Button) findViewById(R.id.register_button);
        input_uname = (EditText) findViewById(R.id.username_input);
        input_pass = (EditText) findViewById(R.id.password_input);
        input_phn = (EditText) findViewById(R.id.phone_input);
        loadingBar = new ProgressDialog(this);


        alreadyreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(register_screen.this,loginScreen.class);
                startActivity(intent);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {

        String uname = input_uname.getText().toString();
        String pass = input_pass.getText().toString();
        String phn = input_phn.getText().toString();

        if (TextUtils.isEmpty(uname)){

            Toast.makeText(this, "Please Enter UserName", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(phn)){

            Toast.makeText(this, "Please Enter PhoneNumber", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(pass)){

            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else {
                 loadingBar.setTitle("Create Account");
                 loadingBar.setMessage("Please Wait While we check Credentials");
                 loadingBar.setCanceledOnTouchOutside(false);
                 loadingBar.show();

                 ValidatePhoneNumber(uname, pass, phn);

        }
    }

    private void ValidatePhoneNumber(final String uname,final String pass,final String phn) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Users").child(phn).exists())){

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phn);
                    userdataMap.put("password", pass);
                    userdataMap.put("userName", uname);

                    RootRef.child("Users").child(phn).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(register_screen.this, "Congratulations, your Account created Successfully",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(register_screen.this,loginScreen.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(register_screen.this, "Network Error: please Try Again",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
                else{
                    Toast.makeText(register_screen.this, "This " + phn + " already Exists!",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(register_screen.this, "Try Again using Another Phone Number",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
