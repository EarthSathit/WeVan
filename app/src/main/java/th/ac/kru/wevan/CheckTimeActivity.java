package th.ac.kru.wevan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.ListviewAdapter;
import Adapter.RoundAdapter;
import Data.RoundData;

public class CheckTimeActivity  extends AppCompatActivity implements BaseSliderView.OnSliderClickListener{

    SliderLayout mDemoSlider;

    private ExpandableHeightListView listview;
    private ArrayList<BeanclassList> Bean;
    private ListviewAdapter baseAdapter;

    private String[] TITLEC = {"Basic Hair Cut", "Advance Hair Cut","Basic Hair Cut"};
    private String[] PRICEC = {"$ 15", "$ 29","$ 15"};
    private String[] DETAILC = {"Trimming, Split removal", "Get fresh look and blow dry look plus spa","Trimming, Split removal"};

    private String[] TITLES = {"Basic Hair Cut", "Advance Hair Cut", "Advance Hair Cut"};
    private String[] PRICES = {"$ 15", "$ 29", "$ 29"};
    private String[] DETAILS = {"Trimming, Split removal", "Get fresh look and blow dry look plus spa", "Trimming, Split removal"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checktime1);
        displayRound();

       /*Bean = new ArrayList<BeanclassList>();

        for (int i=0; i<TITLEC.length; i++){
            BeanclassList BeanclassList = new BeanclassList(TITLEC[i], PRICEC[i], DETAILC[i]);
            Bean.add(BeanclassList);
        }

        baseAdapter = new ListviewAdapter(CheckTimeActivity.this, Bean) {
        };

        listview.setAdapter(baseAdapter);

        //        /        ********LISTVIEW   HAIR STYLING***********

        //  listview = (ExpandableHeightListView)findViewById(R.id.hairstyling_list);


        Bean = new ArrayList<BeanclassList>();

        for (int i=0; i< TITLES.length; i++){
            BeanclassList BeanclassList = new BeanclassList(TITLES[i], PRICES[i], DETAILS[i]);
            Bean.add(BeanclassList);
        }

        baseAdapter = new ListviewAdapter(CheckTimeActivity.this, Bean) {
        };

        listview.setAdapter(baseAdapter);*/


        //         ********Slider*********

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.p_2);
        file_maps.put("2",R.drawable.p_6);
        file_maps.put("3",R.drawable.p_8);


        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
       // mDemoSlider.addOnPageChangeListener(this);
    }

    public void displayRound(){
        String url = "http://wssathit.codehansa.com/manage_round.php";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    ArrayList<RoundData> data = new ArrayList<>();
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
                        listview = (ExpandableHeightListView)findViewById(R.id.round_list);
                        ListviewAdapter adapter = new ListviewAdapter(getApplicationContext(), data);
                        listview.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }
}
