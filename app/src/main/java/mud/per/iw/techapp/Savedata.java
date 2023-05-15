package mud.per.iw.techapp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.FileProvider.getUriForFile;

import static mud.per.iw.techapp.Expandpr.prds;
import static mud.per.iw.techapp.Expandsps.sps;
import static mud.per.iw.techapp.GridViewImageAdapter2.data3;
import static mud.per.iw.techapp.MainActivity.qrcod;
import static mud.per.iw.techapp.MainActivity.svcid;

public class Savedata extends Fragment implements AdapterView.OnItemSelectedListener {
    View view;
    Button firstButton;
    Button savebtn;
    Button spbtn;
    Button prbtn;
    int TAKE_PHOTO_CODE = 0;
    public static int count = 0;
    private GridView gridView;
    private int columnWidth;
    private GridViewImageAdapter2 adapter;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private String purl="";
    private String changeres;
    private FusedLocationProviderClient fusedLocationClient;
    private String lti;
    private String lngi;
    private String sdesc1="";
    private String visitdesc1="";
    private String sage1="";
    private String stadress1="";
    private String savg1="";
    private String sdead1="";
    private String srmk1="";
    private String sbread1="";
    private String scnt1="";
    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private List<Species> spdata;
    private List<Products> prdata;
    private Expandsps spadapter2;
    private Expandpr spadapter3;


    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //  final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        ////File imagePath = new File(container.getContext().getFilesDir(), "images");
       // File newdir = new File(dir);
       // imagePath.mkdirs();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = inflater.inflate(R.layout.actsavdata, container, false);
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        checkloc();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
////////////////////////////////////////////////////////////////
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lti= String.valueOf(location.getLatitude());
                            lngi= String.valueOf(location.getLongitude());
                            if(lti!=null){
                                TextView ll = (TextView) view.findViewById(R.id.textViewll);
                                ll.setText("Latitude:  "+lti+"  Longitude:  "+lngi);
                            }
                        }
                    }
                });



        spdata = new ArrayList<>();
        prdata = new ArrayList<>();
        Spinner spinner = (Spinner)view.findViewById(R.id.state);
        spinner.setOnItemSelectedListener(this);

        String[] stockArr = new String[SecondFragment.siteList.size()];
        stockArr = SecondFragment.siteList.toArray(stockArr);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(container.getContext(),R.layout.spinner_item, stockArr);

        spinner.setAdapter(adapter);
/////////////////////////////////////////////////////////////////////////////
        Spinner spinner1 = (Spinner)view.findViewById(R.id.suburb);
        spinner1.setOnItemSelectedListener(this);

        String[] stockArr1 = new String[SecondFragment.sitetypList.size()];
        stockArr1 = SecondFragment.sitetypList.toArray(stockArr1);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(container.getContext(),R.layout.spinner_item, stockArr1);

        spinner1.setAdapter(adapter1);

////////////////////////////////////////////////////////////////////////////////
        Spinner spinner2 = (Spinner)view.findViewById(R.id.species);
        spinner2.setOnItemSelectedListener(this);

        String[] stockArr2 = new String[SecondFragment.spList.size()];
        stockArr2 = SecondFragment.spList.toArray(stockArr2);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(container.getContext(),R.layout.spinner_item, stockArr2);

        spinner2.setAdapter(adapter2);

 ////////////////////////////////////////////////////////////////////////////////////////
        Spinner spinner3 = (Spinner)view.findViewById(R.id.prod);
        spinner3.setOnItemSelectedListener(this);

        String[] stockArr3 = new String[SecondFragment.prodList.size()];
        stockArr3 = SecondFragment.prodList.toArray(stockArr3);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(container.getContext(),R.layout.spinner_item, stockArr3);

        spinner3.setAdapter(adapter3);
