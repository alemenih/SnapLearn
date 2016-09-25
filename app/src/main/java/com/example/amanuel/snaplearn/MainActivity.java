package com.example.amanuel.snaplearn;

import com.google.tabmanager.TabManager;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.app.Fragment;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener{

    TabHost tabHost;
    TabManager tabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabManager = new TabManager(this, tabHost, R.id.realtabcontent);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Teacher");
        tabSpec.setIndicator("Teacher");
        tabManager.addTab(tabSpec, AddWordActivity.class, null);

        tabSpec = tabHost.newTabSpec("Learner");
        tabSpec.setIndicator("Learner");
        tabManager.addTab(tabSpec, WordCategoryFragment.class, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onTabChanged(String tabId){

    }
}
