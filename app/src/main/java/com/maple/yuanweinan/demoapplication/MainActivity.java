package com.maple.yuanweinan.demoapplication;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.maple.yuanweinan.demoapplication.db.DemoDBHelpler;
import com.maple.yuanweinan.demoapplication.db.UserInfoTable;
import com.maple.yuanweinan.demoapplication.location.DemoLocationController;
import com.maple.yuanweinan.demoapplication.pojo.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanweinan
 */
public class MainActivity extends AppCompatActivity {

    private TextView mGPSLocationDetailTextView;
    private ListView mSearchResultListView;
    private List<UserInfo> mUserInfo = new ArrayList<>();
    private BaseAdapter mUserInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGPSLocationDetailTextView = (TextView) findViewById(R.id.location_id);
        mSearchResultListView = (ListView) findViewById(R.id.listView);
        mUserInfoAdapter = new UserInfoAdapter(MainActivity.this, mUserInfo);
        mSearchResultListView.setAdapter(mUserInfoAdapter);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = null;
                List<UserInfo> userInfoList = UserInfoTable.queryAll(DemoDBHelpler.getInstance(MainActivity.this));
                mUserInfo.clear();
                mUserInfo.addAll(userInfoList);
                mUserInfoAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DemoLocationController.getInstance(this).addGPSLocationListener(new DemoLocationController.GPSLocationListener() {
            @Override
            public void onFinish(Location location) {
                String locationString = "Longitude:" + location.getLongitude() + "  Latitude:" + location.getLatitude();
                mGPSLocationDetailTextView.setText(locationString);
            }

            @Override
            public void onFail() {

            }
        });
        DemoLocationController.getInstance(this).refresh();

        DemoDBHelpler.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DemoLocationController.getInstance(this).onDestroy();
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
}
