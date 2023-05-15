package mud.per.iw.techapp;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static mud.per.iw.techapp.GridViewImageAdapter2.data3;
import static mud.per.iw.techapp.SecondFragment.CodeList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION =1 ;
    private String name;
    public static String svcid;
    String[] menuArray;
    private String responseString;
    private String initials;
    private String Rnkname;
    private Menu menu;
    private String inBedMenuTitle = "Login";
    private String outOfBedMenuTitle = "Logout";
    private boolean inBed = false;
    private String smonth;
    private String yrval;
    public static String qrcod;
    private BottomNavigationView bottomNavigationView;
    private FusedLocationProviderClient fusedLocationClient;

    Fragment fragment1 = null;
    private int intvat;
    private int intentFragment;
    private  List<Album> albumList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Contact Us", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//                Intent intent = new Intent(MainActivity.this, BarcodeScannerActivity.class);
//                startActivity(intent);
//
//            }
//        });

this.albumList=AlbumsAdapter.albumList;
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();



         intentFragment = 0;
        Intent intent = getIntent();

        if (intent.hasExtra("frgToLoad")) {
            intentFragment = getIntent().getExtras().getInt("frgToLoad");
            qrcod=getIntent().getExtras().getString("scode");
            //getdmsg(qrcod);

            String qwef= findCarnet(qrcod.toLowerCase());

            Log.wtf("vbn", qwef);
            if(qwef!=null) {
                if (qwef.toLowerCase().equals(qrcod.toLowerCase())) {
                    intentFragment = 2;
                    Log.wtf("vbx", String.valueOf(intentFragment));
                }
            }
        }


        switch (intentFragment){
            case 1:
                fragment1 = new Savedata();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                ft.replace(R.id.frameLayout, fragment1);
                ft.commit();
                break;
            case 2:
                fragment1 = new Savedata2();
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                ft2.replace(R.id.frameLayout, fragment1);
                ft2.commit();
                break;
           default:
               try {
                   data3.values().clear();
                   data3.clear();
                   //data3 = null;
               }catch (Exception ex){
                   Log.wtf("dfg", ex.toString());
               }
               fragment1 = new SecondFragment();
               FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
               ft1.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
               ft1.replace(R.id.frameLayout, fragment1);
               ft1.commit();
                break;
        }

        SharedPreferences prefs = getSharedPreferences("userinfo", MODE_PRIVATE);
        String restoredText = prefs.getString("UserID", null);
        if (restoredText != null) {
            name = prefs.getString("LName", "no");//"No name defined" is the default value.
            svcid = prefs.getString("UserID", "no"); //0 is the default value.
            initials = prefs.getString("FName", "no");
           Rnkname = prefs.getString("Description", "no");
            NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view);

            View headerView1 = navigationView1.getHeaderView(0);
            TextView b1 = (TextView)headerView1.findViewById(R.id.txtname);
            b1.setText(Rnkname);
            TextView b2 = (TextView)headerView1.findViewById(R.id.txtemail);
            b2.setText(initials+" "+ name);
            inBed=true;
            updateMenuTitles();
            int PERMISSION_ALL = 1;
            String[] PERMISSIONS = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    android.Manifest.permission.CAMERA
            };

            if(!hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }


            Log.wtf("testpass","");
        }
        else{
            Intent intentl = new Intent(this, login.class);
            startActivity(intentl);
        }

