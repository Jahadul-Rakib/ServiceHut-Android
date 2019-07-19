package com.example.jahadulrakib.servicehut.PlumberAdepter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.UserPojo.HirePlumber;

import java.util.ArrayList;

public class PlumberAcceptNotificationAdepter extends RecyclerView.Adapter<PlumberAcceptNotificationAdepter.MyViewHolder> {

    private Context context;
    private ArrayList<HirePlumber> driverjobList = new ArrayList<>();
    private PlumberAcceptNotificationAdepter.OnItemClickListener mListener;

    public PlumberAcceptNotificationAdepter(Context context, ArrayList<HirePlumber> driverjobList) {
        this.context = context;
        this.driverjobList = driverjobList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ArrayList<HirePlumber> driverjobList = new ArrayList<>();
        Context context;
        TextView jobCarType,jobDate,jobDuration,jobNumber,jobDetail;
        public MyViewHolder(View itemView, Context context, ArrayList<HirePlumber> driverjobList ) {
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
                    mListener.OnItemPlumberClick(position);
                }
            }
        }
    }

    @NonNull
    @Override
    public PlumberAcceptNotificationAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_plumber_view,viewGroup,false);
        return new PlumberAcceptNotificationAdepter.MyViewHolder(v,context, driverjobList);
    }
    @Override
    public void onBindViewHolder(@NonNull final PlumberAcceptNotificationAdepter.MyViewHolder myViewHolder, int i) {


        myViewHolder.jobDate.setText(driverjobList.get(i).getDateOfHire());
        myViewHolder.jobNumber.setText(driverjobList.get(i).getNumber());
        myViewHolder.jobDetail.setText(driverjobList.get(i).getDetailInfo());

    }

    @Override
    public int getItemCount() {
        return driverjobList.size();
    }
    public interface OnItemClickListener{
        void OnItemPlumberClick(int position);
    }
    public void setOnItemClickListener(PlumberAcceptNotificationAdepter.OnItemClickListener listener){
        mListener = listener;
    }
}
