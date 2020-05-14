package com.cs179.awsiotconnection.services.aws;

import android.content.Context;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;

public class AWSService {
    private AWSConfiguration awsConfiguration;
    private IdentityManager identityManager;

    public AWSService(Context context) {
        awsConfiguration = new AWSConfiguration(context);
        identityManager = new IdentityManager(context, awsConfiguration);
        IdentityManager.setDefaultIdentityManager(identityManager);
    }

    public IdentityManager getIdentityManager() {
        return identityManager;
    }

    public AWSConfiguration getConfiguration() {
        return awsConfiguration;
    }
}