package com.example.jahadulrakib.servicehut.ElectricianAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.UserPojo.HireElectrician;

import java.util.ArrayList;

public class ElectricianNotNowNotificationAdepter extends RecyclerView.Adapter<ElectricianNotNowNotificationAdepter.MyViewHolder> {

    private Context context;
    private ArrayList<HireElectrician> driverjobList = new ArrayList<>();
    private ElectricianNotNowNotificationAdepter.OnItemClickListener mListener;

    public ElectricianNotNowNotificationAdepter(Context context, ArrayList<HireElectrician> driverjobList) {
        this.context = context;
        this.driverjobList = driverjobList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ArrayList<HireElectrician> driverjobList = new ArrayList<>();
        Context context;
        TextView jobCarType,jobDate,jobDuration,jobNumber,jobDetail;
        public MyViewHolder(View itemView, Context context, ArrayList<HireElectrician> driverjobList ) {
            super(itemView);
            this.context = context;
            this.driverjobList = driverjobList;

            jobDate = itemView.findViewById(R.id.jobDate);
            jobNumber = itemView.findViewById(R.id.jobNumber);
            jobDetail = itemView.findViewById(R.id.jobDetails);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnItemEClick(position);
                }
            }
        }
    }

    @NonNull
    @Override
    public ElectricianNotNowNotificationAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_electrician_view,viewGroup,false);
        return new ElectricianNotNowNotificationAdepter.MyViewHolder(v,context, driverjobList);
    }
    @Override
    public void onBindViewHolder(@NonNull final ElectricianNotNowNotificationAdepter.MyViewHolder myViewHolder, int i) {


        myViewHolder.jobDate.setText(driverjobList.get(i).getDateOfHire());
        myViewHolder.jobNumber.setText(driverjobList.get(i).getNumber());
        myViewHolder.jobDetail.setText(driverjobList.get(i).getDetailInfo());

    }

    @Override
    public int getItemCount() {
        return driverjobList.size();
    }
    public interface OnItemClickListener{
        void OnItemEClick(int position);
    }
    public void setOnItemClickListener(ElectricianNotNowNotificationAdepter.OnItemClickListener listener){
        mListener = listener;
    }
}
