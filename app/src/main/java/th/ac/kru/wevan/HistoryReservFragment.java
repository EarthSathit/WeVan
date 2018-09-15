package th.ac.kru.wevan;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.ReservAdapter;
import Data.ReservData;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryReservFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryReservFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryReservFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String shID, shName, shType, shEmail, shPhone;
    private String id_card;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HistoryReservFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryReservFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryReservFragment newInstance(String param1, String param2) {
        HistoryReservFragment fragment = new HistoryReservFragment();
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
        return inflater.inflate(R.layout.fragment_history_reserv, container, false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
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

        //showToast(shID + shName + shType + shEmail + shPhone);
        displayHistoryReserv(shPhone);
    }

    public void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void displayHistoryReserv(final String phone){
        String url = "http://wssathit.codehansa.com/manage_reserv.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse: ", "Success");
                try {
                    List<ReservData> list = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("rs");
                    if (jsonArray != null){
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject json_data = jsonArray.getJSONObject(i);
                            list.add(new ReservData(
                                    json_data.getString("re_id"),
                                    json_data.getString("round_id"),
                                    json_data.getString("id_card"),
                                    json_data.getString("reserv_date"),
                                    json_data.getString("travel_date"),
                                    json_data.getString("payment_status"),
                                    json_data.getString("payment_method"),
                                    json_data.getString("route"),
                                    json_data.getString("time"),
                                    json_data.getString("price"),
                                    json_data.getString("van_id")));
                        }

                        RecyclerView recyclerView = getActivity().findViewById(R.id.rv_history);
                        recyclerView.setHasFixedSize(true);
                        ReservAdapter reservAdapter = new ReservAdapter(getContext(), list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(reservAdapter);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            protected Map<String, String> getParams(){
                Map<String, String> param = new HashMap<>();
                param.put("cmd", "select");
                param.put("phone", phone);

                return param;
            }
        };
        requestQueue.add(request);
    }
}
