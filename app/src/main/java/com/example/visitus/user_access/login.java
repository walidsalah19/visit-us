package com.example.visitus.user_access;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.visitus.MainActivity;
import com.example.visitus.R;
import com.example.visitus.admin.admin_main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class login extends AppCompatActivity {
    private Button login;
    private EditText email,password;
    private TextView registration ,forget_password_tex;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private SweetAlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sweetdialog();
        Firebase_tool();
    login_method();
    registration_method();
    forget_passeord();
    show_password_method();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user=auth.getCurrentUser();
        if (user !=null)
        {

            startActivity(new Intent(login.this,MainActivity.class));

        }
    }

    private void forget_passeord() {
        forget_password_tex=findViewById(R.id.forgit_password);
        forget_password_tex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,forget_password.class));
            }
        });
    }

    private void login_method() {
        login=findViewById(R.id.login);
        password=findViewById(R.id.email_password_text);
        email=findViewById(R.id.email_edittext);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_data();
            }
        });
    }
    private void check_data()
    {
        if(TextUtils.isEmpty(email.getText().toString()))
        {
           email.setError(getString(R.string.please_enter_email));
        }
        else if(TextUtils.isEmpty(password.getText().toString()))
        {
            password.setError(getString(R.string.please_enter_password));
        }
        else if (email.getText().toString().equals("admin@gmail.com"))
        {
            startActivity(new Intent(login.this,admin_main.class));
        }
       else
        {

            login_to_account();
        }
    }
     private void sweetdialog()
     {
         dialog=   new SweetAlertDialog(login.this, SweetAlertDialog.PROGRESS_TYPE);
         dialog .setTitleText(getString(R.string.login));
         dialog  .setCustomImage(R.drawable.ic_baseline_language_24);
         dialog  .getProgressHelper().setBarColor(Color.RED);
     }
    private void login_to_account() {
        dialog.show();
          auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
             if (task.isSuccessful())
             {
                 dialog.dismiss();

                 startActivity(new Intent(login.this,MainActivity.class));
             }
             else
             {
                 dialog.dismiss();

                 Toast.makeText(login.this, getString(R.string.error_occur), Toast.LENGTH_SHORT).show();
             }
              }
          });
    }


    private void  Firebase_tool()
    {
        auth=FirebaseAuth.getInstance();
        database= FirebaseFirestore.getInstance();
    }

    private void registration_method()
    {
        registration=findViewById(R.id.new_account);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,registration.class));
            }
        });
    }
    private void show_password_method() {
        CheckBox show_password=findViewById(R.id.show_password_login);
        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (show_password.isChecked()) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

            }
        });
    }
}