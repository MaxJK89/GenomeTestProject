package quant.cann.genometestproject;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import quant.cann.genometestproject.adapters.HomeViewPagerAdapter;
import quant.cann.genometestproject.fragments.MapFragment;
import quant.cann.genometestproject.fragments.PlacesNearMeFragment;

public class HomeActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    @Bind(R.id.home_toolbar)
    Toolbar tbToolBar;
    @Bind(R.id.home_view_pager)
    ViewPager vpViewPager;
    @Bind(R.id.home_tab_layout)
    TabLayout tlTabLayout;
    @Bind(R.id.home_app_bar_layout)
    AppBarLayout ablAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        setSupportActionBar(tbToolBar);
        initViewPager(vpViewPager);
        tlTabLayout.setupWithViewPager(vpViewPager);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initViewPager(ViewPager viewPager) {
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MapFragment(), "MAP");
        adapter.addFrag(new PlacesNearMeFragment(), "PLACES NEAR ME");
        viewPager.setAdapter(adapter);
    }


}
