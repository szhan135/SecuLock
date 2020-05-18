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

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs179.androidapp.R;
import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.models.OnClickCallback;

public class LockListAdapter extends PagedListAdapter<Lock, LockListViewHolder> {
    private static DiffUtil.ItemCallback<Lock> DIFF_CALLBACK = new DiffUtil.ItemCallback<Lock>() {
        @Override
        public boolean areItemsTheSame(Lock oldItem, Lock newItem) {
            return oldItem.getLockId().equals(newItem.getLockId());
        }

        @Override
        public boolean areContentsTheSame(Lock oldItem, Lock newItem) {
            return oldItem.getLockId().equals(newItem.getLockId()) &&
                    oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getContent().equals(newItem.getContent());
        }
    };

    private OnClickCallback callback;

    public LockListAdapter(OnClickCallback callback) {
        super(DIFF_CALLBACK);
        this.callback = callback;
    }

    @NonNull
    @Override
    public LockListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lock_list_content, parent, false);
        return new LockListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final LockListViewHolder holder, int position) {
        holder.setLock(getItem(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(holder.getLock());
            }
        });
    }
}
