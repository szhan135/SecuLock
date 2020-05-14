/*
Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cs179.androidapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cs179.androidapp.Injection;
import com.cs179.androidapp.LocksApp;
import com.cs179.androidapp.R;
import com.cs179.androidapp.services.AnalyticsService;

public class LockDetailActivity extends AppCompatActivity {
    /**
     * Injection of the Analytics Service
     */
    private AnalyticsService analyticsService = Injection.getAnalyticsService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_detail);

        // Set up the action bar so that it has a back button
        Toolbar detail_toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(detail_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Load the fragment
        LockDetailFragment fragment = new LockDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(LocksApp.ITEM_ID, getIntent().getStringExtra(LocksApp.ITEM_ID));
        fragment.setArguments(arguments);

        // Add the fragment to the UI
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lock_detail_container, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        analyticsService.recordEvent("LockDetailActivity", null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, LockListActivity.class);
                navigateUpTo(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
