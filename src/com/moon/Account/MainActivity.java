package com.moon.Account;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.moon.adapter.MyPagerAdapter;
import com.moon.fragment.ContentFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    @ViewInject(R.id.viewPager_main)
    private ViewPager viewPager_main;

    @ResInject(id = R.array.tabTitles, type = ResType.StringArray)
    private String[] tabTitles;

    private List<Fragment> list_fragment = new ArrayList<Fragment>();

    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ViewUtils.inject(this);

        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(true);

        for (int i = 0; i <tabTitles.length; i++) {
            actionBar.addTab(actionBar.newTab().setText(tabTitles[i]).setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                    viewPager_main.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                }
            }));

            ContentFragment fragment = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("tabIndex",i);
            fragment.setArguments(bundle);
            list_fragment.add(fragment);
        }

        viewPager_main.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), list_fragment));
        viewPager_main.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                actionBar.setSelectedNavigationItem(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    public void clickButton(View view){
        switch (view.getId()){
            case R.id.textView_date:
                Log.i("1111", "111");
                break;
        }
    }
}
