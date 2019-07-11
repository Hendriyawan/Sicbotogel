package com.hdev.sicbotogel.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hdev.sicbotogel.R;
import com.hdev.sicbotogel.adapter.NotificationAdapter;
import com.hdev.sicbotogel.database.helper.NotificationHelper;
import com.hdev.sicbotogel.database.model.Notifications;
import com.hdev.sicbotogel.interfaces.NotificationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationHistoryActivity extends AppCompatActivity implements NotificationView, NotificationAdapter.OnNotificationClick {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_view_empty_notification)
    TextView textViewDataEmpty;
    @BindView(R.id.recycler_view_notification)
    RecyclerView recyclerViewNotification;
    private NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);
        ButterKnife.bind(this);

        initialize();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationHelper.getAllNotifications(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onNotificationFirstLoaded(List<Notifications> notifications) {

    }

    @Override
    public void onNotificationLoaded(List<Notifications> notifications) {
        initRecyclerView(notifications);
    }

    @Override
    public void onDataEmpty() {
        recyclerViewNotification.setVisibility(View.GONE);
        textViewDataEmpty.setVisibility(View.VISIBLE);
    }





    /*
    Initialize
     */

    private void initialize() {
        initToolbar();
        notificationHelper = new NotificationHelper(this);
        notificationHelper.open();
        notificationHelper.getAllNotifications(this);
    }

    /*
    Init Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    /*
    Init RecyclerView
     */
    private void initRecyclerView(List<Notifications> notificationsList) {
        NotificationAdapter adapter = new NotificationAdapter(this, notificationsList, this);
        recyclerViewNotification.setAdapter(adapter);
    }

    @Override
    public void onClick(Notifications notifications) {
        Intent viewNotificationIntent = new Intent(this, ViewNotification.class);
        viewNotificationIntent.putExtra("parcelable_notification", notifications);
        startActivity(viewNotificationIntent);
    }

    @Override
    public void onDelete(int id) {
        notificationHelper.delete(id);
        notificationHelper.getAllNotifications(this);
    }
}