package Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Data.RoundData;
import th.ac.kru.wevan.LoginActivity;
import th.ac.kru.wevan.MainActivity;
import th.ac.kru.wevan.R;


public class ListviewAdapter extends BaseAdapter {
    Context context;
    ArrayList<RoundData> roundData;
    Typeface fonts1, fonts2;

    String msgSeat;
    private static ItemClickListener clickListener;

    public ListviewAdapter(Context context, ArrayList<RoundData> roundData) {
        this.context = context;
        this.roundData = roundData;
    }

    @Override
    public int getCount() {
        return roundData.size();
    }

    @Override
    public Object getItem(int position) {
        return roundData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        fonts1 =  Typeface.createFromAsset(context.getAssets(),
                "fonts/MavenPro-Regular.ttf");

        fonts2 = Typeface.createFromAsset(context.getAssets(),
                "fonts/MavenPro-Regular.ttf");

        final ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row,null);

            viewHolder = new ViewHolder();

            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.tvVanID = (TextView) convertView.findViewById(R.id.tv_van_id);
            viewHolder.tvRoute = (TextView) convertView.findViewById(R.id.tv_route);
            viewHolder.tvTimeRoute = (TextView) convertView.findViewById(R.id.tv_time_route);
            viewHolder.tvSeat = (TextView) convertView.findViewById(R.id.tv_seat);
            viewHolder.tvReserv = (TextView) convertView.findViewById(R.id.tv_reserv);

            viewHolder.tvTime.setTypeface(fonts1);
            viewHolder.tvReserv.setOnClickListener(mMyButtonClickListener);


            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RoundData roundData = (RoundData) getItem(position);

        viewHolder.tvTime.setText("รอบ : " + roundData.getTime());
        viewHolder.tvPrice.setText("ราคา : " + roundData.getPrice() + ".-");
        viewHolder.tvVanID.setText("ทะเบียนรถ : " + roundData.getVan_id());
        viewHolder.tvTimeRoute.setText(roundData.getTime_route());
        viewHolder.tvRoute.setText(roundData.getRoute());
        viewHolder.seat = roundData.getSeat();
        int seat = Integer.parseInt(roundData.getSeat());
        if (seat == 0){
            msgSeat = "ที่นั่งเต็มแล้ว";
            viewHolder.tvSeat.setTextColor(Color.RED);
            viewHolder.tvSeat.setTextSize(15);
            //viewHolder.tvReserv.setEnabled(false);
            viewHolder.tvReserv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder
                            .setIcon(R.drawable.ic_info)
                            .setTitle("แจ้งเตือน")
                            .setMessage("ไม่สามารถจองได้ เนื่องจากที่นั่งเต็มแล้ว กรุณาจองรอบถัดไป")
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                   dialogInterface.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        } else {
            msgSeat = "คงเหลือ : " + seat + " ที่นั่ง";
        }
        viewHolder.tvSeat.setText(msgSeat);

        viewHolder.tvReserv.setTag(position);

        return convertView;
    }

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

    private class ViewHolder {
        TextView tvTime, tvRoute, tvPrice, tvVanID, tvTimeRoute, tvSeat, tvReserv;
        String seat;
    }

    public void showTost(){
        //Toast.makeText(getApplication(), "Clicked Laugh Vote", Toast.LENGTH_SHORT).show();
    }
}



