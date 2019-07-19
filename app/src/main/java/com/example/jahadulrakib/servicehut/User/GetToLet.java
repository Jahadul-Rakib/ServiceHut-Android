package com.example.jahadulrakib.servicehut.User;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jahadulrakib.servicehut.LatLang;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.ToLetMap.SetLocationMapsActivity;
import com.example.jahadulrakib.servicehut.UserAdepter.ViewOwnToLet;
import com.example.jahadulrakib.servicehut.UserPojo.CreateToLetPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetToLet extends AppCompatActivity implements ViewOwnToLet.OnItemClickListener {
    private  boolean okFlag;
    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<CreateToLetPojo> toletList,toletList1;
    private DatabaseReference rootReReference;
    private DatabaseReference estimateRefarence,favourateRefarence;
    ViewOwnToLet adapter;
    EditText search;
    private FirebaseAuth auth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_to_let);

        search = findViewById(R.id.txtSearchToLet);
        auth = FirebaseAuth.getInstance();
        rootReReference = FirebaseDatabase.getInstance().getReference();
        estimateRefarence = rootReReference.child("postToLet");
        //favourateRefarence = rootReReference.child("favourateToLet");
        mRecyclerView = findViewById(R.id.getToLetRecyclerView);
        toletList = new ArrayList<>();
        adapter = new ViewOwnToLet(GetToLet.this, toletList);
        layoutManager = new LinearLayoutManager(GetToLet.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        estimateRefarence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toletList.clear();
                for (DataSnapshot tolet : dataSnapshot.getChildren()) {
                    CreateToLetPojo toLet = tolet.getValue(CreateToLetPojo.class);
                    toletList.add(toLet);
                }
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(GetToLet.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

        toletList1 = new ArrayList<>();

        for (CreateToLetPojo item : toletList) {
            if (item.getToLetMonth().toLowerCase().contains(text.toLowerCase())) {
                toletList1.add(item);
            }
            else if (item.getToLetMonthRent().toLowerCase().contains(text.toLowerCase())) {
                toletList1.add(item);
            }
            else if (item.getToLetAdvance().toLowerCase().contains(text.toLowerCase())) {
                toletList1.add(item);
            }
            else if (item.getType().toLowerCase().contains(text.toLowerCase())) {
                toletList1.add(item);
            }
            else if (item.getToLetType().toLowerCase().contains(text.toLowerCase())) {
                toletList1.add(item);
            }
            else if (item.getToLetAddress().toLowerCase().contains(text.toLowerCase())) {
                toletList1.add(item);
            }
        }
        adapter.filterList(toletList1);

    }


    @Override
    public void OnItemClick(final int position) {
        if (toletList1 == null){

            CreateToLetPojo createToLetPojo = toletList.get(position);
            final String toletId = createToLetPojo.getToLetId();
            final double lat = createToLetPojo.getLat();
            final double lon = createToLetPojo.getLon();

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.view_add);
            dialog.show();

            TextView mRentMonth = dialog.findViewById(R.id.month);
            TextView rentType = dialog.findViewById(R.id.rentFor);
            TextView clientType = dialog.findViewById(R.id.rentForMenorWomen);
            TextView details = dialog.findViewById(R.id.itemDetails);

            Button favourate = dialog.findViewById(R.id.btnAddFevourate);
            Button call = dialog.findViewById(R.id.btnCall);
            Button mapView = dialog.findViewById(R.id.btnMap);
            mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LatLang latLang = new LatLang();
                    latLang.setLatiture(lat);
                    latLang.setLongiture(lon);
                    Intent intent = new Intent(GetToLet.this, SetLocationMapsActivity.class);
                    startActivity(intent);
                }
            });

            mRentMonth.setText(createToLetPojo.getToLetMonth());
            rentType.setText(createToLetPojo.getToLetType());
            clientType.setText(createToLetPojo.getType());

            details.setText("Phone Number: "+createToLetPojo.getToLetPhone()+"\n"+
                    "Monthly Rent : "+createToLetPojo.getToLetMonthRent()+"\n"+
                    "Advance : "+createToLetPojo.getToLetAdvance()+"\n"+
                    "Location : "+createToLetPojo.getToLetAddress()+"\n"+
                    "About : "+createToLetPojo.getToLetDetails()+"\n"

            );

            favourate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String userId = auth.getCurrentUser().getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("favourate");
                    Query query = databaseReference.orderByChild("userId").equalTo(userId);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChildren()){
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    CreateToLetPojo ctlp = ds.getValue(CreateToLetPojo.class);
                                    if(ctlp.getToLetId().equals(toletId)){
                                        okFlag = false;
                                        break;
                                    }
                                    else{
                                        okFlag = true;
                                        continue;
                                    }
                                }

                            }
                            else{
                                okFlag = true;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if (okFlag == true){

                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("favourate");
                        String favId = dr.push().getKey();
                        CreateToLetPojo  createToLetPojo = new CreateToLetPojo(userId,toletId, favId);
                        dr.child(favId).setValue(createToLetPojo);
                        Toast.makeText(GetToLet.this, "Added To Favourate.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else if(okFlag == false){
                        Toast.makeText(GetToLet.this, "Already Added To Favourate.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateToLetPojo createToLetPojo3 = toletList.get(position);
                    String phoneNumber = createToLetPojo3.getToLetPhone();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNumber));

                    if (ActivityCompat.checkSelfPermission(GetToLet.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    startActivity(callIntent);
                    dialog.dismiss();
                }
            });
        }
        else {

            CreateToLetPojo createToLetPojo = toletList1.get(position);
            final String toletId = createToLetPojo.getToLetId();
            final double lat = createToLetPojo.getLat();
            final double lon = createToLetPojo.getLon();

            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.view_add);
            dialog.show();

            TextView mRentMonth = dialog.findViewById(R.id.month);
            TextView rentType = dialog.findViewById(R.id.rentFor);
            TextView clientType = dialog.findViewById(R.id.rentForMenorWomen);
            TextView details = dialog.findViewById(R.id.itemDetails);

            Button favourate = dialog.findViewById(R.id.btnAddFevourate);
            Button call = dialog.findViewById(R.id.btnCall);
            Button mapView = dialog.findViewById(R.id.btnMap);
            mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LatLang latLang = new LatLang();
                    latLang.setLatiture(lat);
                    latLang.setLongiture(lon);
                    Intent intent = new Intent(GetToLet.this, SetLocationMapsActivity.class);
                    startActivity(intent);

                }
            });

            mRentMonth.setText(createToLetPojo.getToLetMonth());
            rentType.setText(createToLetPojo.getToLetType());
            clientType.setText(createToLetPojo.getType());

            details.setText("Phone Number: "+createToLetPojo.getToLetPhone()+"\n"+
                    "Monthly Rent : "+createToLetPojo.getToLetMonthRent()+"\n"+
                    "Advance : "+createToLetPojo.getToLetAdvance()+"\n"+
                    "Location : "+createToLetPojo.getToLetAddress()+"\n"+
                    "About : "+createToLetPojo.getToLetDetails()+"\n"

            );

            favourate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String userId = auth.getCurrentUser().getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("favourate");
                    Query query = databaseReference.orderByChild("userId").equalTo(userId);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChildren()){
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    CreateToLetPojo ctlp = ds.getValue(CreateToLetPojo.class);
                                    if(ctlp.getToLetId().equals(toletId)){
                                        okFlag = false;
                                        break;
                                    }
                                    else{
                                        okFlag = true;
                                        continue;
                                    }
                                }

                            }
                            else{
                                okFlag = true;
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if (okFlag == true){

                        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("favourate");
                        String favId = dr.push().getKey();
                        CreateToLetPojo  createToLetPojo = new CreateToLetPojo(userId,toletId, favId);
                        dr.child(favId).setValue(createToLetPojo);
                        Toast.makeText(GetToLet.this, "Added To Favourate.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else if(okFlag == false){
                        Toast.makeText(GetToLet.this, "Already Added To Favourate.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateToLetPojo createToLetPojo3 = toletList1.get(position);
                    String phoneNumber = createToLetPojo3.getToLetPhone();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNumber));

                    if (ActivityCompat.checkSelfPermission(GetToLet.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    startActivity(callIntent);
                    dialog.dismiss();
                }
            });

            toletList1.clear();
            mRecyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(GetToLet.this);
        }
        }



}