//        fragment1 = new SecondFragment();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
//        ft.replace(R.id.frameLayout, fragment1);
//        ft.commit();
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//
//        mAdapter = new MoviesAdapter(movieList);
//
//        recyclerView.setHasFixedSize(true);
//
//        // vertical RecyclerView
//        // keep movie_list_row.xml width to `match_parent`
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//
//        // horizontal RecyclerView
//        // keep movie_list_row.xml width to `wrap_content`
//        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
//
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        // adding inbuilt divider line
//       recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//
//        // adding custom divider line with padding 16dp
//        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
//        //recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        recyclerView.setAdapter(mAdapter);
//
//        // row click listener
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Movie movie = movieList.get(position);
//
//                  //  Toast.makeText(view.getContext(), "ITEM PRESSED = " + movie.getTitle(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, findUs.class);
//                startActivity(intent);
//
//            }
////            @Override
////            public void iconImageViewOnClick(View v, int position) {
////
////            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//               // Toast.makeText(getApplicationContext(), "is selected!", Toast.LENGTH_SHORT).show();
//            }
//        }));
//
//        prepareMovieData();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    String findCarnet(String codeIsIn) {
        for(Code carnet : CodeList) {
            if(carnet.getCode().toLowerCase().equals(codeIsIn)) {
                return carnet.getCode();
            }
        }
        return null;
    }
    private void updateMenuTitles() {

        MenuItem bedMenuItem = menu.findItem(R.id.nav_camera5);
        if (inBed) {
            bedMenuItem.setTitle(outOfBedMenuTitle);
        } else {
            bedMenuItem.setTitle(inBedMenuTitle);
        }
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_camera:
//                fragment = new Savedata();
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
//                ft.replace(R.id.frameLayout, fragment);
//                ft.commit();


                break;
            case R.id.nav_camera1:
                try {
                    data3.values().clear();
                    data3.clear();
                    //data3 = null;
                }catch (Exception ex){
                    Log.wtf("dfg", ex.toString());
                }
                fragment = new SecondFragment();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                ft1.replace(R.id.frameLayout, fragment);
                ft1.commit();
                break;
            case R.id.nav_camera2:
                try {
                    data3.values().clear();
                    data3.clear();
                    //data3 = null;
                }catch (Exception ex){
                    Log.wtf("dfg", ex.toString());
                }
                fragment = new ThirdFragment();
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                ft2.replace(R.id.frameLayout, fragment);
                ft2.commit();
                break;
            case R.id.nav_camera3:
                try {
                    data3.values().clear();
                    data3.clear();
                    //data3 = null;
                }catch (Exception ex){
                    Log.wtf("dfg", ex.toString());
                }
                Intent intent1 = new Intent(this, BarcodeScannerActivity.class);
                startActivity(intent1);
                break;
            case R.id.nav_camera5:

                if (inBed) {
                    signOut();
                } else {

                    Intent intent2 = new Intent(this, login.class);
                    startActivity(intent2);
                }
                break;

        }

        return true;
    }


    public  void signOut(){

        SharedPreferences settings = getSharedPreferences("userinfo", MODE_PRIVATE);
        settings.edit().clear().commit();
        name ="no";//"No name defined" is the default value.
        svcid = "no"; //0 is the default value.
        initials ="no";
        Rnkname ="no";
        cleard();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView b1 = (TextView)headerView.findViewById(R.id.txtname);
        b1.setText("");
        TextView b2 = (TextView)headerView.findViewById(R.id.txtemail);
        b2.setText("");
        Log.wtf("testpass","");
        inBed=false;
       // updateMenuTitles();
finish();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public Integer getdmsg(String  val1 ){
        String url = this.getResources().getString( R.string.weburl8)+"?id="+val1;



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String retval = response.toString();
                        try{

                            JSONObject dbovh = null;

                            dbovh = new JSONObject(retval);

                            JSONArray we = null;

                            we = dbovh.getJSONArray("Table");



                            int b=0;

                            for (int row = 0; row < (we.length()); row++) {

                                JSONObject we1 = we.getJSONObject(b);

                                String Code = we1.get("UId").toString();
                                if(Code != null && !Code.isEmpty()) {
                                    intvat=Integer.parseInt(Code);
                                    Log.wtf("tyt", Code);
                                }



                                b++;
                            }




                        } catch (NullPointerException ex){
                            Log.wtf("CameraDemo", ex.toString());
                        } catch (Exception e){
                            Log.wtf("CameraDemo", e.toString());
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("CameraDemo", error.toString());



                    }
                });


        singletongm.getInstance(this).addToRequestQueue(jsonObjectRequest);


        return intvat;
    }

    public  void changepass(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setMessage("Change Password?");
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText titleBox = new EditText(MainActivity.this);
        titleBox.setHint("New Password");
        titleBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
        layout.addView(titleBox); // Notice this is an add method

// Add another TextView here for the "Description" label
        final EditText descriptionBox = new EditText(MainActivity.this);
        descriptionBox.setHint("Confirm Password");
        descriptionBox.setTransformationMethod(PasswordTransformationMethod.getInstance());
        layout.addView(descriptionBox); // Another add method
        alertDialog.setView(layout);

        alertDialog.setIcon(R.drawable.ic_menu_manage);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Change password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String emnttext = titleBox.getText().toString();
                String descr = descriptionBox.getText().toString();
                if(!emnttext.equals(descr)){
                    Toast.makeText(MainActivity.this, "Please Make Sure New Password and Confirm Password Same!",
                            Toast.LENGTH_LONG).show();
                }
                else {

                   // chnguser(emnttext, cryptWithMD5(descr));
                }

            }
        });

        alertDialog.show();

    }


    public String chnguser(String  scid,String  pass){
        String url = "http://203.115.24.227:8888/mbs/chngpass.ashx?pass="+pass+"&svcid="+svcid+"";
        Log.wtf("eee", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        responseString = response.toString();
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Password Changed!",
                                Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.wtf("eee", "ww2");
                        Toast.makeText(MainActivity.this, "Error Changing Password!",
                                Toast.LENGTH_LONG).show();
                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        singletongm.getInstance(this).addToRequestQueue(jsonObjectRequest);
        return  responseString;
    }
    public void  cleard(){
//        TextView b1 = (TextView)findViewById(R.id.txtmesamt);
//        b1.setText("");

    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }





}
