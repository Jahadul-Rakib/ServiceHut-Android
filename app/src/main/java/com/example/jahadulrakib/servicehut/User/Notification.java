package com.example.jahadulrakib.servicehut.User;

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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.AddItems.AddItem;
import com.example.jahadulrakib.servicehut.AddItems.ItemAdepter;
import com.example.jahadulrakib.servicehut.DriverAdapter.DriverNotNowNotificationAdepter;
import com.example.jahadulrakib.servicehut.ElectricianAdapter.ElectricianAcceptNotificationAdepter;
import com.example.jahadulrakib.servicehut.ElectricianAdapter.ElectricianNotNowNotificationAdepter;
import com.example.jahadulrakib.servicehut.ElectricianAdapter.ElectricianRejectNotificationAdepter;
import com.example.jahadulrakib.servicehut.GasFiterAdepter.GasAcceptNotificationAdepter;
import com.example.jahadulrakib.servicehut.GasFiterAdepter.GasNotNowNotificationAdepter;
import com.example.jahadulrakib.servicehut.GasFiterAdepter.GasRejectNotificationAdepter;
import com.example.jahadulrakib.servicehut.LabourAdepter.LabourAcceptNotificationAdepter;
import com.example.jahadulrakib.servicehut.LabourAdepter.LabourNotNowNotificationAdepter;
import com.example.jahadulrakib.servicehut.LabourAdepter.LabourRejectNotificationAdepter;
import com.example.jahadulrakib.servicehut.NurseAdepter.NurseAcceptNotificationAdepter;
import com.example.jahadulrakib.servicehut.NurseAdepter.NurseNotNowNotificationAdepter;
import com.example.jahadulrakib.servicehut.NurseAdepter.NurseRejectNotificationAdepter;
import com.example.jahadulrakib.servicehut.PlumberAdepter.PlumberAcceptNotificationAdepter;
import com.example.jahadulrakib.servicehut.PlumberAdepter.PlumberNotNowNotificationAdepter;
import com.example.jahadulrakib.servicehut.PlumberAdepter.PlumberRejectNotificationAdepter;
import com.example.jahadulrakib.servicehut.Transection.Transection;
import com.example.jahadulrakib.servicehut.DriverAdapter.DriverAcceptNotificationAdepter;
import com.example.jahadulrakib.servicehut.DriverAdapter.DriverRejectNotificationAdepter;
import com.example.jahadulrakib.servicehut.Notification.NotificationAdeptere;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.UserPojo.HireDriver;
import com.example.jahadulrakib.servicehut.Registration;
import com.example.jahadulrakib.servicehut.Notification.NotificationService;
import com.example.jahadulrakib.servicehut.UserPojo.HireElectrician;
import com.example.jahadulrakib.servicehut.UserPojo.HireGesFitter;
import com.example.jahadulrakib.servicehut.UserPojo.HireLabour;
import com.example.jahadulrakib.servicehut.UserPojo.HireNurse;
import com.example.jahadulrakib.servicehut.UserPojo.HirePlumber;
import com.example.jahadulrakib.servicehut.UserRepeat.FindDrivers;
import com.example.jahadulrakib.servicehut.UserServices.FindElectrician;
import com.example.jahadulrakib.servicehut.UserServices.FindGasFitter;
import com.example.jahadulrakib.servicehut.UserServices.FindLabour;
import com.example.jahadulrakib.servicehut.UserServices.FindNurse;
import com.example.jahadulrakib.servicehut.UserServices.FindPlumber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notification extends Fragment implements
        DriverAcceptNotificationAdepter.OnItemClickListener,
        DriverRejectNotificationAdepter.OnItemClickListener,
        DriverNotNowNotificationAdepter.OnItemClickListener,
        NurseAcceptNotificationAdepter.OnItemClickListener,
        NurseRejectNotificationAdepter.OnItemClickListener,
        NurseNotNowNotificationAdepter.OnItemClickListener,
        LabourAcceptNotificationAdepter.OnItemClickListener,
        LabourRejectNotificationAdepter.OnItemClickListener,
        LabourNotNowNotificationAdepter.OnItemClickListener,
        ElectricianAcceptNotificationAdepter.OnItemClickListener,
        ElectricianRejectNotificationAdepter.OnItemClickListener,
        ElectricianNotNowNotificationAdepter.OnItemClickListener,
        PlumberAcceptNotificationAdepter.OnItemClickListener,
        PlumberRejectNotificationAdepter.OnItemClickListener,
        PlumberNotNowNotificationAdepter.OnItemClickListener,
        GasAcceptNotificationAdepter.OnItemClickListener,
        GasRejectNotificationAdepter.OnItemClickListener,
        GasNotNowNotificationAdepter.OnItemClickListener,
        NotificationAdeptere.OnItemClickListener,ItemAdepter.OnItemClickListener {
    private RecyclerView mDriverRecyclerView,mNurseRecyclerView,mLabourRecyclerView,mElectricianRecyclerView,mPlumberRecyclerView,mGasfitterRecyclerView,
            nDriverRecyclerView,nNurseRecyclerView,nLabourRecyclerView,nElectricianRecyclerView,nPlumberRecyclerView,nGasfitterRecyclerView,
            oDriverRecyclerView,oNurseRecyclerView,oLabourRecyclerView,oElectricianRecyclerView,oPlumberRecyclerView,oGasfitterRecyclerView, pRecyclerView;
    RecyclerView.LayoutManager driverLayoutManager,nurseLayoutManager,labourLayoutManager,electricianLayoutManager,plumberLayoutManager,gasLayoutManager,
            driverLayoutManagers,nurseLayoutManagers,labourLayoutManagers,elecricianLayoutManagers,plumberLayoutManagers,gasLayoutManagers,
            odriverLayoutManagers,onurseLayoutManagers,olabourLayoutManagers,oelecricianLayoutManagers,oplumberLayoutManagers,ogasLayoutManagers;

    private ArrayList<HireDriver> driverJobList, CreatedPendingDriverJobList,odriverJobList;
    private ArrayList<HireNurse> nurseJobList, CreatedPendingNurseJobList,onurseJobList;
    private ArrayList<HireLabour> labourJobList, CreatedPendingLabourJobList,olabourJobList;
    private ArrayList<HireElectrician> electricianJobList, CreatedPendingElectricianJobList,oelectricianJobList;
    private ArrayList<HirePlumber> plumberJobList, CreatedPendingPlumberJobList,oplumberJobList;
    private ArrayList<HireGesFitter> gasJobList, CreatedPendingGasJobList,ogasJobList;

    private ArrayList<NotificationService> notificationList;
    private DatabaseReference rootReference;
    private DatabaseReference driverRefarence,transectionRefarence,notificationRefarence,userTable,invoiceRefarence;

    DriverAcceptNotificationAdepter driverAdapter;
    NurseAcceptNotificationAdepter nurseAdapter;
    LabourAcceptNotificationAdepter labourAdapter;
    ElectricianAcceptNotificationAdepter electricianAdapter;
    PlumberAcceptNotificationAdepter plumberAdapter;
    GasAcceptNotificationAdepter gasAdapter;

    DriverRejectNotificationAdepter driverAdapters;
    NurseRejectNotificationAdepter nurseAdapters;
    LabourRejectNotificationAdepter labourAdapters;
    ElectricianRejectNotificationAdepter electricianAdapters;
    PlumberRejectNotificationAdepter plumberAdapters;
    GasRejectNotificationAdepter gasAdapters;


    DriverNotNowNotificationAdepter odriverAdapters;
    NurseNotNowNotificationAdepter onurseAdapters;
    LabourNotNowNotificationAdepter olabourAdapters;
    ElectricianNotNowNotificationAdepter oelectricianAdapters;
    PlumberNotNowNotificationAdepter oplumberAdapters;
    GasNotNowNotificationAdepter ogasAdapters;

    RecyclerView.LayoutManager layoutManageres;
    RecyclerView.LayoutManager olayoutManageres;
    NotificationAdeptere notificationAdapter;
    FirebaseAuth auth;
    RecyclerView rRecyclerView;
    RecyclerView.LayoutManager rlayoutManager;
    ArrayList<AddItem> itemList;
    ItemAdepter radapter;
    double a =0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notification, container, false);

        auth = FirebaseAuth.getInstance();
        rootReference = FirebaseDatabase.getInstance().getReference();
        driverRefarence = rootReference.child("driverJob");
        transectionRefarence = rootReference.child("transection");
        notificationRefarence = rootReference.child("notification");
        userTable = rootReference.child("user");
        invoiceRefarence = rootReference.child("invoice");


        mDriverRecyclerView = v.findViewById(R.id.createdJobHistryDriver);
        mNurseRecyclerView = v.findViewById(R.id.createdJobHistryNurse);
        mLabourRecyclerView = v.findViewById(R.id.createdJobHistryLabour);
        mElectricianRecyclerView = v.findViewById(R.id.createdJobHistryElectrician);
        mPlumberRecyclerView = v.findViewById(R.id.createdJobHistryPlumber);
        mGasfitterRecyclerView = v.findViewById(R.id.createdJobHistryGass);

        nDriverRecyclerView = v.findViewById(R.id.createdPendingJobHistryDrivers);
        nNurseRecyclerView = v.findViewById(R.id.createdPendingJobHistryNurses);
        nLabourRecyclerView = v.findViewById(R.id.createdPendingJobHistryLabours);
        nElectricianRecyclerView = v.findViewById(R.id.createdPendingJobHistryElectricians);
        nPlumberRecyclerView = v.findViewById(R.id.createdPendingJobHistryPlumbers);
        nGasfitterRecyclerView = v.findViewById(R.id.createdPendingJobHistryGass);


        oDriverRecyclerView = v.findViewById(R.id.createdPendingJobHistryDriver);
        oNurseRecyclerView = v.findViewById(R.id.createdPendingJobHistryNurse);
        oLabourRecyclerView = v.findViewById(R.id.createdPendingJobHistryLabour);
        oElectricianRecyclerView = v.findViewById(R.id.createdPendingJobHistryElectrician);
        oPlumberRecyclerView = v.findViewById(R.id.createdPendingJobHistryPlumber);
        oGasfitterRecyclerView = v.findViewById(R.id.createdPendingJobHistryGas);

        pRecyclerView = v.findViewById(R.id.customerNotification);

        driverJobList = new ArrayList<HireDriver>();
        nurseJobList = new ArrayList<HireNurse>();
        labourJobList = new ArrayList<HireLabour>();
        electricianJobList = new ArrayList<HireElectrician>();
        plumberJobList = new ArrayList<HirePlumber>();
        gasJobList = new ArrayList<HireGesFitter>();

        CreatedPendingDriverJobList = new ArrayList<HireDriver>();
        CreatedPendingNurseJobList = new ArrayList<HireNurse>();
        CreatedPendingLabourJobList = new ArrayList<HireLabour>();
        CreatedPendingElectricianJobList = new ArrayList<HireElectrician>();
        CreatedPendingPlumberJobList = new ArrayList<HirePlumber>();
        CreatedPendingGasJobList = new ArrayList<HireGesFitter>();

        odriverJobList = new ArrayList<HireDriver>();
        onurseJobList = new ArrayList<HireNurse>();
        olabourJobList = new ArrayList<HireLabour>();
        oelectricianJobList = new ArrayList<HireElectrician>();
        oplumberJobList = new ArrayList<HirePlumber>();
        ogasJobList = new ArrayList<HireGesFitter>();

        notificationList = new ArrayList<NotificationService>();

        driverAdapter = new DriverAcceptNotificationAdepter(getActivity(), driverJobList);
        nurseAdapter = new NurseAcceptNotificationAdepter(getActivity(), nurseJobList);
        labourAdapter = new LabourAcceptNotificationAdepter(getActivity(), labourJobList);
        electricianAdapter = new ElectricianAcceptNotificationAdepter(getActivity(), electricianJobList);
        plumberAdapter = new PlumberAcceptNotificationAdepter(getActivity(), plumberJobList);
        gasAdapter = new GasAcceptNotificationAdepter(getActivity(), gasJobList);


        driverAdapters = new DriverRejectNotificationAdepter(getActivity(), CreatedPendingDriverJobList);
        nurseAdapters = new NurseRejectNotificationAdepter(getActivity(), CreatedPendingNurseJobList);
        labourAdapters = new LabourRejectNotificationAdepter(getActivity(), CreatedPendingLabourJobList);
        electricianAdapters = new ElectricianRejectNotificationAdepter(getActivity(), CreatedPendingElectricianJobList);
        plumberAdapters = new PlumberRejectNotificationAdepter(getActivity(), CreatedPendingPlumberJobList);
        gasAdapters = new GasRejectNotificationAdepter(getActivity(), CreatedPendingGasJobList);

        odriverAdapters = new DriverNotNowNotificationAdepter(getActivity(), odriverJobList);
        onurseAdapters = new NurseNotNowNotificationAdepter(getActivity(), onurseJobList);
        olabourAdapters = new LabourNotNowNotificationAdepter(getActivity(), olabourJobList);
        oelectricianAdapters = new ElectricianNotNowNotificationAdepter(getActivity(), oelectricianJobList);
        oplumberAdapters = new PlumberNotNowNotificationAdepter(getActivity(), oplumberJobList);
        ogasAdapters = new GasNotNowNotificationAdepter(getActivity(), ogasJobList);


        notificationAdapter = new NotificationAdeptere(getActivity(), notificationList);


        driverLayoutManager = new LinearLayoutManager(getActivity());
        nurseLayoutManager = new LinearLayoutManager(getActivity());
        labourLayoutManager = new LinearLayoutManager(getActivity());
        electricianLayoutManager = new LinearLayoutManager(getActivity());
        plumberLayoutManager = new LinearLayoutManager(getActivity());
        gasLayoutManager = new LinearLayoutManager(getActivity());

        driverLayoutManagers = new LinearLayoutManager(getActivity());
        nurseLayoutManagers = new LinearLayoutManager(getActivity());
        labourLayoutManagers = new LinearLayoutManager(getActivity());
        elecricianLayoutManagers = new LinearLayoutManager(getActivity());
        plumberLayoutManagers = new LinearLayoutManager(getActivity());
        gasLayoutManagers = new LinearLayoutManager(getActivity());

        odriverLayoutManagers = new LinearLayoutManager(getActivity());
        onurseLayoutManagers = new LinearLayoutManager(getActivity());
        olabourLayoutManagers = new LinearLayoutManager(getActivity());
        oelecricianLayoutManagers = new LinearLayoutManager(getActivity());
        oplumberLayoutManagers = new LinearLayoutManager(getActivity());
        ogasLayoutManagers = new LinearLayoutManager(getActivity());

        layoutManageres = new LinearLayoutManager(getActivity());

        mDriverRecyclerView.setLayoutManager(driverLayoutManager);
        mNurseRecyclerView.setLayoutManager(nurseLayoutManager);
        mLabourRecyclerView.setLayoutManager(labourLayoutManager);
        mElectricianRecyclerView.setLayoutManager(electricianLayoutManager);
        mPlumberRecyclerView.setLayoutManager(plumberLayoutManager);
        mGasfitterRecyclerView.setLayoutManager(gasLayoutManager);

        nDriverRecyclerView.setLayoutManager(driverLayoutManagers);
        nNurseRecyclerView.setLayoutManager(nurseLayoutManagers);
        nLabourRecyclerView.setLayoutManager(labourLayoutManagers);
        nElectricianRecyclerView.setLayoutManager(elecricianLayoutManagers);
        nPlumberRecyclerView.setLayoutManager(plumberLayoutManagers);
        nGasfitterRecyclerView.setLayoutManager(gasLayoutManagers);

        oDriverRecyclerView.setLayoutManager(odriverLayoutManagers);
        oNurseRecyclerView.setLayoutManager(onurseLayoutManagers);
        oLabourRecyclerView.setLayoutManager(olabourLayoutManagers);
        oElectricianRecyclerView.setLayoutManager(oelecricianLayoutManagers);
        oPlumberRecyclerView.setLayoutManager(oplumberLayoutManagers);
        oGasfitterRecyclerView.setLayoutManager(ogasLayoutManagers);

        pRecyclerView.setLayoutManager(layoutManageres);

        mDriverRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNurseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLabourRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mElectricianRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mPlumberRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mGasfitterRecyclerView.setItemAnimator(new DefaultItemAnimator());

        nDriverRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nNurseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nLabourRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nElectricianRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nPlumberRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nGasfitterRecyclerView.setItemAnimator(new DefaultItemAnimator());

        oDriverRecyclerView.setItemAnimator(new DefaultItemAnimator());
        oNurseRecyclerView.setItemAnimator(new DefaultItemAnimator());
        oLabourRecyclerView.setItemAnimator(new DefaultItemAnimator());
        oElectricianRecyclerView.setItemAnimator(new DefaultItemAnimator());
        oPlumberRecyclerView.setItemAnimator(new DefaultItemAnimator());
        oGasfitterRecyclerView.setItemAnimator(new DefaultItemAnimator());

        pRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String userID = auth.getCurrentUser().getUid();

        Query notification = notificationRefarence.orderByChild("receiverId").equalTo(userID);
        notification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationList.clear();
                int a = 0;
                for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                    NotificationService notification = notifications.getValue(NotificationService.class);
                        notificationList.add(notification);
                }
                pRecyclerView.setAdapter(notificationAdapter);
                notificationAdapter.setOnItemClickListener(Notification.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query query = driverRefarence.orderByChild("userId").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driverJobList.clear();
                nurseJobList.clear();
                labourJobList.clear();
                electricianJobList.clear();
                plumberJobList.clear();
                gasJobList.clear();
                for ( DataSnapshot jobList: dataSnapshot.getChildren()){
                    final HireDriver hireDrivers = jobList.getValue(HireDriver.class);
                    String jobStatus = hireDrivers.getJobStatus();
                    if (!(hireDrivers.getDriverId() == null) && jobStatus.equals("accept")) {

                            driverJobList.add(hireDrivers);
                    }
                    final HireNurse hireNurse = jobList.getValue(HireNurse.class);
                    String jobStatus2 = hireNurse.getJobStatus();
                    if (!(hireNurse.getNurseId() == null) && jobStatus2.equals("accept")) {
                            nurseJobList.add(hireNurse);
                    }
                    final HireLabour hireLabour = jobList.getValue(HireLabour.class);
                    String jobStatus3 = hireLabour.getJobStatus();
                    if (!(hireLabour.getLabourId() == null)) {
                        if (jobStatus3.equals("accept")) {
                            labourJobList.add(hireLabour);
                        }
                    }
                    final HireElectrician hireElectrician = jobList.getValue(HireElectrician.class);
                    String jobStatus4 = hireElectrician.getJobStatus();
                    if (!(hireElectrician.getElectricianId()== null)) {
                        if (jobStatus4.equals("accept")) {
                            electricianJobList.add(hireElectrician);
                        }
                    }
                    final HirePlumber hirePlumber = jobList.getValue(HirePlumber.class);
                    String jobStatus5 = hirePlumber.getJobStatus();
                    if (!(hirePlumber.getPlumberId()== null)) {
                        if (jobStatus5.equals("accept")) {
                            plumberJobList.add(hirePlumber);
                        }
                    }
                    final HireGesFitter hireGas = jobList.getValue(HireGesFitter.class);
                    String jobStatus6 = hireGas.getJobStatus();
                    if (!(hireGas.getGessFitterId()== null)) {
                        if (jobStatus6.equals("accept")) {
                            gasJobList.add(hireGas);
                        }
                    }
                }
                mDriverRecyclerView.setAdapter(driverAdapter);
                driverAdapter.setOnItemClickListener(Notification.this);

                mNurseRecyclerView.setAdapter(nurseAdapter);
                nurseAdapter.setOnItemClickListener(Notification.this);

                mLabourRecyclerView.setAdapter(labourAdapter);
                labourAdapter.setOnItemClickListener(Notification.this);

                mElectricianRecyclerView.setAdapter(electricianAdapter);
                electricianAdapter.setOnItemClickListener(Notification.this);

                mPlumberRecyclerView.setAdapter(plumberAdapter);
                plumberAdapter.setOnItemClickListener(Notification.this);

                mGasfitterRecyclerView.setAdapter(gasAdapter);
                gasAdapter.setOnItemClickListener(Notification.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query query2 = driverRefarence.orderByChild("userId").equalTo(userID);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CreatedPendingDriverJobList.clear();
                CreatedPendingNurseJobList.clear();
                CreatedPendingLabourJobList.clear();
                CreatedPendingElectricianJobList.clear();
                CreatedPendingPlumberJobList.clear();
                CreatedPendingGasJobList.clear();

                for ( DataSnapshot jobList: dataSnapshot.getChildren()){
                    final HireDriver hireDrivers = jobList.getValue(HireDriver.class);
                    String jobStatus = hireDrivers.getJobStatus();
                    if(!(hireDrivers.getDriverId()== null)) {
                        if (jobStatus.equals("pending")) {
                            CreatedPendingDriverJobList.add(hireDrivers);
                        }
                    }
                    final HireNurse hireNurse = jobList.getValue(HireNurse.class);
                    String jobStatus2 = hireNurse.getJobStatus();
                    if (!(hireNurse.getNurseId()== null)) {
                        if (jobStatus2.equals("pending")) {
                            CreatedPendingNurseJobList.add(hireNurse);
                        }
                    }
                    final HireLabour hireLabour = jobList.getValue(HireLabour.class);
                    String jobStatus3 = hireLabour.getJobStatus();
                    if (!(hireLabour.getLabourId()== null)) {
                        if (jobStatus3.equals("pending")) {
                            CreatedPendingLabourJobList.add(hireLabour);
                        }
                    }
                    final HireElectrician hireElectrician = jobList.getValue(HireElectrician.class);
                    String jobStatus4 = hireElectrician.getJobStatus();
                    if (!(hireElectrician.getElectricianId()== null)) {
                        if (jobStatus4.equals("pending")) {
                            CreatedPendingElectricianJobList.add(hireElectrician);
                        }
                    }
                    final HirePlumber hirePlumber = jobList.getValue(HirePlumber.class);
                    String jobStatus5 = hirePlumber.getJobStatus();
                    if (!(hirePlumber.getPlumberId()== null)) {
                        if (jobStatus5.equals("pending")) {
                            CreatedPendingPlumberJobList.add(hirePlumber);
                        }
                    }
                    final HireGesFitter hireGas = jobList.getValue(HireGesFitter.class);
                    String jobStatus6 = hireGas.getJobStatus();
                    if (!(hireGas.getGessFitterId()== null)) {
                        if (jobStatus6.equals("pending")) {
                            CreatedPendingGasJobList.add(hireGas);
                        }
                    }
                }



                nDriverRecyclerView.setAdapter(driverAdapters);
                driverAdapters.setOnItemClickListener(Notification.this);

                nNurseRecyclerView.setAdapter(nurseAdapters);
                nurseAdapters.setOnItemClickListener(Notification.this);

                nLabourRecyclerView.setAdapter(labourAdapters);
                labourAdapters.setOnItemClickListener(Notification.this);

                nElectricianRecyclerView.setAdapter(electricianAdapters);
                electricianAdapters.setOnItemClickListener(Notification.this);

                nPlumberRecyclerView.setAdapter(plumberAdapters);
                plumberAdapters.setOnItemClickListener(Notification.this);

                nGasfitterRecyclerView.setAdapter(gasAdapters);
                gasAdapters.setOnItemClickListener(Notification.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query query3 = driverRefarence.orderByChild("userId").equalTo(userID);
        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                odriverJobList.clear();
                onurseJobList.clear();
                olabourJobList.clear();
                oelectricianJobList.clear();
                oplumberJobList.clear();
                ogasJobList.clear();

                for ( DataSnapshot jobList: dataSnapshot.getChildren()){
                    final HireDriver hireDrivers = jobList.getValue(HireDriver.class);
                    String jobStatus = hireDrivers.getJobStatus();
                    if(!(hireDrivers.getDriverId()== null)) {
                        if (jobStatus.equals("NotNow")) {
                            odriverJobList.add(hireDrivers);
                        }
                    }
                    final HireNurse hireNurse = jobList.getValue(HireNurse.class);
                    String jobStatus2 = hireNurse.getJobStatus();
                    if (!(hireNurse.getNurseId()== null)) {
                        if (jobStatus2.equals("NotNow")) {
                            onurseJobList.add(hireNurse);
                        }
                    }
                    final HireLabour hireLabour = jobList.getValue(HireLabour.class);
                    String jobStatus3 = hireLabour.getJobStatus();
                    if (!(hireLabour.getLabourId()== null)) {
                        if (jobStatus3.equals("NotNow")) {
                            olabourJobList.add(hireLabour);
                        }
                    }
                    final HireElectrician hireElectrician = jobList.getValue(HireElectrician.class);
                    String jobStatus4 = hireElectrician.getJobStatus();
                    if (!(hireElectrician.getElectricianId()== null)) {
                        if (jobStatus4.equals("NotNow")) {
                            oelectricianJobList.add(hireElectrician);
                        }
                    }
                    final HirePlumber hirePlumber = jobList.getValue(HirePlumber.class);
                    String jobStatus5 = hirePlumber.getJobStatus();
                    if (!(hirePlumber.getPlumberId()== null)) {
                        if (jobStatus5.equals("NotNow")) {
                            oplumberJobList.add(hirePlumber);
                        }
                    }
                    final HireGesFitter hireGas = jobList.getValue(HireGesFitter.class);
                    String jobStatus6 = hireGas.getJobStatus();
                    if (!(hireGas.getGessFitterId()== null)) {
                        if (jobStatus6.equals("NotNow")) {
                            ogasJobList.add(hireGas);
                        }
                    }
                }



                oDriverRecyclerView.setAdapter(odriverAdapters);
                odriverAdapters.setOnItemClickListener(Notification.this);

                oNurseRecyclerView.setAdapter(onurseAdapters);
                onurseAdapters.setOnItemClickListener(Notification.this);

                oLabourRecyclerView.setAdapter(olabourAdapters);
                olabourAdapters.setOnItemClickListener(Notification.this);

                oElectricianRecyclerView.setAdapter(oelectricianAdapters);
                oelectricianAdapters.setOnItemClickListener(Notification.this);

                oPlumberRecyclerView.setAdapter(oplumberAdapters);
                oplumberAdapters.setOnItemClickListener(Notification.this);

                oGasfitterRecyclerView.setAdapter(ogasAdapters);
                ogasAdapters.setOnItemClickListener(Notification.this);

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

        final HireDriver hireDriver = driverJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobId = hireDriver.getJobId();
        final String driverId = hireDriver.getDriverId();
        final int totals = hireDriver.getTotals();

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.customer_responce);
        dialog.setTitle("Are You Confirm ?");
        dialog.show();
        Button compliteJob = dialog.findViewById(R.id.complete);
        Button cancleJob = dialog.findViewById(R.id.cancle);
        Button call = dialog.findViewById(R.id.call);

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

        compliteJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RatingBar ratingBar = dialog.findViewById(R.id.ratingBarOfDriver);
                double getratingValue = ratingBar.getRating();

                int total = (5*totals)/100;

                Transection transection = new Transection(jobId,driverId,total,getratingValue);
                transectionRefarence.child(jobId).setValue(transection);
                HireDriver updayeDriver = new HireDriver(hireDriver.getUserId(), hireDriver.getDriverId(), jobId,
                        "complete", hireDriver.getCarType(), hireDriver.getHireForDay(), hireDriver.getDateOfHire(), hireDriver.getDetailInfo(),
                        hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                driverRefarence.child(jobId).setValue(updayeDriver);
                dialog.dismiss();
            }
        });
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void OnItemClicks(int position) {

        final HireDriver hireDriver = CreatedPendingDriverJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobID = hireDriver.getJobId();
        final String userId = hireDriver.getUserId();
        final String driverId = hireDriver.getDriverId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pending_notification);
        Button call = dialog.findViewById(R.id.call);
        Button cancleJob = dialog.findViewById(R.id.cancleJob);
        dialog.show();

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
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = auth.getCurrentUser().getUid();
                Query queryforUserName = userTable.orderByChild("uId").equalTo(userID);
                queryforUserName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Registration registration = dataSnapshot1.getValue(Registration.class);
                            String fullName = registration.getFullName();
                            //Toast.makeText(getActivity(), fullName, Toast.LENGTH_LONG).show();
                            driverRefarence.child(jobID).removeValue();
                            String notificationId = notificationRefarence.push().getKey();

                            NotificationService notification = new NotificationService(notificationId,userID, driverId,
                                    "Sorry Dear, I do not want this Service At Now.", fullName);
                            notificationRefarence.child(notificationId).setValue(notification);
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();


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
    public void OnItemElectricianClick(int position) {

        final HireElectrician hireDriver = electricianJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobId = hireDriver.getJobId();
        final String driverId = hireDriver.getElectricianId();
        final int totals = hireDriver.getTotals();

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.dynamic_responce);
        dialog.setTitle("Are You Confirm ?");
        dialog.show();
        Button compliteJob = dialog.findViewById(R.id.complete);
        Button cancleJob = dialog.findViewById(R.id.cancle);
        Button call = dialog.findViewById(R.id.call);
        final TextView total = dialog.findViewById(R.id.totalMoney);
        rRecyclerView = dialog.findViewById(R.id.getItem);
        itemList = new ArrayList<>();
        radapter = new ItemAdepter(getActivity(), itemList);
        rRecyclerView.setLayoutManager(rlayoutManager);
        rRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Query query = invoiceRefarence.orderByChild("jobId").equalTo(jobId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                a = 0;
                for (DataSnapshot job : dataSnapshot.getChildren()) {
                    AddItem itemValue = job.getValue(AddItem.class);
                    itemList.add(itemValue);
                    double take= itemValue.getAmountOfMoney();
                    a = a + take;
                }
                total.setText("ToTal Amount Pay: "+ a);
                rRecyclerView.setAdapter(radapter);
                rRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                radapter.setOnItemClickListener(Notification.this);

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

        compliteJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RatingBar ratingBar = dialog.findViewById(R.id.ratingBarOfDriver);
                double getratingValue = ratingBar.getRating();
                int total = (int) (((15*totals)/100)+((10*a)/100));
                Transection transection = new Transection(jobId,driverId,total,getratingValue);
                transectionRefarence.child(jobId).setValue(transection);
                HireElectrician updayeDriver = new HireElectrician(hireDriver.getUserId(), hireDriver.getElectricianId(), jobId,
                        "complete",  hireDriver.getDateOfHire(), hireDriver.getDetailInfo(),
                        hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                driverRefarence.child(jobId).setValue(updayeDriver);
                dialog.dismiss();
            }
        });
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void OnItemElectricianClicks(int position) {

        final HireElectrician hireDriver = CreatedPendingElectricianJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobID = hireDriver.getJobId();
        final String userId = hireDriver.getUserId();
        final String driverId = hireDriver.getElectricianId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pending_notification);
        Button call = dialog.findViewById(R.id.call);
        Button cancleJob = dialog.findViewById(R.id.cancleJob);
        dialog.show();

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
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = auth.getCurrentUser().getUid();
                Query queryforUserName = userTable.orderByChild("uId").equalTo(userID);
                queryforUserName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Registration registration = dataSnapshot1.getValue(Registration.class);
                            String fullName = registration.getFullName();
                            //Toast.makeText(getActivity(), fullName, Toast.LENGTH_LONG).show();
                            driverRefarence.child(jobID).removeValue();
                            String notificationId = notificationRefarence.push().getKey();

                            NotificationService notification = new NotificationService(notificationId,userID, driverId,
                                    "Sorry Dear, I do not want this Service At Now.", fullName);
                            notificationRefarence.child(notificationId).setValue(notification);
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();


            }
        });

    }

    @Override
    public void OnItemGasClick(int position) {

        final HireGesFitter hireDriver = gasJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobId = hireDriver.getJobId();
        final String driverId = hireDriver.getGessFitterId();
        final int totals = hireDriver.getTotals();

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.dynamic_responce);
        dialog.setTitle("Are You Confirm ?");
        dialog.show();
        Button compliteJob = dialog.findViewById(R.id.complete);
        Button cancleJob = dialog.findViewById(R.id.cancle);
        Button call = dialog.findViewById(R.id.call);
        final TextView total = dialog.findViewById(R.id.totalMoney);
        rRecyclerView = dialog.findViewById(R.id.getItem);
        itemList = new ArrayList<>();
        radapter = new ItemAdepter(getActivity(), itemList);
        rRecyclerView.setLayoutManager(rlayoutManager);
        rRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Query query = invoiceRefarence.orderByChild("jobId").equalTo(jobId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                a = 0;
                for (DataSnapshot job : dataSnapshot.getChildren()) {
                    AddItem itemValue = job.getValue(AddItem.class);
                    itemList.add(itemValue);
                    double take= itemValue.getAmountOfMoney();
                    a = a + take;
                }
                total.setText("ToTal Amount Pay: "+ a);
                rRecyclerView.setAdapter(radapter);
                rRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                radapter.setOnItemClickListener(Notification.this);

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

        compliteJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RatingBar ratingBar = dialog.findViewById(R.id.ratingBarOfDriver);
                double getratingValue = ratingBar.getRating();
                int total = (int) (((15*totals)/100)+((10*a)/100));
                Transection transection = new Transection(jobId,driverId,total,getratingValue);
                transectionRefarence.child(jobId).setValue(transection);
                HireGesFitter updayeDriver = new HireGesFitter(hireDriver.getUserId(), hireDriver.getGessFitterId(), jobId,
                        "complete", hireDriver.getDateOfHire(), hireDriver.getDetailInfo(),
                        hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                driverRefarence.child(jobId).setValue(updayeDriver);
                dialog.dismiss();
            }
        });
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void OnItemGasClicks(int position) {

        final HireGesFitter hireDriver = CreatedPendingGasJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobID = hireDriver.getJobId();
        final String userId = hireDriver.getUserId();
        final String driverId = hireDriver.getGessFitterId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pending_notification);
        Button call = dialog.findViewById(R.id.call);
        Button cancleJob = dialog.findViewById(R.id.cancleJob);
        dialog.show();

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
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = auth.getCurrentUser().getUid();
                Query queryforUserName = userTable.orderByChild("uId").equalTo(userID);
                queryforUserName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Registration registration = dataSnapshot1.getValue(Registration.class);
                            String fullName = registration.getFullName();
                            //Toast.makeText(getActivity(), fullName, Toast.LENGTH_LONG).show();
                            driverRefarence.child(jobID).removeValue();
                            String notificationId = notificationRefarence.push().getKey();

                            NotificationService notification = new NotificationService(notificationId,userID, driverId,
                                    "Sorry Dear, I do not want this Service At Now.", fullName);
                            notificationRefarence.child(notificationId).setValue(notification);
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();


            }
        });

    }

    @Override
    public void OnItemLabourClick(int position) {

        final HireLabour hireDriver = labourJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobId = hireDriver.getJobId();
        final String driverId = hireDriver.getLabourId();
        final int totals = hireDriver.getTotals();

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.customer_responce);
        dialog.setTitle("Are You Confirm ?");
        dialog.show();
        Button compliteJob = dialog.findViewById(R.id.complete);
        Button cancleJob = dialog.findViewById(R.id.cancle);
        Button call = dialog.findViewById(R.id.call);

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

        compliteJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RatingBar ratingBar = dialog.findViewById(R.id.ratingBarOfDriver);
                double getratingValue = ratingBar.getRating();

                int total = (5*totals)/100;

                Transection transection = new Transection(jobId,driverId,total,getratingValue);
                transectionRefarence.child(jobId).setValue(transection);
                HireLabour updayeDriver = new HireLabour(hireDriver.getUserId(), hireDriver.getLabourId(), jobId,
                        "complete", hireDriver.getCarType(), hireDriver.getHireForDay(), hireDriver.getDateOfHire(), hireDriver.getDetailInfo(),
                        hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                driverRefarence.child(jobId).setValue(updayeDriver);
                dialog.dismiss();
            }
        });
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void OnItemLabourClicks(int position) {

        final HireLabour hireDriver = CreatedPendingLabourJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobID = hireDriver.getJobId();
        final String userId = hireDriver.getUserId();
        final String driverId = hireDriver.getLabourId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pending_notification);
        Button call = dialog.findViewById(R.id.call);
        Button cancleJob = dialog.findViewById(R.id.cancleJob);
        dialog.show();

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
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = auth.getCurrentUser().getUid();
                Query queryforUserName = userTable.orderByChild("uId").equalTo(userID);
                queryforUserName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Registration registration = dataSnapshot1.getValue(Registration.class);
                            String fullName = registration.getFullName();
                            //Toast.makeText(getActivity(), fullName, Toast.LENGTH_LONG).show();
                            driverRefarence.child(jobID).removeValue();
                            String notificationId = notificationRefarence.push().getKey();

                            NotificationService notification = new NotificationService(notificationId,userID, driverId,
                                    "Sorry Dear, I do not want this Service At Now.", fullName);
                            notificationRefarence.child(notificationId).setValue(notification);
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();


            }
        });

    }

    @Override
    public void OnItemNurseClick(int position) {

        final HireNurse hireDriver = nurseJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobId = hireDriver.getJobId();
        final String driverId = hireDriver.getNurseId();
        final int totals = hireDriver.getTotals();

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.customer_responce);
        dialog.setTitle("Are You Confirm ?");
        dialog.show();
        Button compliteJob = dialog.findViewById(R.id.complete);
        Button cancleJob = dialog.findViewById(R.id.cancle);
        Button call = dialog.findViewById(R.id.call);

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

        compliteJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RatingBar ratingBar = dialog.findViewById(R.id.ratingBarOfDriver);
                double getratingValue = ratingBar.getRating();

                int total = (5*totals)/100;

                Transection transection = new Transection(jobId,driverId,total,getratingValue);
                transectionRefarence.child(jobId).setValue(transection);
                HireNurse updayeDriver = new HireNurse(hireDriver.getUserId(), hireDriver.getNurseId(), jobId,
                        "complete", hireDriver.getCarType(), hireDriver.getHireForDay(), hireDriver.getDateOfHire(), hireDriver.getDetailInfo(),
                        hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                driverRefarence.child(jobId).setValue(updayeDriver);
                dialog.dismiss();
            }
        });
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void OnItemNurseClicks(int position) {

        final HireNurse hireDriver = CreatedPendingNurseJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobID = hireDriver.getJobId();
        final String userId = hireDriver.getUserId();
        final String driverId = hireDriver.getNurseId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pending_notification);
        Button call = dialog.findViewById(R.id.call);
        Button cancleJob = dialog.findViewById(R.id.cancleJob);
        dialog.show();

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
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = auth.getCurrentUser().getUid();
                Query queryforUserName = userTable.orderByChild("uId").equalTo(userID);
                queryforUserName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Registration registration = dataSnapshot1.getValue(Registration.class);
                            String fullName = registration.getFullName();
                            //Toast.makeText(getActivity(), fullName, Toast.LENGTH_LONG).show();
                            driverRefarence.child(jobID).removeValue();
                            String notificationId = notificationRefarence.push().getKey();

                            NotificationService notification = new NotificationService(notificationId,userID, driverId,
                                    "Sorry Dear, I do not want this Service At Now.", fullName);
                            notificationRefarence.child(notificationId).setValue(notification);
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();


            }
        });

    }

    @Override
    public void OnItemPlumberClick(int position) {

        final HirePlumber hireDriver = plumberJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobId = hireDriver.getJobId();
        final String driverId = hireDriver.getPlumberId();
        final int totals = hireDriver.getTotals();

        final Dialog dialog= new Dialog(getActivity());
        dialog.setContentView(R.layout.dynamic_responce);
        dialog.setTitle("Are You Confirm ?");
        dialog.show();
        Button compliteJob = dialog.findViewById(R.id.complete);
        Button cancleJob = dialog.findViewById(R.id.cancle);
        Button call = dialog.findViewById(R.id.call);
        final TextView total = dialog.findViewById(R.id.totalMoney);
        rRecyclerView = dialog.findViewById(R.id.getItem);
        itemList = new ArrayList<>();
        radapter = new ItemAdepter(getActivity(), itemList);
        rRecyclerView.setLayoutManager(rlayoutManager);
        rRecyclerView.setItemAnimator(new DefaultItemAnimator());
        Query query = invoiceRefarence.orderByChild("jobId").equalTo(jobId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                a = 0;
                for (DataSnapshot job : dataSnapshot.getChildren()) {
                    AddItem itemValue = job.getValue(AddItem.class);
                    itemList.add(itemValue);
                    double take= itemValue.getAmountOfMoney();
                    a = a + take;
                }
                total.setText("ToTal Amount Pay: "+ a);
                rRecyclerView.setAdapter(radapter);
                rRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                radapter.setOnItemClickListener(Notification.this);

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

        compliteJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RatingBar ratingBar = dialog.findViewById(R.id.ratingBarOfDriver);
                double getratingValue = ratingBar.getRating();

                int total = (int) (((15*totals)/100)+((10*a)/100));

                Transection transection = new Transection(jobId,driverId,total,getratingValue);
                transectionRefarence.child(jobId).setValue(transection);
                HirePlumber updayeDriver = new HirePlumber(hireDriver.getUserId(), hireDriver.getPlumberId(), jobId,
                        "complete", hireDriver.getDateOfHire(), hireDriver.getDetailInfo(),
                        hireDriver.getNumber(), hireDriver.getTotals(),hireDriver.getLat(),hireDriver.getLon());
                driverRefarence.child(jobId).setValue(updayeDriver);
                dialog.dismiss();
            }
        });
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    @Override
    public void OnItemPlumberClicks(int position) {

        final HirePlumber hireDriver = CreatedPendingPlumberJobList.get(position);
        final String phoneNumber = hireDriver.getNumber();
        final String jobID = hireDriver.getJobId();
        final String userId = hireDriver.getUserId();
        final String driverId = hireDriver.getPlumberId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pending_notification);
        Button call = dialog.findViewById(R.id.call);
        Button cancleJob = dialog.findViewById(R.id.cancleJob);
        dialog.show();

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
        cancleJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = auth.getCurrentUser().getUid();
                Query queryforUserName = userTable.orderByChild("uId").equalTo(userID);
                queryforUserName.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            Registration registration = dataSnapshot1.getValue(Registration.class);
                            String fullName = registration.getFullName();
                            driverRefarence.child(jobID).removeValue();
                            String notificationId = notificationRefarence.push().getKey();

                            NotificationService notification = new NotificationService(notificationId,userID, driverId,
                                    "Sorry Dear, I do not want this Service At Now.", fullName);
                            notificationRefarence.child(notificationId).setValue(notification);
                            break;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();


            }
        });

    }

    @Override
    public void OnDItemClicks(int position) {
        final HireDriver hireDriver = odriverJobList.get(position);
        final String jobID = hireDriver.getJobId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.rejected_order);
        dialog.show();
        Button delete = dialog.findViewById(R.id.btnDelete);
        Button hire = dialog.findViewById(R.id.btnHire);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                driverRefarence.child(jobID).removeValue();
                dialog.dismiss();
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindDrivers.class);
                intent.putExtra("object", hireDriver);
                startActivity(intent);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void OnItemEClick(int position) {

        final HireElectrician hireElectrician = oelectricianJobList.get(position);
        final String jobID = hireElectrician.getJobId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.rejected_order);
        dialog.show();
        Button delete = dialog.findViewById(R.id.btnDelete);
        Button hire = dialog.findViewById(R.id.btnHire);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverRefarence.child(jobID).removeValue();
                dialog.dismiss();
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),FindElectrician.class);
                intent.putExtra("object", hireElectrician);
                startActivity(intent);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void OnItemGClick(int position) {
        final HireGesFitter hireGesFitter = ogasJobList.get(position);
        final String jobID = hireGesFitter.getJobId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.rejected_order);
        dialog.show();
        Button delete = dialog.findViewById(R.id.btnDelete);
        Button hire = dialog.findViewById(R.id.btnHire);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverRefarence.child(jobID).removeValue();
                dialog.dismiss();
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindGasFitter.class);
                intent.putExtra("object", hireGesFitter);
                startActivity(intent);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void OnItemLClick(int position) {

        final HireLabour hireLabour = olabourJobList.get(position);
        final String jobID = hireLabour.getJobId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.rejected_order);
        dialog.show();
        Button delete = dialog.findViewById(R.id.btnDelete);
        Button hire = dialog.findViewById(R.id.btnHire);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverRefarence.child(jobID).removeValue();
                dialog.dismiss();
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindLabour.class);
                intent.putExtra("object", hireLabour);
                startActivity(intent);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void OnItemNClick(int position) {

        final HireNurse hireNurse = onurseJobList.get(position);
        final String jobID = hireNurse.getJobId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.rejected_order);
        dialog.show();
        Button delete = dialog.findViewById(R.id.btnDelete);
        Button hire = dialog.findViewById(R.id.btnHire);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverRefarence.child(jobID).removeValue();
                dialog.dismiss();
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),FindNurse.class);
                intent.putExtra("object", hireNurse);
                startActivity(intent);
                dialog.dismiss();

            }
        });

    }

    @Override
    public void OnItemPlClick(int position) {

        final HirePlumber hirePlumber = oplumberJobList.get(position);
        final String jobID = hirePlumber.getJobId();
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.rejected_order);
        dialog.show();
        Button delete = dialog.findViewById(R.id.btnDelete);
        Button hire = dialog.findViewById(R.id.btnHire);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverRefarence.child(jobID).removeValue();
                dialog.dismiss();
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FindPlumber.class);
                intent.putExtra("object", hirePlumber);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void OnAddItemClick(int position) {



    }
}
