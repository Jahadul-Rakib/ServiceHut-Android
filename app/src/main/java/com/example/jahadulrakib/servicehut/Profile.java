package com.example.jahadulrakib.servicehut;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    TextView button;
    ImageView imageView;
    TextView uEmail,pass,username,addresss,phoneNumbers;
    private DatabaseReference rootReference;
    private DatabaseReference userReference;
    private static final int UPLOAD_IMAGE_REQUEST = 1;
    private Uri imageUri;
    StorageReference storageReference;
    String imag_url;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, container, false);

        rootReference = FirebaseDatabase.getInstance().getReference();
        userReference = rootReference.child("user");
        storageReference = FirebaseStorage.getInstance().getReference("profile");

        auth = FirebaseAuth.getInstance();
        final String userId = auth.getCurrentUser().getUid();

        uEmail = v.findViewById(R.id.puEmail);
        pass = v.findViewById(R.id.pUPass);
        username = v.findViewById(R.id.pUserName);
        addresss = v.findViewById(R.id.pUAddress);
        phoneNumbers = v.findViewById(R.id.pUPhone);
        imageView = v.findViewById(R.id.pImage);

        databaseReference =FirebaseDatabase.getInstance().getReference("user");
        Query query = databaseReference.orderByChild("uId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Registration registration = data.getValue(Registration.class);
                    Picasso.with(getActivity()).load(registration.getUrlImage()).placeholder(R.drawable.profile_picture).into(imageView);
                    uEmail.setText(registration.getuEmail());
                    pass.setText(registration.getuPass());
                    username.setText(registration.getFullName());
                    addresss.setText(registration.getuAddress());
                    phoneNumbers.setText(registration.getuPhone());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        button = v.findViewById(R.id.signOut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(),LogIn.class);
                startActivity(intent);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.update_profile);

                final EditText userName = dialog.findViewById(R.id.userName);
                final EditText fullName  = dialog.findViewById(R.id.uFullName);
                final Spinner userType  = dialog.findViewById(R.id.uType);
                final EditText uEmail = dialog.findViewById(R.id.uEmail);
                final EditText uPass = dialog.findViewById(R.id.uPassword);
                final EditText uPhone = dialog.findViewById(R.id.uPhoneNumber);
                final EditText uAddress = dialog.findViewById(R.id.uAddress);
                imageView = dialog.findViewById(R.id.imageView);


                databaseReference =FirebaseDatabase.getInstance().getReference("user");
                Query query = databaseReference.orderByChild("uId").equalTo(userId);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            Registration registration = data.getValue(Registration.class);
                            Picasso.with(getActivity()).load(registration.getUrlImage()).placeholder(R.drawable.profile_picture).into(imageView);
                            uEmail.setText(registration.getuEmail());
                            uPass.setText(registration.getuPass());
                            fullName.setText(registration.getFullName());
                            uAddress.setText(registration.getuAddress());
                            uPhone.setText(registration.getuPhone());
                            userName.setText(registration.getUserName());
                            imag_url = registration.getUrlImage();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                dialog.show();

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPhoto();
                    }
                });
                Button regestation = dialog.findViewById(R.id.reg);
                regestation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String uName = userName.getText().toString().trim();
                        final String uFullName  = fullName.getText().toString().trim();
                        final String uType  = userType.getSelectedItem().toString().trim();
                        final String userEmail = uEmail.getText().toString().trim();
                        final String userPass = uPass.getText().toString().trim();
                        final String userPhone = uPhone.getText().toString().trim();
                        final String userAddress = uAddress.getText().toString().trim();

                        if (imageUri != null){
                            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                                    +"."+getExtention(imageUri) );

                            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imag_url = taskSnapshot.getDownloadUrl().toString();
                                    registration(uName,uFullName,uType,userEmail,userPass,userPhone,userAddress,imag_url);
                                }
                            });
                        }
                        else{
                            registration(uName,uFullName,uType,userEmail,userPass,userPhone,userAddress,imag_url);
                        }
                        dialog.dismiss();
                    }
                });


            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void registration(final String uName,final String uFullName,final String uType,
                              final String userEmail,final String userPass,final String userPhone,
                              final String userAddress, final String imageUrl)

    {
                                                String uID = auth.getCurrentUser().getUid();
                                                Registration user = new Registration(uName,uFullName,uType,userEmail,userPass,userPhone,userAddress,uID,imageUrl);
                                                userReference.child(uID).setValue(user);
                                                Toast.makeText(getActivity(), "Successfullay Updated", Toast.LENGTH_SHORT).show();
                                            }



    private  String getExtention(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPLOAD_IMAGE_REQUEST &&
                resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();
                Picasso.with(getActivity()).load(imageUri).into(imageView);

            }
        }
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, UPLOAD_IMAGE_REQUEST);
    }
}
