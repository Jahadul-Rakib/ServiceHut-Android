package com.example.jahadulrakib.servicehut.User;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jahadulrakib.servicehut.LatLang;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.ToLetMap.GetLocationMapsActivity;
import com.example.jahadulrakib.servicehut.UserAdepter.ViewOwnToLet;
import com.example.jahadulrakib.servicehut.UserPojo.CreateToLetPojo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CreateToLet extends AppCompatActivity implements ViewOwnToLet.OnItemClickListener {

    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<CreateToLetPojo> toletList;

    private DatabaseReference rootReReference;
    private DatabaseReference estimateRefarence;
    ViewOwnToLet adapter;

    Button button,post;
    ImageView imageView;

    private FirebaseAuth mAuth;
    private DatabaseReference rootReference;
    private DatabaseReference userReference;
    private static final int UPLOAD_IMAGE_REQUEST = 1;
    private Uri imageUri;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_let);

        mAuth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();
        userReference = rootReference.child("postToLet");
        storageReference = FirebaseStorage.getInstance().getReference("toLetImage");

        button =findViewById(R.id.btnCreateToLet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(CreateToLet.this);
                dialog.setContentView(R.layout.crete_add);
                dialog.show();
                final EditText takePhones = dialog.findViewById(R.id.phoneNumber);
                final EditText monthRents = dialog.findViewById(R.id.rent);
                final EditText advances = dialog.findViewById(R.id.advance);
                final EditText locations = dialog.findViewById(R.id.location);
                final EditText detailsss = dialog.findViewById(R.id.itemDetails);
                final Spinner rentMonths = dialog.findViewById(R.id.month);
                final Spinner rentTypes = dialog.findViewById(R.id.rentFor);
                final Spinner types = dialog.findViewById(R.id.rentForMenorWomen);
                final Button locationMap = dialog.findViewById(R.id.locationMap);
                post = dialog.findViewById(R.id.btnPost);

                locationMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CreateToLet.this, GetLocationMapsActivity.class);
                        startActivity(intent);
                        post.setVisibility(View.VISIBLE);
                    }
                });



                imageView = dialog.findViewById(R.id.imgHome);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPhoto();
                    }
                });

                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String takePhone = takePhones.getText().toString().trim();
                        final String monthRent = monthRents.getText().toString().trim()+ "/Tk";
                        final String advance = advances.getText().toString().trim()+ "/Tk";
                        final String location = locations.getText().toString().trim();
                        final String details = detailsss.getText().toString().trim();
                        final String rentMonth = rentMonths.getSelectedItem().toString().trim();
                        final String rentType = rentTypes.getSelectedItem().toString().trim();
                        final String type = types.getSelectedItem().toString().trim();
                        if (imageUri != null){
                            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                                    +"."+getExtention(imageUri) );

                            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String imag_url = taskSnapshot.getDownloadUrl().toString();
                                    String userId = mAuth.getCurrentUser().getUid();
                                    LatLang latLang = new LatLang();
                                    double lat = latLang.getLatiture();
                                    double lon = latLang.getLongiture();
                                    String toLetId = userReference.push().getKey();
                                    CreateToLetPojo createToLetPojo = new CreateToLetPojo(userId,toLetId,imag_url,takePhone,monthRent,advance,location,rentMonth,rentType,details,type,lat,lon);
                                    userReference.child(toLetId).setValue(createToLetPojo);
                                    Toast.makeText(CreateToLet.this, "Add To-Let Successfull", Toast.LENGTH_SHORT).show();
                                    }
                            });

                        }
                        dialog.dismiss();
                    }
                });
            }
        });


        rootReReference = FirebaseDatabase.getInstance().getReference();
        estimateRefarence = rootReReference.child("postToLet");

        String userId = mAuth.getCurrentUser().getUid();
        mRecyclerView = findViewById(R.id.toLetRecyclerView);
        toletList = new ArrayList<>();
        adapter = new ViewOwnToLet(CreateToLet.this, toletList);
        layoutManager = new LinearLayoutManager(CreateToLet.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Query query = estimateRefarence.orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toletList.clear();
                for (DataSnapshot tolet : dataSnapshot.getChildren()) {
                    CreateToLetPojo toLet = tolet.getValue(CreateToLetPojo.class);
                    toletList.add(toLet);
                }
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(CreateToLet.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, UPLOAD_IMAGE_REQUEST);
    }
    private  String getExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_IMAGE_REQUEST &&
                resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                Picasso.with(this).load(imageUri).into(imageView);

            }
        }
    }

    @Override
    public void OnItemClick(final int position) {
        CreateToLetPojo createToLetPojo = toletList.get(position);
        final String toletId = createToLetPojo.getToLetId();
        final Dialog dialog = new Dialog(CreateToLet.this);
        dialog.setContentView(R.layout.modify_add);
        ImageView mHomeImg = dialog.findViewById(R.id.imgHome);
        Spinner mRentMonth = dialog.findViewById(R.id.month);
        Spinner rentType = dialog.findViewById(R.id.rentFor);
        Spinner clientType = dialog.findViewById(R.id.rentForMenorWomen);
        EditText phoneNumber = dialog.findViewById(R.id.phoneNumber);
        EditText monthlyRent = dialog.findViewById(R.id.rent);
        EditText advance = dialog.findViewById(R.id.advance);
        EditText location = dialog.findViewById(R.id.location);
        EditText details = dialog.findViewById(R.id.itemDetails);


        final Button delete = dialog.findViewById(R.id.btnDelete);
        final Button update = dialog.findViewById(R.id.btnUpdate);
        Button locationMap = dialog.findViewById(R.id.locationMap);
        delete.setVisibility(View.VISIBLE);
        locationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateToLet.this, GetLocationMapsActivity.class);
                startActivity(intent);
                update.setVisibility(View.VISIBLE);
            }
        });


        details.setText(createToLetPojo.getToLetDetails());
        location.setText(createToLetPojo.getToLetAddress());
        advance.setText(createToLetPojo.getToLetAdvance());
        monthlyRent.setText(createToLetPojo.getToLetMonthRent());
        phoneNumber.setText(createToLetPojo.getToLetPhone());

        mRentMonth.setSelection(getIndex(mRentMonth, createToLetPojo.getToLetMonth()));
        rentType.setSelection(getIndex(rentType, createToLetPojo.getToLetType()));
        clientType.setSelection(getIndex(clientType, createToLetPojo.getType()));


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateToLetPojo momentPojo = toletList.get(position);
                String toletId = momentPojo.getToLetId();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("postToLet");
                Query delete = databaseReference.orderByChild("toLetId").equalTo(toletId);
                delete.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            snapshot.getRef().removeValue();
                            Toast.makeText(CreateToLet.this, "Delete Successfully.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }
     });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateToLetPojo momentPojo = toletList.get(position);
                String toletId = momentPojo.getToLetId();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("postToLet");
                Query update = databaseReference.orderByChild("toLetId").equalTo(toletId);
                update.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                            CreateToLetPojo createToLetPojo = toletList.get(position);
                            String toletId = createToLetPojo.getToLetId();
                            String url = createToLetPojo.getToLetImageURL();
                            String userId = createToLetPojo.getUserId();

                            Spinner mRentMonths = dialog.findViewById(R.id.month);
                            Spinner rentTypes = dialog.findViewById(R.id.rentFor);
                            Spinner clientTypes = dialog.findViewById(R.id.rentForMenorWomen);
                            EditText phoneNumbers = dialog.findViewById(R.id.phoneNumber);
                            EditText monthlyRents = dialog.findViewById(R.id.rent);
                            EditText advances = dialog.findViewById(R.id.advance);
                            EditText locations = dialog.findViewById(R.id.location);
                            EditText detailss = dialog.findViewById(R.id.itemDetails);

                            final String phoneNumber = phoneNumbers.getText().toString().trim();
                            final String monthlyRent = monthlyRents.getText().toString().trim()+ "/Tk";
                            final String advance = advances.getText().toString().trim()+ "/Tk";
                            final String location = locations.getText().toString().trim();
                            final String details = detailss.getText().toString().trim();
                            final String mRentMonth = mRentMonths.getSelectedItem().toString().trim();
                            final String rentType = rentTypes.getSelectedItem().toString().trim();
                            final String clientType = clientTypes.getSelectedItem().toString().trim();


                            LatLang latLang = new LatLang();
                            double lat = latLang.getLatiture();
                            double lon = latLang.getLongiture();

                            CreateToLetPojo createToLetPojos = new CreateToLetPojo(userId,toletId,url,phoneNumber,monthlyRent,advance,location,mRentMonth,rentType,details,clientType,lat,lon);
                            snapshot.getRef().setValue(createToLetPojos);
                            Toast.makeText(CreateToLet.this, "Successfully Updated.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



        dialog.show();

    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
}
