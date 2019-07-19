package com.example.jahadulrakib.servicehut.Labour;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jahadulrakib.servicehut.LabourAdepter.LabourAvailableJobAdepter;
import com.example.jahadulrakib.servicehut.LabourAdepter.LabourJobHistoryAdepter;
import com.example.jahadulrakib.servicehut.LatLang;
import com.example.jahadulrakib.servicehut.Notification.NotificationAdeptere;
import com.example.jahadulrakib.servicehut.Notification.NotificationService;
import com.example.jahadulrakib.servicehut.NurseAdepter.NurseAvailableJobAdepter;
import com.example.jahadulrakib.servicehut.NurseAdepter.NurseJobHistoryAdepter;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Registration;
import com.example.jahadulrakib.servicehut.ToLetMap.SetLocationMapsActivity;
import com.example.jahadulrakib.servicehut.Transection.Transection;
import com.example.jahadulrakib.servicehut.UserPojo.HireLabour;
import com.example.jahadulrakib.servicehut.UserPojo.HireNurse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LabourProfile extends Fragment implements LabourAvailableJobAdepter.OnItemClickListener,LabourJobHistoryAdepter.OnItemClickListener,NotificationAdeptere.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView nRecyclerView;
    private RecyclerView pRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManagers;
    RecyclerView.LayoutManager layoutManageres;
    private ArrayList<HireLabour> jobList;
    private ArrayList<HireLabour> acceptList;
    private ArrayList<NotificationService> notificationList;
    private DatabaseReference rootReReference;
    private DatabaseReference jobRefarence,transectionRefarence,notificationRefarence,userTable;
    LabourAvailableJobAdepter adapter;
    LabourJobHistoryAdepter adapters;
    NotificationAdeptere notificationAdapter;
    private FirebaseAuth auth;
    String driverID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.driver_profile, container, false);

        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        rootReReference = FirebaseDatabase.getInstance().getReference();
        jobRefarence = rootReReference.child("driverJob");
        transectionRefarence = rootReReference.child("transection");
        notificationRefarence = rootReReference.child("notification");
        userTable = rootReReference.child("user");

        mRecyclerView = v.findViewById(R.id.jobDriver);
        nRecyclerView = v.findViewById(R.id.currentJobDriver);
        pRecyclerView = v.findViewById(R.id.notificationDriver);

        jobList = new ArrayList<>();
        acceptList = new ArrayList<>();
        notificationList = new ArrayList<>();

        adapter = new LabourAvailableJobAdepter(getActivity(), jobList);
        adapters = new LabourJobHistoryAdepter(getActivity(), acceptList);
        notificationAdapter = new NotificationAdeptere(getActivity(), notificationList);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManagers = new LinearLayoutManager(getActivity());
        layoutManageres = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);
        nRecyclerView.setLayoutManager(layoutManagers);
        pRecyclerView.setLayoutManager(layoutManageres);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pRecyclerView.setItemAnimator(new DefaultItemAnimator());



        Query notification = notificationRefarence.orderByChild("receiverId").equalTo(userId);
        notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                    NotificationService notificationss = notifications.getValue(NotificationService.class);
                        notificationList.add(notificationss);
                }

                pRecyclerView.setAdapter(notificationAdapter);
                notificationAdapter.setOnItemClickListener(LabourProfile.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        Query query = jobRefarence.orderByChild("labourId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jobList.clear();
                for (DataSnapshot job : dataSnapshot.getChildren()) {
                    HireLabour totalJob = job.getValue(HireLabour.class);
                    if(totalJob.getJobStatus().equals("pending")){
                        jobList.add(totalJob);
                    }
                }
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(LabourProfile.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query getAeecptedJob = jobRefarence.orderByChild("labourId").equalTo(userId);
        getAeecptedJob.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                acceptList.clear();
                for (DataSnapshot job : dataSnapshot.getChildren()) {
                    HireLabour currentJob = job.getValue(HireLabour.class);
                    if(currentJob.getJobStatus().equals("accept")){
                        acceptList.add(currentJob);
                    }
                }

                nRecyclerView.setAdapter(adapters);
                adapters.setOnItemClickListener(LabourProfile.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void OnItemClick(int position) {

        final HireLabour hireDriver = jobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobId = hireDriver.getJobId();

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.driver_responce);
        dialog.show();
        Button accept = dialog.findViewById(R.id.accept);
        Button reject = dialog.findViewById(R.id.reject);
        Button call = dialog.findViewById(R.id.call);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobRefarence = rootReReference.child("driverJob");
                HireLabour updayeDriver = new HireLabour(hireDriver.getUserId(), hireDriver.getLabourId(), jobId,
                        "accept", hireDriver.getCarType(), hireDriver.getHireForDay(), hireDriver.getDateOfHire(), hireDriver.getDetailInfo(),
                        hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                jobRefarence.child(jobId).setValue(updayeDriver);
                dialog.dismiss();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobRefarence = rootReReference.child("driverJob");
                String jobIdNew = jobRefarence.push().getKey();
                HireLabour updayeDriver = new HireLabour(hireDriver.getUserId(), hireDriver.getLabourId(), jobId,
                "reject", hireDriver.getCarType(), hireDriver.getHireForDay(), hireDriver.getDateOfHire(),
                        hireDriver.getDetailInfo(), hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                jobRefarence.child(jobId).setValue(updayeDriver);

                HireLabour updayeDrivers = new HireLabour(hireDriver.getUserId(), hireDriver.getLabourId(), jobId,
                        "NotNow", hireDriver.getCarType(), hireDriver.getHireForDay(), hireDriver.getDateOfHire(),
                        hireDriver.getDetailInfo(), hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                jobRefarence.child(jobIdNew).setValue(updayeDrivers);

                String userId = hireDriver.getUserId();
                driverID = hireDriver.getLabourId();
                rejectMessage(jobId,userId,driverID );
                dialog.dismiss();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));

                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                startActivity(callIntent);
            }
        });
    }
    public void OnItemClicks(int position){

        final HireLabour hireDrivers = acceptList.get(position);
        final String phoneNumber = hireDrivers.getNumber();
        final String jobId = hireDrivers.getJobId();

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.driver_currentjob_responce);
        dialog.show();
        Button reject = dialog.findViewById(R.id.cancleJob);
        Button call = dialog.findViewById(R.id.call);
        Button mapLocation = dialog.findViewById(R.id.mapLocation);
        mapLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final double lat = hireDrivers.getLat();
                final double lon = hireDrivers.getLon();
                LatLang latLang = new LatLang();
                latLang.setLatiture(lat);
                latLang.setLongiture(lon);
                Intent intent = new Intent(getActivity(), SetLocationMapsActivity.class);
                startActivity(intent);

            }
        });


        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobRefarence = rootReReference.child("driverJob");
                String jobIdNew = jobRefarence.push().getKey();
                HireLabour updayeDriver = new HireLabour(hireDrivers.getUserId(), hireDrivers.getLabourId(), jobId,
                        "reject", hireDrivers.getCarType(), hireDrivers.getHireForDay(), hireDrivers.getDateOfHire(), hireDrivers.getDetailInfo(),
                        hireDrivers.getNumber(), hireDrivers.getTotals(),hireDrivers.getLat(),hireDrivers.getLon());
                jobRefarence.child(jobId).setValue(updayeDriver);

                HireLabour updayeDrivers = new HireLabour(hireDrivers.getUserId(), hireDrivers.getLabourId(), jobId,
                        "NotNow", hireDrivers.getCarType(), hireDrivers.getHireForDay(), hireDrivers.getDateOfHire(), hireDrivers.getDetailInfo(),
                        hireDrivers.getNumber(), hireDrivers.getTotals(),hireDrivers.getLat(),hireDrivers.getLon());
                jobRefarence.child(jobIdNew).setValue(updayeDrivers);

                Transection transection = new Transection(jobId,hireDrivers.getLabourId(),25);
                transectionRefarence.child(jobId).setValue(transection);
                String userId = hireDrivers.getUserId();
                driverID = hireDrivers.getLabourId();
                rejectMessage(jobId,userId,driverID );



                dialog.dismiss();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phoneNumber));

                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                startActivity(callIntent);
            }
        });
    }

    private void rejectMessage(String jobId, final String userId, final String DriverId) {

        Query queryrr = userTable.orderByChild("uId").equalTo(driverID);
        queryrr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Registration registration = snapshot.getValue(Registration.class);
                    String fullName = registration.getFullName();
                    String notificationId = notificationRefarence.push().getKey();
                    NotificationService notification = new NotificationService(notificationId,driverID,userId,
                            "Sorry Sir, I am Not Able to Perform The Job At Now.",fullName);
                    notificationRefarence.child(notificationId).setValue(notification);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void OnItemClickes(int position) {
        NotificationService notificationService = notificationList.get(position);
        String id = notificationService.getNotificationId();
        notificationRefarence.child(id).removeValue();
    }
}
