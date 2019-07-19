package com.example.jahadulrakib.servicehut.Plumber;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.ElectricianAdapter.ElectricianHistoryAdepter;
import com.example.jahadulrakib.servicehut.PlumberAdepter.PlumberHistoryAdepter;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Transection.Transection;
import com.example.jahadulrakib.servicehut.UserPojo.HireElectrician;
import com.example.jahadulrakib.servicehut.UserPojo.HirePlumber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PlumberJobHisotry extends Fragment implements PlumberHistoryAdepter.OnItemClickListener {

    private RecyclerView nRecyclerView;
    RecyclerView.LayoutManager layoutManagers;
    private ArrayList<HirePlumber> compliteList;
    private DatabaseReference rootReReference;
    private DatabaseReference jobRefarence,paymentRefarence;
    PlumberHistoryAdepter adapters;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.driver_job_history, container, false);


        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        rootReReference = FirebaseDatabase.getInstance().getReference();
        jobRefarence = rootReReference.child("driverJob");
        paymentRefarence = rootReReference.child("transection");

        nRecyclerView = v.findViewById(R.id.jobHistory);
        compliteList = new ArrayList<>();
        adapters = new PlumberHistoryAdepter(getActivity(), compliteList);
        layoutManagers = new LinearLayoutManager(getActivity());
        nRecyclerView.setLayoutManager(layoutManagers);
        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
        final TextView textView = v.findViewById(R.id.totalBill);

        Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
        queery2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView.setText(null);
                int i = 0;
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Transection transection = snapshot.getValue(Transection.class);
                    int getrating = transection.getTotal();
                    i = i+getrating;
                }
                textView.setText(""+i);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        Query getAeecptedJob = jobRefarence.orderByChild("plumberId").equalTo(userId);
        getAeecptedJob.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                compliteList.clear();
                for (DataSnapshot job : dataSnapshot.getChildren()) {
                    HirePlumber currentJob = job.getValue(HirePlumber.class);
                    if(currentJob.getJobStatus().equals("complete")){
                        compliteList.add(currentJob);
                    }
                }

                nRecyclerView.setAdapter(adapters);
                adapters.setOnItemClickListener(PlumberJobHisotry.this);
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
    public void OnItemClicks(int position) {

    }
}
