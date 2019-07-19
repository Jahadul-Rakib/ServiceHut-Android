package com.example.jahadulrakib.servicehut.NurseAdepter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.UserPojo.HireDriver;
import com.example.jahadulrakib.servicehut.UserPojo.HireNurse;

import java.util.ArrayList;

public class NurseJobHistoryAdepter extends RecyclerView.Adapter<NurseJobHistoryAdepter.MyViewHolder> {

    private Context context;
    private ArrayList<HireNurse> driverjobList = new ArrayList<>();
    private NurseJobHistoryAdepter.OnItemClickListener mListener;

    public NurseJobHistoryAdepter(Context context, ArrayList<HireNurse> driverjobList) {
        this.context = context;
        this.driverjobList = driverjobList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ArrayList<HireNurse> driverjobList = new ArrayList<>();
        Context context;
        TextView jobCarType,jobDate,jobDuration,jobNumber,jobDetail;
        public MyViewHolder(View itemView, Context context, ArrayList<HireNurse> driverjobList ) {
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
    public NurseJobHistoryAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.job_nurse_view,viewGroup,false);
        return new NurseJobHistoryAdepter.MyViewHolder(v,context, driverjobList);
    }
    @Override
    public void onBindViewHolder(@NonNull final NurseJobHistoryAdepter.MyViewHolder myViewHolder, int i) {

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
    public void setOnItemClickListener(NurseJobHistoryAdepter.OnItemClickListener listener){
        mListener = listener;
    }
}
