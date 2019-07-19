package com.example.jahadulrakib.servicehut.DriverAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.UserPojo.HireDriver;

import java.util.ArrayList;

public class DriverJobHistoryAdepter extends RecyclerView.Adapter<DriverJobHistoryAdepter.MyViewHolder> {

    private Context context;
    private ArrayList<HireDriver> driverjobList = new ArrayList<>();
    private DriverJobHistoryAdepter.OnItemClickListener mListener;

    public DriverJobHistoryAdepter(Context context, ArrayList<HireDriver> driverjobList) {
        this.context = context;
        this.driverjobList = driverjobList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ArrayList<HireDriver> driverjobList = new ArrayList<>();
        Context context;
        TextView jobCarType,jobDate,jobDuration,jobNumber,jobDetail;
        public MyViewHolder(View itemView, Context context, ArrayList<HireDriver> driverjobList ) {
            super(itemView);
            this.context = context;
            this.driverjobList = driverjobList;

            jobCarType = itemView.findViewById(R.id.jobCarType);
            jobDate = itemView.findViewById(R.id.jobDate);
            jobDuration = itemView.findViewById(R.id.jobDuration);
            jobNumber = itemView.findViewById(R.id.jobNumber);
            jobDetail = itemView.findViewById(R.id.jobDetails);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnItemClicks(position);
                }
            }
        }
    }

    @NonNull
    @Override
    public DriverJobHistoryAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_driver_view,viewGroup,false);
        return new DriverJobHistoryAdepter.MyViewHolder(v,context, driverjobList);
    }
    @Override
    public void onBindViewHolder(@NonNull final DriverJobHistoryAdepter.MyViewHolder myViewHolder, int i) {

        myViewHolder.jobCarType.setText(driverjobList.get(i).getCarType());
        myViewHolder.jobDate.setText(driverjobList.get(i).getDateOfHire());
        myViewHolder.jobDuration.setText(""+driverjobList.get(i).getHireForDay());
        myViewHolder.jobNumber.setText(driverjobList.get(i).getNumber());
        myViewHolder.jobDetail.setText(driverjobList.get(i).getDetailInfo());

    }

    @Override
    public int getItemCount() {
        return driverjobList.size();
    }
    public interface OnItemClickListener{
        void OnItemClicks(int position);
    }
    public void setOnItemClickListener(DriverJobHistoryAdepter.OnItemClickListener listener){
        mListener = listener;
    }
}
