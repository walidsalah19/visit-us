package com.example.visitus.admin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;


public class maintanace_place extends AppCompatActivity {
    private Uri image_uri;
    private String place_id;
    private EditText city,name,about;
    private Button update,delete;
    private String  image_str="";
    private String longitude="31.131302",latitude="29.976480",image_id,image_delet_id;
    private ImageButton location,image;
    private final static int PLACE_PICKER_REQUEST = 999;

    public maintanace_place() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maintanace_place);
        get_id();
        intioaliza();
        container();
        add_image();
        add_location();
        update_method();
        delete_methode();
    }
    private void get_id()
    {
        Bundle b=getIntent().getExtras();
        place_id=b.getString("id");
    }
    private void intioaliza()
    {
        city=findViewById(R.id.maintenance_city_name);
        name=findViewById(R.id.maintenance_place_name);
        about=findViewById(R.id.maintenance_place_descrype);
        update=findViewById(R.id.update_place_btn);
        delete=findViewById(R.id.delete_location_ptn);
        location=findViewById(R.id.maintenance_location_place_btn);
        image=findViewById(R.id.maintenance_media_place_btn);
    }
    private void delete_methode()
    {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delet_place();
            }
        });
    }
    private void container() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("places").document(place_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  if (task.isSuccessful())
                  {
                      image_delet_id=task.getResult().get("image_id").toString();
                      city.setText(task.getResult().get("city").toString());
                      name.setText(task.getResult().get("name").toString());
                      about.setText(task.getResult().get("about").toString());
                     image_str=task.getResult().get("image").toString();
                      place_id=task.getResult().get("id").toString();
                  }
            }
        });
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
    private void update_method()
    {
        update.setOnClickListener(new View.OnClickListener() {
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
        else {
            add_to_database();
        }

    }

    private void add_to_database() {
        String id= UUID.randomUUID().toString();
        place_data data=new place_data(image_str,name.getText().toString(),city.getText().toString(),longitude,latitude,about.getText().toString(),id,image_id);
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        database.collection("places").document(id).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    delet_place();
                }
                else
                {
                    Toast.makeText(maintanace_place.this, "fail", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void delet_place() {
        FirebaseFirestore database=FirebaseFirestore.getInstance();
        database.collection("places").document(place_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(maintanace_place.this, "successful ", Toast.LENGTH_SHORT).show();
                    delet_image();
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
            Intent i=builder.build(maintanace_place.this);
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
        if (requestCode==2 && resultCode == Activity.RESULT_OK )
        {
            Place place = PlacePicker.getPlace(maintanace_place.this, data);
            latitude = place.getLatLng().latitude+"";
            longitude = place.getLatLng().longitude+"";

        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
             image_uri=data.getData();
            //upload_image(data.getData());
        }

    }
    private void upload_image() {
        image_id=UUID.randomUUID().toString();
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
                    delet_image();
                    check_data();

                }
                else
                {
                    check_data();
                }
            }
        });
    }

    private void delet_image() {
        FirebaseStorage storage=FirebaseStorage.getInstance();
        Task<Void> reference=storage.getReference("images/").child( image_delet_id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

}