package com.example.jahadulrakib.servicehut.Admin;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.jahadulrakib.servicehut.AdminAdapter.AllUserAdepter;
import com.example.jahadulrakib.servicehut.DriverAdapter.DriverJobHistoryAdepter;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Registration;
import com.example.jahadulrakib.servicehut.Transection.AdminTransection;
import com.example.jahadulrakib.servicehut.Transection.Transection;
import com.example.jahadulrakib.servicehut.UserPojo.HireDriver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdminClientFragment extends Fragment implements AllUserAdepter.OnItemClickListener,
        DriverJobHistoryAdepter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<Registration> userList, nextUserList;
    private DatabaseReference rootReference;
    private DatabaseReference databaseReference,availableDriverRefarence,paymentRefarence,adminTransection;
    AllUserAdepter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_client_fragment, container, false);

        rootReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = rootReference.child("user");
        availableDriverRefarence = rootReference.child("driverJob");
        paymentRefarence = rootReference.child("transection");
        adminTransection = rootReference.child("adminTransection");

        mRecyclerView = v.findViewById(R.id.getUser);
        userList = new ArrayList<>();
        adapter = new AllUserAdepter(getActivity(), userList);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Registration registration = snapshot.getValue(Registration.class);
                    userList.add(registration);
                }

                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                adapter.setOnItemClickListener(AdminClientFragment.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        EditText search = v.findViewById(R.id.search);
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

        return v;
    }

    private void filter(String text) {
        nextUserList = new ArrayList<>();
        for (Registration item : userList) {
            if (item.getFullName().toLowerCase().contains(text.toLowerCase())) {
                nextUserList.add(item);
            } else if (item.getUserType().toLowerCase().contains(text.toLowerCase())) {
                nextUserList.add(item);
            } else if (item.getuPhone().toLowerCase().contains(text.toLowerCase())) {
                nextUserList.add(item);
            }else if (item.getuEmail().toLowerCase().contains(text.toLowerCase())) {
                nextUserList.add(item);
            }
        }
        adapter.filterList(nextUserList);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(AdminClientFragment.this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void OnItemClick(int position) {
        if (nextUserList == null){
            Registration registration = userList.get(position);
            final String userId = registration.getuId();
            final String userType = registration.getUserType();
            final String phoneNumber = registration.getuPhone();

            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.user_view);
            dialog.setTitle("Please Perform Carefully");
            final TextView totalDue = dialog.findViewById(R.id.totalDue);
            final TextView totalJob = dialog.findViewById(R.id.totalJob);
            final TextView one = dialog.findViewById(R.id.one);
            final TextView two = dialog.findViewById(R.id.two);
            Button removeUser = dialog.findViewById(R.id.deleteUser);
            Button paidBill = dialog.findViewById(R.id.billPaid);
            Button call = dialog.findViewById(R.id.callServiceProveider);
            dialog.show();
            if (userType.equals("User")){
                one.setVisibility(View.GONE);
                two.setVisibility(View.GONE);
                totalDue.setVisibility(View.GONE);
                totalJob.setVisibility(View.GONE);
                paidBill.setVisibility(View.GONE);

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

                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });


            }
            else if (userType.equals("Driver")){
                Query queery = availableDriverRefarence.orderByChild("driverId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });

            }



            else if (userType.equals("Labour")){
                Query queery = availableDriverRefarence.orderByChild("labourId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (userType.equals("Nurse")){
                Query queery = availableDriverRefarence.orderByChild("nurseId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();

                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else if (userType.equals("Electrician")){
                Query queery = availableDriverRefarence.orderByChild("electricianId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();

                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else if (userType.equals("Plumber")){
                Query queery = availableDriverRefarence.orderByChild("plumberId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else if (userType.equals("GasFitter")){
                Query queery = availableDriverRefarence.orderByChild("gessFitterId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else {

                dialog.dismiss();
            }

        }

        else{

            Registration registration = nextUserList.get(position);
            final String userId = registration.getuId();
            final String userType = registration.getUserType();
            final String phoneNumber = registration.getuPhone();

            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.user_view);
            dialog.setTitle("Please Perform Carefully");
            final TextView totalDue = dialog.findViewById(R.id.totalDue);
            final TextView totalJob = dialog.findViewById(R.id.totalJob);
            final TextView one = dialog.findViewById(R.id.one);
            final TextView two = dialog.findViewById(R.id.two);
            Button removeUser = dialog.findViewById(R.id.deleteUser);
            Button paidBill = dialog.findViewById(R.id.billPaid);
            Button call = dialog.findViewById(R.id.callServiceProveider);
            dialog.show();
            if (userType.equals("User")){
                one.setVisibility(View.GONE);
                two.setVisibility(View.GONE);
                totalDue.setVisibility(View.GONE);
                totalJob.setVisibility(View.GONE);
                paidBill.setVisibility(View.GONE);

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

                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });
                nextUserList.clear();

            }
            else if (userType.equals("Driver")){
                Query queery = availableDriverRefarence.orderByChild("driverId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });
                nextUserList.clear();

            }



            else if (userType.equals("Labour")){
                Query queery = availableDriverRefarence.orderByChild("labourId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });
                nextUserList.clear();
            }
            else if (userType.equals("Nurse")){
                Query queery = availableDriverRefarence.orderByChild("nurseId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });
                nextUserList.clear();
            }
            else if (userType.equals("Electrician")){
                Query queery = availableDriverRefarence.orderByChild("electricianId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });
                nextUserList.clear();
            }
            else if (userType.equals("Plumber")){
                Query queery = availableDriverRefarence.orderByChild("plumberId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });
                nextUserList.clear();
            }
            else if (userType.equals("GasFitter")){
                Query queery = availableDriverRefarence.orderByChild("gessFitterId").equalTo(userId);
                queery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalJob.setText(null);
                        int complitedJob = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HireDriver hireDriver = snapshot.getValue(HireDriver.class);
                            if (hireDriver.getJobStatus().equals("complete")) {
                                complitedJob = complitedJob + 1;
                            }
                        }

                        totalJob.setText("" + complitedJob);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                final Query queery2 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                queery2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        totalDue.setText(null);
                        int i = 0;
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Transection transection = snapshot.getValue(Transection.class);
                            int getrating = transection.getTotal();
                            i = i+getrating;
                        }
                        totalDue.setText(""+i);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                paidBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Query queery12 = paymentRefarence.orderByChild("driverId").equalTo(userId);
                        queery12.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalDue.setText(null);
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    Transection transection = snapshot.getValue(Transection.class);
                                    String jobid = transection.getJobId();
                                    double rating = transection.getRating();
                                    int total = transection.getTotal();
                                    if (total > 0){
                                        AdminTransection transection2 = new AdminTransection(getDate(),userType,total);
                                        adminTransection.child(jobid).setValue(transection2);

                                        Transection transection1 = new Transection(jobid,userId,0,rating);
                                        paymentRefarence.child(jobid).setValue(transection1);
                                        Toast.makeText(getActivity(), "Successfull", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                });
                removeUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child(userId).removeValue();
                        Toast.makeText(getActivity(), "Successfull Delete", Toast.LENGTH_SHORT).show();
                    }
                });
                nextUserList.clear();
            }
            else {
                dialog.dismiss();
                nextUserList.clear();
            }

            nextUserList.clear();
        }

    }

    @Override
    public void OnItemClicks(int position) {

    }
    public String getDate(){
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }
}
