package gravicodev.qash.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
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
import android.widget.Toast;

import gravicodev.qash.Adapter.TabFragmentPagerAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gravicodev.qash.Models.User;
import gravicodev.qash.R;
import gravicodev.qash.Session.SessionManager;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
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

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_generateqr);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_scanqr);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_history);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_transaction);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(Color.parseColor("#014A87"), PorterDuff.Mode.SRC_IN);

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }


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

            sessionManager.logOut();
            userOut();
        }

        return super.onOptionsItemSelected(item);
    }

    public String getUid(){
        User user = sessionManager.getUser();
        return user.getUserid();
    }
    public User getUser(){
        User user = sessionManager.getUser();
        return user;
    }

    public String moneyParser(int data){
        int delimiter = 10;
        ArrayList<Integer> hasil = new ArrayList<>();
        while(true){
            if(data > 0){
                hasil.add(data % delimiter);
                data /= delimiter;
            }else{
                break;
            }
        }

        String strHasil = "";
        int x = 1;
        for(int i=0; i < hasil.size();i++){
            if(x==3){
                strHasil = "." + String.valueOf(hasil.get(i)) + strHasil;
                x = 0;
            }else{
                strHasil = String.valueOf(hasil.get(i)) + strHasil;
            }
            x++;
        }

        return "Rp. " +strHasil;
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

        Log.d(TAG,""+moneyParserToInt(strHasil));

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

    @Override
    protected void onStop() {
        super.onStop();
    }
}
