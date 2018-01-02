package com.example.harsh.projectthree_a1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity implements ListFragment.OnURLSelectedListener,ActivityCompat.OnRequestPermissionsResultCallback {

    boolean detailPage = false;
    DetailFragment detailFragment;
    String myURL;
   final int reqcode=0;
    int pos;

    private static final String ACTION_BOOM = "com.example.harsh.projectthree_a2.boom_action";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("AndroidFragmentActivity", "onCreate()");

        Log.v("AndroidFragmentsavedInstanceState", savedInstanceState == null ? "true" : "false");

        setContentView(R.layout.activity_main);

            /*If launching first time just show list*/
            if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ListFragment listFragment = new ListFragment();

            ft.add(R.id.displayList, listFragment, "List_Fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }

        if(savedInstanceState!=null)
            myURL=savedInstanceState.getString("Selected URL",myURL);
        /*if in ladscape view and there's a url selected*/
        if (findViewById(R.id.displayDetail) != null && myURL!=null) {
            detailPage = true;
            getFragmentManager().popBackStack();


             detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.displayDetail);
            if (detailFragment == null) {
                findViewById(R.id.displayDetail).setVisibility(View.VISIBLE);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setURLContent(myURL);
                ft.replace(R.id.displayDetail, detailFragment, "Detail_Fragment1");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);

                ft.commit();
            }
        }
        /*no url then hide fragment*/
        else if(findViewById(R.id.displayDetail)!=null){
            findViewById(R.id.displayDetail).setVisibility(View.GONE);
        }


    }

/*WHen a URL is selected as by the interface from ListFragment class*/
    @Override
    public void onURLSelected(String URL) {
        Log.v("AndroidFragmentActivity", URL);
        myURL=URL;
        if (detailPage) {
            DetailFragment detailFragment = (DetailFragment)
                    getFragmentManager().findFragmentById(R.id.displayDetail);
            detailFragment.updateURLContent(URL);
        }else if(findViewById(R.id.displayDetail) != null)
        {
            findViewById(R.id.displayDetail).setVisibility(View.VISIBLE);
            detailPage = true;
            getFragmentManager().popBackStack();

            detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.displayDetail);
            if (detailFragment == null) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                detailFragment = new DetailFragment();
                detailFragment.setURLContent(myURL);
                ft.replace(R.id.displayDetail, detailFragment, "Detail_Fragment1");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
        else {
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setURLContent(URL);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.displayList, detailFragment, "Detail_Fragment2");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
/*Retrieve variable between config changes*/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null) {
            myURL = savedInstanceState.getString("Selected URL");
            Log.d("Main Activity URL",myURL);

        }

    }
/*Store variable between config changes*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int pos;
        if(myURL!=null)
        if(!myURL.trim().contentEquals(""))
        outState.putString("Selected URL",myURL);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {
            case R.id.exit_app:

                return true;
            case R.id.launch_A2:

                final String[] permissions = new String[]{"com.example.harsh.projectthree_a2.BOOM_PERM"};
               /*Check the permission before sending broadcast*/
                if ( ContextCompat.checkSelfPermission(this,"com.example.harsh.projectthree_a2.BOOM_PERM") != PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, "com.example.harsh.projectthree_a2.BOOM_PERM")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("To move to application 2 you need the permission which Goes boom");
                        builder.setTitle("Application 2");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, permissions, reqcode);



                            }
                        });

                        builder.show();
                    } else {
                        ActivityCompat.requestPermissions(this, permissions, 0);

                    }

                }
               else {
                    final Intent intent = new Intent();
                    intent.setAction(ACTION_BOOM);
                    //flag insures that the broadcast reachs out even apps not running
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendBroadcast(intent);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
           if(detailPage)
           {
FragmentTransaction  ft=getFragmentManager().beginTransaction();
             List<Fragment> a1= getFragmentManager().getFragments();
               if(this.detailFragment!=null) {
                   for (Fragment frag : a1) {
                       ft.remove(frag);
                       ft.commit();
                   }
               }
               /*If there was a detail page in Landscape then load the web view */
          ft = getFragmentManager().beginTransaction();
               ListFragment listFragment = new ListFragment();

               ft.add(R.id.displayList, listFragment, "List_Fragment");
               ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
               ft.commit();
               DetailFragment detailFragment = new DetailFragment();
               detailFragment.setURLContent(myURL);
                ft = getFragmentManager().beginTransaction();
               ft.replace(R.id.displayList, detailFragment, "Detail_Fragment2");
               ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
               ft.addToBackStack(null);
               ft.commit();



           }
             else {
               /*if there wasn;t any website selected just recreate activity*/
               this.recreate();
             }
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
/* Portrait to landcape trasition with URL+ Portrait to landscape without url  */
            FragmentTransaction  ft=getFragmentManager().beginTransaction();
            List<Fragment> a1= getFragmentManager().getFragments();
            if(this.detailFragment!=null) {
                for (Fragment frag : a1) {
                    ft.remove(frag);
                    ft.commit();
                }
            }
          this.recreate();

            }
        }

    @Override
    public void onBackPressed() {
        myURL=null;
        if(detailPage) {
            detailPage = false;
        findViewById(R.id.displayDetail).setVisibility(View.GONE);
                 }
        super.onBackPressed();
    }
/* void onBackKeyPressed() {
        boolean backKeyHandled = false;
        getFragmentManager().popBackStack();
        Fragment activeFragment = getActiveFragment();
        if (activeFragment instanceof BackKeyListener) {
            backKeyHandled = ((BackKeyListener) activeFragment).onBackPressed();
        }
        if (!backKeyHandled) {
            // If should process back key normally, pass to super..
           super.onBackPressed();
        }
    }

    Fragment getActiveFragment() {
        // I'm assuming you place all your fragments here, if not..
        // alter to suit
        return getFragmentManager().findFragmentById(R.id.displayList);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }*/


    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case reqcode:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final Intent intent = new Intent();
                    intent.setAction(ACTION_BOOM);
                    //flag insures that the broadcast reachs out even apps not running
                    intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendBroadcast(intent);
                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
