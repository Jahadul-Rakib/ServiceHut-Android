package com.example.jahadulrakib.servicehut.GasFiter;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jahadulrakib.servicehut.AddItems.AddItem;
import com.example.jahadulrakib.servicehut.AddItems.ItemAdepter;
import com.example.jahadulrakib.servicehut.Electrician.ElectricianProfile;
import com.example.jahadulrakib.servicehut.GasFiterAdepter.GasAvailableJobAdepter;
import com.example.jahadulrakib.servicehut.GasFiterAdepter.GasHistoryAdepter;
import com.example.jahadulrakib.servicehut.LatLang;
import com.example.jahadulrakib.servicehut.Notification.NotificationAdeptere;
import com.example.jahadulrakib.servicehut.Notification.NotificationService;
import com.example.jahadulrakib.servicehut.PlumberAdepter.PlumberAvailableJobAdepter;
import com.example.jahadulrakib.servicehut.PlumberAdepter.PlumberHistoryAdepter;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Registration;
import com.example.jahadulrakib.servicehut.ToLetMap.SetLocationMapsActivity;
import com.example.jahadulrakib.servicehut.Transection.Transection;
import com.example.jahadulrakib.servicehut.UserPojo.HireGesFitter;
import com.example.jahadulrakib.servicehut.UserPojo.HirePlumber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GasProfile extends Fragment implements GasAvailableJobAdepter.OnItemClickListener,
        GasHistoryAdepter.OnItemClickListener,
        NotificationAdeptere.OnItemClickListener,ItemAdepter.OnItemClickListener
{
    private RecyclerView mRecyclerView;
    private RecyclerView nRecyclerView;
    private RecyclerView pRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager layoutManagers;
    RecyclerView.LayoutManager layoutManageres;
    private ArrayList<HireGesFitter> jobList;
    private ArrayList<HireGesFitter> acceptList;
    private ArrayList<NotificationService> notificationList;
    private DatabaseReference rootReReference;
    private DatabaseReference jobRefarence,transectionRefarence,notificationRefarence,userTable,invoiceRefarence;
    GasAvailableJobAdepter adapter;
    GasHistoryAdepter adapters;
    NotificationAdeptere notificationAdapter;
    private FirebaseAuth auth;
    String driverID;
    RecyclerView rRecyclerView;
    RecyclerView.LayoutManager rlayoutManager;
    ArrayList<AddItem> itemList;
    ItemAdepter radapter;
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
        invoiceRefarence = rootReReference.child("invoice");

        mRecyclerView = v.findViewById(R.id.jobDriver);
        nRecyclerView = v.findViewById(R.id.currentJobDriver);
        pRecyclerView = v.findViewById(R.id.notificationDriver);

        jobList = new ArrayList<>();
        acceptList = new ArrayList<>();
        notificationList = new ArrayList<>();

        adapter = new GasAvailableJobAdepter(getActivity(), jobList);
        adapters = new GasHistoryAdepter(getActivity(), acceptList);
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
                notificationAdapter.setOnItemClickListener(GasProfile.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query query = jobRefarence.orderByChild("gessFitterId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jobList.clear();
                for (DataSnapshot job : dataSnapshot.getChildren()) {
                    HireGesFitter totalJob = job.getValue(HireGesFitter.class);
                    if(totalJob.getJobStatus().equals("pending")){
                        jobList.add(totalJob);
                    }
                }
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(GasProfile.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query getAeecptedJob = jobRefarence.orderByChild("gessFitterId").equalTo(userId);
        getAeecptedJob.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                acceptList.clear();
                for (DataSnapshot job : dataSnapshot.getChildren()) {
                    HireGesFitter currentJob = job.getValue(HireGesFitter.class);
                    if(currentJob.getJobStatus().equals("accept")){
                        acceptList.add(currentJob);
                    }
                }

                nRecyclerView.setAdapter(adapters);
                adapters.setOnItemClickListener(GasProfile.this);
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

        final HireGesFitter hireDriver = jobList.get(position);
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
                HireGesFitter updayeDriver = new HireGesFitter(hireDriver.getUserId(), hireDriver.getGessFitterId(), jobId,
                        "accept",hireDriver.getDateOfHire(), hireDriver.getDetailInfo(),
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

                HireGesFitter updayeDriver = new HireGesFitter(hireDriver.getUserId(), hireDriver.getGessFitterId(), jobId,
                "reject", hireDriver.getDateOfHire(),
                        hireDriver.getDetailInfo(), hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                jobRefarence.child(jobId).setValue(updayeDriver);

                HireGesFitter updayeDrivers = new HireGesFitter(hireDriver.getUserId(), hireDriver.getGessFitterId(), jobId,
                        "NotNow", hireDriver.getDateOfHire(),
                        hireDriver.getDetailInfo(), hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                jobRefarence.child(jobIdNew).setValue(updayeDrivers);

                String userId = hireDriver.getUserId();
                driverID = hireDriver.getGessFitterId();
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

        final HireGesFitter hireDrivers = acceptList.get(position);
        final String phoneNumber = hireDrivers.getNumber();
        final String jobId = hireDrivers.getJobId();

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.driver_currentjob_responce);
        dialog.show();
        Button reject = dialog.findViewById(R.id.cancleJob);
        Button call = dialog.findViewById(R.id.call);
        Button invoice = dialog.findViewById(R.id.btnInvoice);
        invoice.setVisibility(View.VISIBLE);
        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                final Dialog dialog1 = new Dialog(getActivity());
                dialog1.setContentView(R.layout.make_invoice);


                final EditText amount = dialog1.findViewById(R.id.amountMoneyss);
                final TextView total = dialog1.findViewById(R.id.totalAmount);



                dialog1.show();
                Button addMoney = dialog1.findViewById(R.id.addItem);
                addMoney.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double takeMm;
                        try {
                            takeMm = new Double(amount.getText().toString());
                        } catch (NumberFormatException e) {
                            takeMm = 0;
                        }
                        EditText details = dialog1.findViewById(R.id.addDetails);
                        String dtails =details.getText().toString().trim();
                        String invoiceId = invoiceRefarence.push().getKey();
                        AddItem addItem =new AddItem(jobId,invoiceId,dtails, takeMm);
                        invoiceRefarence.child(invoiceId).setValue(addItem);
                        amount.setText(null);
                        details.setText(null);
                    }
                });
                rRecyclerView = dialog1.findViewById(R.id.addedItem);
                itemList = new ArrayList<>();
                radapter = new ItemAdepter(getActivity(), itemList);
                rRecyclerView.setLayoutManager(rlayoutManager);
                rRecyclerView.setItemAnimator(new DefaultItemAnimator());
                Query query = invoiceRefarence.orderByChild("jobId").equalTo(jobId);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        itemList.clear();
                        double a =0;
                        for (DataSnapshot job : dataSnapshot.getChildren()) {
                            AddItem itemValue = job.getValue(AddItem.class);
                            itemList.add(itemValue);
                            double take= itemValue.getAmountOfMoney();
                            a = a + take;
                        }
                        total.setText("ToTal Amount Of Money: "+ a);
                        rRecyclerView.setAdapter(radapter);
                        rRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        radapter.setOnItemClickListener(GasProfile.this);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


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

                HireGesFitter updayeDriver = new HireGesFitter(hireDrivers.getUserId(), hireDrivers.getGessFitterId(), jobId,
                        "reject", hireDrivers.getDateOfHire(), hireDrivers.getDetailInfo(),
                        hireDrivers.getNumber(), hireDrivers.getTotals(),hireDrivers.getLat(),hireDrivers.getLon());
                jobRefarence.child(jobId).setValue(updayeDriver);

                HireGesFitter updayeDrivers = new HireGesFitter(hireDrivers.getUserId(), hireDrivers.getGessFitterId(), jobId,
                        "NotNow", hireDrivers.getDateOfHire(), hireDrivers.getDetailInfo(),
                        hireDrivers.getNumber(), hireDrivers.getTotals(),hireDrivers.getLat(),hireDrivers.getLon());
                jobRefarence.child(jobIdNew).setValue(updayeDrivers);

                Transection transection = new Transection(jobId,hireDrivers.getGessFitterId(),25);
                transectionRefarence.child(jobId).setValue(transection);
                String userId = hireDrivers.getUserId();
                driverID = hireDrivers.getGessFitterId();
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

    @Override
    public void OnAddItemClick(int position) {

        AddItem addItem =itemList.get(position);
        final String itemID = addItem.getItemId();

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.control_item);
        Button cancleItem = dialog.findViewById(R.id.cancle);
        Button deleteItem = dialog.findViewById(R.id.delete);
        dialog.show();

        cancleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceRefarence.child(itemID).removeValue();
                Toast.makeText(getActivity(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
}
