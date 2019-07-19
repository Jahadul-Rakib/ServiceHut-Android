package com.example.jahadulrakib.servicehut.UserRepeat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Registration;
import com.example.jahadulrakib.servicehut.UserAdepter.FindGassFitterAdepter;
import com.example.jahadulrakib.servicehut.UserPojo.HirePlumber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindPlumbers extends AppCompatActivity implements FindGassFitterAdepter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Registration> driverList,driverlistNext;
    private DatabaseReference rootReference;
    private DatabaseReference driverRefarence,createJobRefarence,availableDriverRefarence,transection;
    FindGassFitterAdepter adapter;
    private int mYear, mMonth, mDay;
    final Context context = this;
    private FirebaseAuth firebaseAuth;
    HirePlumber hirePlumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_electrician);

        Intent i = getIntent();
        hirePlumber = (HirePlumber) i.getSerializableExtra("object");

        rootReference = FirebaseDatabase.getInstance().getReference();
        driverRefarence = rootReference.child("user");
        availableDriverRefarence = rootReference.child("driverJob");
        transection = rootReference.child("transection");
        mRecyclerView = findViewById(R.id.DriverRecyclerView);
        driverList = new ArrayList<>();
        adapter = new FindGassFitterAdepter(FindPlumbers.this, driverList);
        layoutManager = new LinearLayoutManager(FindPlumbers.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        Query query = driverRefarence.orderByChild("userType").equalTo("Plumber");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driverList.clear();
                for (final DataSnapshot driver : dataSnapshot.getChildren()) {
                    final Registration driverView = driver.getValue(Registration.class);
                    String driverId = driverView.getuId();
                    Query availabeDriver = availableDriverRefarence.orderByChild("plumberId").equalTo(driverId);
                    availabeDriver.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int a = 0;
                           for (DataSnapshot avDrivers : dataSnapshot.getChildren()) {
                               HirePlumber getDriver = avDrivers.getValue(HirePlumber.class);
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
                adapter.setOnItemClickListener(FindPlumbers.this);

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
            final String userId = firebaseAuth.getCurrentUser().getUid();;



            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.hire_again);
            Button yes = dialog.findViewById(R.id.btnYes);
            Button no = dialog.findViewById(R.id.btnNo);
            dialog.show();

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    hirePlumber = new HirePlumber(hirePlumber.getUserId(), driverId, hirePlumber.getJobId(),
                            "pending",
                            hirePlumber.getDateOfHire(),
                            hirePlumber.getDetailInfo(), hirePlumber.getNumber(), hirePlumber.getTotals(),hirePlumber.getLat(),
                            hirePlumber.getLon());
                    availableDriverRefarence.child(hirePlumber.getJobId()).setValue(hirePlumber);
                    dialog.dismiss();
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                }
            });


            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnItemClickListener(FindPlumbers.this);
        }
        else {

            firebaseAuth = FirebaseAuth.getInstance();
            final Registration registration = driverlistNext.get(position);
            final String driverId = registration.getuId();
            final String userId = firebaseAuth.getCurrentUser().getUid();;

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.hire_again);
            Button yes = dialog.findViewById(R.id.btnYes);
            Button no = dialog.findViewById(R.id.btnNo);
            dialog.show();

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    hirePlumber = new HirePlumber(hirePlumber.getUserId(), driverId, hirePlumber.getJobId(),
                            "pending",
                            hirePlumber.getDateOfHire(),
                            hirePlumber.getDetailInfo(), hirePlumber.getNumber(), hirePlumber.getTotals(),hirePlumber.getLat(),
                            hirePlumber.getLon());
                    availableDriverRefarence.child(hirePlumber.getJobId()).setValue(hirePlumber);
                    dialog.dismiss();
                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                }
            });

            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.setOnItemClickListener(FindPlumbers.this);
            driverlistNext.clear();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