/////////////////////////////////////////////////////////////////////////////////////////
        firstButton = (Button) view.findViewById(R.id.btn_tpic);

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;

                String file =count+".jpg";
                try {



                    File file2 = new File(new File(Environment.getExternalStorageDirectory(), "Pictures"), file);

                    Uri outputFileUri = FileProvider.getUriForFile(container.getContext(), "mud.per.iw.techapp.fileprovider", file2);



                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                  purl=  outputFileUri.toString();
                    Log.wtf("CameraDemo", outputFileUri.toString());
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }


                //Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_LONG).show();
            }
        });


        spbtn = (Button) view.findViewById(R.id.btn_sps);

        spbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                try {
                    int spcpos = spinner2.getSelectedItemPosition();
                    String spcpos2 = SecondFragment.spList1.get(spcpos);
                    EditText sbread = (EditText) view.findViewById(R.id.sbread);
                    sbread1 = sbread.getText().toString();
                    EditText sage = (EditText) view.findViewById(R.id.sage);
                    sage1 = sage.getText().toString();
                    EditText savg = (EditText) view.findViewById(R.id.savg);
                    savg1 = savg.getText().toString();
                    EditText sdead = (EditText) view.findViewById(R.id.sdead);
                    sdead1 = sdead.getText().toString();
                    EditText scnt = (EditText) view.findViewById(R.id.cnt);
                    scnt1 = scnt.getText().toString();
//                    EditText srmk = (EditText) view.findViewById(R.id.srmk);
//                    srmk1 = srmk.getText().toString();

                    if(validate2()) {
                        Species a = new Species(spcpos2, sbread1, sage1, spinner2.getSelectedItem().toString(), savg1, sdead1, scnt1, "");
                        spdata.add(a);
                        spadapter2 = new Expandsps(spdata);
                        recyclerView = (RecyclerView) view.findViewById(R.id.recycler2);
                        recyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
//fetch data and on ExpandableRecyclerAdapter
                        recyclerView.setAdapter(spadapter2);
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }


                //Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_LONG).show();
            }
        });
        prbtn = (Button) view.findViewById(R.id.btn_prd);

        prbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                try {
                    int prodpos = spinner3.getSelectedItemPosition();
                    String prodpos2 = SecondFragment.prodList1.get(prodpos);
                    Products a = new Products(prodpos2,spinner3.getSelectedItem().toString());
                    prdata.add(a);
                    spadapter3 = new Expandpr( prdata);
                    recyclerView2 = (RecyclerView)view.findViewById(R.id.recycler4);
                    recyclerView2.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext());
                    recyclerView2.setLayoutManager(layoutManager);
