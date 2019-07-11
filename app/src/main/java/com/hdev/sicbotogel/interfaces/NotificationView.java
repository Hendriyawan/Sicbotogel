package com.hdev.sicbotogel.interfaces;

import com.hdev.sicbotogel.database.model.Notifications;

import java.util.List;

public interface NotificationView {

    void onNotificationFirstLoaded(List<Notifications> notifications);

    void onNotificationLoaded(List<Notifications> notifications);

    void onDataEmpty();
}
