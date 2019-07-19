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
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Favourate extends Fragment implements ViewOwnToLet.OnItemClickListener{
    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ArrayList<CreateToLetPojo> toletList;
    private DatabaseReference rootReReference;
    private DatabaseReference getFavourateRefarence,getToletRefarence;
    ViewOwnToLet adapter;
    private FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favourate, container, false);

        auth = FirebaseAuth.getInstance();
        rootReReference = FirebaseDatabase.getInstance().getReference();
        getFavourateRefarence = rootReReference.child("favourate");
        getToletRefarence = rootReReference.child("postToLet");
        final String userId = auth.getCurrentUser().getUid();
        mRecyclerView = v.findViewById(R.id.favourateRecycler);
        toletList = new ArrayList<>();
        adapter = new ViewOwnToLet(getActivity(), toletList);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        final Query query = getFavourateRefarence.orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toletList.clear();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    String t_id = d.child("toLetId").getValue(String.class);
                    Query query1 = getToletRefarence.orderByChild("toLetId").equalTo(t_id);
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                CreateToLetPojo ctlp = ds.getValue(CreateToLetPojo.class);
                                toletList.add(ctlp);
                            }
                            mRecyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(Favourate.this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
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
    public void OnItemClick(final int position) {

        final CreateToLetPojo createToLetPojo = toletList.get(position);
        final String toletId = createToLetPojo.getToLetId();
        final double lat = createToLetPojo.getLat();
        final double lon = createToLetPojo.getLon();

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.view_favourate);
        dialog.show();
        ImageView mHomeImg = dialog.findViewById(R.id.imgHome);
        TextView mRentMonth = dialog.findViewById(R.id.month);
        TextView rentType = dialog.findViewById(R.id.rentFor);
        TextView clientType = dialog.findViewById(R.id.rentForMenorWomen);
        TextView details = dialog.findViewById(R.id.itemDetails);

        Button remove = dialog.findViewById(R.id.btnRemoveFevourate);
        Button call = dialog.findViewById(R.id.btnCall);
        Button mapView = dialog.findViewById(R.id.btnMap);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLang latLang = new LatLang();
                latLang.setLatiture(lat);
                latLang.setLongiture(lon);
                Intent intent = new Intent(getActivity(), SetLocationMapsActivity.class);
                startActivity(intent);
            }
        });

        Picasso.with(getActivity()).load(createToLetPojo.getToLetImageURL()).into(mHomeImg);
        mRentMonth.setText(createToLetPojo.getToLetMonth());
        rentType.setText(createToLetPojo.getToLetType());
        clientType.setText(createToLetPojo.getType());

        details.setText(
                "Phone Number: "+createToLetPojo.getToLetPhone()+"\n"+
                "Monthly Rent : "+createToLetPojo.getToLetMonthRent()+"\n"+
                "Advance : "+createToLetPojo.getToLetAdvance()+"\n"+
                "Location : "+createToLetPojo.getToLetAddress()+"\n"+
                "About : "+createToLetPojo.getToLetDetails()+"\n"
        );

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateToLetPojo pojo = toletList.get(position);
                String toletId = pojo.getToLetId();
                final String user_id = auth.getCurrentUser().getUid();
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("favourate");
                Query delete = databaseReference.orderByChild("toLetId").equalTo(toletId);
                delete.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String u_id = snapshot.child("userId").getValue(String.class);
                            if (u_id.equals(user_id)){
                                String fav_id = snapshot.child("favId").getValue(String.class);
                                databaseReference.child(fav_id).removeValue();
                            }
                            Toast.makeText(getActivity(), "Remove Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                dialog.dismiss();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = createToLetPojo.getToLetPhone();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phoneNumber));

                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        return;
                    }
                    startActivity(callIntent);
                dialog.dismiss();
            }
        });
    }

    }
