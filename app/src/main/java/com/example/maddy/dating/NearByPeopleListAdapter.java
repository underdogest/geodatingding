package com.example.maddy.dating;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maddy on 03.12.2015.
 */
public class NearByPeopleListAdapter<N> extends ArrayAdapter<NearByPeopleEntryShort> {


    Context context;

    public NearByPeopleListAdapter(Context context, int resourceId, //resourceId=your layout
                                   List<NearByPeopleEntryShort> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        NearByPeopleEntryShort rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_people_nearby_short, null);
            holder = new ViewHolder();
            //ändern in text aus rowItem
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            //ändern in rohes bild aus rowItem
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());

        return convertView;
    }
}
