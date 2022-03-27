package com.example.visitus.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.visitus.R;
import com.example.visitus.data.place_data;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.util.Objects;
import java.util.UUID;


public class add_touristic_places  extends AppCompatActivity {
    private EditText city,name,about;
    private Button add;
    private String image_str,longitude="31.131302",latitude="29.976480",image_id;
    private ImageButton location,image;
    private final static int PLACE_PICKER_REQUEST = 2;
   private Uri image_uri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_show_touristic_places);

        intioaliza();
        add_location();
        add_image();
        add_method();
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
        if(city.getText().toString().equals(""))
        {
            city.setError("please enter city name");
        }
        else if(name.getText().toString().equals(""))
        {
            name.setError("please enter place name");
        }
        else if(about.getText().toString().equals(""))
        {
            about.setError("please enter abstract about place");
        }
        else if(longitude.equals(""))
        {
            Toast.makeText(this, "please choose location of the place ", Toast.LENGTH_SHORT).show();
        }
        else if(image_str.equals(""))
        {
            Toast.makeText(this, "please choose image of the place ", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "successful ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void add_location()
    {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_location();
            }
        });
    }

    private void get_location(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent i=builder.build(this);
            startActivityForResult(i, 2);
        } catch (GooglePlayServicesRepairableException e) {
            Log.d("Exception", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d("Exception", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode==2 && resultCode == RESULT_OK )
        {
            Place place = PlacePicker.getPlace(getApplicationContext(), data);
            latitude = place.getLatLng().latitude+"";
            longitude = place.getLatLng().longitude+"";

        }*/
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            image_uri=data.getData();
            //upload_image(data.getData());
        }

        }
    private void upload_image() {
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
                    Toast.makeText(add_touristic_places.this, "error occur in image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}