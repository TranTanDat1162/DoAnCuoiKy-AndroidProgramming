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
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    private ListView lv;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageButton userDetails;
    private FloatingActionButton create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


//        // initiating the tabhost
        TabHost tabhost = findViewById(R.id.tabhost);

//        // setting up the tab host
        tabhost.setup();
//        // Code for adding Tab assignment to the tabhost
//        TabHost.TabSpec spec = tabhost.newTabSpec("Bài Tập");
//
//        // setting the name of the tab 1 as "Tab One"
//        spec.setIndicator("Bài Tập", getResources().getDrawable(R.drawable.tabhost_icon_books)).setContent(R.id.assignmenttab);
//
//        // adding the tab to tabhost
//        tabhost.addTab(spec);
//
//        // Code for adding Tab 2 to the tabhost
//        spec = tabhost.newTabSpec("Hoạt Động");

        // setting the name of the tab 1 as "Tab Two"
//        spec.setIndicator("Hoạt Động", getResources().getDrawable(R.drawable.baseline_collections_bookmark_24)).setContent(R.id.activitytab);
//        tabhost.addTab(spec);

        TabHost.TabSpec spec1 = tabhost.newTabSpec("Bài Tập");
        spec1.setIndicator("",getResources().getDrawable(R.drawable.book_icon_tabhost));
        spec1.setContent(R.id.assignmenttab);

        TabHost.TabSpec spec2 = tabhost.newTabSpec("Hoạt Động");
        spec2.setIndicator("",getResources().getDrawable(R.drawable.history_icon_tabhost));
        spec2.setContent(R.id.activitytab);

        tabhost.addTab(spec1);
        tabhost.addTab(spec2);
        final TabWidget tabWidget = tabhost.getTabWidget();
        for (int i = 0; i < tabWidget.getTabCount(); i++) {
            final View tab = tabWidget.getChildTabViewAt(i);
            final TextView title = (TextView) tab.findViewById(android.R.id.title);
            title.setSingleLine();
        }
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
                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
        // FAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        // FAB.setOnClickListener(new View.OnClickListener(){
        //     @Override
        //     public void onClick(View view) {
        //         Intent intent = new Intent(HomeActivity.this, CreateBaiTap.class);
        //         startActivity(intent);
        //     }
        // });
        create= findViewById(R.id.floatingActionButton);
        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
    }

}
