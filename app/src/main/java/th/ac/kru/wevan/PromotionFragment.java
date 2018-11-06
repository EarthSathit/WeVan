package th.ac.kru.wevan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.ListviewAdapter;
import Adapter.ReservAdapter;
import Adapter.SeatAdapter;
import Data.RoundData;
import Data.SeatData;

public class PromotionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String shID, shName, shType, shEmail, shPhone;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PromotionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PromotionFragment newInstance(String param1, String param2) {
        PromotionFragment fragment = new PromotionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotion, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences mPreference = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        shID = mPreference.getString("SESSION_ID","");
        shName = mPreference.getString("SESSION_NAME", "");
        shType = mPreference.getString("SESSION_USER_TYPE", "");
        shEmail = mPreference.getString("SESSION_EMAIL", "");
        shPhone = mPreference.getString("SESSION_PHONE", "");

        display_promotion(shPhone);
    }

    public void display_promotion(final String phone){
        String url = "http://wssathit.codehansa.com/manage_reserv.php";

        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor_pro = sharedPreferences.edit();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final ArrayList<SeatData> data = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("rs");
                    if(jsonArray != null){
                        for(int i=0; i < jsonArray.length(); i++){
                            JSONObject json_data = jsonArray.getJSONObject(i);
                            data.add(new SeatData(
                                    json_data.getString("amount_service")));
                        }
                        RecyclerView recyclerView = getActivity().findViewById(R.id.rv_times);
                        recyclerView.setHasFixedSize(true);
                        SeatAdapter seatAdapter = new SeatAdapter(getContext(), data);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(seatAdapter);
                        seatAdapter.setClickListener(new SeatAdapter.ItemClickListener(){
                            @Override
                            public void onClick(View view, int position) {
                                SeatData current = data.get(position);
                                Log.d("seat", current.getSeat());
                                if(Integer.parseInt(current.getSeat()) >= 10){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder
                                            .setIcon(R.drawable.ic_info)
                                            .setTitle("แจ้งเตือน")
                                            .setMessage("ยืนยันการรับสิทธิ")
                                            .setPositiveButton("รับสิทธิ", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    editor_pro.putString("SESSION_PROMOTION", "1");
                                                    editor_pro.commit();
                                                    usedPromotion(phone);
                                                    dialogInterface.dismiss();
                                                    //viewHolder.tvReserv.setEnabled(false);
                                                }
                                            })
                                            .setNegativeButton("ไม่รับ", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    editor_pro.putString("SESSION_PROMOTION", "0");
                                                    editor_pro.commit();
                                                    unUsedPromotion(phone);
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder
                                            .setIcon(R.drawable.ic_info)
                                            .setTitle("แจ้งเตือน")
                                            .setMessage("ไม่สามารถใช้สิทธิได้ เนื่องจากจำนวนครั้งที่ใช้บริการยังไม่ครบตามกำหนด")
                                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    //viewHolder.tvReserv.setEnabled(false);
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }
                        });
                        /*seatAdapter.setClickListener(new ListviewAdapter.ItemClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                SeatData current = data.get(position);
                                Intent openConfirm = new Intent(getActivity(), ReservActivity.class);
                                openConfirm.putExtra("priceReserv", current.getPrice());
                                openConfirm.putExtra("round_id", current.getRound_id());
                                openConfirm.putExtra("id_card", shID);
                                openConfirm.putExtra("phone", shPhone);
                                startActivity(openConfirm);
                                Log.d("ffff","fffff");
                            }
                        });*/
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            protected Map<String,String> getParams(){
                Map<String,String> param = new HashMap<>();
                param.put("cmd", "history");
                param.put("phone", phone);
                return param;
            }
        };
        queue.add(stringRequest);
    }

    public void usedPromotion(final String prmPhone) {
        String url = "http://wssathit.codehansa.com/manage_reserv.php";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "คุณได้รับสิทธิแล้ว", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        )
        {
            protected Map<String,String> getParams(){
                Map<String,String> param = new HashMap<>();
                param.put("cmd", "used_promotion");
                param.put("phone", prmPhone);
                return param;
            }
        };
        queue.add(stringRequest);
    }

    public void unUsedPromotion(final String prmPhone){
        String url = "http://wssathit.codehansa.com/manage_reserv.php";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "คุณได้ยกเลิกสิทธิแล้ว", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        )
        {
            protected Map<String,String> getParams(){
                Map<String,String> param = new HashMap<>();
                param.put("cmd", "unused_promotion");
                param.put("phone", prmPhone);
                return param;
            }
        };
        queue.add(stringRequest);
    }
}
