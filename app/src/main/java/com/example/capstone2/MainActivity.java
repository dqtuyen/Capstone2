package com.example.capstone2;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import vn.zalopay.sdk.ZaloPaySDK;

public class MainActivity extends AppCompatActivity {
    List<Fragment> fragmentList;
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;

    public static final String ACTION_RELOAD_HOME_FRAGMENT = "com.example.capstone2.RELOAD_HOME_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getAction() != null && getIntent().getAction().equals(ACTION_RELOAD_HOME_FRAGMENT)) {
            reloadHomeFragment();
        }

        // Lấy userEmail từ Intent
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("user_email");

        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        fragmentList = new ArrayList<>();
        // Truyền userEmail cho HomeFragment khi tạo instance của nó
        fragmentList.add(HomeFragment.newInstance(userEmail));
        fragmentList.add(QRFragment.newInstance(userEmail));
        fragmentList.add(AccountFragment.newInstance(userEmail));


        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);

        setEvent();

    }

    void setEvent() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_1).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_2).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_tab_3).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_tab_1) {
                    mViewPager.setCurrentItem(0);
                    return true;
                } else if (item.getItemId() == R.id.menu_tab_2) {
                    mViewPager.setCurrentItem(1);
                    return true;
                } else if (item.getItemId() == R.id.menu_tab_3) {
                    mViewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
    private void reloadHomeFragment() {
        // Replace HomeFragment với chính nó để làm mới lại nó
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recyclerView, new HomeFragment())
                .commit();
    }

}
