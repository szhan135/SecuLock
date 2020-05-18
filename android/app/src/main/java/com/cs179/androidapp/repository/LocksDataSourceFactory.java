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
package com.cs179.androidapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.services.DataService;

/**
 * Factory for creating data sources.  When the LocksDataSource is invalidated (because
 * of reverse paging or because the list has been altered), we have to create a new
 * data source.
 */
public class LocksDataSourceFactory extends DataSource.Factory<String, Lock> {
    private DataService dataService;
    private MutableLiveData<LocksDataSource> mDataSource;
    private LiveData<LocksDataSource> currentDataSource;

    LocksDataSourceFactory(DataService dataService) {
        this.dataService = dataService;
        mDataSource = new MutableLiveData<>();
        currentDataSource = mDataSource;
    }

    public LiveData<LocksDataSource> getCurrentDataSource() {
        return currentDataSource;
    }

    @Override
    public DataSource<String, Lock> create() {
        LocksDataSource dataSource = new LocksDataSource(dataService);
        mDataSource.postValue(dataSource);
        return dataSource;
    }
}
