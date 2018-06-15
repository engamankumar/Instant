package com.example.administrator.aman;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Administrator on 3/10/2017.
 */

public class Emergency extends Fragment {
    final LocationListener[] listener = new LocationListener[1];
    GpsTracker gps;
    Button emergen,remove;
    EditText sedit;
    at.markushi.ui.CircleButton emergenc;
    private GcmNetworkManager mGcmNetworkManager;


    private BroadcastReceiver mReceiver;

    private static final String TAG = "MainActivity";
    private static final int RC_PLAY_SERVICES = 123;


    public static final String TASK_TAG_PERIODIC = "periodic_task";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_emergency, container, false);



    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mGcmNetworkManager = GcmNetworkManager.getInstance(getActivity());
        emergenc = (at.markushi.ui.CircleButton)getView().findViewById(R.id.emergency);
        remove=(Button)getView().findViewById(R.id.search);
        sedit=(EditText) getView().findViewById(R.id.edits);

        LocationManager L = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);

        if (!L.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent I = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(I);
        }
        // [END get_gcm_network_manager]


        // BroadcastReceiver to get information from MyTaskService about task completion.
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Alert.ACTION_DONE)) {
                    String tag = intent.getStringExtra(Alert.EXTRA_TAG);
                    int result = intent.getIntExtra(Alert.EXTRA_RESULT, -1);

                    String msg = String.format("DONE: %s (%d)", tag, result);

                }
                else {
                    return;
                }
            }
        };

        Log.d(TAG, "startPeriodicTask");

        // [START start_periodic_task]
        PeriodicTask task = new PeriodicTask.Builder()
                .setService(Alert.class)
                .setTag(TASK_TAG_PERIODIC)
                .setPeriod(60L)
                .setPersisted(true)

                .build();

        mGcmNetworkManager.schedule(task);


        // Check that Google Play Services is available, since we need it to use GcmNetworkManager
        // but the API does not use GoogleApiClient, which would normally perform the check
        // automatically.
        checkPlayServicesAvailable();
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s=sedit.getText().toString();
                final DatabaseReference gref = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                gref.child("Friend").setValue(s);
                Intent map= new Intent(getActivity(),MapsActivity.class);

                startActivity(map);


            }
        });
        emergenc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                final DatabaseReference gref = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userId");





                                gref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String usame = dataSnapshot.getValue(String.class);


                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference ref = database.getReference("Location");


                                        final GeoFire geoFire = new GeoFire(ref);
                                        gps = new GpsTracker(getActivity());

                                        // check if GPS enabled



                                        listener[0] = new LocationListener() {
                                            @Override
                                            public void onLocationChanged(android.location.Location location) {


                                                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(location.getLatitude(),location.getLongitude()), new GeoFire.CompletionListener() {
                                                    @Override
                                                    public void onComplete(String key, DatabaseError error) {
                                                        if (error != null) {
                                                           return;
                                                        } else {
return;
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onStatusChanged(String s, int i, Bundle bundle) {
                                            }

                                            @Override
                                            public void onProviderEnabled(String s) {
                                            }

                                            @Override
                                            public void onProviderDisabled(String s) {
                                            }


                                        };

                                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

                                        //noinspection MissingPermission
                                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10, 1, listener[0]);




                                        geoFire.getLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new LocationCallback() {
                                            @Override
                                            public void onLocationResult(String key, GeoLocation location) {
                                                if (location != null) {
                                                    double la=location.latitude;
                                                    double lo=location.longitude;


                                                    query(la,lo);


                                                } else {
                                                    return;
                                                }
                                            }
                                            private void   fam(final String n){
                                                int i;

                                                for(i=1;i<=5;i++) {
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference uref = database.getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("person"+i);
                                                    uref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String name = dataSnapshot.getValue(String.class);
                                                            if(name.length()>=5){

                                                                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                                                DatabaseReference pref = database1.getReference("username");

                                                                DatabaseReference usersRef1 = pref.child(name).child("UserId");
                                                                usersRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                                        String key1 = dataSnapshot.getValue(String.class);
                                                                        if(key1!=null){
                                                                            FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                                                                            DatabaseReference pref2 = database2.getReference("user");

                                                                            DatabaseReference usersRef = pref2.child(key1);
                                                                            usersRef.child("Help").setValue(n);

                                                                        }
                                                                        else
                                                                        {
                                                                            return;
                                                                        }
                                                                    }


                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });
                                                            }
                                                            else
                                                            {
                                                                return;
                                                            }

                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            private void query(double la, double lo) {
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Location");
                                                GeoFire geoFire = new GeoFire(ref);
                                                GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(la, lo), 20.0);
                                                geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                                                    @Override
                                                    public void onKeyEntered(final String key, GeoLocation location) {

                                                        final FirebaseDatabase database = FirebaseDatabase.getInstance();;
                                                        DatabaseReference uref = database.getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("mobile");
                                                        uref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                String name = dataSnapshot.getValue(String.class);
                                                                fam(name+usame);
                                                                DatabaseReference pref = database.getReference("user");

                                                                DatabaseReference usersRef = pref.child(key);

                                                                usersRef.child("Help").setValue(name+usame);}




                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });



                                                    }

                                                    @Override
                                                    public void onKeyExited(String key) {


                                                        ;
                                                    }

                                                    @Override
                                                    public void onKeyMoved(String key, GeoLocation location) {

                                                    }

                                                    @Override
                                                    public void onGeoQueryReady() {


                                                    }

                                                    @Override
                                                    public void onGeoQueryError(DatabaseError error) {
                                                    }
                                                });
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

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Location");

        final GeoFire geoFire = new GeoFire(ref);


        listener[0] = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {


                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                        } else {

                        }
                    }
                });
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000*60, 1, listener[0]);


        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Emergency");

    }
    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Alert.ACTION_DONE);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(mReceiver, filter);
    }







    public void startPeriodicTask() {

        // [END start_periodic_task]
    }

    public void stopPeriodicTask() {
        Log.d(TAG, "stopPeriodicTask");

        // [START stop_periodic_task]
        mGcmNetworkManager.cancelTask(TASK_TAG_PERIODIC, Alert.class);
        // [END stop_per
    }

    private void checkPlayServicesAvailable() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int resultCode = availability.isGooglePlayServicesAvailable(getActivity());

        if (resultCode != ConnectionResult.SUCCESS) {
            if (availability.isUserResolvableError(resultCode)) {
                // Show dialog to resolve the error.
                availability.getErrorDialog(getActivity(), resultCode, RC_PLAY_SERVICES).show();
            } else {
                // Unresolvable error
                Toast.makeText(getActivity(), "Google Play Services error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
