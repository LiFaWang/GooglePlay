package tony.com.googleplay.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import tony.com.googleplay.R;
import tony.com.googleplay.ui.fragment.BaseFragment;
import tony.com.googleplay.ui.fragment.FragmentFactory;
import tony.com.googleplay.utils.UIUtils;

public class MainActivity extends AppCompatActivity {

    private String[] mMyTabNames;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private BaseFragment mFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private AnimationDrawable mAnimationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabLayout = (TabLayout) findViewById(R.id.tl_title);
        mViewPager= (ViewPager) findViewById(R.id.vp_vp_pager);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mFragment = (BaseFragment) FragmentFactory.createFragment(position);
                mFragment.loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        initActionBar();


    }

    /**
     * 初始化actionBar
     */
    private void initActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true); //设置返回键可用

        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };

        mToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            //左上角被点击，切换侧边栏
            mToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends FragmentPagerAdapter {

        private MyAdapter(FragmentManager fm) {
            super(fm);
            mMyTabNames = UIUtils.getStringArray(R.array.tab_names);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }
        @Override
        public int getCount() {
            return mMyTabNames.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mMyTabNames[position];
        }
    }
}
