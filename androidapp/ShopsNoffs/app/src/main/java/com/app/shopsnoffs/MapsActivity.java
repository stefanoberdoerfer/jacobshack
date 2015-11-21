package com.app.shopsnoffs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.shopsnoffs.model.Json;
import com.app.shopsnoffs.model.ShopsNoffsData;
import com.app.shopsnoffs.model.ShopsNoffsDatum;
import com.app.shopsnoffs.model.ShopsNoffsShop;
import com.app.shopsnoffs.model.ShopsNoffsShopData;
import com.app.shopsnoffs.network.CustomRequestListener;
import com.app.shopsnoffs.network.Request;
import com.app.shopsnoffs.network.RequestType;
import com.app.shopsnoffs.network.Successhandler;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by stefan on 17.10.15.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener, Successhandler {

    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private int radius = 1;
    private LatLng position;
    //private LatLng position = new LatLng(53.091347d,8.780611d); TODO
    private String sQuery = null;

    private MapDrawer mDrawer;
    private SeekBar radiusSeekbar;
    private TextView tv_radius;
    private ProgressDialog pdialog;

    private View slideview;
    private ImageView slideimg;
    private TextView slidetext;
    private Button slidebtn;
    private Animation btmup,btmout;
    private List<ShopsNoffsShop> shops;
    private TextView slidetextproduct;

    private SpiceManager spiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            sQuery = extras.getString("query",null);
        }

        tv_radius = (TextView) findViewById(R.id.tv_radius);
        radiusSeekbar = (SeekBar) findViewById(R.id.mapseekbar);
        radiusSeekbar.setActivated(false);

        slideview = findViewById(R.id.slideView);
        slideimg = (ImageView) findViewById(R.id.imgslide);
        slidetext = (TextView) findViewById(R.id.textslide);
        slidebtn = (Button) findViewById(R.id.btnslide);
        slidetextproduct = (TextView) findViewById(R.id.textslideproduct);

        pdialog = new ProgressDialog(this);
        pdialog.setIndeterminate(true);
        pdialog.show();

        radiusSeekbar.setProgress(radius);


        btmup = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        btmout = AnimationUtils.loadAnimation(this,R.anim.bottom_out);

        radiusSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress;
                if (progress == 0) {
                    radius = 1;
                }
                tv_radius.setText("Radius: " + (int)(radius * 1.60934) + "km");
                mDrawer.drawCircle(position, radius);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                requestOffers(position.latitude, position.longitude, radius);
            }
        });

        mMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);


        mMapFragment.getMapAsync(this);

    }

    private void requestOffers(double lati, double longi, int distance){
        String query = "?lati=" + String.valueOf((float)lati) + "&longi=" + String.valueOf((float)longi) + "&dist=" + String.valueOf(distance) + ((sQuery != null) ? ("&query=" + sQuery) : "");
        Log.i("URLSTRING",query);
        Request offerrequest;
        if(sQuery != null){
            if(sQuery.startsWith(".")){
                offerrequest = new Request(RequestType.getOffersByCategory, query.substring(0,query.length()-sQuery.length())+sQuery.substring(1));
            }else {
                offerrequest = new Request(RequestType.getOffersByName, query);
            }
        }else {
            offerrequest = new Request(RequestType.getOffersByLoc, query);
        }
        spiceManager.execute(offerrequest, new CustomRequestListener(this));
    }

    @Override
    public void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        radiusSeekbar.setActivated(true);
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                slideview.startAnimation(btmup);
                slideview.setVisibility(View.VISIBLE);
                for(ShopsNoffsShop s : shops){
                    if(s.getStoreName().equals(marker.getTitle())){
                        Glide.with(MapsActivity.this).load(s.getUrl()).into(slideimg);
                        final List<Double> loc = s.getLocation();
                        slidebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e("GEO LOCATION","geo:"+loc.get(0)+","+loc.get(1)+"?z=9");
                                Uri gmmIntentUri = Uri.parse("google.navigation:q="+loc.get(0)+","+loc.get(1));
                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                mapIntent.setPackage("com.google.android.apps.maps");
                                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(mapIntent);
                                }
                            }
                        });
                        if(s.getItemName()!=null) {
                            slidetextproduct.setText(s.getItemName());
                        }else{
                            slidetextproduct.setVisibility(View.GONE);
                        }
                    }
                }
                slidetext.setText(marker.getTitle());

                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(slideview.getVisibility() == View.VISIBLE) {
                    slideview.startAnimation(btmout);
                    slideview.setVisibility(View.GONE);
                }
            }
        });

        mDrawer = new MapDrawer(googleMap);


        // Getting LocationManager object from System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Enabling MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        /*
        lat = address.getLatitude();
        lng = address.getLongitude();

        position = new LatLng(lat, lng);
        mDrawer.drawCircle(position, radius);
        mMap.addMarker(new MarkerOptions()
                .position(position));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 8));
        postalcodeX = postalcode;
        b_mapsubmit.setIcon(R.drawable.ic_check_solo_white);
        focuslost = true;
        positionIsSearchResult = true;
        */

        /*  //Es ist nicht möglich von lat/long auf Postalcode zu schließen. Code könnte jedoch noch nützlich sein
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                position = latLng;
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 8);
                mMap.animateCamera(cameraUpdate);
                mDrawer.drawCircle(position, radius);
                mMap.addMarker(new MarkerOptions()
                        .position(position));
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        if(slideview.getVisibility() == View.VISIBLE){
            slideview.startAnimation(btmout);
            slideview.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMyLocationChange(Location location) {


            if(position == null){
                final double lat = location.getLatitude();
                final double longi = location.getLongitude();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestOffers(lat, longi, radius);
                    }
                },1500);
            }

            position = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(position, 14);
            mMap.animateCamera(cameraUpdate);
            mDrawer.drawCircle(position, radius);
            mMap.setOnMyLocationChangeListener(null);

    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onSuccess(Json json) {
        Log.e("NetworkLocationReq", "SUCCESS");
        pdialog.dismiss();
        if(json instanceof ShopsNoffsShopData){
            ShopsNoffsShopData data = (ShopsNoffsShopData) json;
            shops = data;
            int i = 0;
            for(ShopsNoffsShop sd : data){
                if(i < radius*20) {
                    if(sd.getItemName() != null) {
                        if(sd.getDiscount()>9){
                            mMap.addMarker(new MarkerOptions()
                                    .title(sd.getStoreName())
                                    .snippet(sd.getDiscount() + "% OFF")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.rsz_hot_icon))
                                    .position(new LatLng(sd.getLocation().get(0), sd.getLocation().get(1))));
                        }else {
                            mMap.addMarker(new MarkerOptions()
                                    .title(sd.getStoreName())
                                    .snippet(sd.getItemName() + " is available")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                    .position(new LatLng(sd.getLocation().get(0), sd.getLocation().get(1))));
                        }
                    }else{
                        mMap.addMarker(new MarkerOptions()
                                .title(sd.getStoreName())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                .position(new LatLng(sd.getLocation().get(0), sd.getLocation().get(1))));
                    }
                    Log.i("LOCATIONS_NEARBY", sd.getStoreName() + "," + sd.getLocation().toString());
                    i++;
                }
            }
        }
    }

}
