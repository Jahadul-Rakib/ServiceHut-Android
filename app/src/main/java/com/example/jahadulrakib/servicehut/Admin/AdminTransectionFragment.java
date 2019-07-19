package com.example.jahadulrakib.servicehut.Admin;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Transection.AdminTransection;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminTransectionFragment extends Fragment {

    private DatabaseReference rootReference,adminTransection,driverTransection;
    int dtotalTaka = 0;
    int ntotalTaka = 0;
    int ltotalTaka = 0;
    int etotalTaka = 0;
    int ptotalTaka = 0;
    int gtotalTaka = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.admin_transection_fragment, container, false);

        final TextView driver = v.findViewById(R.id.driverIncome);
        final TextView nurse = v.findViewById(R.id.nurseIncome);
        final TextView labour = v.findViewById(R.id.labourIncome);
        final TextView elect = v.findViewById(R.id.electricianIncome);
        final TextView gas = v.findViewById(R.id.gasIncome);
        final TextView plumber = v.findViewById(R.id.plumberIncome);
        final TextView dueTotal = v.findViewById(R.id.dueIncome);

        rootReference = FirebaseDatabase.getInstance().getReference();
        adminTransection = rootReference.child("adminTransection");
        driverTransection = rootReference.child("driverJob");
        adminTransection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                 dtotalTaka = 0;
                 ntotalTaka = 0;
                 ltotalTaka = 0;
                 etotalTaka = 0;
                 ptotalTaka = 0;
                 gtotalTaka = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    AdminTransection adminTransection = snapshot.getValue(AdminTransection.class);
                    int taka = adminTransection.getTotalAmount();
                    String userType = adminTransection.getUserType();
                    if (userType.equals("Driver")){
                        dtotalTaka = dtotalTaka+taka;
                    }
                    else if (userType.equals("Labour")){
                        ltotalTaka = ltotalTaka+taka;
                    }
                    else if (userType.equals("Nurse")){
                        ntotalTaka = ntotalTaka+taka;
                    }
                    else if (userType.equals("Electrician")){
                        etotalTaka = etotalTaka+taka;
                    }
                    else if (userType.equals("Plumber")){
                        ptotalTaka = ptotalTaka+taka;
                    }
                    else if (userType.equals("GasFitter")){
                        gtotalTaka = gtotalTaka+taka;
                    }
                    else {
                        break;
                    }
                }


                driver.setText(""+ dtotalTaka);
                nurse.setText(""+ ntotalTaka);
                labour.setText(""+ ltotalTaka);
                elect.setText(""+ etotalTaka);
                gas.setText(""+ gtotalTaka);
                plumber.setText(""+ ptotalTaka);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        driverTransection.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int totals = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    totals = snapshot.child("totals").getValue(Integer.class);

                }

                dueTotal.setText(""+ totals);
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
}
