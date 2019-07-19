package com.example.jahadulrakib.servicehut.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.example.jahadulrakib.servicehut.MapService.MapsActivity;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.UserServices.FindDriver;
import com.example.jahadulrakib.servicehut.UserServices.FindElectrician;
import com.example.jahadulrakib.servicehut.UserServices.FindGasFitter;
import com.example.jahadulrakib.servicehut.UserServices.FindLabour;
import com.example.jahadulrakib.servicehut.UserServices.FindNurse;
import com.example.jahadulrakib.servicehut.UserServices.FindPlumber;

public class HomeFragment extends Fragment {
    Button postTolet,btnToLet,btnNearBy;
    ImageView driver,electrician,gasfitter,plumber,nurse,labour;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment_layout, container, false);
        postTolet = v.findViewById(R.id.btnOwnToLet);
        postTolet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),CreateToLet.class);
                startActivity(intent);
            }
        });

        btnToLet = v.findViewById(R.id.btnToLet);
        btnToLet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),GetToLet.class);
                startActivity(intent);
            }
        });
        driver = v.findViewById(R.id.imageDriver);
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent driver = new Intent(getActivity(),FindDriver.class);
                startActivity(driver);
            }
        });
        electrician = v.findViewById(R.id.imageElectrician);
        electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent electrician = new Intent(getActivity(),FindElectrician.class);
                startActivity(electrician);
            }
        });
        gasfitter = v.findViewById(R.id.imageGasFitter);
        gasfitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent electrician = new Intent(getActivity(),FindGasFitter.class);
                startActivity(electrician);
            }
        });
        plumber = v.findViewById(R.id.imagePlumber);
        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent electrician = new Intent(getActivity(),FindPlumber.class);
                startActivity(electrician);
            }
        });
        nurse = v.findViewById(R.id.imageTransport);
        nurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent electrician = new Intent(getActivity(),FindNurse.class);
                startActivity(electrician);
            }
        });
        labour = v.findViewById(R.id.imageLabour);
        labour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent electrician = new Intent(getActivity(),FindLabour.class);
                startActivity(electrician);
            }
        });


        btnNearBy = v.findViewById(R.id.btnNearby);
        btnNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent map = new Intent(getActivity(),MapsActivity.class);
                startActivity(map);

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
