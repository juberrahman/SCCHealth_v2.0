package nsf.esarplab.scchealth;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by mrahman8 on 7/25/2017.
 */

public class Notification_receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){

        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_intent=new Intent(context,LoginActivity.class);

        PendingIntent pendingIntent=PendingIntent.getActivity(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT) ;

        NotificationCompat.Builder builder=(android.support.v7.app.NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.flogo)
                .setContentTitle("Health Checkup Reminder")
                .setContentText("Hey! It's time to do the test")
                .setAutoCancel(true);


        notificationManager.notify(100, builder.build());
    }


}
