package com.aveek.badgetest.util

import android.content.Context
import androidx.core.app.NotificationCompat
import com.aveek.badgetest.R

class BadgeManager {
    private val badgeNotificationId = 0
    fun toggleBadge (context: Context, showBadge: Boolean) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.cancel(badgeNotificationId)

        if (!showBadge) {
            return
        }

        val builder : NotificationCompat.Builder = NotificationCompat.Builder(context, "channelId")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Badge Test")
            .setContentText("This is a test notification")
            .setNumber(1)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setAutoCancel(true)
        notificationManager.notify(badgeNotificationId, builder.build())
        return
    }
}