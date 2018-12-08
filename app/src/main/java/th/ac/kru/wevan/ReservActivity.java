package th.ac.kru.wevan;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Adapter.MethodAdapter;
import Adapter.SpinSeatAdapter;
import Adapter.SpinnerClassAdapter;
import Adapter.SpinnerCousineAdapter;
import Data.ItemData;
import Data.ItemDataClass;
import Data.MethodData;
import Data.SeatData;
import android.support.v4.app.Fragment;

public class ReservActivity extends AppCompatActivity implements ReservFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener{

    TextView tvSubmit, tvCancel, tvReservPrice;
    Spinner spinSeat, spinMethod;
    DatePicker dateTravel;

    private String priceReserv, id_card, round_id, phone;

    private String currentDate, reID, strYear;

    private int day, month, year;
    private int iSeat, pr;
    private int valueSeat, valueMethod;
    private int idYear;

    private String shPromotion;

    private String[] seat, methodArr;
    ArrayList<String> seat_list = new ArrayList<String>();
    private int shPromotion_int;

    private int seat_available;

    String strDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserv);

        SharedPreferences mPreference = PreferenceManager
                .getDefaultSharedPreferences(this);
        shPromotion = mPreference.getString("SESSION_PROMOTION","");

        spinSeat = (Spinner) findViewById(R.id.spin_seat);
        spinMethod = (Spinner) findViewById(R.id.spin_method);

        seat_available = Integer.parseInt(getIntent().getExtras().getString("seat_available"));
        //Log.d("seat_available", seat_available);
        for (int i = 1; i<=seat_available; i++){
            String ii = String.valueOf(i);
            seat_list.add(ii);
        }

        shPromotion_int = Integer.parseInt(shPromotion);
        if(shPromotion_int == 1){
            seat = new String[] {"1"};
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, seat);
            spinSeat.setAdapter(spinnerArrayAdapter);

            methodArr = new String[] {"ส่วนลดจากโปรโมชัน"};
            ArrayAdapter<String> array = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, methodArr);
            spinMethod.setAdapter(array);
        } else {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, seat_list);
            spinSeat.setAdapter(arrayAdapter);

            methodArr = new String[] {"ชำระเงินด้วยตนเอง", "ชำระเงินด้วยการโอน"};
            ArrayAdapter<String> array = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, methodArr);
            spinMethod.setAdapter(array);
        }

        initialValue();
    }

    public void initialValue(){
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        // tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvReservPrice = (TextView) findViewById(R.id.tv_reserv_price);
        dateTravel = (DatePicker) findViewById(R.id.date_travel);

        priceReserv = getIntent().getExtras().getString("priceReserv");
        Log.d("priceReserv", priceReserv);
        id_card = getIntent().getExtras().getString("id_card");
        round_id = getIntent().getExtras().getString("round_id");
        phone = getIntent().getExtras().getString("phone");

        tvReservPrice.setText("300");

        /*tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        spinSeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d("shPromotion", shPromotion);
                shPromotion_int = Integer.parseInt(shPromotion);
                iSeat = i + 1;
                if (shPromotion_int == 1){
                    if (iSeat == 1){
                        pr = Integer.parseInt(priceReserv) * 0;
                        Log.d("If", "True");
                    } else {
                        Log.d("iSeat", "True");
                        iSeat -= 1;
                        pr = Integer.parseInt(priceReserv) * iSeat;
                    }
                } else {
                    pr = Integer.parseInt(priceReserv) * iSeat;
                    Log.d("If", "False");
                }
                tvReservPrice.setText(String.valueOf(pr));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tvReservPrice.setText(priceReserv);
            }
        });

        spinMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                valueMethod = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                String currentTime = sdf.format(new Date());
                Log.d("time", currentTime.toString());

               /* idYear = 5;
                idYear = idYear + 1;*/

                year = dateTravel.getYear();
                month = dateTravel.getMonth();
                day = dateTravel.getDayOfMonth();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                strDate = format.format(calendar.getTime());

               /* if (idYear < 10){
                    strYear = "000" + String.valueOf(idYear);
                }else if (idYear < 100){
                    strYear = "00" + String.valueOf(idYear);
                }else if (idYear < 1000){
                    strYear = "0" + String.valueOf(idYear);
                }*/

                SimpleDateFormat forMat = new SimpleDateFormat("yyyyMM");
                reID = forMat.format(calendar.getTime()) + currentTime;

                Log.d("re_id", reID);
                /*Log.d("re_id: ", reID);
                Log.d("round_id: ", String.valueOf(round_id));
                Log.d("id_card: ", String.valueOf(id_card));
                Log.d("reserv_date", currentDate);
                Log.d("travel_date: ", strDate);
                Log.d("pm_method", String.valueOf(valueMethod));
                Log.d("seat: ", String.valueOf(iSeat));
                Log.d("price: ", String.valueOf(pr));*/
                alertDialogWarning("คุณต้องการจองที่นั่งหรือไม่?");

            }
        });
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void saveReserv(final String reID, final String round_id, final String phone,
                           final String reservDate, final String travelDate, final String pmMethod,
                           final String seat, final String price){
        final String url = "http://wssathit.codehansa.com/manage_reserv.php";

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor_pro = sharedPreferences.edit();
        editor_pro.putString("SESSION_PROMOTION", "0");
        editor_pro.commit();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", response);
                alertDialogSuccess("บันทึกการจองสำเร็จ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog("จองไม่สำเร็จ กรุณาทำการจองอีกครั้ง");
                String message = null;
                if (error instanceof NetworkError) {
                    message = "NetworkError! Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "AuthFailureError! Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "NoConnectionError! Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Log.d("onError", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cmd", "insert");
                params.put("re_id", reID);
                params.put("round_id", round_id);
                params.put("phone", phone);
                //params.put("id_card", id_card);
                params.put("reserv_date", reservDate);
                params.put("travel_date", travelDate);
                params.put("payment_status", "1");
                params.put("payment_method", pmMethod);
                params.put("reserv_seat", seat);
                params.put("reserv_price", price);

                return params;
            }
        };
        requestQueue.add(request);
    }

    public void alertDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservActivity.this);
        builder
                .setIcon(R.drawable.ic_info)
                .setTitle("แจ้งเตือน")
                .setMessage(msg)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertDialogSuccess(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservActivity.this);
        builder
                .setIcon(R.drawable.ic_info)
                .setTitle("แจ้งเตือน")
                .setMessage(msg)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      dialogInterface.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertDialogWarning(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservActivity.this);
        builder
                .setIcon(R.drawable.ic_info)
                .setTitle("แจ้งเตือน")
                .setMessage(msg)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveReserv(reID, round_id, phone, currentDate, strDate, String.valueOf(valueMethod),
                                String.valueOf(iSeat), String.valueOf(pr));
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
