package com.example.jahadulrakib.servicehut.AdminAdapter;

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

public class AllUserAdepter extends RecyclerView.Adapter<AllUserAdepter.MyViewHolder> {

    private Context context;
    private ArrayList<Registration> userList = new ArrayList<>();
    private AllUserAdepter.OnItemClickListener mListener;

    public AllUserAdepter(Context context, ArrayList<Registration> userList) {
        this.context = context;
        this.userList = userList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ArrayList<Registration> userList = new ArrayList<>();
        Context context;
        ImageView imageView;
        TextView phoneNumber,userName,userType,userEmail;
        public MyViewHolder(View itemView, Context context, ArrayList<Registration> driverlist ) {
            super(itemView);
            this.context = context;
            this.userList = driverlist;

            imageView = itemView.findViewById(R.id.imageToLet);
            userName = itemView.findViewById(R.id.userName);
            userType = itemView.findViewById(R.id.userType);
            userEmail = itemView.findViewById(R.id.userEmail);
            phoneNumber = itemView.findViewById(R.id.userPhone);
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
    public AllUserAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.admin_user_view,viewGroup,false);
        return new AllUserAdepter.MyViewHolder(v,context, userList);
    }
    @Override
    public void onBindViewHolder(@NonNull final AllUserAdepter.MyViewHolder myViewHolder, int i) {

        myViewHolder.userName.setText(userList.get(i).getFullName());
        myViewHolder.userType.setText(userList.get(i).getUserType());
        myViewHolder.userEmail.setText(userList.get(i).getuEmail());
        myViewHolder.phoneNumber.setText(userList.get(i).getuPhone());
        Picasso.with(context)
                .load(userList.get(i).getUrlImage())
                .fit()
                .placeholder(R.drawable.plumber)
                .centerCrop()
                .into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public void setOnItemClickListener(AllUserAdepter.OnItemClickListener listener){
        mListener = listener;
    }
    public void filterList(ArrayList<Registration> filteredList) {
        userList = new ArrayList<>();
        userList = filteredList;
        notifyDataSetChanged();
    }
}
