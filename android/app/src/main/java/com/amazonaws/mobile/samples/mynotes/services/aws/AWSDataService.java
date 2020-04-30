package com.amazonaws.mobile.samples.mynotes.services.aws;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.amplify.generated.graphql.CreateNoteMutation;
import com.amazonaws.amplify.generated.graphql.DeleteNoteMutation;
import com.amazonaws.amplify.generated.graphql.GetNoteQuery;
import com.amazonaws.amplify.generated.graphql.ListNotesQuery;
import com.amazonaws.amplify.generated.graphql.UpdateNoteMutation;
import com.amazonaws.mobile.samples.mynotes.models.Note;
import com.amazonaws.mobile.samples.mynotes.models.PagedListConnectionResponse;
import com.amazonaws.mobile.samples.mynotes.models.ResultCallback;
import com.amazonaws.mobile.samples.mynotes.services.DataService;
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

import type.CreateNoteInput;
import type.UpdateNoteInput;
import type.DeleteNoteInput;

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
    public void loadNotes(int limit, String after, ResultCallback<PagedListConnectionResponse<Note>> callback) {
        ListNotesQuery query = ListNotesQuery.builder().limit(limit).nextToken(after).build();
        client.query(query)
                .responseFetcher(AppSyncResponseFetchers.NETWORK_FIRST)
                .enqueue(new GraphQLCall.Callback<ListNotesQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ListNotesQuery.Data> response) {
                        String nextToken = response.data().listNotes().nextToken();
                        List<ListNotesQuery.Item> rItems = response.data().listNotes().items();

                        List<Note> items = new ArrayList<>();
                        for (ListNotesQuery.Item item : rItems) {
                            Note n = new Note(item.id());
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
    public void getNote(String noteId, ResultCallback<Note> callback) {
        GetNoteQuery query = GetNoteQuery.builder().id(noteId).build();
        client.query(query)
                .responseFetcher(AppSyncResponseFetchers.NETWORK_FIRST)
                .enqueue(new GraphQLCall.Callback<GetNoteQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<GetNoteQuery.Data> response) {
                        GetNoteQuery.GetNote item = response.data().getNote();
                        final Note note = new Note(noteId);
                        note.setTitle(item != null ? (item.title().equals(" ") ? "" : item.title()) : "");
                        note.setContent(item != null ? (item.content().equals(" ") ? "" : item.content()) : "");
                        runOnUiThread(() -> callback.onResult(note));
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e(TAG, String.format(Locale.ENGLISH, "Error during GraphQL Operation: %s", e.getMessage()), e);
                    }
                });
    }

    @Override
    public void deleteNote(String noteId, ResultCallback<Boolean> callback) {
        DeleteNoteInput input = DeleteNoteInput.builder().id(noteId).build();
        DeleteNoteMutation mutation = DeleteNoteMutation.builder().input(input).build();

        client.mutate(mutation)
                .enqueue(new GraphQLCall.Callback<DeleteNoteMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<DeleteNoteMutation.Data> response) {
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
    public void createNote(String title, String content, ResultCallback<Note> callback) {
        CreateNoteInput input = CreateNoteInput.builder()
                .title(title.isEmpty() ? " " : title)
                .content(content.isEmpty() ? " " : content)
                .build();
        CreateNoteMutation mutation = CreateNoteMutation.builder().input(input).build();

        client.mutate(mutation)
                .enqueue(new GraphQLCall.Callback<CreateNoteMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<CreateNoteMutation.Data> response) {
                        if (response.hasErrors()) {
                            showErrors(response.errors());
                            runOnUiThread(() -> callback.onResult(null));
                        } else {
                            CreateNoteMutation.CreateNote item = response.data().createNote();
                            final Note returnedNote = new Note(item.id());
                            returnedNote.setTitle(item.title().equals(" ") ? "" : item.title());
                            returnedNote.setContent(item.content().equals(" ") ? "" : item.content());
                            runOnUiThread(() -> callback.onResult(returnedNote));
                        }
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e(TAG, String.format(Locale.ENGLISH, "Error during GraphQL Operation: %s", e.getMessage()), e);
                    }
                });
    }

    @Override
    public void updateNote(Note note, ResultCallback<Note> callback) {
        UpdateNoteInput input = UpdateNoteInput.builder()
                .id(note.getNoteId())
                .title(note.getTitle().isEmpty() ? " " : note.getTitle())
                .content(note.getContent().isEmpty() ? " " : note.getContent())
                .build();
        UpdateNoteMutation mutation = UpdateNoteMutation.builder().input(input).build();

        client.mutate(mutation)
                .enqueue(new GraphQLCall.Callback<UpdateNoteMutation.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<UpdateNoteMutation.Data> response) {
                        if (response.hasErrors()) {
                            showErrors(response.errors());
                            runOnUiThread(() -> callback.onResult(null));
                        } else {
                            UpdateNoteMutation.UpdateNote item = response.data().updateNote();
                            final Note returnedNote = new Note(item.id());
                            returnedNote.setTitle(item.title().equals(" ") ? "" : item.title());
                            returnedNote.setContent(item.content().equals(" ") ? "" : item.content());
                            runOnUiThread(() -> callback.onResult(returnedNote));
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