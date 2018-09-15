package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Data.ReservData;
import th.ac.kru.wevan.R;

/**
 * Created by Asus on 5/8/2561.
 */

public class DataReservAdapter extends RecyclerView.Adapter{
    List<ReservData> mList;
    Context mContext;
    LayoutInflater mInflater;

    private static ItemClickListener clickListener;

    public DataReservAdapter(Context context, List<ReservData> list){
        this.mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_data_reserv, parent, false);
        DataReservAdapter.MyViewHolder holder = new DataReservAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DataReservAdapter.MyViewHolder myViewHolder = (DataReservAdapter.MyViewHolder) holder;
        ReservData reservData = mList.get(position);
        myViewHolder.tvTravelDate.setText(reservData.getTravel_date());
        myViewHolder.tvRoute.setText(reservData.getRoute());
        myViewHolder.tvTime.setText("รอบ : " + reservData.getTime());
        myViewHolder.tvPrice.setText("ราตา : " + reservData.getPrice() + ".-");
        myViewHolder.tvVanID.setText("ทะเบียนรถ : " + reservData.getVan_id());
        int pm_status = Integer.parseInt(reservData.getPm_status());
        String msg_status;
        if (pm_status == 1){
            msg_status = "ยังไมไ่ด้ชำระเงิน";
        }else {
            msg_status = "ชำระเงินแล้ว";
        }
        myViewHolder.tvShowStatus.setText(msg_status);
        myViewHolder.tvPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder
                        .setIcon(R.drawable.van1)
                        .setTitle("Warning")
                        .setMessage("ยืนยันการชำระเงิน")
                        .setPositiveButton("ชำระเงิน", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            /*    Intent intent = new Intent(getActivity(), GalleryActivity.class);
                                Params params = new Params();
                                params.setPickerLimit(3);
                                params.setCaptureLimit(3);
                                intent.putExtra(SyncStateContract.Constants.KEY_PARAMS, params);
                                startActivityForResult(intent, SyncStateContract.Constants.TYPE_MULTI_PICKER); */
                                Log.d("Patment Reserv", "payment success");
                                //dialogInterface.dismiss();
                                //viewHolder.tvReserv.setEnabled(false);
                            }
                        })
                        .setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("Patment Reserv", "cancel payment");
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        myViewHolder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder
                        .setIcon(R.drawable.van1)
                        .setTitle("Warning")
                        .setMessage("คุณต้องการยกเลิกการจองหรือไม่?")
                        .setPositiveButton("ยกเลิก", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("Cancel Reserv", "Cancel Success");
                                dialogInterface.dismiss();
                                //viewHolder.tvReserv.setEnabled(false);
                            }
                        })
                        .setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("Cancel Reserv", "Cancel");
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
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
        TextView tvTravelDate, tvRoute, tvTime, tvPrice, tvVanID, tvShowStatus, tvPayment, tvCancel;
        CardView cvReserv;

        public MyViewHolder(View itemView){
            super(itemView);

            tvTravelDate = itemView.findViewById(R.id.tv_travel_date);
            tvRoute = itemView.findViewById(R.id.tv_route);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvVanID = itemView.findViewById(R.id.tv_van_id);
            tvShowStatus = itemView.findViewById(R.id.tv_show_status);
            tvPayment = itemView.findViewById(R.id.tv_payment);
            tvCancel = itemView.findViewById(R.id.tv_cancel);
            cvReserv = itemView.findViewById(R.id.cv_reserv);
        }
    }

    public void cancelReserv() {

    }
}
