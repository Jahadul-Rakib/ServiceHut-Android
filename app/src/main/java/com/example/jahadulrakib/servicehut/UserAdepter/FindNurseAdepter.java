package com.example.jahadulrakib.servicehut.UserAdepter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Registration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FindNurseAdepter extends RecyclerView.Adapter<FindNurseAdepter.MyViewHolder> {

    private Context context;
    private ArrayList<Registration> driverList = new ArrayList<>();
    private FindNurseAdepter.OnItemClickListener mListener;

    public FindNurseAdepter(Context context, ArrayList<Registration> driverList) {
        this.context = context;
        this.driverList = driverList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ArrayList<Registration> driverList = new ArrayList<>();
        Context context;
        ImageView imageView;
        TextView driverName,phoneNumber,address;
        public MyViewHolder(View itemView, Context context, ArrayList<Registration> driverlist ) {
            super(itemView);
            this.context = context;
            this.driverList = driverlist;

            imageView = itemView.findViewById(R.id.driverImage);
            driverName = itemView.findViewById(R.id.driverName);
            address = itemView.findViewById(R.id.driverLocation);
            phoneNumber = itemView.findViewById(R.id.driverNumber);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnItemClick(position);
                }
            }
        }
    }

    @NonNull
    @Override
    public FindNurseAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hire_nurse_view,viewGroup,false);
        return new FindNurseAdepter.MyViewHolder(v,context, driverList);
    }
    @Override
    public void onBindViewHolder(@NonNull final FindNurseAdepter.MyViewHolder myViewHolder, int i) {

        myViewHolder.driverName.setText(driverList.get(i).getFullName());
        myViewHolder.address.setText(driverList.get(i).getuAddress());
        myViewHolder.phoneNumber.setText(driverList.get(i).getuPhone());
        Picasso.with(context)
                .load(driverList.get(i).getUrlImage())
                .fit()
                .placeholder(R.drawable.nurse)
                .centerCrop()
                .into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public void setOnItemClickListener(FindNurseAdepter.OnItemClickListener listener){
        mListener = listener;
    }
    public void filterList(ArrayList<Registration> filteredList) {
        driverList = new ArrayList<>();
        driverList = filteredList;
        notifyDataSetChanged();
    }
}
