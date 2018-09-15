package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import Data.MethodData;
import Data.SeatData;

/**
 * Created by Asus on 18/5/2561.
 */

public class MethodAdapter extends ArrayAdapter<MethodData>{
    private int mGroup_id;
    private Context mContext;
    private List<MethodData> mArrayList;
    private LayoutInflater mInflater;

    public MethodAdapter(Context context, int group_id, List<MethodData> arrayList){
        super(context, group_id, arrayList);
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mArrayList = arrayList;
        this.mGroup_id = group_id;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View methodView = mInflater.inflate(mGroup_id, parent, false);
        return methodView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
