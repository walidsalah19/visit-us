package com.example.visitus.admin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.visitus.R;
import com.example.visitus.admin.map.move_location;
import com.example.visitus.data.place_data;
import com.example.visitus.admin.map.MapsActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class add_touristic_places  extends AppCompatActivity {
    private EditText city,name,about;
    private Button add;
    private String image_str,longitude,latitude,image_id;
    private ImageButton location,image;
    private final static int PLACE_PICKER_REQUEST = 2;
   private Uri image_uri;
    private SweetAlertDialog pDialogLoading,pDialogSuccess,pDialogerror;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_show_touristic_places);
        sweetalert();
        intioaliza();
        add_location();
        add_image();
        add_method();
    }
    private void sweetalert()
    {
        //loading
        pDialogLoading = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialogLoading.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialogLoading.setCancelable(false);

        //error
        pDialogerror= new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialogerror.setConfirmText(getString(R.string.dialog_ok));
        pDialogerror.setConfirmClickListener(sweetAlertDialog -> {
            pDialogerror.dismiss();
        });
        pDialogerror.setCancelable(true);

        //Success
        pDialogSuccess= new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialogSuccess.setConfirmText(getString(R.string.dialog_ok));
        pDialogSuccess.setConfirmClickListener(sweetAlertDialog -> {
            pDialogSuccess.dismiss();
        });
        pDialogSuccess.setCancelable(true);
    }

    private void add_image() {
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

    private void intioaliza()
    {
        city=findViewById(R.id.add_city_txt);
        name=findViewById(R.id.input_add_place);
        about=findViewById(R.id.input_about_place);
        add=findViewById(R.id.add_place_data_btn);
        location=findViewById(R.id.add_new_location_place_btn);
        image=findViewById(R.id.add_new_media_place_btn);
    }
    private void add_method()
    {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               upload_image();
            }
        });
    }

    private void check_data() {
        longitude= move_location.getLongitude();
        latitude=move_location.getLatitude();
        if(city.getText().toString().equals(""))
        {
            city.setError(getString(R.string.please_enter_city_name));
        }
        else if(name.getText().toString().equals(""))
        {
            name.setError(getString(R.string.please_enter_place_name));
        }
        else if(about.getText().toString().equals(""))
        {
            about.setError(getString(R.string.abstract_));
        }
        else if(longitude.equals(null))
        {
            Toast.makeText(this, getString(R.string.location_), Toast.LENGTH_SHORT).show();
        }
        else if(image_str.equals(""))
        {
            Toast.makeText(this, getString(R.string.choose_imag), Toast.LENGTH_SHORT).show();
        }
        else {
            add_to_database();
        }
       
    }

    private void add_to_database() {
        String id=UUID.randomUUID().toString();
        place_data data=new place_data(image_str,name.getText().toString(),city.getText().toString(),longitude,latitude,about.getText().toString(),id,image_id);
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        database.collection("places").document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    pDialogLoading.dismiss();
                    pDialogSuccess.setTitleText(getString(R.string.successful));
                    pDialogSuccess.show();
                }
                else
                {
                    pDialogLoading.dismiss();
                    pDialogerror.setTitleText(R.string.fail);
                    pDialogerror.show();
                }
            }
        });
    }
    private void add_location()
    {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(add_touristic_places.this, MapsActivity.class));
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            image_uri=data.getData();
        }

        }
    private void upload_image() {
        pDialogLoading.setTitleText(getString(R.string.upload_place));
        pDialogLoading.show();
        image_id= UUID.randomUUID().toString();
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference reference=storage.getReference("images/").child(image_id);
        StorageTask task=reference.putFile(image_uri);
        task.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful())
                {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                {
                    Uri i=task.getResult();
                    image_str=i.toString();
                    check_data();
                }
                else {
                    pDialogLoading.dismiss();
                    pDialogerror.setTitleText(R.string.error_occur_in_image);
                    pDialogerror.show();
                    Toast.makeText(add_touristic_places.this, getString(R.string.error_occur_in_image), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}