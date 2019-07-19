package com.example.jahadulrakib.servicehut.UserAdepter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.UserPojo.CreateToLetPojo;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ViewOwnToLet extends RecyclerView.Adapter<ViewOwnToLet.MyViewHolder> {
    private Context context;
    private ArrayList<CreateToLetPojo> toletList = new ArrayList<>();
    private OnItemClickListener mListener;


    public ViewOwnToLet(Context context, ArrayList<CreateToLetPojo> toletList) {
        this.context = context;
        this.toletList = toletList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ArrayList<CreateToLetPojo> toletList = new ArrayList<>();
        Context context;
        ImageView imageView;
        TextView rentMonth,type,location,rentType,advance,rentFrom;
        public MyViewHolder(View itemView, Context context, ArrayList<CreateToLetPojo> toletList ) {
            super(itemView);
            this.context = context;
            this.toletList = toletList;

            imageView = itemView.findViewById(R.id.imageToLet);
            rentMonth = itemView.findViewById(R.id.rentMonth);
            type = itemView.findViewById(R.id.rentTypeClient);
            location = itemView.findViewById(R.id.rentLocation);
            rentType = itemView.findViewById(R.id.rentType);
            advance = itemView.findViewById(R.id.rentAdvance);
            rentFrom = itemView.findViewById(R.id.rentMonthName);

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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.to_let_view,viewGroup,false);
        return new MyViewHolder(v,context, toletList);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        myViewHolder.rentMonth.setText(toletList.get(i).getToLetMonthRent());
        myViewHolder.advance.setText(toletList.get(i).getToLetAdvance());
        myViewHolder.type.setText(toletList.get(i).getType());
        myViewHolder.rentType.setText(toletList.get(i).getToLetType());
        myViewHolder.location.setText(toletList.get(i).getToLetAddress());
        myViewHolder.rentFrom.setText(toletList.get(i).getToLetMonth());
        Picasso.with(context)
                .load(toletList.get(i).getToLetImageURL())
                .fit()
                .placeholder(R.drawable.home_black)
                .centerCrop()
                .into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return toletList.size();
    }
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void filterList(ArrayList<CreateToLetPojo> filteredList) {
        toletList = new ArrayList<>();
        toletList = filteredList;
        notifyDataSetChanged();
    }

}
