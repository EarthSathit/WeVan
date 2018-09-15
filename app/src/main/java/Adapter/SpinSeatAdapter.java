package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import Data.RoundData;
import Data.SeatData;

/**
 * Created by Asus on 18/5/2561.
 */

public class SpinSeatAdapter extends ArrayAdapter<RoundData> {
    int mGroup_id;
    Context mContext;
    ArrayList<RoundData> mArrayList;
    LayoutInflater mInflater;
    public SpinSeatAdapter(Context context, int group_id, int id, ArrayList<RoundData> arrayList){
        super(context, id, arrayList);
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mArrayList = arrayList;
        this.mGroup_id = group_id;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mGroup_id, parent, false);


        return view;
    }
}
