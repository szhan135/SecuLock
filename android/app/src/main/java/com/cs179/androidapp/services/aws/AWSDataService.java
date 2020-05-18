package com.cs179.androidapp.services.aws;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.amplify.generated.graphql.CreateLockMutation;
import com.amazonaws.amplify.generated.graphql.DeleteLockMutation;
import com.amazonaws.amplify.generated.graphql.GetLockQuery;
import com.amazonaws.amplify.generated.graphql.ListLocksQuery;
import com.amazonaws.amplify.generated.graphql.UpdateLockMutation;
import com.cs179.androidapp.models.Lock;
import com.cs179.androidapp.models.PagedListConnectionResponse;
import com.cs179.androidapp.models.ResultCallback;
import com.cs179.androidapp.services.DataService;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.appsync.sigv4.BasicCognitoUserPoolsAuthProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;

import type.CreateLockInput;
import type.UpdateLockInput;
import type.DeleteLockInput;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class AWSDataService implements DataService {
    private static final String TAG = "AWSDataService";
    private AWSAppSyncClient client;

    public AWSDataService(Context context, AWSService awsService) {
        // Create an AppSync client from the AWSConfiguration
        AWSConfiguration config = awsService.getConfiguration();
        CognitoUserPool userPool = new CognitoUserPool(context, awsService.getConfiguration());
        client = AWSAppSyncClient.builder()
                .context(context)
                .awsConfiguration(config)
                .cognitoUserPoolsAuthProvider(new BasicCognitoUserPoolsAuthProvider(userPool))
                .build();
    }

    @Override
    public void loadLocks(int limit, String after, ResultCallback<PagedListConnectionResponse<Lock>> callback) {
        ListLocksQuery query = ListLocksQuery.builder().limit(limit).nextToken(after).build();
        client.query(query)
                .responseFetcher(AppSyncResponseFetchers.NETWORK_FIRST)
                .enqueue(new GraphQLCall.Callback<ListLocksQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ListLocksQuery.Data> response) {
                        String nextToken = response.data().listLocks().nextToken();
                        List<ListLocksQuery.Item> rItems = response.data().listLocks().items();

                        List<Lock> items = new ArrayList<>();
                        for (ListLocksQuery.Item item : rItems) {
                            Lock n = new Lock(item.id());
                            n.setTitle(item.title().equals(" ") ? "" : item.title());
                            n.setContent(item.content().equals(" ") ? "" : item.content());
                            items.add(n);
                        }
                        runOnUiThread(() -> callback.onResult(new PagedListConnectionResponse<>(items, nextToken)));
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e(TAG, String.format(Locale.ENGLISH, "Error during GraphQL Operation: %s", e.getMessage()), e);
                    }
                });
    }

    @Override
    public void getLock(String lockId, ResultCallback<Lock> callback) {
        GetLockQuery query = GetLockQuery.builder().id(lockId).build();
        client.query(query)
                .responseFetcher(AppSyncResponseFetchers.NETWORK_FIRST)
                .enqueue(new GraphQLCall.Callback<GetLockQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetLockQuery.Data> response) {
                        GetLockQuery.GetLock item = response.data().getLock();
                        final Lock lock = new Lock(lockId);
                        lock.setTitle(item != null ? (item.title().equals(" ") ? "" : item.title()) : "");
                        lock.setContent(item != null ? (item.content().equals(" ") ? "" : item.content()) : "");
                        runOnUiThread(() -> callback.onResult(lock));
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e(TAG, String.format(Locale.ENGLISH, "Error during GraphQL Operation: %s", e.getMessage()), e);
                    }
                });
    }

    @Override
    public void deleteLock(String lockId, ResultCallback<Boolean> callback) {
        DeleteLockInput input = DeleteLockInput.builder().id(lockId).build();
        DeleteLockMutation mutation = DeleteLockMutation.builder().input(input).build();

        client.mutate(mutation)
                .enqueue(new GraphQLCall.Callback<DeleteLockMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<DeleteLockMutation.Data> response) {
                        runOnUiThread(() -> callback.onResult(true));
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e(TAG, String.format(Locale.ENGLISH, "Error during GraphQL Operation: %s", e.getMessage()), e);
                        callback.onResult(false);
                    }
                });
    }

    @Override
    public void createLock(String title, String content, ResultCallback<Lock> callback) {
        CreateLockInput input = CreateLockInput.builder()
                .title(title.isEmpty() ? " " : title)
                .content(content.isEmpty() ? " " : content)
                .build();
        CreateLockMutation mutation = CreateLockMutation.builder().input(input).build();

        client.mutate(mutation)
                .enqueue(new GraphQLCall.Callback<CreateLockMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<CreateLockMutation.Data> response) {
                        if (response.hasErrors()) {
                            showErrors(response.errors());
                            runOnUiThread(() -> callback.onResult(null));
                        } else {
                            CreateLockMutation.CreateLock item = response.data().createLock();
                            final Lock returnedLock = new Lock(item.id());
                            returnedLock.setTitle(item.title().equals(" ") ? "" : item.title());
                            returnedLock.setContent(item.content().equals(" ") ? "" : item.content());
                            runOnUiThread(() -> callback.onResult(returnedLock));
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e(TAG, String.format(Locale.ENGLISH, "Error during GraphQL Operation: %s", e.getMessage()), e);
                    }
                });
    }

    @Override
    public void updateLock(Lock lock, ResultCallback<Lock> callback) {
        UpdateLockInput input = UpdateLockInput.builder()
                .id(lock.getLockId())
                .title(lock.getTitle().isEmpty() ? " " : lock.getTitle())
                .content(lock.getContent().isEmpty() ? " " : lock.getContent())
                .build();
        UpdateLockMutation mutation = UpdateLockMutation.builder().input(input).build();

        client.mutate(mutation)
                .enqueue(new GraphQLCall.Callback<UpdateLockMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<UpdateLockMutation.Data> response) {
                        if (response.hasErrors()) {
                            showErrors(response.errors());
                            runOnUiThread(() -> callback.onResult(null));
                        } else {
                            UpdateLockMutation.UpdateLock item = response.data().updateLock();
                            final Lock returnedLock = new Lock(item.id());
                            returnedLock.setTitle(item.title().equals(" ") ? "" : item.title());
                            returnedLock.setContent(item.content().equals(" ") ? "" : item.content());
                            runOnUiThread(() -> callback.onResult(returnedLock));
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e(TAG, String.format(Locale.ENGLISH, "Error during GraphQL Operation: %s", e.getMessage()), e);
                    }
                });
    }

    private void showErrors(List<Error> errors) {
        Log.e(TAG, "Response has errors:");
        for (Error e : errors) {
            Log.e(TAG, String.format(Locale.ENGLISH, "Error: %s", e.message()));
        }
        Log.e(TAG, "End of Response errors");
    }
}