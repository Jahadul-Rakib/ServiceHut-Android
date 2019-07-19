package com.example.jahadulrakib.servicehut.AddItems;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jahadulrakib.servicehut.R;

import java.util.ArrayList;

public class ItemAdepter extends RecyclerView.Adapter<ItemAdepter.MyViewHolder> {

    private Context context;
    private ArrayList<AddItem> driverjobList = new ArrayList<>();
    private ItemAdepter.OnItemClickListener mListener;

    public ItemAdepter(Context context, ArrayList<AddItem> driverjobList) {
        this.context = context;
        this.driverjobList = driverjobList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ArrayList<AddItem> driverjobList = new ArrayList<>();
        Context context;
        TextView jobCarType,jobDate;
        public MyViewHolder(View itemView, Context context, ArrayList<AddItem> driverjobList ) {
            super(itemView);
            this.context = context;
            this.driverjobList = driverjobList;

            jobCarType = itemView.findViewById(R.id.itemDetails);
            jobDate = itemView.findViewById(R.id.amountMoney);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnAddItemClick(position);
                }
            }
        }
    }

    @NonNull
    @Override
    public ItemAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view,viewGroup,false);
        return new ItemAdepter.MyViewHolder(v,context, driverjobList);
    }
    @Override
    public void onBindViewHolder(@NonNull final ItemAdepter.MyViewHolder myViewHolder, int i) {

        myViewHolder.jobCarType.setText(driverjobList.get(i).getItemDetails());
        myViewHolder.jobDate.setText(""+ driverjobList.get(i).getAmountOfMoney());

    }

    @Override
    public int getItemCount() {
        return driverjobList.size();
    }
    public interface OnItemClickListener{
        void OnAddItemClick(int position);
    }
    public void setOnItemClickListener(ItemAdepter.OnItemClickListener listener){
        mListener = listener;
    }
}
