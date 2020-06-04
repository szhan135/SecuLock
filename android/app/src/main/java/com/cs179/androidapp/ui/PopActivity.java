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

import android.app.Activity;
import android.app.FragmentManager;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.cs179.androidapp.Injection;
import com.cs179.androidapp.LocksApp;
import com.cs179.androidapp.R;
import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.services.AnalyticsService;
import com.cs179.androidapp.viewmodels.LockListViewModel;

import java.util.HashMap;

public class PopActivity extends Activity {
    private boolean twoPane = false;

    /**
     * The view model
     */
    //private LockListViewModel viewModel = ViewModelProviders.of(com.cs179.androidapp.ui.LockListActivity).get(LockListViewModel.class);

    private AnalyticsService analyticsService = Injection.getAnalyticsService();

    String token;
    private FragmentManager supportFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token =  getIntent().getStringExtra("token");
        setContentView(R.layout.activity_pop);
        Button button2;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.7));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
        button2 = (Button) findViewById(R.id.button2);
        //button2.setOnClickListener((View v) -> loadLockDetailFragment("new"));
        //final LockListAdapter adapter = new LockListAdapter((Lock item) -> loadLockDetailFragment(item.getLockId()));

        // Create the swipe-to-delete handler
        //LockListViewModel viewModel;
        //SwipeToDelete swipeHandler = new SwipeToDelete(this, (Lock item) -> viewModel.removeLock(item.getLockId()));
        //ItemTouchHelper swipeToDelete = new ItemTouchHelper(swipeHandler);

        // Configure the lock list
        //RecyclerView lock_list = findViewById(R.id.lock_list);
        //swipeToDelete.attachToRecyclerView(lock_list);
        //lock_list.setAdapter(adapter);

        // Ensure the lock list is updated whenever the repository is updated
        //viewModel.getLocksList().observe((LifecycleOwner) this, adapter::submitList);
    }
    /*private void loadLockDetailFragment(String lockId) {
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
    }*/
    public void onResume() {
        super.onResume();
        HashMap<String,String> attributes = new HashMap<>();
        attributes.put("twoPane", twoPane ? "true" : "false");
        analyticsService.recordEvent("LockListActivity", attributes, null);
    }



    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }

    public void setSupportFragmentManager(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
        
    }
}
