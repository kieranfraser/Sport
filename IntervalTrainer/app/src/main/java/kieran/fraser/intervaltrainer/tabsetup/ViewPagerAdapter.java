package kieran.fraser.intervaltrainer.tabsetup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import kieran.fraser.intervaltrainer.Dashboard.MusicFragment;
import kieran.fraser.intervaltrainer.Dashboard.StatsFragment;
import kieran.fraser.intervaltrainer.Dashboard.WorkoutFragment;

/**
 * Created by kfraser on 08/03/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {


        switch(position){
            case 0:
                WorkoutFragment tab1 = new WorkoutFragment();
                return tab1;
            case 1:
                StatsFragment tab2 = new StatsFragment();
                return tab2;
            case 2:
                MusicFragment tab3 = new MusicFragment();
                return tab3;
            default:
                WorkoutFragment defaultfrag = new WorkoutFragment();
                return defaultfrag;
        }
//        if(position == 0) // if the position is 0 we are returning the First tab
//        {
//            EarningsFragment tab1 = new EarningsFragment();
//            return tab1;
//        }
//        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
//        {
//            ExpensesFragment tab2 = new ExpensesFragment();
//            return tab2;
//        }


    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
