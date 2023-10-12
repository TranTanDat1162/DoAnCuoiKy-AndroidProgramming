package edu.uef.doan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    private ListView lv;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // initiating the tabhost
        TabHost tabhost = findViewById(R.id.tabhost);
        // setting up the tab host
        tabhost.setup();
        // Code for adding Tab assignment to the tabhost
        TabHost.TabSpec spec = tabhost.newTabSpec("Bài Tập");
        spec.setContent(R.id.assignmenttab);

        // setting the name of the tab 1 as "Tab One"
        spec.setIndicator("Bài Tập");

        // adding the tab to tabhost
        tabhost.addTab(spec);

        // Code for adding Tab 2 to the tabhost
        spec = tabhost.newTabSpec("Hoạt Động");
        spec.setContent(R.id.activitytab);

        // setting the name of the tab 1 as "Tab Two"
        spec.setIndicator("Hoạt Động");
        tabhost.addTab(spec);
        viewPager  = findViewById(R.id.submitted_pager);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabLayout = findViewById(R.id.assignmenttab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.history_fragment, new HistoryFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
        // onclick listener for user details
        userDetails = (ImageButton) findViewById(R.id.userDetail);
        userDetails.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

}
