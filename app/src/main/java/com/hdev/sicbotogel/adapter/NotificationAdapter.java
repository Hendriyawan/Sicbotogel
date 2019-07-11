package com.hdev.sicbotogel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hdev.sicbotogel.R;
import com.hdev.sicbotogel.database.model.Notifications;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.biubiubiu.justifytext.library.JustifyTextView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    private Context context;
    private List<Notifications> notificationsList;
    private OnNotificationClick onNotificationClick;

    public NotificationAdapter(Context context, List<Notifications> notificationsList, OnNotificationClick onNotificationClick) {
        this.context = context;
        this.notificationsList = notificationsList;
        this.onNotificationClick = onNotificationClick;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NotificationHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder notificationHolder, int position) {
        final Notifications notifications = notificationsList.get(position);
        notificationHolder.textViewTitle.setText(String.format(context.getResources().getString(R.string.text_title), notifications.getTitle()));
        notificationHolder.textViewBody.setText(String.format(context.getResources().getString(R.string.text_body), notifications.getBody()));
        notificationHolder.textViewDate.setText(String.format(context.getResources().getString(R.string.text_date), notifications.getDate()));
        if (notifications.getStatus_read().equals("readed")) {
            notificationHolder.textViewStatusRead.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
        }
        notificationHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotificationClick.onClick(notifications);
            }
        });
        notificationHolder.imageButtonDeleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotificationClick.onDelete(notifications.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList != null && notificationsList.size() > 0 ? notificationsList.size() : 0;
    }

    public interface OnNotificationClick {

        void onDelete(int id);

        void onClick(Notifications notifications);
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_button_delete_notification)
        ImageButton imageButtonDeleteNotification;
        @BindView(R.id.text_view_title_notification)
        TextView textViewTitle;
        @BindView(R.id.text_view_body_notification)
        JustifyTextView textViewBody;
        @BindView(R.id.text_view_date_notification)
        TextView textViewDate;
        @BindView(R.id.text_view_status_read_notification)
        TextView textViewStatusRead;
        View view;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }
    }
}
