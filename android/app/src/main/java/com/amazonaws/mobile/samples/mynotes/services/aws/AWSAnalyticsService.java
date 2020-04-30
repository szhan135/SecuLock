package com.amazonaws.mobile.samples.mynotes.services.aws;

import android.content.Context;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.samples.mynotes.services.AnalyticsService;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import java.util.Map;

public class AWSAnalyticsService implements AnalyticsService {
    private PinpointManager pinpointManager;

    public AWSAnalyticsService(Context context, AWSService awsService) {
        AWSCredentialsProvider cp = awsService.getIdentityManager().getCredentialsProvider();
        PinpointConfiguration config = new PinpointConfiguration(context, cp,  awsService.getConfiguration());
        pinpointManager = new PinpointManager(config);

        // Automatically record a startSession event
        startSession();
    }

    @Override
    public void startSession() {
        pinpointManager.getSessionClient().startSession();
        pinpointManager.getAnalyticsClient().submitEvents();
    }

    @Override
    public void stopSession() {
        pinpointManager.getSessionClient().stopSession();
        pinpointManager.getAnalyticsClient().submitEvents();
    }

    @Override
    public void recordEvent(String eventName, Map<String, String> attributes, Map<String, Double> metrics) {
        final AnalyticsEvent event = pinpointManager.getAnalyticsClient().createEvent(eventName);
        if (attributes != null) {
            for (Map.Entry<String,String> entry : attributes.entrySet()) {
                event.addAttribute(entry.getKey(), entry.getValue());
            }
        }
        if (metrics != null) {
            for (Map.Entry<String,Double> entry : metrics.entrySet()) {
                event.addMetric(entry.getKey(), entry.getValue());
            }
        }
        pinpointManager.getAnalyticsClient().recordEvent(event);
        pinpointManager.getAnalyticsClient().submitEvents();
    }
}