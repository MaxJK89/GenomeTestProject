package quant.cann.genometestproject;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import quant.cann.genometestproject.adapters.HomeViewPagerAdapter;
import quant.cann.genometestproject.fragments.MapFragment;
import quant.cann.genometestproject.fragments.PlacesNearMeFragment;

public class HomeActivity extends AppCompatActivity {
    @Bind(R.id.home_toolbar)
    Toolbar tbToolBar;
    @Bind(R.id.home_view_pager)
    ViewPager vpViewPager;
    @Bind(R.id.home_tab_layout)
    TabLayout tlTabLayout;
    @Bind(R.id.home_app_bar_layout)
    AppBarLayout ablAppBarLayout;

    public static FragmentManager fragmentManager;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
