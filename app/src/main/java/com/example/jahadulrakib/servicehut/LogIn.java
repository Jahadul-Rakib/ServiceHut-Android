package com.example.jahadulrakib.servicehut;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jahadulrakib.servicehut.Admin.AdminMainActivity;
import com.example.jahadulrakib.servicehut.Driver.MainDriverProfile;
import com.example.jahadulrakib.servicehut.Electrician.MainElectricianProfile;
import com.example.jahadulrakib.servicehut.GasFiter.MainGasProfile;
import com.example.jahadulrakib.servicehut.Labour.MainLabourProfile;
import com.example.jahadulrakib.servicehut.Nurse.MainNurseProfile;
import com.example.jahadulrakib.servicehut.Plumber.MainPlumberProfile;
import com.example.jahadulrakib.servicehut.User.MainActivity;
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

public class LogIn extends AppCompatActivity {
    Button button;
    Button reg;
    Context context = this;

    private FirebaseAuth mAuth;
    private DatabaseReference rootReference;
    private DatabaseReference userReference;
    private static final int UPLOAD_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView imageView;
    StorageReference storageReference;
    ProgressBar simpleProgressBar;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        simpleProgressBar=(ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();
        userReference = rootReference.child("user");
        storageReference = FirebaseStorage.getInstance().getReference("profile");




        if (mAuth.getCurrentUser() != null){
            userType();
            simpleProgressBar.setVisibility(View.VISIBLE);
            setProgressValue(progress);
            finish();

        }

        button = findViewById(R.id.lLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText userNames = findViewById(R.id.lUserName);
                EditText password = findViewById(R.id.lPassword);
                String  userName = userNames.getText().toString();
                String uPass = password.getText().toString();

                if(userName.isEmpty()){
                    userNames.setError("Email is Required");
                    userNames.requestFocus();
                    return;
                }
                if(uPass.isEmpty()){
                    password.setError("Password is Required");
                    password.requestFocus();
                    return;
                }

                userLogin(userName,uPass);
                simpleProgressBar.setVisibility(View.VISIBLE);
                setProgressValue(progress);
            }
        });


        reg = findViewById(R.id.signup);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.registration);

                final EditText userName = dialog.findViewById(R.id.userName);
                final EditText fullName  = dialog.findViewById(R.id.uFullName);
                final Spinner userType  = dialog.findViewById(R.id.uType);
                final EditText uEmail = dialog.findViewById(R.id.uEmail);
                final EditText uPass = dialog.findViewById(R.id.uPassword);
                final EditText uPhone = dialog.findViewById(R.id.uPhoneNumber);
                final EditText uAddress = dialog.findViewById(R.id.uAddress);
                imageView = dialog.findViewById(R.id.imageView);
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
                                    String imag_url = taskSnapshot.getDownloadUrl().toString();
                                    registration(uName,uFullName,uType,userEmail,userPass,userPhone,userAddress,imag_url);
                                    simpleProgressBar.setVisibility(View.VISIBLE);
                                    setProgressValue(progress);
                                }
                            });
                        }
                        dialog.dismiss();
                    }
                });


            }
        });
    }

    private void userType() {
        final String uID = mAuth.getCurrentUser().getUid();
        Query query = userReference.orderByChild("uId").equalTo(uID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Registration registration = snapshot.getValue(Registration.class);


                    if (registration.getUserType().equals("User")) {
                        Intent intent = new Intent(LogIn.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    else if (registration.getUserType().equals("Driver")) {

                        Intent intent = new Intent(LogIn.this, MainDriverProfile.class);
                        startActivity(intent);
                        break;
                    }
                    else if (registration.getUserType().equals("Nurse")) {

                        Intent intent = new Intent(LogIn.this, MainNurseProfile.class);
                        startActivity(intent);
                        break;
                    }
                    else if (registration.getUserType().equals("Labour")) {

                        Intent intent = new Intent(LogIn.this, MainLabourProfile.class);
                        startActivity(intent);
                        break;
                    }
                    else if (registration.getUserType().equals("Electrician")) {

                        Intent intent = new Intent(LogIn.this, MainElectricianProfile.class);
                        startActivity(intent);
                        break;
                    }
                    else if (registration.getUserType().equals("Plumber")) {

                        Intent intent = new Intent(LogIn.this, MainPlumberProfile.class);
                        startActivity(intent);
                        break;
                    }
                    else if (registration.getUserType().equals("GasFitter")) {

                        Intent intent = new Intent(LogIn.this, MainGasProfile.class);
                        startActivity(intent);
                        break;
                    }
                    else if (registration.getUserType().equals("Admin")) {

                        Intent intent = new Intent(LogIn.this, AdminMainActivity.class);
                        intent.putExtra("adminProfile", registration);
                        startActivity(intent);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void userLogin(String userName, String uPass) {
        mAuth.signInWithEmailAndPassword(userName, uPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(LogIn.this, "Log In Successfull", Toast.LENGTH_SHORT).show();
                            userType();

                        }
                        else {
                            Toast.makeText(LogIn.this, "Log In Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void registration(final String uName,final String uFullName,final String uType, final String userEmail,final String userPass,final String userPhone,final String userAddress, final String imageUrl) {
        mAuth.createUserWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            mAuth.signInWithEmailAndPassword(userEmail, userPass)
                                    .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){
                                                String uID = mAuth.getCurrentUser().getUid();
                                                Registration user = new Registration(uName,uFullName,uType,userEmail,userPass,userPhone,userAddress,uID,imageUrl);
                                                userReference.child(uID).setValue(user);
                                                Toast.makeText(LogIn.this, "Successfullay Registered", Toast.LENGTH_SHORT).show();
                                                userType();
                                            }
                                            else {
                                                Toast.makeText(LogIn.this, "Log In Failed", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                        }
                        else {
                            Toast.makeText(LogIn.this, "Registered Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, UPLOAD_IMAGE_REQUEST);
    }

    private void setProgressValue(final int progress) {
        simpleProgressBar.setProgress(progress);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(progress + 10);
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
