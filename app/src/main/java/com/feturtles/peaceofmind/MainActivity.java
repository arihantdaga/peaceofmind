package com.feturtles.peaceofmind;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
  private NotificationReceiver nReceiver;
  private TextView txtView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show();
      }
    });


    txtView = (TextView) findViewById(R.id.txtView);
    nReceiver = new NotificationReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction("com.feturtles.peaceofmind.NOTIFICATION_LISTENER_EXAMPLE");
    registerReceiver(nReceiver, filter);

  }
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(nReceiver);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
  class NotificationReceiver extends  BroadcastReceiver{
      @Override
      public  void onReceive(Context contenxt, Intent intent){
          String temp = intent.getStringExtra("notification_event") + "\n" + txtView.getText();

      }
  }
}
