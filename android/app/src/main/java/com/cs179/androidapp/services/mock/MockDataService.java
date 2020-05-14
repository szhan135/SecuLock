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
package com.cs179.androidapp.services.mock;

import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.models.PagedListConnectionResponse;
import com.cs179.androidapp.models.ResultCallback;
import com.cs179.androidapp.services.DataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A mock data store.  This will create 30 locks so you can see the scrolling action, but
 * otherwise acts as a data service.  This should be easily rewritten to use an actual cloud API
 */
public class MockDataService implements DataService {
    private ArrayList<Lock> items;

    public MockDataService() {
        items = new ArrayList<>();
        for (int i = 0 ; i < 30 ; i++) {
            Lock item = new Lock();
            item.setTitle(String.format(Locale.US, "Lock %d", i));
            item.setContent(String.format(Locale.US, "Content for lock %d", i));
            items.add(item);
        }
    }

    /**
     * Simulate an API call to a network service that returns paged data.
     *
     * @param limit the requested number of items
     * @param after the "next token" from a prior call
     * @param callback the response from the server
     */
    @Override
    public void loadLocks(int limit, String after, ResultCallback<PagedListConnectionResponse<Lock>> callback) {
        if (limit < 1 || limit > 100) throw new IllegalArgumentException("Limit must be between 1 and 100");

        int firstItem = 0;
        if (after != null) {
            firstItem = indexOfFirst(after);
            if (firstItem < 0) {
                callback.onResult(new PagedListConnectionResponse<>(Collections.<Lock>emptyList(), null));
                return;
            }
            firstItem++;
        }
        if (firstItem > items.size() - 1) {
            callback.onResult(new PagedListConnectionResponse<>(Collections.<Lock>emptyList(), null));
            return;
        }
        int nItems = Math.min(limit, items.size() - firstItem);
        if (nItems == 0) {
            callback.onResult(new PagedListConnectionResponse<>(Collections.<Lock>emptyList(), null));
            return;
        }

        List<Lock> sublist = new ArrayList<>(items.subList(firstItem, firstItem + nItems));
        String nextToken = (firstItem + nItems - 1 == items.size()) ? null : sublist.get(sublist.size() - 1).getLockId();
        callback.onResult(new PagedListConnectionResponse<>(sublist, nextToken));
    }

    /**
     * Load a single lock from the current list of locks
     *
     * @param lockId the request ID
     * @param callback the response from the server
     */
    @Override
    public void getLock(String lockId, ResultCallback<Lock> callback) {
        if (lockId == null || lockId.isEmpty()) throw new IllegalArgumentException();

        int idx = indexOfFirst(lockId);
        callback.onResult(idx >= 0 ? items.get(idx) : null);
    }

    /**
     * Create a new lock a lock to the backing store
     *
     * @param title the title of the new lock
     * @param content the content for the new lock
     * @param callback the response from the server (null would indicate that the operation failed)
     */
    @Override
    public void createLock(String title, String content, ResultCallback<Lock> callback) {
        Lock lock = new Lock();
        lock.setTitle(title);
        lock.setContent(content);
        items.add(lock);
        callback.onResult(lock);
    }

    /**
     * Update an existing lock in the backing store
     *
     * @param lock the new contents of the lock
     * @param callback the response from the server (null would indicate that the operation failed)
     */
    @Override
    public void updateLock(Lock lock, ResultCallback<Lock> callback) {
        int idx = indexOfFirst(lock.getLockId());
        if (idx >= 0) {
            items.set(idx, lock);
            callback.onResult(lock);
        } else {
            callback.onResult(null);
        }
    }

    /**
     * Delete a lock from the backing store
     *
     * @param lockId the ID of the lock to be deleted
     * @param callback the response from the server (Boolean = true indicates success)
     */
    @Override
    public void deleteLock(String lockId, ResultCallback<Boolean> callback) {
        if (lockId == null || lockId.isEmpty()) throw new IllegalArgumentException();

        int idx = indexOfFirst(lockId);
        if (idx >= 0) items.remove(idx);
        callback.onResult(idx >= 0);
    }

    /**
     * Returns the index of the first lock that matches
     * @param lockId the lock to match
     * @return the index of the lock, or -1 if not found
     */
    private int indexOfFirst(String lockId) {
        if (items.isEmpty()) throw new IndexOutOfBoundsException();
        for (int i = 0 ; i < items.size() ; i++) {
            if (items.get(i).getLockId().equals(lockId))
                return i;
        }
        return -1;
    }
}
