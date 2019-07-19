package com.example.jahadulrakib.servicehut.UserServices;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jahadulrakib.servicehut.LatLang;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Registration;
import com.example.jahadulrakib.servicehut.ToLetMap.GetLocationMapsActivity;
import com.example.jahadulrakib.servicehut.Transection.Transection;
import com.example.jahadulrakib.servicehut.UserAdepter.FindGassFitterAdepter;
import com.example.jahadulrakib.servicehut.UserPojo.HireGesFitter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class FindGasFitter extends AppCompatActivity implements FindGassFitterAdepter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Registration> driverList,driverlistNext;
    private DatabaseReference rootReference;
    private DatabaseReference driverRefarence,createJobRefarence,availableDriverRefarence,transection;
    FindGassFitterAdepter adapter;
    private int mYear, mMonth, mDay;
    final Context context = this;
    private FirebaseAuth firebaseAuth;
    int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_electrician);

        rootReference = FirebaseDatabase.getInstance().getReference();
        driverRefarence = rootReference.child("user");
        availableDriverRefarence = rootReference.child("driverJob");
        transection = rootReference.child("transection");
        mRecyclerView = findViewById(R.id.DriverRecyclerView);
        driverList = new ArrayList<>();
        adapter = new FindGassFitterAdepter(FindGasFitter.this, driverList);
        layoutManager = new LinearLayoutManager(FindGasFitter.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Query query = driverRefarence.orderByChild("userType").equalTo("GasFitter");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driverList.clear();
                for (final DataSnapshot driver : dataSnapshot.getChildren()) {
                    final Registration driverView = driver.getValue(Registration.class);
                    String driverId = driverView.getuId();
                    Query availabeDriver = availableDriverRefarence.orderByChild("gessFitterId").equalTo(driverId);
                    availabeDriver.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int a = 0;
                           for (DataSnapshot avDrivers : dataSnapshot.getChildren()) {
                               HireGesFitter getDriver = avDrivers.getValue(HireGesFitter.class);
                               String jobStatus = getDriver.getJobStatus();
                               if(!jobStatus.equals("pending")){
                                   if (!jobStatus.equals("accept")){
                                       a = 1;
                                   }
                               }
                               else{
                                   a = 0;
                                   break;
                               }

                           }
                           if(!dataSnapshot.exists() ){
                                a = 1;
                            }
                           if (a == 1){
                               driverList.add(driverView);
                           }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(FindGasFitter.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        EditText search = findViewById(R.id.txtSearchToLet);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }
    private void filter(String text) {

        driverlistNext = new ArrayList<>();
        for (Registration item : driverList) {
            if (item.getFullName().toLowerCase().contains(text.toLowerCase())) {
                driverlistNext.add(item);
            } else if (item.getuAddress().toLowerCase().contains(text.toLowerCase())) {
                driverlistNext.add(item);
            } else if (item.getuPhone().toLowerCase().contains(text.toLowerCase())) {
                driverlistNext.add(item);
            }
        }
        adapter.filterList(driverlistNext);
    }

    @Override
    public void OnItemClick(final int position) {

        if (driverlistNext ==null){
            firebaseAuth = FirebaseAuth.getInstance();

            final Registration registration = driverList.get(position);
            final String driverId = registration.getuId();
            final String phoneNumber = registration.getuPhone();
            final String userId = firebaseAuth.getCurrentUser().getUid();;

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.hire_electrician);
            final TextView totalJob = dialog.findViewById(R.id.complitedJob);
            final TextView rejectJob = dialog.findViewById(R.id.rejectedJob);
            final RatingBar rating = dialog.findViewById(R.id.customerViewRatingBar);
            dialog.setTitle("Create Job");


            Query queery = availableDriverRefarence.orderByChild("gessFitterId").equalTo(driverId);
            queery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    totalJob.setText(null);
                    int complitedJob = 0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        HireGesFitter hireDriver = snapshot.getValue(HireGesFitter.class);
                        if (hireDriver.getJobStatus().equals("complete")){
                            complitedJob =  complitedJob+1;
                        }
                    }

                    totalJob.setText(""+complitedJob);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            Query queery1 = availableDriverRefarence.orderByChild("gessFitterId").equalTo(driverId);
            queery1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rejectJob.setText(null);
                    int rejectJobs = 0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        HireGesFitter hireDriver = snapshot.getValue(HireGesFitter.class);
                        if (hireDriver.getJobStatus().equals("reject")){
                            rejectJobs =  rejectJobs+1;
                        }
                    }
                    rejectJob.setText(""+rejectJobs);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            Query queery2 = transection.orderByChild("driverId").equalTo(driverId);
            queery2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rating.setRating(0);
                    double ratings= 0;
                    int i = 0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Transection transection = snapshot.getValue(Transection.class);
                        double getrating = transection.getRating();
                        ratings = ratings+getrating;
                        i = i+1;
                    }
                    double totalRate = ratings/i;
                    rating.setRating((float) totalRate);
                    rating.setIsIndicator(true);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            dialog.show();

            final Button call = dialog.findViewById(R.id.callDriver);
            final Button hireDriver = dialog.findViewById(R.id.hireDriver);
            final EditText getDate = dialog.findViewById(R.id.getDate);
            final EditText details = dialog.findViewById(R.id.detailsOfJob);
            final EditText number = dialog.findViewById(R.id.phoneNumber);

            final Button selectLocation = dialog.findViewById(R.id.selectLocation);
            hireDriver.setVisibility(View.GONE);
            call.setVisibility(View.GONE);

            selectLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(FindGasFitter.this,GetLocationMapsActivity.class);
                    startActivity(intent);
                    hireDriver.setVisibility(View.VISIBLE);
                    call.setVisibility(View.VISIBLE);
                }
            });

            getDate.setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                            getDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }

            });

            hireDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String dateOfHire = getDate.getText().toString().trim();
                    final String detailInfo = details.getText().toString().trim();
                    final String jobStatus = "pending";
                    final int price = 50;
                    createJobRefarence = rootReference.child("driverJob");

                    LatLang latLang = new LatLang();
                    double lat = latLang.getLatiture();
                    double lon = latLang.getLongiture();

                    String userNumber = number.getText().toString().trim();
                    String jobId = createJobRefarence.push().getKey();
                    HireGesFitter hireDriver = new HireGesFitter(userId,driverId,jobId,jobStatus,dateOfHire,detailInfo,userNumber,price,lat,lon);
                    createJobRefarence.child(jobId).setValue(hireDriver);
                    Toast.makeText(FindGasFitter.this, "Hire Request Submitted Successfully.", Toast.LENGTH_SHORT).show();
                    mRecyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(FindGasFitter.this);
                    dialog.dismiss();
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNumber));

                    if (ActivityCompat.checkSelfPermission(FindGasFitter.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    startActivity(callIntent);

                }
            });
        }
        else {

            firebaseAuth = FirebaseAuth.getInstance();

            final Registration registration = driverlistNext.get(position);
            final String driverId = registration.getuId();
            final String phoneNumber = registration.getuPhone();
            final String userId = firebaseAuth.getCurrentUser().getUid();;

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.hire_electrician);
            final TextView totalJob = dialog.findViewById(R.id.complitedJob);
            final TextView rejectJob = dialog.findViewById(R.id.rejectedJob);
            final RatingBar rating = dialog.findViewById(R.id.customerViewRatingBar);
            dialog.setTitle("Create Job");


            Query queery = availableDriverRefarence.orderByChild("gessFitterId").equalTo(driverId);
            queery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    totalJob.setText(null);
                    int complitedJob = 0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        HireGesFitter hireDriver = snapshot.getValue(HireGesFitter.class);
                        if (hireDriver.getJobStatus().equals("complete")){
                            complitedJob =  complitedJob+1;
                        }
                    }

                    totalJob.setText(""+complitedJob);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            Query queery1 = availableDriverRefarence.orderByChild("gessFitterId").equalTo(driverId);
            queery1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rejectJob.setText(null);
                    int rejectJobs = 0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        HireGesFitter hireDriver = snapshot.getValue(HireGesFitter.class);
                        if (hireDriver.getJobStatus().equals("reject")){
                            rejectJobs =  rejectJobs+1;
                        }
                    }
                    rejectJob.setText(""+rejectJobs);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            Query queery2 = transection.orderByChild("driverId").equalTo(driverId);
            queery2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    rating.setRating(0);
                    double ratings= 0;
                    int i = 0;
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Transection transection = snapshot.getValue(Transection.class);
                        double getrating = transection.getRating();
                        ratings = ratings+getrating;
                        i = i+1;
                    }
                    double totalRate = ratings/i;
                    rating.setRating((float) totalRate);
                    rating.setIsIndicator(true);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            dialog.show();

            final Button call = dialog.findViewById(R.id.callDriver);
            final Button hireDriver = dialog.findViewById(R.id.hireDriver);
            final EditText getDate = dialog.findViewById(R.id.getDate);
            final EditText details = dialog.findViewById(R.id.detailsOfJob);
            final EditText number = dialog.findViewById(R.id.phoneNumber);
            final Button selectLocation = dialog.findViewById(R.id.selectLocation);
            hireDriver.setVisibility(View.GONE);
            call.setVisibility(View.GONE);

            selectLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(FindGasFitter.this,GetLocationMapsActivity.class);
                    startActivity(intent);
                    hireDriver.setVisibility(View.VISIBLE);
                    call.setVisibility(View.VISIBLE);
                }
            });

            getDate.setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {

                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                            getDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }

            });

            hireDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String dateOfHire = getDate.getText().toString().trim();
                    final String detailInfo = details.getText().toString().trim();
                    final String jobStatus = "pending";
                    final int price = 50;
                    createJobRefarence = rootReference.child("driverJob");

                    LatLang latLang = new LatLang();
                    double lat = latLang.getLatiture();
                    double lon = latLang.getLongiture();

                    String userNumber = number.getText().toString().trim();
                    String jobId = createJobRefarence.push().getKey();
                    HireGesFitter hireDriver = new HireGesFitter(userId,driverId,jobId,jobStatus,dateOfHire,detailInfo,userNumber,price,lat,lon);
                    createJobRefarence.child(jobId).setValue(hireDriver);
                    Toast.makeText(FindGasFitter.this, "Hire Request Submitted Successfully.", Toast.LENGTH_SHORT).show();
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickListener(FindGasFitter.this);
                    dialog.dismiss();
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNumber));

                    if (ActivityCompat.checkSelfPermission(FindGasFitter.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    startActivity(callIntent);

                }
            });

            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnItemClickListener(FindGasFitter.this);

            driverlistNext.clear();
        }

    }

}
