package com.appbell.iraisefund4u.resto.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.appbell.iraisefund4u.R;

public class UserLoginActivity extends CommonActionBarActivity{
    private static final String CLASS_ID = UserLoginActivity.class.getSimpleName() + ": ";

    Fragment currentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);

		initToolbar(getString(R.string.lblTitleLogin), true);

		//Load Login Fragment
		loadFragment(UserLoginFragment.getInstance());

		findViewById(R.id.btnForgotPwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFragment == null || currentFragment instanceof UserLoginFragment){
                    loadFragment(ForgotPasswordFragment.getInstance());
                }else{
                    loadFragment(UserLoginFragment.getInstance());
                }
            }
        });
	}


    /**
     * Load the fragment
     * @param fragment
     */
	private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(currentFragment != null)
            transaction.setCustomAnimations(R.anim.pull_in_bottom_to_top, R.anim.slide_right_out);

        if(currentFragment == null){
            transaction.add(R.id.fragmentContainer4Login, fragment);
        }else{
            transaction.replace(R.id.fragmentContainer4Login, fragment);
        }

        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();

        if(fragment instanceof ForgotPasswordFragment) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
        currentFragment = fragment;

        setTextForHeaders();

    }

    /**
     * Change the header texts
     */
    private void setTextForHeaders(){

        boolean isLoginFragment = currentFragment instanceof UserLoginFragment;

        ((Button) findViewById(R.id.btnForgotPwd)).setText(getString(isLoginFragment ? R.string.lblForgotPassword : R.string.lblBackToUserLogin));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
        }else{
            Intent intent = NavUtils.getParentActivityIntent(this);
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("isSyncData", true);
                NavUtils.navigateUpTo(this, intent);
            }
        }
        return true;
    }
}
