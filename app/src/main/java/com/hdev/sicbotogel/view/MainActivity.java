package com.hdev.sicbotogel.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hdev.sicbotogel.R;
import com.hdev.sicbotogel.api.NotificationPresenter;
import com.hdev.sicbotogel.database.helper.NotificationHelper;
import com.hdev.sicbotogel.database.model.Notifications;
import com.hdev.sicbotogel.fcm.FirebaseCloudMessageService;
import com.hdev.sicbotogel.interfaces.NotificationView;
import com.hdev.sicbotogel.utils.AppPreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NotificationView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_view_count_notification)
    TextView textViewCountNotification;
    @BindView(R.id.progressbar_horizontal)
    ProgressBar progressBarHorizontal;
    @BindView(R.id.web_view)
    WebView webView;
    private NotificationHelper notificationHelper;
    /*
    BroadcastReceiver
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notificationHelper.getCountNotificationUnread(textViewCountNotification);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initialize();

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("NOTIFICATION_VALUE"));
        notificationHelper.getCountNotificationUnread(textViewCountNotification);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        notificationHelper.getCountNotificationUnread(textViewCountNotification);
    }


    /*
   OnClick open notification
    */
    @OnClick(R.id.relative_layout_notification)
    public void relativeClickNotification() {
        startActivity(new Intent(this, NotificationHistoryActivity.class));
    }


    /*
    initialize
     */
    private void initialize() {
        initFCM();
        initToolbar();
        initProgressbar();
        initWebView();
        initDefaultNotification();
        notificationHelper = new NotificationHelper(this);
        notificationHelper.open();
    }

    /*
    Init FCM
     */
    private void initFCM() {
        FirebaseCloudMessageService.subscribe("Sicbotogel");
    }

    /*
    Init Toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    /*
    Init ProgressBar Horizontal
     */
    private void initProgressbar() {
        progressBarHorizontal.setMax(100);
        progressBarHorizontal.setProgress(0);
    }

    /*
    Init WebView
     */
    private void initWebView() {
        webView.loadUrl("http://sicbotogel.me");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarHorizontal.setProgress(newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBarHorizontal.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String closeBanner = "javascript:(function(){document.getElementById('close_button').click();})()";
                view.loadUrl(closeBanner);
                progressBarHorizontal.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onNotificationFirstLoaded(List<Notifications> notifications) {
        insertAll(notifications);
    }

    @Override
    public void onNotificationLoaded(List<Notifications> notifications) {

    }

    @Override
    public void onDataEmpty() {

    }

    /*
    Init default notification
     */
    private void initDefaultNotification() {
        NotificationPresenter notificationPresenter = new NotificationPresenter(this, this);
        notificationPresenter.loadNotification();
    }

    /*
    insert all first
     */
    private void insertAll(List<Notifications> notifications) {
        Notifications notification;
        for (int i = 0; i < notifications.size(); i++) {
            notification = new Notifications();

            String title = notifications.get(i).getTitle();
            String body = Html.fromHtml(notifications.get(i).getBody()).toString();
            String date = notifications.get(i).getDate();
            String status_read = notifications.get(i).getStatus_read();

            notification.setTitle(title);
            notification.setBody(body);
            notification.setDate(date);
            notification.setStatus_read(status_read);
            notificationHelper.insert(notification);
            notificationHelper.getCountNotificationUnread(textViewCountNotification);
        }
        AppPreferences.setFirstInstall(false);
    }
}
