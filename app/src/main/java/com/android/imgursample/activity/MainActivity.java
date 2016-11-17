package com.android.imgursample.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.imgursample.R;
import com.android.imgursample.fragments.AboutFragment;
import com.android.imgursample.fragments.AboutFragment_;
import com.android.imgursample.fragments.GalleryDetailFragment;
import com.android.imgursample.fragments.GalleryDetailFragment_;
import com.android.imgursample.fragments.GalleryFragment;
import com.android.imgursample.fragments.GalleryFragment_;
import com.android.imgursample.interfaces.OnMainFragmentListener;
import com.android.imgursample.restful.model.ImageObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements OnMainFragmentListener {

    private String[] mPlanetTitles;

    @ViewById(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById(R.id.app_bar)
    AppBarLayout appBarLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    Toolbar toolbar;

    @AfterViews
    protected void afterViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.gallery);
        setUpNavigationView();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
                if (stackHeight > 0) { // if we have something on the stack (doesn't include the current shown fragment)
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    setUpNavigationView();
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }

        });
        toGalleryFragment();
    }

    public void toGalleryFragment() {
        GalleryFragment galleryFragment = new GalleryFragment_();
        replaceFragment(R.id.content_frame, galleryFragment, false);
    }

    public void toAboutFragment() {
        AboutFragment aboutFragment = new AboutFragment_();
        replaceFragment(R.id.content_frame, aboutFragment, false);
    }

    @Override
    public void onCloseFragment(String tag) {

    }

    @Override
    public void onStartFragment(String tag) {

    }

    @Override
    public void toImageDetailFragment(ImageObject imageObject) {
        GalleryDetailFragment galleryDetailFragment = GalleryDetailFragment_.builder().imageObject(imageObject).build();
        getSupportActionBar().setTitle(R.string.gallery_detail);
        appBarLayout.setExpanded(true);
        replaceFragment(R.id.content_frame, galleryDetailFragment, true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        getSupportActionBar().setTitle(R.string.gallery);
                        toGalleryFragment();
                        break;
                    case R.id.nav_about:
                        getSupportActionBar().setTitle(R.string.about);
                        toAboutFragment();
                        break;
                }
                drawerLayout.closeDrawers();
                item.setChecked(item.isChecked() ? false : true);
                return true;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
