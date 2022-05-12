package com.kyg.sharegifticon

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        setAlarmNotification(context)
    }

    private fun setAlarmNotification(context: Context) {
        ManageNotification(
            context = context,
            channelId = Constants.channelIdExpirationDate,
            channelName = Constants.channelNameExpirationDate,
            notificationId = Constants.notificationIdExpirationDate,
            notificationContentTitle = context.getString(R.string.ALARM_NOTIFICATION_TITLE),
            notificationContentText = context.getString(R.string.ALARM_NOTIFICATION_TEXT)
        )
    }
}