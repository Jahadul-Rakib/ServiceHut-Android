package com.example.jahadulrakib.servicehut.Notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.R;

import java.util.ArrayList;

public class NotificationAdeptere extends RecyclerView.Adapter<NotificationAdeptere.MyViewHolder> {

    private Context context;
    private ArrayList<NotificationService> notificationList = new ArrayList<>();
    private NotificationAdeptere.OnItemClickListener mListener;

    public NotificationAdeptere(Context context, ArrayList<NotificationService> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ArrayList<NotificationService> notificationList = new ArrayList<>();
        Context context;
        TextView message,senderName;
        public MyViewHolder(View itemView, Context context, ArrayList<NotificationService> notificationList) {
            super(itemView);
            this.context = context;
            this.notificationList = notificationList;

            message = itemView.findViewById(R.id.message);
            senderName = itemView.findViewById(R.id.senderNameMessage);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnItemClickes(position);
                }
            }
        }
    }

    @NonNull
    @Override
    public NotificationAdeptere.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_view,viewGroup,false);
        return new NotificationAdeptere.MyViewHolder(v,context, notificationList);
    }
    @Override
    public void onBindViewHolder(@NonNull final NotificationAdeptere.MyViewHolder myViewHolder, int i) {

        myViewHolder.message.setText(notificationList.get(i).getMessage());
        myViewHolder.senderName.setText(notificationList.get(i).getProverName());


    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
    public interface OnItemClickListener{
        void OnItemClickes(int position);
    }
    public void setOnItemClickListener(NotificationAdeptere.OnItemClickListener listener){
        mListener = listener;
    }
}
