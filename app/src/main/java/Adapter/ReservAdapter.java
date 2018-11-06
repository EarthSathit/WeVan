package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import Data.ReservData;
import th.ac.kru.wevan.R;

/**
 * Created by Asus on 21/5/2561.
 */

public class ReservAdapter extends RecyclerView.Adapter{
    List<ReservData> mList;
    Context mContext;
    LayoutInflater mInflater;

    public ReservAdapter(Context context, List<ReservData> list){
        this.mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_reserv, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        ReservData reservData = mList.get(position);
        myViewHolder.tvTravelDate.setText(reservData.getTravel_date());
        myViewHolder.tvRoute.setText(reservData.getRoute());
        myViewHolder.tvTime.setText("รอบ : " + reservData.getTime());
        myViewHolder.tvPrice.setText("ราตา : " + reservData.getPrice() + ".-");
        myViewHolder.tvVanID.setText("ทะเบียนรถ : " + reservData.getVan_id());
        String status;
        char pm_status = reservData.getPm_status().charAt(0);
        if(pm_status == '1'){
            status = "รอยืนยันการชำระเงิน";
            myViewHolder.tvStatus.setTextColor(Color.RED);
        } else {
            status = "ชำระเงินเรียบร้อย";
            myViewHolder.tvStatus.setTextColor(Color.GREEN);
        }
        myViewHolder.tvStatus.setText("สถานะ : " + status);
    }

    @Override
    public int getItemCount() { return mList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTravelDate, tvRoute, tvTime, tvPrice, tvVanID, tvStatus;
        CardView cvReserv;

        public MyViewHolder(View itemView){
            super(itemView);

            tvTravelDate = itemView.findViewById(R.id.tv_travel_date);
            tvRoute = itemView.findViewById(R.id.tv_route);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvVanID = itemView.findViewById(R.id.tv_van_id);
            cvReserv = itemView.findViewById(R.id.cv_reserv);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