//fetch data and on ExpandableRecyclerAdapter
                    recyclerView2.setAdapter(spadapter3);



                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }


                //Toast.makeText(getActivity(), "First Fragment", Toast.LENGTH_LONG).show();
            }
        });


        gridView = (GridView) view.findViewById(R.id.grid_view2);
        savebtn = (Button) view.findViewById(R.id.btn_sn);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {


                EditText sdesc = (EditText) view.findViewById(R.id.stdesc);
                 sdesc1 = sdesc.getText().toString();
                EditText visitdesc = (EditText) view.findViewById(R.id.visitdesc);
                 visitdesc1 = visitdesc.getText().toString();




//                int spcpos = spinner2.getSelectedItemPosition();
//                String spcpos2 = SecondFragment.spList1.get(spcpos);
                int stpos = spinner.getSelectedItemPosition();
                String stpos2 = SecondFragment.siteList1.get(stpos);
                int subpos = spinner1.getSelectedItemPosition();
                String subpos2 = SecondFragment.sitetypList1.get(subpos);



if(validate()){
    view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
    savebtn.setEnabled(false);
    isertdata(qrcod,stpos2,subpos2,sdesc1,lti,lngi,"13",svcid,"1",visitdesc1,svcid);
    Log.wtf("cls1", qrcod);
}




            }
            });


        return view;
    }

    public boolean validate() {
        boolean valid = true;

         String sdesc2 =sdesc1;
         String visitdesc2=visitdesc1;

         String srmk2=srmk1;

        EditText sdesc = (EditText) view.findViewById(R.id.stdesc);
        EditText visitdesc = (EditText) view.findViewById(R.id.visitdesc);







        if (sdesc2.isEmpty() ) {


            sdesc.setError("enter value!");
            valid = false;
        } else {
            sdesc.setError(null);
        }
        if (visitdesc2.isEmpty() ) {


            visitdesc.setError("enter value!");
            valid = false;
        } else {
            visitdesc.setError(null);
        }





        return valid;
    }

    public boolean validate2() {
        boolean valid = true;

        String sdesc2 =sdesc1;

        String sage2=sage1;
        String scnt2=scnt1;
        String savg2=savg1;
        String sdead2=sdead1;
        String srmk2=srmk1;
        String sbread2=sbread1;
        //  EditText sdesc = (EditText) view.findViewById(R.id.sdesc);

        EditText sage = (EditText) view.findViewById(R.id.sage);

        EditText savg = (EditText) view.findViewById(R.id.savg);

        EditText sdead = (EditText) view.findViewById(R.id.sdead);

        //EditText srmk = (EditText) view.findViewById(R.id.srmk);

        EditText sbread = (EditText) view.findViewById(R.id.sbread);

        EditText scnt = (EditText) view.findViewById(R.id.cnt);




        if (scnt2.isEmpty() ) {


            scnt.setError("enter value!");
            valid = false;
        } else {
            scnt.setError(null);
        }
        if (sage2.isEmpty() ) {


            sage.setError("enter value!");
            valid = false;
        } else {
            sage.setError(null);
        }


        if (savg2.isEmpty() ) {


            savg.setError("enter value!");
            valid = false;
        } else {
            savg.setError(null);
        }

        if (sdead2.isEmpty() ) {


            sdead.setError("enter value!");
            valid = false;
        } else {
            sdead.setError(null);
        }


        if (sbread2.isEmpty() ) {


            sbread.setError("enter value!");
            valid = false;
        } else {
            sbread.setError(null);
        }

        return valid;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
count=0;
            Log.wtf("cls1", ((Object) this).getClass().getSimpleName() + " is NOT on screen");
        }
        else
        {
            Log.wtf("cls2", ((Object) this).getClass().getSimpleName() + " is on screen");
        }
    }
    public String isertdata(String code,String suid,String subuid,String desc,String lat,String longi,String cntry,String cruser,String vistyp,String visdet,String empid ){
        String url = getContext().getResources().getString( R.string.weburl10)+"?code="+code+"&suid="+suid+"&subuid="+subuid+"&desc="+desc+"&lat="+lat+"&longi="+longi+"&cntry="+cntry+"&cruser="+cruser+"&vistyp="+vistyp+"&visdet="+visdet+"&empid="+empid+"&pcnt="+data3.size()+"";



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        changeres = response.toString();
                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(response.toString());


                        JSONArray we = null;

                        we = jsonObject.getJSONArray("Table");


                        JSONObject we1 = we.getJSONObject(0);

                        String s1 = we1.get("name").toString();
                            insertfolder(s1);
                            for(int u=0;u<sps.size();u++) {
                                isertsp(s1,sps.get(u).getspuid(),sps.get(u).getspdesc(),sps.get(u).getavgcns(),sps.get(u).getdeadpr(),sps.get(u).getage(),sps.get(u).getbreed(),sps.get(u).getcnt(),svcid );
                            }
                            for(int u=0;u<prds.size();u++) {
                                isertpr(s1,prds.get(u).getpuid(),svcid);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("nwe1", error.toString());
                        Toast.makeText(getContext(), "Error!",
                                Toast.LENGTH_LONG).show();


                    }
                });


        singletongm.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


        return changeres;
    }

    public String isertsp(String visitid ,String spid ,String inspdet ,String avgcns,String dedpr ,String spage ,String brd ,String spcnt,String empid){
        String url = getContext().getResources().getString( R.string.weburl18)+"?visitid="+visitid+"&spid="+spid+"&inspdet="+inspdet+"&avgcns="+avgcns+"&dedpr="+dedpr+"&spage="+spage+"&brd="+brd+"&spcnt="+spcnt+"&empid="+empid+"";



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {





                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("nwe1", error.toString());
                        Toast.makeText(getContext(), "Error!",
                                Toast.LENGTH_LONG).show();


                    }
                });


        singletongm.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


        return changeres;
    }

    public String isertpr(String visitid ,String prid,String empid){
        String url = getContext().getResources().getString( R.string.weburl17)+"?visitid="+visitid+"&prid="+prid+"&empid="+empid+"";



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {





                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("nwe1", error.toString());
                        Toast.makeText(getContext(), "Error!",
                                Toast.LENGTH_LONG).show();


                    }
                });


        singletongm.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);


        return changeres;
    }


    public String insertfolder(String  vtuid){

        String url = this.getContext().getResources().getString( R.string.weburl12)+"?vtuid="+vtuid+"";
        Log.wtf("eee", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                       for(int u=0;u<data3.size();u++){
                           InputStream is = null;
                           try {
                               is = getContext().getContentResolver().openInputStream( Uri.parse(data3.get(u)));
                           } catch (FileNotFoundException e) {
                               e.printStackTrace();
                           }
                           Bitmap bitmap = BitmapFactory.decodeStream(is);
                           insertimage(bitmap, String.valueOf(u+1)+".jpg",vtuid);

                       }
                       try {
                           data3.values().clear();
                           data3.clear();
                           //data3 = null;
                       }catch (Exception ex){
                           Log.wtf("dfg", ex.toString());
                       }
                        Toast.makeText(getContext(), "Success!",
                                Toast.LENGTH_LONG).show();
                        Fragment  fragment1 = new SecondFragment();
                        FragmentTransaction ft1 =getActivity().getSupportFragmentManager().beginTransaction();
                        ft1.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
                        ft1.replace(R.id.frameLayout, fragment1);
                        ft1.commit();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.wtf("ee2", error.toString());

                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        singletongm.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        return  "";
    }

    public String insertimage(Bitmap  bitmap,String imgn,String vtuid){

        String url = this.getContext().getResources().getString( R.string.weburl13);
        Log.wtf("eee", url);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

                if(s.equals("true")){
                   // Toast.makeText(getContext(), "Uploaded Successful", Toast.LENGTH_LONG).show();
                }
                else{
                   // Toast.makeText(getContext(), "Some error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", imageString);
                parameters.put("imgn", imgn);
                parameters.put("vtuid", vtuid);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

        return  "";
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.wtf("CameraDemo", "Pic saved");
            InitilizeGridLayout(count);
            imagePaths.add(purl);
            adapter = new GridViewImageAdapter2(Savedata.this.getContext(), imagePaths,
                    columnWidth);


            gridView.setAdapter(adapter);

        }
    }

public void checkloc(){

    LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
    boolean gps_enabled = false;
    boolean network_enabled = false;

    try {
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    } catch(Exception ex) {}

    try {
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    } catch(Exception ex) {}

    if(!gps_enabled && !network_enabled) {
        // notify user
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.gps_network_not_enabled)
                .setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        getContext().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                        .setNegativeButton(R.string.Cancel,null)
                        .show();

    }

}

    private void InitilizeGridLayout(int cnty) {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS +8) * padding)) / AppConstant.NUM_OF_COLUMNS);

        gridView.setNumColumns(AppConstant.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        double newh=  cnty/3;
        newh=  Math.ceil(newh)+1;
        Log.wtf("CameraDemo233", String.valueOf(newh));
        params.height = (int) ((columnWidth*newh)+(padding*3)+50);
        params.width = (int) ((columnWidth*3)+(padding*3)+150);
        gridView.setLayoutParams(params);
    }


    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}