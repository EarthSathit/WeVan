package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import Data.ReservData;
import Data.RoundData;
import Data.SeatData;
import th.ac.kru.wevan.R;

/**
 * Created by Asus on 15/7/2561.
 */

public class SeatAdapter extends RecyclerView.Adapter{
    List<SeatData> mList;
    Context mContext;
    LayoutInflater mInflater;

    String msgSeat;
    private static ItemClickListener clickListener;

    public SeatAdapter(Context context, List<SeatData> list){
        this.mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_times, parent, false);
        SeatAdapter.MyViewHolder holder = new SeatAdapter.MyViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SeatAdapter.MyViewHolder myViewHolder = (SeatAdapter.MyViewHolder) holder;
        SeatData seatData = mList.get(position);
        int seat_cus = Integer.parseInt(seatData.getSeat());

        myViewHolder.tvCusTimes.setText(" " + seatData.getSeat());

        myViewHolder.btnPromotion.setOnClickListener(mMyButtonClickListener);
        myViewHolder.btnPromotion.setTag(position);
    }

    public int getItemCount() { return mList.size(); }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    private View.OnClickListener mMyButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            if (clickListener != null) clickListener.onClick(v, position);
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvCusTimes;
        Button btnPromotion;
        public MyViewHolder(View itemView){
            super(itemView);
            tvCusTimes = itemView.findViewById(R.id.tv_cus_times);
            btnPromotion = itemView.findViewById(R.id.btn_use_pro);
        }
    }
}
