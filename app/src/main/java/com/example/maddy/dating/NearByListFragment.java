package com.example.maddy.dating;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A list fragment representing a list of NearBys. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link NearByDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class NearByListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Context ctx, Integer id);

    }
    public interface RefreshCallbacks
    {
        public void onRefreshComplete();
    }
    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Context ctx, Integer id) {
            //starte hier activity zum anzeigen der profildaten
            Intent upanel = new Intent(ctx, NearByDetailActivity.class);
            upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            upanel.putExtra(NearByDetailFragment.ARG_ITEM_ID, id.toString());
            ctx.startActivity(upanel);
        }
    };


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NearByListFragment() {
    }

    NearByPeopleListAdapter<NearByPeopleEntryShort> meinAdapter = null;

    public void refreshList(final RefreshCallbacks refreshCallbacks)
    {
        class RefreshAsyncTask extends AsyncTask<Void, Void, List<NearByPeopleEntryShort>>
        {
            @Override
            protected List<NearByPeopleEntryShort> doInBackground(Void... params)
            {
                return PeopleCache.getListFixedLocation(getContext());
            }

            @Override
            protected void onPostExecute(List<NearByPeopleEntryShort> result)
            {
                meinAdapter.clear();
                meinAdapter.addAll(result);
                meinAdapter.notifyDataSetChanged();
                refreshCallbacks.onRefreshComplete();
            }
        }
        RefreshAsyncTask task = new RefreshAsyncTask();
        task.execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meinAdapter = new NearByPeopleListAdapter<NearByPeopleEntryShort>(getActivity(), new ArrayList<NearByPeopleEntryShort>());
        setListAdapter(meinAdapter);

        refreshList(new RefreshCallbacks()
        {
            @Override
            public void onRefreshComplete()
            {
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.

        mCallbacks.onItemSelected(getContext(), meinAdapter.getItem(position).userID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }


}
