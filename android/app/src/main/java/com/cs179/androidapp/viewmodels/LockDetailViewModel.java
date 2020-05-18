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
package com.cs179.androidapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.cs179.androidapp.Injection;
import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.models.ResultCallback;
import com.cs179.androidapp.repository.LocksRepository;

public class LockDetailViewModel extends ViewModel {
    private String lockId;
    private MutableLiveData<String> mTitle;
    private MutableLiveData<String> mContent;
    private LocksRepository locksRepository;

    public LockDetailViewModel() {
        this.locksRepository = Injection.getLocksRepository();
        this.mTitle = new MutableLiveData<>();
        this.mContent = new MutableLiveData<>();
    }

    public void setLockId(final String lockId) {
        this.lockId = lockId;
        locksRepository.get(lockId, (Lock result) -> {
            if (result != null)
                mTitle.postValue(result.getTitle());
                mContent.postValue(result.getContent());
                this.lockId = result.getLockId();
        });
    }

    public LiveData<String> getTitle() {
        return mTitle;
    }

    public LiveData<String> getContent() {
        return mContent;
    }

    public synchronized void create(String title, String content, ResultCallback<Lock> callback) {
        locksRepository.create(title, content, (Lock result) -> {
            if (result != null) {
                lockId = result.getLockId();
                callback.onResult(result);
            }
        });
    }

    public synchronized void update(String title, String content) {
        Lock newLock = new Lock(lockId, title, content);
        locksRepository.update(newLock, (Lock result) -> {
            /* Do Nothing */
        });
    }
}
