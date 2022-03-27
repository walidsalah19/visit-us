package com.example.visitus.user_access;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.visitus.MainActivity;
import com.example.visitus.R;
import com.example.visitus.user.user_profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class registration extends AppCompatActivity {
    private EditText email,password,conform;
    private TextView return_login;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Firebase_tool();
        intialization();
        method_registiration();
        show_password_method();
    }
    private void  Firebase_tool()
    {
        auth= FirebaseAuth.getInstance();
        database= FirebaseFirestore.getInstance();
    }
    private void  method_registiration()
    {
        Button registration=(Button) findViewById(R.id.registration_button);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            check_data();
            }
        });
    }

    private void check_data() {
        if(TextUtils.isEmpty(email.getText().toString()))
        {
            email.setError("please enter you'r email");
        }
        else if(TextUtils.isEmpty(password.getText().toString())&&password.getText().toString().length()<8 )
        {
            password.setError(" you'r password should be more than 8 ");
        }
        else if(! password.getText().toString().equals(conform.getText().toString()))
        {
            conform.setError("you'r password misfit");
        }
        else
        {
            complete_registration();
        }


    }

    private void complete_registration() {
        auth.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    user_id=task.getResult().getUser().getUid();
                   add_to_database(task.getResult().getUser().getUid().toString());
                    //startActivity(new Intent(registration.this,user_profile.class));

                }
                else
                {
                    Toast.makeText(registration.this, "error occur", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void add_to_database(String user_id) {
        HashMap<String, String> user_map=new HashMap<>();
        user_map.put("email",email.getText().toString());
        user_map.put("id",user_id);
        database.collection("users").document(user_id).set(user_map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(registration.this, "successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(registration.this, user_profile.class));

                }
                else
                {
                    Toast.makeText(registration.this, "error occur", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void show_password_method() {
       CheckBox  show_password=findViewById(R.id.show_password_regist);
        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (show_password.isChecked()) {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    conform.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conform.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                }

            }
        });
    }
    private void intialization()
    {
        email=findViewById(R.id.email_registration);
        password=findViewById(R.id.password_registration);
        conform=findViewById(R.id.confirm_registration);
        return_login=findViewById(R.id.return_to_login);
    }
}