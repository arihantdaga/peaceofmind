package com.feturtles.peaceofmind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotificationService extends NotificationListenerService {
  private String TAG = this.getClass().getSimpleName();
  private NLServiceReceiver nlservicereciver;
  List<String> blockedPackages = new ArrayList<>();

  @Override
  public void onListenerConnected() {
    super.onListenerConnected();
    Log.i(TAG, "**********  onListenerConnected");
  }
  @Override
  public void onCreate(){
    super.onCreate();
    nlservicereciver = new NLServiceReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction("com.feturtles.peaceofmind.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
    registerReceiver(nlservicereciver,filter);
    Log.i(TAG, "This is test message");

    findBlockedPackages();

  }
  @Override
  public void onDestroy(){
    super.onDestroy();
    unregisterReceiver(nlservicereciver);
  }

  @Override
  public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
    //super.onNotificationPosted(sbn, rankingMap);
    Log.i(TAG,"**********  onNotificationPosted (API 21)");
    String packageName = sbn.getPackageName();

    Log.i(TAG, "Package Name : " + packageName);
    if(blockedPackages.contains(packageName)){
      Log.w(TAG, "Notification from a blocked package is detected");
      if(Build.VERSION.SDK_INT > 20){
        Log.i(TAG, "Cancelling notification for api level Higher then 20");
        String key = sbn.getKey();
        cancelNotification(key);
      }else{
        Log.i(TAG, "Cancelling notification for api level less then 20");
        cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
      }

    }


    Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

    // Test: cancel all notifications as soon as we know about them
//    cancelAllNotifications();
  }

  @Override
  public void onNotificationPosted(StatusBarNotification sbn){
    Log.i(TAG,"**********  onNotificationPosted");
    Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
    Intent i = new  Intent("com.feturtles.peaceofmind.NOTIFICATION_LISTENER_EXAMPLE");
    i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "\n");
    sendBroadcast(i);

    // Test: cancel all notifications as soon as we know about them
    //cancelAllNotifications();

  }
  @Override
  public void onNotificationRemoved(StatusBarNotification sbn) {
    Log.i(TAG,"********** onNOtificationRemoved");
    Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
    Intent i = new  Intent("com.feturtles.peaceofmind.NOTIFICATION_LISTENER_EXAMPLE");
    i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");

    sendBroadcast(i);
  }

  class NLServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      if(intent.getStringExtra("command").equals("clearall")){
        NotificationService.this.cancelAllNotifications();
      }
      else if(intent.getStringExtra("command").equals("list")){
        Intent i1 = new  Intent("com.feturtles.peaceofmind.NOTIFICATION_LISTENER_EXAMPLE");
        i1.putExtra("notification_event","=====================");
        sendBroadcast(i1);
        int i=1;
        for (StatusBarNotification sbn : NotificationService.this.getActiveNotifications()) {
          Intent i2 = new  Intent("com.feturtles.peaceofmind.NOTIFICATION_LISTENER_EXAMPLE");
          i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
          sendBroadcast(i2);
          i++;
        }
        Intent i3 = new  Intent("com.feturtles.peaceofmind.NOTIFICATION_LISTENER_EXAMPLE");
        i3.putExtra("notification_event","===== Notification List ====");
        sendBroadcast(i3);

      }

    }
  }

  void findBlockedPackages(){
    this.blockedPackages.add("com.feturtles.theopendiaries");
    this.blockedPackages.add("com.whatsapp");
  }

  public NotificationService() {
  }
}
