package com.example.maddy.dating;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A fragment representing a single NearBy detail screen.
 * This fragment is either contained in a {@link NearByListActivity}
 * in two-pane mode (on tablets) or a {@link NearByDetailActivity}
 * on handsets.
 */
public class NearByDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
	View rootView = null;

    /**
     * The dummy content this fragment is presenting.
     */
    private PeopleProfileFull mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NearByDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = new PeopleProfileFull(new Integer(getArguments().getString(ARG_ITEM_ID)));
        }
        else
            mItem = new PeopleProfileFull(0);
    }

	@Override
	public void onActivityCreated (Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		class GetProfileAsyncTask extends AsyncTask<View, Void, View>
		{
			@Override
			protected View doInBackground(View... params)
			{
				mItem.loadFromWeb();
				return params[0];
			}

			@Override
			protected void onPostExecute(View result)
			{
				ImageView imageView = (ImageView) result.findViewById(R.id.imageView);
				imageView.setImageBitmap(mItem.bitmap);

				TextView textName = ((TextView) result.findViewById(R.id.AnzeigeName));
				if(textName != null)
					textName.setText(mItem.firstName);

				TextView textAlter = ((TextView) result.findViewById(R.id.AnzeigeAlter));
				if(textAlter != null)
					textAlter.setText(mItem.profileExtra.alter.toString());

				TextView textGeschlecht = ((TextView) result.findViewById(R.id.AnzeigeGeschlecht));
				if(textGeschlecht != null)
					textGeschlecht.setText(mItem.profileExtra.getGenderString());

				TextView textOrientierung = ((TextView) result.findViewById(R.id.AnzeigeOrientierung));
				if(textOrientierung != null)
					textOrientierung.setText(mItem.profileExtra.getLookingForString());

				TextView textInteressen = ((TextView) result.findViewById(R.id.AnzeigeInteressen));
				if(textInteressen != null)
					textInteressen.setText(mItem.profileExtra.getInterestsString());

			}
		}
		if (mItem != null)
		{
			GetProfileAsyncTask task = new GetProfileAsyncTask();
			task.execute(getView());
		}
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_nearby_detail, container, false);
        // Show the dummy content as text in a TextView.

        return rootView;
    }
}
