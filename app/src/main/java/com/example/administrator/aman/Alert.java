package com.example.administrator.aman;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.LocationListener;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class Alert extends GcmTaskService {
    final LocationListener[] listener = new LocationListener[1];
    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612;

    private static final String TAG = "MyTaskService";

    public static final String ACTION_DONE = "GcmTaskService#ACTION_DONE";
    public static final String EXTRA_TAG = "extra_tag";
    public static final String EXTRA_RESULT = "extra_result";


    @Override
    public void onInitializeTasks() {
        // When your package is removed or updated, all of its network tasks are cleared by
        // the GcmNetworkManager. You can override this method to reschedule them in the case of
        // an updated package. This is not called when your application is first installed.
        //
        // This is called on your application's main thread.

        // TODO(developer): In a real app, this should be implemented to re-schedule important tasks.
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.d(TAG, "onRunTask: " + taskParams.getTag());

        String tag = taskParams.getTag();

        // Default result is success.
        int result = GcmNetworkManager.RESULT_SUCCESS;

        // Choose method based on the tag.

        if (Emergency.TASK_TAG_PERIODIC.equals(tag)) {
            result = doPeriodicTask();
        }

        // Create Intent to broadcast the task information.
        Intent intent = new Intent();
        intent.setAction(ACTION_DONE);
        intent.putExtra(EXTRA_TAG, tag);
        intent.putExtra(EXTRA_RESULT, result);

        // Send local broadcast, running Activities will be notified about the task.
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.sendBroadcast(intent);

        // Return one of RESULT_SUCCESS, RESULT_FAILURE, or RESULT_RESCHEDULE
        return result;
    }



    private int doPeriodicTask() {



        DatabaseReference gref = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Help");





        gref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                if(name!=null) {

               notification = new NotificationCompat.Builder(Alert.this);
                    notification.setAutoCancel(true);

                    notification.setSmallIcon(R.drawable.emer);
                    notification.setTicker("InSTanT");
                    notification.setWhen(System.currentTimeMillis());
                    notification.setContentTitle("Emergency");
                    notification.setContentText("Hey Some One Needs Your Help"+name);
                    notification.setDefaults(Notification.DEFAULT_VIBRATE);

                    Uri uri =Uri.parse("android.resource://"+Alert.this.getPackageName()+"/"+R.raw.em);
                    notification.setSound(uri);

                    Intent intent = new Intent(Alert.this, MapsActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(Alert.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setContentIntent(pendingIntent);

                    //Builds notification and issues it
                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(uniqueID, notification.build());

                    DatabaseReference gref = FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Help");





                    gref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            //else close
                }
                else {
                    return;
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return GcmNetworkManager.RESULT_SUCCESS;




    }









}