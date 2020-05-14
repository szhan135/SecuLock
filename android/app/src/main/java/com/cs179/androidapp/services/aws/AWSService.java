package com.cs179.androidapp.services.aws;

import android.content.Context;
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;

public class AWSService {
    private AWSConfiguration awsConfiguration;
    private IdentityManager identityManager;

    public AWSService(Context context) {
        awsConfiguration = new AWSConfiguration(context);
        identityManager = new IdentityManager(context, awsConfiguration);
        identityManager.addSignInProvider(CognitoUserPoolsSignInProvider.class);
        IdentityManager.setDefaultIdentityManager(identityManager);
    }

    public IdentityManager getIdentityManager() {
        return identityManager;
    }

    public AWSConfiguration getConfiguration() {
        return awsConfiguration;
    }
}