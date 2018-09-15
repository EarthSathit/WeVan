package th.ac.kru.wevan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.ListviewAdapter;
import Data.RoundData;

public class ReservFragment extends Fragment {

    TextView tvSeatAvailable;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String shID, shName, shType, shEmail, shPhone;

    TextView tvReserv;

    SliderLayout mDemoSlider;

    String roundIDm, routeID;

    private String seat_available;

    private ExpandableHeightListView listview;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReservFragment() {
        // Required empty public constructor

    }

    public static ReservFragment newInstance(String param1, String param2) {
        ReservFragment fragment = new ReservFragment();
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
        return inflater.inflate(R.layout.fragment_reserv, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

       //seat_available = tvSeatAvailable.getText().toString();
        //showToast(shID + shName + shType + shEmail + shPhone);

        sliderShow();
        displayRound();
    }

    public void sliderShow(){
        mDemoSlider = (SliderLayout) getActivity().findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.p_2);
        // file_maps.put("2",R.drawable.p_6);
        //  file_maps.put("3",R.drawable.p_8);


        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            //.setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
        // mDemoSlider.addOnPageChangeListener();
    }

    public void displayRound(){
        String url = "http://wssathit.codehansa.com/manage_round.php";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final ArrayList<RoundData> data = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("rs");
                    if(jsonArray != null){
                        for(int i=0; i < jsonArray.length(); i++){
                            JSONObject json_data = jsonArray.getJSONObject(i);
                            data.add(new RoundData(
                                    json_data.getString("round_id"),
                                    json_data.getString("route_id"),
                                    json_data.getString("route"),
                                    json_data.getString("price"),
                                    json_data.getString("time_route"),
                                    json_data.getString("time"),
                                    json_data.getString("van_id"),
                                    json_data.getString("seat")));
                        }
                        listview = (ExpandableHeightListView) getActivity().findViewById(R.id.round_list);
                        ListviewAdapter adapter = new ListviewAdapter(getContext(), data);
                        tvReserv = (TextView) getActivity().findViewById(R.id.tv_reserv);
                        listview.setAdapter(adapter);
                        adapter.setClickListener(new ListviewAdapter.ItemClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                RoundData current = data.get(position);
                                Intent openConfirm = new Intent(getActivity(), ReservActivity.class);
                                openConfirm.putExtra("priceReserv", current.getPrice());
                                openConfirm.putExtra("round_id", current.getRound_id());
                                openConfirm.putExtra("id_card", shID);
                                openConfirm.putExtra("phone", shPhone);
                                openConfirm.putExtra("seat_available", current.getSeat());
                                startActivity(openConfirm);
                            }
                        });
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
                param.put("cmd", "select");
                return param;
            }
        };
        queue.add(stringRequest);
    }

    public void onSliderClick(BaseSliderView slider) {}

    public void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
