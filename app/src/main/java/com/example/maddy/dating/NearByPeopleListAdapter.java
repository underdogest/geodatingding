package com.example.maddy.dating;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Maddy on 03.12.2015.
 */
public class NearByPeopleListAdapter<N> extends ArrayAdapter<NearByPeopleEntryShort>
{
    public NearByPeopleListAdapter(Context context, List<NearByPeopleEntryShort> items)
    {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
	{
		class GetPictureAsyncTask extends AsyncTask<Pair<String, ImageView>, Void, Pair<Bitmap, ImageView>>
		{
			@Override
			protected Pair<Bitmap, ImageView> doInBackground(Pair<String, ImageView>... params)
			{
				Bitmap bitmap = null;
				try
				{
					bitmap = BitmapFactory.decodeStream((InputStream)new URL(params[0].first).getContent());
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				return new Pair(bitmap, params[0].second);
			}

			@Override
			protected void onPostExecute(Pair<Bitmap, ImageView> result)
			{
				if(result.first != null)
					result.second.setImageBitmap(result.first);
			}
		}

        // Get the data item for this position
        NearByPeopleEntryShort user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_people_nearby_short, parent, false);
        }
        // Lookup view for data population
		ImageView tvImage = (ImageView) convertView.findViewById(R.id.tvImage);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
        // Populate the data into the template view using the data object
        tvName.setText(user.name);
        tvDistance.setText(user.distance.toString());
		GetPictureAsyncTask task = new GetPictureAsyncTask();
		task.execute(new Pair<String, ImageView>(user.imageUrl, tvImage));
        // Return the completed view to render on screen
        return convertView;
    }
}
