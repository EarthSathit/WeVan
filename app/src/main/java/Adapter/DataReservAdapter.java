package Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Data.ReservData;
import th.ac.kru.wevan.ConfirmPayment;
import th.ac.kru.wevan.R;

/**
 * Created by Asus on 5/8/2561.
 */

public class DataReservAdapter extends RecyclerView.Adapter{
    List<ReservData> mList;
    Context mContext;
    LayoutInflater mInflater;
    private ImageView selectedImage;
    private Bitmap currentImage;

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
        final ReservData reservData = mList.get(position);
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
                        .setIcon(R.drawable.ic_info)
                        .setTitle("แจ้งเตือน")
                        .setMessage("ยืนยันการชำระเงิน")
                        .setPositiveButton("ชำระเงิน", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("Patment Reserv", "payment success");
                                //selectImage();
                                Intent intent = new Intent(mContext, ConfirmPayment.class);
                                intent.putExtra("re_id", reservData.getRe_id());
                                mContext.startActivity(intent);
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
                        .setIcon(R.drawable.ic_info)
                        .setTitle("แจ้งเตือน")
                        .setMessage("คุณต้องการยกเลิกการจองหรือไม่?")
                        .setPositiveButton("ยกเลิก", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("Cancel Reserv", reservData.getId_card());
                                cancelReserv(reservData.getRe_id());
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

    public void cancelReserv(final String re_id) {
        String url = "http://wssathit.codehansa.com/manage_reserv.php";
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse: ", "Success");
                Toast toast = Toast.makeText(mContext, "ยกเลิกการจองเรียบร้อย",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onErrorResponse", error.getMessage());
            }
        })
        {
            protected Map<String, String> getParams(){
                Map<String, String> param = new HashMap<>();
                param.put("cmd", "cancel_reserv");
                //param.put("phone", phone);
                param.put("re_id", re_id);

                return param;
            }
        };
        requestQueue.add(request);
    }

    public void selectImage() {
        // invoke the image gallery using an implict intent.
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        // set the data and type.  Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");
        ((Activity) mContext).startActivityForResult(photoPickerIntent, 1);
        //saveImage(data.toString());
    }

    public void saveImage(String path) {
        Toast.makeText(mContext, path, Toast.LENGTH_LONG).show();
    }
}
