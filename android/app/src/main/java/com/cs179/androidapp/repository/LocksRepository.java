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
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.models.ResultCallback;
import com.cs179.androidapp.services.DataService;

public class LocksRepository {
    private LiveData<PagedList<Lock>> pagedList;
    private LiveData<LocksDataSource> dataSource;

    public LocksRepository(DataService dataService) {
        LocksDataSourceFactory factory = new LocksDataSourceFactory(dataService);
        dataSource = factory.getCurrentDataSource();
        pagedList = new LivePagedListBuilder<>(factory, 20).build();
    }

    /**
     * An observable lifecycle-aware version of the paged list of locks.  This is used
     * to render a RecyclerView of all the locks.
     */
    public LiveData<PagedList<Lock>> getPagedList() {
        return pagedList;
    }

    /**
     * API operation to create an item in the data store
     */
    public void create(String title, String content, ResultCallback<Lock> callback) {
        dataSource.getValue().createItem(title, content, callback);
    }

    /**
     * API operation to update an item in the data store
     */
    public void update(Lock lock, ResultCallback<Lock> callback) {
        dataSource.getValue().updateItem(lock, callback);
    }

    /**
     * API operation to delete an item from the data store
     */
    public void delete(String lockId, ResultCallback<Boolean> callback) {
        dataSource.getValue().deleteItem(lockId, callback);
    }

    /**
     * API operation to get an item from the data store
     */
    public void get(String lockId, ResultCallback<Lock> callback) {
        dataSource.getValue().getItem(lockId, callback);
    }
}
