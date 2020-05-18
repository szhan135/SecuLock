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
package com.cs179.androidapp.services;

import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.models.PagedListConnectionResponse;
import com.cs179.androidapp.models.ResultCallback;

/**
 * Definition of a data service.  This maps to an API definition on the cloud backend.
 * Each call should be async and run on a background thread.
 */
public interface DataService {
    /**
     * Load a single page of locks.
     *
     * @param limit the requested number of items
     * @param after the "next token" from a prior call
     * @param callback the response from the server
     */
    void loadLocks(int limit, String after, ResultCallback<PagedListConnectionResponse<Lock>> callback);

    /**
     * Load a single lock
     *
     * @param lockId the request ID
     * @param callback the response from the server
     */
    void getLock(String lockId, ResultCallback<Lock> callback);

    /**
     * Create a new lock a lock to the backing store
     *
     * @param title the title of the new lock
     * @param content the content for the new lock
     * @param callback the response from the server (null would indicate that the operation failed)
     */
    void createLock(String title, String content, ResultCallback<Lock> callback);

    /**
     * Update an existing lock in the backing store
     *
     * @param lock the new contents of the lock
     * @param callback the response from the server (null would indicate that the operation failed)
     */
    void updateLock(Lock lock, ResultCallback<Lock> callback);

    /**
     * Delete a lock from the backing store
     *
     * @param lockId the ID of the lock to be deleted
     * @param callback the response from the server (Boolean = true indicates success)
     */
    void deleteLock(String lockId, ResultCallback<Boolean> callback);

}
