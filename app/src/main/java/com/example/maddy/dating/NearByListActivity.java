package com.example.maddy.dating;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


/**
 * An activity representing a list of NearBys. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NearByDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link NearByListFragment} and the item details
 * (if present) is a {@link NearByDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link NearByListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class NearByListActivity extends AppCompatActivity
        implements NearByListFragment.Callbacks {

    private Menu optionsMenu;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nearby, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.nearby_menu_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nearby_menu_refresh:
                setRefreshActionButtonState(true);
                ((NearByListFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nearby_list))
                        .refreshList(new NearByListFragment.RefreshCallbacks(){
                            @Override
                            public void onRefreshComplete()
                            {
                                setRefreshActionButtonState(false);
                            }
                        });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_list);

        if (findViewById(R.id.nearby_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((NearByListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nearby_list))
                    .setActivateOnItemClick(true);
        }
    }

    /**
     * Callback method from {@link NearByListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Context ctx, Integer id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(NearByDetailFragment.ARG_ITEM_ID, id);
            NearByDetailFragment fragment = new NearByDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nearby_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, NearByDetailActivity.class);
            detailIntent.putExtra(NearByDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
