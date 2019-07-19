package com.example.jahadulrakib.servicehut.Admin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Transection.AdminTransection;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminReportFragment extends Fragment {
    PieChart pieChart;
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
        View v = inflater.inflate(R.layout.admin_report_fragment, container, false);
        pieChart= v.findViewById(R.id.piechart_1);


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

                setPieChart(dtotalTaka,ltotalTaka,ntotalTaka,etotalTaka,ptotalTaka,gtotalTaka);

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

    public void setPieChart(int dtotalTaka, int ltotalTaka, int ntotalTaka, int etotalTaka, int ptotalTaka, int gtotalTaka) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(dtotalTaka,"Driver"));
        yValues.add(new PieEntry(ntotalTaka,"Nurse"));
        yValues.add(new PieEntry(ptotalTaka,"Plumber"));
        yValues.add(new PieEntry(ltotalTaka,"Labour"));
        yValues.add(new PieEntry(gtotalTaka,"Gas Fitter"));
        yValues.add(new PieEntry(etotalTaka,"Electrician"));

        PieDataSet dataSet = new PieDataSet(yValues, "Earn From Service");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);
        pieChart.setData(pieData);
    }
}
