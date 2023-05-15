package mud.per.iw.techapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ThirdFragment extends Fragment implements
        GoogleMap.OnMarkerClickListener, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>
         {

    View view;
    Button firstButton;
    MapView mMapView;

    private GoogleMap googleMap;
             private String changeres;

             @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment


        view = inflater.inflate(R.layout.third_fragment, container, false);



// get the reference of Button
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                mMap.setOnMarkerClickListener(ThirdFragment.this::onMarkerClick);

                // For showing a move to my location button
               // googleMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(true);
                // For dropping a marker at a point on the Map
//                LatLng sydney = new LatLng(-34, 151);
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description").icon(BitmapDescriptorFactory.fromResource(R.drawable.mouse)));
//
//                // For zooming automatically to the location of the marker
//                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(3).build();
//                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                setUpClusterer();

            }




        });





        return view;
    }


             @Override
             public boolean onMarkerClick(final Marker marker) {

                 // Retrieve the data from the marker.
                 Integer clickCount = (Integer) marker.getTag();
                 Log.wtf("icw","Map clicked");
                 // Check if a click count was set, then display the click count.
                 if (clickCount != null) {
                     clickCount = clickCount + 1;
                     marker.setTag(clickCount);

                 }

                 // Return false to indicate that we have not consumed the event and that we wish
                 // for the default behavior to occur (which is for the camera to move such that the
                 // marker is centered and for the marker's info window to open, if it has one).
                 return false;
             }

             // Declare a variable for the cluster manager.
             private ClusterManager<MyItem> mClusterManager;

             private void setUpClusterer() {
                 // Position the map.
                 googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-26.0596013, 139.1578578), 3));

                 // Initialize the manager with the context and the map.
                 // (Activity extends context, so we can pass 'this' in the constructor.)
                 mClusterManager = new ClusterManager<MyItem>(getContext(),googleMap);
                 mClusterManager.setRenderer(new MarkerClusterRenderer(getContext(), googleMap, mClusterManager));
                 // Point the map's listeners at the listeners implemented by the cluster
                 // manager.
                 googleMap.setOnCameraIdleListener(mClusterManager);
                 googleMap.setOnMarkerClickListener(mClusterManager);
                 googleMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

                 googleMap.setOnInfoWindowClickListener(mClusterManager); //added
                 mClusterManager.setOnClusterItemInfoWindowClickListener(this);



                 // Add cluster items (markers) to the cluster manager.
                // getdmsg("");
                 addItems();
             }

             private void addItems() {

                 // Set some lat/lng coordinates to start with.
                 double lat = 0;
                 double lng = 0;

                 // Add ten cluster items in close proximity, for purposes of this example.
                 for (int i = 0; i < StationAdapter.statoinList.size(); i++) {
                     station album =StationAdapter.statoinList.get(i);
                     if(!album.getLat().equals("null")&&!album.getLong().equals("null")&&!album.getLat().equals("")&&!album.getLong().equals("null")){
                     lat =Double.parseDouble( album.getLat()) ;
                     lng = Double.parseDouble(album.getLong()) ;
                         MyItem offsetItem = new MyItem(lat, lng,album.getDescription(),album.getstypedes(),album.getUId());
                         mClusterManager.addItem(offsetItem);
                     }



                 }
             }

             @Override
             public void onClusterItemInfoWindowClick(MyItem myItem) {

                 //Cluster item InfoWindow clicked, set title as action
                 Log.wtf("icw1",myItem.getTitle());
                 AppCompatActivity activity = (AppCompatActivity) view.getContext();
                 Fragment myFragment = new fragmentfifth();
                 Bundle bundle=new Bundle();
                 bundle.putString("stationuid",myItem.getqr());
                 // bundle.putString("longi",album.getlongi());

                 myFragment.setArguments(bundle);
                 activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, myFragment).addToBackStack(null).commit();

                 //You may want to do different things for each InfoWindow:
                 if (myItem.getTitle().equals("some title")){

                     //do something specific to this InfoWindow....

                 }

             }


}
