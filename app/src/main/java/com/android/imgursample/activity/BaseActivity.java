package com.android.imgursample.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.imgursample.R;
import com.android.imgursample.util.DialogManager;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

@EActivity
public abstract class BaseActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private Dialog progressDialog;

    protected void addFragment(int containerViewId, Fragment fragment, boolean isBackStack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        if (isBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    protected void addFragmentWithAnim(int containerViewId, Fragment fragment, boolean isBackStack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.add(containerViewId, fragment, tag);
        if (isBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, boolean isBackStack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        if (isBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    protected void replaceFragmentWithAnim(int containerViewId, Fragment fragment, boolean isBackStack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(containerViewId, fragment, tag);
        if (isBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    protected void addFragmentAndClearBackStack(int containerViewId, Fragment fragment, boolean isBackStack) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, tag);
        if (fragmentManager.getBackStackEntryCount() > 1) {
            int fragmentId = fragmentManager.getBackStackEntryAt(1).getId();
            fragmentManager.popBackStack(fragmentId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (isBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    protected void replaceFragmentAndClearBackStack(int containerViewId, Fragment fragment, boolean isBackStack) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        if (fragmentManager.getBackStackEntryCount() > 1) {
            int fragmentId = fragmentManager.getBackStackEntryAt(1).getId();
            fragmentManager.popBackStack(fragmentId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (isBackStack) fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    protected SharedPreferences getPreferences() {
        if (preferences == null) preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        return preferences;
    }

    protected boolean isVisibleFragment(String fragmentTag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (fragment != null && fragment.isResumed()) return true;
        else return false;
    }

    @UiThread
    protected void showProgressDialog() {
        if (progressDialog == null)
            progressDialog = DialogManager.getInstance().getProgressDialog(this, R.string.loading);
        progressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @UiThread
    protected void hideProgressDialog() {
        progressDialog.dismiss();
    }
}
