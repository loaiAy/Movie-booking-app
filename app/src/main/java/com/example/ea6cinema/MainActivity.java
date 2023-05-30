package com.example.ea6cinema;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int current = 0;

    /*The mainActivity include two fragments, today and tomorrow fragments. The tablayout listens
      which tab is clicked and it notify the tabAdapter. The view pager listens to the items on the fragment. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        viewPager = findViewById(R.id.viewpage);
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), 2);

        viewPager.setAdapter(tabAdapter);
        tabLayout = findViewById(R.id.tab);
        tabLayout.selectTab(tabLayout.getTabAt(0));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() >= 0 || tab.getPosition() <= 1){
                    tabAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    // Onresume() return to the last point in MainActivity when returning back.
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(current);
    }
}
