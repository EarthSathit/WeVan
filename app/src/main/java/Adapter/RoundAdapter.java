package Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import Data.RoundData;
import th.ac.kru.wevan.R;

/**
 * Created by Asus on 16/5/2561.
 */

public class RoundAdapter extends ArrayAdapter<RoundData>{
    private List<RoundData> feedItemList;
    private Context mContext;
    private LayoutInflater inflater;

    public RoundAdapter(Context context, List<RoundData> feedItemList) {
        super(context, 0, feedItemList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RoundData roundData = feedItemList.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
        }

        TextView tvTime, tvRoute, tvPrice, tvVanID, tvTimeRoute;
        //tvTime = (TextView) convertView.findViewById(R.id.tv)

        return convertView;
    }
}
