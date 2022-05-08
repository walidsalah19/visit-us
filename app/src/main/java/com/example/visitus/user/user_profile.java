package com.example.visitus.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.visitus.MainActivity;
import com.example.visitus.R;
import com.example.visitus.admin.add_touristic_places;
import com.example.visitus.user_access.registration;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.HashBiMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class user_profile extends AppCompatActivity {

    private EditText name,email;
    private Button add;
    private CircleImageView image;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String image_str="",user_id,image_status="";
    private Uri image_uri;
    private SweetAlertDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
        setContentView(R.layout.fragment_profile);
        sweetdialog();
        intioalization();
        get_data();
        add_profile();
        image_method();
    }
    private void sweetdialog()
    {
        dialog=   new SweetAlertDialog(user_profile.this, SweetAlertDialog.PROGRESS_TYPE);
        dialog .setTitleText(getString(R.string.get_profile_data));
        dialog  .setCustomImage(R.drawable.ic_baseline_language_24);
        dialog  .getProgressHelper().setBarColor(Color.RED);
    }
    private void intioalization()
    {
        name=findViewById(R.id.pr_name);
        email=findViewById(R.id.pr_email);
        add=findViewById(R.id.add_profile);
        image=findViewById(R.id.image_pro);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        user_id=auth.getCurrentUser().getUid().toString();
    }
    private void get_data()
    {
        dialog.show();
        firestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if (task.getResult().contains("image")) {
                        name.setText(task.getResult().get("name").toString());
                        email.setText(task.getResult().get("email").toString());
                        image_str=task.getResult().get("image").toString();
                        Glide.with(user_profile.this).load(image_str).into(image);
                        dialog.dismiss();
                    }
                    else if (task.getResult().contains("name"))
                    {
                        name.setText(task.getResult().get("name").toString());
                        email.setText(task.getResult().get("email").toString());
                        dialog.dismiss();
                    }
                    else
                    {
                        email.setText(task.getResult().get("email").toString());
                        dialog.dismiss();
                    }
                }
            }
        });
    }
    private void add_profile()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_image();
            }
        });
    }

    private void check_data() {
        if (name.getText().toString().equals(""))
        {
            name.setError(getString(R.string.please_enter_your_name));
        }
        else if(image_str.equals(""))
        {
            HashMap<String, String> map=new HashMap<>();
            map.put("email",email.getText().toString());
            map.put("id",user_id);
            map.put("name",name.getText().toString());
            add_to_database(map);
        }
        else if(! image_str.equals(""))
        {
            HashMap<String, String> map=new HashMap<>();
            map.put("email",email.getText().toString());
            map.put("id",user_id);
            map.put("name",name.getText().toString());
            map.put("image",image_str);
            add_to_database(map);
        }
    }
    private void add_to_database(HashMap<String, String> map)
    {
        firestore.collection("users").document(user_id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(user_profile.this, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(user_profile.this, MainActivity.class));
                }
            }
        });
    }
    private void image_method()
    {
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_image();
            }
        });
    }
    private void get_image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            image_uri=data.getData();
            image_status="hesham";
            image.setImageURI(image_uri);
        }

    }
    private void upload_image() {
        if (image_status.toString().equals("")) {
            check_data();
        } else {
            String image_id = UUID.randomUUID().toString();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference("images_users/").child(image_id);
            StorageTask task = reference.putFile(image_uri);
            task.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri i = task.getResult();
                        image_str = i.toString();
                        check_data();
                    } else {
                        Toast.makeText(user_profile.this, getString(R.string.error_occur_in_image), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}