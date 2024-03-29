package gravicodev.qash.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import gravicodev.qash.Adapter.TabFragmentPagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gravicodev.qash.Helper.FirebaseUtils;
import gravicodev.qash.Models.QHistory;
import gravicodev.qash.Models.User;
import gravicodev.qash.Preference.QHistoryManager;
import gravicodev.qash.Preference.QMasterManager;
import gravicodev.qash.R;
import gravicodev.qash.Session.SessionManager;
import gravicodev.qash.Volley.VolleyHelper;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FrameLayout dimmer;
    private LottieAnimationView animSuccess;
    private TextView ammountAccepted;
    private static final int REQUEST_COARSE_LOCATION = 100;
    private static final int BARCODE_READER_REQUEST_CODE = 1;

    private static  HashMap<DatabaseReference, ValueEventListener> valueListenerList = new HashMap<>();
    private static HashMap<DatabaseReference, ChildEventListener> childListenerList = new HashMap<>();


    private SessionManager sessionManager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.container_main);
        tabLayout = (TabLayout) findViewById(R.id.tabs_main);

        viewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        dimmer = (FrameLayout) findViewById(R.id.dimmerSuccess);
        ammountAccepted = (TextView) findViewById(R.id.ammountAccepted);
        animSuccess = (LottieAnimationView) findViewById(R.id.animation_success);

        // Change Icon Tab Layout
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_generateqr);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_scanqr);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_history);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_more);

        Intent intent = getIntent();
        if(intent.hasExtra("index")){
            int index = intent.getIntExtra("index",3);
            // Change Color Icon Tab Layout
            tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(3).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(index).select();
        }
        else{
            // Change Color Icon Tab Layout
            tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
        }


        // Change Selected Color Icon Tab Layout
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                super.onTabSelected(tab);
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Change Title Action Bar in each fragment
                switch (position){
                    case 0 :
                        getSupportActionBar().setTitle("Qash");
                        break;
                    case 1 :
                        getSupportActionBar().setTitle("Generate");
                        break;
                    case 2 :
                        getSupportActionBar().setTitle("Scan");
                        break;
                    case 3 :
                        getSupportActionBar().setTitle("History");
                        break;
                    case 4 :
                        getSupportActionBar().setTitle("More");
                        break;
                    default :
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }

        firebaseHandler();

    }

    private void firebaseHandler() {
        FirebaseUtils.getBaseRef().child("qhistory")
                .child(sessionManager.getUser().accountNumber)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        QHistory qhistory = dataSnapshot.getValue(QHistory.class);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public User getUser(){
        User user = sessionManager.getUser();
        return user;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.

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
        if (id == R.id.action_about) {
            startActivity(new Intent(getApplication(), AboutActivity.class));
        }
        else if (id == R.id.action_logout) {
            Toast.makeText(getApplication(), "Thank you for using QashApps", Toast.LENGTH_SHORT).show();
            new QMasterManager(this).delete();
            new QHistoryManager(this).delete();
            sessionManager.logOut();
            userOut();
        }

        return super.onOptionsItemSelected(item);
    }


    public String moneyParserToInt(String data){
        ArrayList<String> input = new ArrayList<>();
        for(int i = data.length()-1;i>=0;i--){
            if(!".".equals(String.valueOf(data.charAt(i)))){
                input.add(String.valueOf(data.charAt(i)));
            }
        }

        int hasil = 0;
        int pengali = 1;

        for(int i = 0; i < input.size(); i++){
            hasil+= Integer.parseInt(input.get(i))*pengali;
            pengali *=10;
        }

        return String.valueOf(hasil);
    }

    public String moneyParserString(String data){
        ArrayList<String> input = new ArrayList<>();
        for(int i = data.length()-1;i>=0;i--){
            if(!".".equals(String.valueOf(data.charAt(i)))){
                input.add(String.valueOf(data.charAt(i)));
            }
        }

        String strHasil = "";
        int x = 1;
        for(int i=0; i < input.size();i++){
            if(x==3 && i != (input.size()-1)){
                strHasil = "." + input.get(i) + strHasil;
                x = 0;
            }else{
                strHasil = input.get(i) + strHasil;
            }
            x++;
        }

        return strHasil;
    }


    // Method for adding value listener
    public void addListener(DatabaseReference db, ValueEventListener value) {
        this.valueListenerList.put(db,value);
    }

    // Method for adding child listener
    public void addChildListener(DatabaseReference db, ChildEventListener value){
        this.childListenerList.put(db,value);
    }

    // Method for massal remover of valuelistener
    public static void removeValueListener(HashMap<DatabaseReference, ValueEventListener> hashMap) {

        for (Map.Entry<DatabaseReference, ValueEventListener> entry : hashMap.entrySet()) {

            DatabaseReference databaseReference = entry.getKey();

            ValueEventListener valueEventListener = entry.getValue();

            databaseReference.removeEventListener(valueEventListener);

        }

    }

    // Method for massal remover of childlistener
    public static void removeChildListener(HashMap<DatabaseReference, ChildEventListener> hashMap) {

        for (Map.Entry<DatabaseReference, ChildEventListener> entry : hashMap.entrySet()) {

            DatabaseReference databaseReference = entry.getKey();

            ChildEventListener childEventListener = entry.getValue();

            databaseReference.removeEventListener(childEventListener);

        }

    }

    public void showSuccess(Integer acceptedNumber){
        ammountAccepted.setText("Rp. "+ (moneyParserString(String.valueOf(acceptedNumber))));
        toolbar.setVisibility(View.GONE);
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        dimmer.setVisibility(View.VISIBLE);
        animSuccess.playAnimation();

        int delay = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbar.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);

                dimmer.setVisibility(View.GONE);

            }
        }, delay * 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
