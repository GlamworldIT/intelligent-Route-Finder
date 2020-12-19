package com.example.shortestpath;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    //google map object
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;

    //polyline object
    private List<Polyline> polylines = null;

    private int count = 0;
    private double pointerLat,pointerLong;

    ArrayList<LatLng> arrayList = new ArrayList<>();
    LatLng depot = new LatLng(23.767941, 90.403939);
    LatLng outletOne = new LatLng(23.8639063, 90.3970899);
    LatLng outletTwo = new LatLng(23.8139908, 90.3237293);
    LatLng outletThree = new LatLng(23.8818046, 90.3885748);
    LatLng outletFour = new LatLng(23.8457796, 90.417862);
    LatLng outletFive = new LatLng(23.8339741, 90.4153739);
    LatLng outletSix = new LatLng(23.764741, 90.429108);
    LatLng outletSeven = new LatLng(23.764608, 90.42914);
    LatLng outletEight = new LatLng(23.7290047, 90.4295342);
    LatLng outletNine= new LatLng(23.7611984, 90.4293065);
    LatLng outletTen= new LatLng(23.8098247, 90.3864708);
    ArrayList<String> title = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //request location permission.
        requestPermision();

        //init google map fragment to show map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        arrayList.add(depot);
        arrayList.add(outletOne);
        arrayList.add(outletTwo);
        arrayList.add(outletThree);
        arrayList.add(outletFour);
        arrayList.add(outletFive);
        arrayList.add(outletSix);
        arrayList.add(outletSeven);
        arrayList.add(outletEight);
        arrayList.add(outletNine);
        arrayList.add(outletTen);

        title.add("Depot");
        title.add("Shwapno Outlet 1");
        title.add("Shwapno Outlet 2");
        title.add("Shwapno Outlet 3");
        title.add("Shwapno Outlet 4");
        title.add("Shwapno Outlet 5");
        title.add("Shwapno Outlet 6");
        title.add("Shwapno Outlet 7");
        title.add("Shwapno Outlet 8");
        title.add("Shwapno Outlet 9");
        title.add("Shwapno Outlet 10");


    }

    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    //to get user location
    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},101);

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                myLocation=location;
                LatLng ltlng=new LatLng(location.getLatitude(),location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        ltlng, 10f);
                mMap.animateCamera(cameraUpdate);

            }
        });

        //get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                count +=1;
                if (count<=10){
                    end=latLng;
                    //mMap.clear();
                    start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                    //start route finding
                    Findroutes(start,end);
                }
                else {
                    Toast.makeText(MainActivity.this, "Ten location are already selected", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //getMyLocation();
        for (int i=0; i<arrayList.size();i++){
            mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title(String.valueOf(title.get(i))));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    arrayList.get(i), 12f);
            mMap.moveCamera(cameraUpdate);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

    }




    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End) {
        if(Start==null || End==null) {
            Toast.makeText(MainActivity.this,"Unable to get location",Toast.LENGTH_LONG).show();
        } else {
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyCqvreu0pBTYnznSOCaRdozUMi95ydvPLQ")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    //Routing call back functions.
    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar= Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
//    Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(MainActivity.this,"Finding Shortest Route...",Toast.LENGTH_SHORT).show();
    }

    //If Route finding success..
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if(polylines!=null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng=null;
        LatLng polylineEndLatLng=null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i <route.size(); i++) {

            if(i==shortestRouteIndex)
            {
                polyOptions.color(getResources().getColor(R.color.colorPrimaryDark));
                polyOptions.width(9);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k-1);
                polylines.remove(polyline);
                polylines.add(polyline);

            } else {

            }


        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);
        mMap.setTrafficEnabled(true);


        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);

        double end_lat, end_long;
        float result[] = new float[10];

        switch (count){
            case 1:
                endMarker.title("Destination One");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                mMap.addMarker(endMarker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.one));
                break;
            case 2:
                endMarker.title("Destination Two");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                mMap.addMarker(endMarker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.two));
                break;
            case 3:
                endMarker.title("Destination Three");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                mMap.addMarker(endMarker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.three));
                break;
            case 4:
                endMarker.title("Destination Four");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                mMap.addMarker(endMarker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.four));
                break;
            case 5:
                endMarker.title("Destination Five");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                mMap.addMarker(endMarker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.five));
                break;
            case 6:
                endMarker.title("Destination Six");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                mMap.addMarker(endMarker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.six));
                break;
            case 7:
                endMarker.title("Destination Seven");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                mMap.addMarker(endMarker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.seven));
                break;
            case 8:
                endMarker.title("Destination Eight");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                mMap.addMarker(endMarker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.eight));
                break;
            case 9:
                endMarker.title("Destination Nine");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                break;
            case 10:
                endMarker.title("Destination Ten");
                end_lat= endMarker.getPosition().latitude;
                end_long= endMarker.getPosition().longitude;
                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),end_lat,end_long,result);
                endMarker.snippet("Distance:"+(result[0]/1000)+"km");
                mMap.addMarker(endMarker).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ten));
                break;
        }
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start,end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(start,end);

    }
}