package com.example.administrator.aman;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(6.0f);
        DatabaseReference greff = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Friend");
        greff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String un = dataSnapshot.getValue(String.class);
                if(un!=null) {
                    DatabaseReference ukey = FirebaseDatabase.getInstance().getReference("username").child(un).child("UserId");
                    ukey.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String key = dataSnapshot.getValue(String.class);
                            if(key!=null){
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference("Location");

                                GeoFire geoFire = new GeoFire(ref);


                                geoFire.getLocation(key, new LocationCallback() {
                                    @Override
                                    public void onLocationResult(String key, GeoLocation location) {
                                        double la = location.latitude;

                                        double lo = location.longitude;
                                        LatLng friendLocatio = new LatLng(la, lo);

                                        String l=Double.toString(la);
                                        String lt=Double.toString(lo);

                                        Toast.makeText(MapsActivity.this, l+"friend"+lt, Toast.LENGTH_LONG).show();
                                        mMap.addMarker(new MarkerOptions().position(friendLocatio).title("friend"));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(friendLocatio));
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference ref = database.getReference("Location");

                                        GeoFire geoFire = new GeoFire(ref);


                                        geoFire.getLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new LocationCallback() {
                                            @Override
                                            public void onLocationResult(String key, GeoLocation location) {
                                                double la=location.latitude;

                                                double lo=location.longitude;
                                                LatLng  friendLocatio = new LatLng(la, lo);


                                                mMap.addMarker(new MarkerOptions().position(friendLocatio).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                                mMap.moveCamera(CameraUpdateFactory.newLatLng(friendLocatio));
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
//if2
                            }
                            else {
                                Toast.makeText(MapsActivity.this, "There is No Such User Register " , Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //if closing
                }
                else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Location");

                    GeoFire geoFire = new GeoFire(ref);


                    geoFire.getLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new LocationCallback() {
                        @Override
                        public void onLocationResult(String key, GeoLocation location) {
                            double la=location.latitude;

                            double lo=location.longitude;
                            LatLng  friendLocatio = new LatLng(la, lo);


                            mMap.addMarker(new MarkerOptions().position(friendLocatio).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(friendLocatio));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//help
            DatabaseReference gref = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Help");
        gref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String un = dataSnapshot.getValue(String.class);
                if(un!=null) {
                DatabaseReference ukey = FirebaseDatabase.getInstance().getReference("username").child(un).child("UserId");
                ukey.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.getValue(String.class);
                        if(key!=null){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("Location");

                        GeoFire geoFire = new GeoFire(ref);


                        geoFire.getLocation(key, new LocationCallback() {
                            @Override
                            public void onLocationResult(String key, GeoLocation location) {
                                double la = location.latitude;

                                double lo = location.longitude;
                                LatLng friendLocatio = new LatLng(la, lo);

                                String l=Double.toString(la);
                                String lt=Double.toString(lo);

                                Toast.makeText(MapsActivity.this, l+"Help"+lt, Toast.LENGTH_LONG).show();
                              mMap.addMarker(new MarkerOptions().position(friendLocatio).title("Help"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(friendLocatio));
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference ref = database.getReference("Location");

                                GeoFire geoFire = new GeoFire(ref);


                                geoFire.getLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new LocationCallback() {
                                    @Override
                                    public void onLocationResult(String key, GeoLocation location) {
                                        double la=location.latitude;

                                        double lo=location.longitude;
                                        LatLng  friendLocatio = new LatLng(la, lo);


                                        mMap.addMarker(new MarkerOptions().position(friendLocatio).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(friendLocatio));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
//if2
                    }
                    else {
                            Toast.makeText(MapsActivity.this, "There is No Such User Register " , Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
             //if closing
                }
                else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference("Location");

                    GeoFire geoFire = new GeoFire(ref);


                    geoFire.getLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new LocationCallback() {
                        @Override
                        public void onLocationResult(String key, GeoLocation location) {
                            double la=location.latitude;

                            double lo=location.longitude;
                            LatLng  friendLocatio = new LatLng(la, lo);


                            mMap.addMarker(new MarkerOptions().position(friendLocatio).title("Your location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(friendLocatio));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}



