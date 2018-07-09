package com.jsaw.ibreast.laugh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.jsaw.ibreast.R;
import java.util.Objects;

public class laugh extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laugh);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // 設定 ViewPager 和 Pager Adapter.
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            // return fragment due to tab position
            switch (position) {
                case 0:
                    fragment = new laugh_night();
                    break;
                case 1:
                    fragment = new laugh_fashion();
                    break;
                case 2:
                    fragment = new laugh_fashion();
                    break;
                case 3:
                    fragment = new laugh_fashion();
                    break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "一夜好眠";
                case 1:
                    return "時尚自信";
                case 2:
                    return "心靈補給";
                case 3:
                    return "音樂饗宴";
                default:
                    return null;
            }
        }
    }

    //action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, Objects.requireNonNull(upIntent))) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;

            default:
                return true;
        }
    }
}
