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

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.cs179.androidapp.Injection;
import com.cs179.androidapp.LocksApp;
import com.cs179.androidapp.R;
import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.services.AnalyticsService;
import com.cs179.androidapp.viewmodels.LockListViewModel;

import java.util.HashMap;

public class LockListActivity extends AppCompatActivity {
    /**
     * If the device is running in two-pane mode, then this is set to true.  In two-pane mode,
     * the UI is a side-by-side, with the list on the left and the details on the right.  In one
     * pane mode, the list and details are separate pages.
     */
    private boolean twoPane = false;

    /**
     * The view model
     */
    private LockListViewModel viewModel;

    /**
     * The analytics service
     */
    private AnalyticsService analyticsService = Injection.getAnalyticsService();

    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token =  getIntent().getStringExtra("token");
        viewModel = ViewModelProviders.of(this).get(LockListViewModel.class);
        setContentView(R.layout.activity_lock_list);

        if (findViewById(R.id.lock_detail_container) != null) twoPane = true;

        // Configure the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());

        // Add an item click handler to the floating action button for adding a lock
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //pop window
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PopActivity.class));
            }
        });
        //fab.setOnClickListener((View v) -> loadLockDetailFragment("new"));

        // Create the adapter that will be used to load items into the recycler view
        final LockListAdapter adapter = new LockListAdapter((Lock item) -> loadLockDetailFragment(item.getLockId()));

        // Create the swipe-to-delete handler
        SwipeToDelete swipeHandler = new SwipeToDelete(this, (Lock item) -> viewModel.removeLock(item.getLockId()));
        ItemTouchHelper swipeToDelete = new ItemTouchHelper(swipeHandler);

        // Configure the lock list
        RecyclerView lock_list = findViewById(R.id.lock_list);
        swipeToDelete.attachToRecyclerView(lock_list);
        lock_list.setAdapter(adapter);

        // Ensure the lock list is updated whenever the repository is updated
        viewModel.getLocksList().observe(this, adapter::submitList);
    }

    @Override
    public void onResume() {
        super.onResume();
        HashMap<String,String> attributes = new HashMap<>();
        attributes.put("twoPane", twoPane ? "true" : "false");
        analyticsService.recordEvent("LockListActivity", attributes, null);
    }

    /**
     * Loads the lock details the right way, depending on if this is two-pane mode.
     *
     * @param lockId the ID of the lock to load
     */
    private void loadLockDetailFragment(String lockId) {
        if (twoPane) {
            Fragment fragment = new LockDetailFragment();
            Bundle arguments = new Bundle();
            arguments.putString(LocksApp.ITEM_ID, lockId);
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.lock_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, LockDetailActivity.class);
            intent.putExtra(LocksApp.ITEM_ID, lockId);
            intent.putExtra("token", token);
            startActivity(intent);
        }
    }
}
