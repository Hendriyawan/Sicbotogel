package com.hdev.sicbotogel.api;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.hdev.sicbotogel.interfaces.NotificationView;
import com.hdev.sicbotogel.utils.AppPreferences;

public class NotificationPresenter {
    private NotificationView view;
    private Context context;

    public NotificationPresenter(Context context, NotificationView view) {
        this.context = context;
        this.view = view;
    }

    public void loadNotification() {
        boolean firstInstall = AppPreferences.getFisrtInstall();
        if (firstInstall) {
            AndroidNetworking.get(EndPoint.URL)
                    .setPriority(Priority.LOW)
                    .setTag("GET_NOTIFICATION")
                    .build()
                    .getAsObject(NotificationResponse.class, new ParsedRequestListener<NotificationResponse>() {
                        @Override
                        public void onResponse(NotificationResponse response) {
                            if (response.getSuccess()) {
                                view.onNotificationFirstLoaded(response.getNotification());
                            } else {
                                view.onDataEmpty();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            view.onDataEmpty();
                            Log.d("DEBUG", "onAnError : " + anError.getErrorDetail());
                        }
                    });
        }
    }
}
