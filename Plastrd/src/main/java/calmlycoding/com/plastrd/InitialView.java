package calmlycoding.com.plastrd;

import java.util.ArrayList;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class InitialView extends FragmentActivity implements
        ActionBar.TabListener {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    static Plastrd global;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialview);
        global = (Plastrd) getApplicationContext();

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (!global.connected) {
            Toast toast = Toast.makeText(InitialView.this, "No Connection.", Toast.LENGTH_LONG);
            toast.show();
        } else {
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the app.
            mSectionsPagerAdapter = new SectionsPagerAdapter(
                    getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            // When swiping between different sections, select the corresponding
            // tab. We can also use ActionBar.Tab#select() to do this if we have
            // a reference to the Tab.
            mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

            });

            // For each of the sections in the app, add a tab to the action bar.
            for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                // Create a tab with text corresponding to the page title defined by
                // the adapter. Also specify this Activity object, which implements
                // the TabListener interface, as the callback (listener) for when
                // this tab is selected.
                actionBar.addTab(actionBar.newTab()
                        .setText(mSectionsPagerAdapter.getPageTitle(i))
                        .setTabListener(this));
            }


        }
    }

    //Menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //Onclick menu options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.turnOff:
                stopService();
                return true;
            case R.id.turnOn:
                startService();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Stops the repeated broadcast receiver alarm
    public void stopService() {
        Intent myIntent = new Intent(InitialView.this, RepeatingAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(InitialView.this, 0, myIntent, 0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        am.cancel(pendingIntent);

        Toast toast = Toast.makeText(InitialView.this, "Your playlist has been turned off.", Toast.LENGTH_LONG);
        toast.show();
    }

    //Starts the repeated broadcast receiver alarm
    public void startService() {
        //Set up repeating alarm code (This will have to be moved to a start service method after playlists are set up
        Intent myIntent = new Intent(InitialView.this, RepeatingAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(InitialView.this, 0, myIntent, 0);

        // We want the alarm to go off 15 seconds from now.
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 15 * 1000;

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                15 * 1000, pendingIntent);

        Toast toast = Toast.makeText(InitialView.this, "Random playlist has been turned on.", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 6 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Newest";
                case 1:
                    return "Featured";
                case 2:
                    return "Popular";
                case 3:
                    return "Newest Playlists";
                case 4:
                    return "Featured Playlists";
                case 5:
                    return "Popular Playlists";
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            switch (getArguments().getInt(
                    ARG_SECTION_NUMBER)) {
                //Newest screen
                case 1:
                    GridView newGrid = new GridView(getActivity());
                    newGrid.setColumnWidth((int) Math.round(global.width / 2));
                    newGrid.setNumColumns(2);
                    newGrid.setVerticalSpacing(global.horizontalSpacing);
                    newGrid.setHorizontalSpacing(global.horizontalSpacing);
                    newGrid.setAdapter(new ImageAdapter(getActivity(), global, 1));
                    return newGrid;

                //Featured screen
                case 2:
                    GridView featuredGrid = new GridView(getActivity());
                    featuredGrid.setColumnWidth((int) Math.round(global.width / 2));
                    featuredGrid.setNumColumns(3);
                    featuredGrid.setVerticalSpacing(global.horizontalSpacing);
                    featuredGrid.setHorizontalSpacing(global.horizontalSpacing);
                    featuredGrid.setAdapter(new ImageAdapter(getActivity(), global, 2));

                    return featuredGrid;
                //Popular screen
                case 3:
                    GridView popularGrid = new GridView(getActivity());
                    popularGrid.setColumnWidth((int) Math.round(global.width / 2));
                    popularGrid.setNumColumns(2);
                    popularGrid.setVerticalSpacing(global.horizontalSpacing);
                    popularGrid.setHorizontalSpacing(global.horizontalSpacing);

                    popularGrid.setAdapter(new ImageAdapter(getActivity(), global, 2));

                    return popularGrid;
                default:
                    System.out.println(getArguments().getInt(
                            ARG_SECTION_NUMBER));
                    // Create a new TextView and set its text to the fragment's section
                    // number argument value.
                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setText(Integer.toString(getArguments().getInt(
                            ARG_SECTION_NUMBER)));
                    return textView;
            }
        }
    }
}

