package org.michaelbel.movies.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import org.michaelbel.movies.common.ktx.isTimePasses
import org.michaelbel.movies.notifications.ktx.isPostNotificationsPermissionGranted
import org.michaelbel.movies.notifications.model.MoviesPush
import org.michaelbel.movies.persistence.datastore.MoviesPreferences
import org.michaelbel.movies.ui.icons.MoviesIcons
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationClient @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManagerCompat,
    private val preferences: MoviesPreferences
) {

    private val channelId: String
        get() = context.getString(R.string.notification_channel_id)

    suspend fun notificationsPermissionRequired(time: Long): Boolean {
        val expireTime: Long = preferences.getNotificationExpireTime() ?: 0L
        val currentTime: Long = System.currentTimeMillis()
        val isTimePasses: Boolean = isTimePasses(ONE_DAY_MILLS, expireTime, currentTime)
        delay(time)
        return !context.isPostNotificationsPermissionGranted && isTimePasses
    }

    suspend fun updateNotificationExpireTime() {
        val currentTime: Long = System.currentTimeMillis()
        preferences.setNotificationExpireTime(currentTime)
    }

    fun send(push: MoviesPush) {
        createChannel()

        val notification = NotificationCompat.Builder(context, channelId).apply {
            setContentTitle(push.notificationTitle)
            setContentText(push.notificationDescription)
            setSmallIcon(MoviesIcons.NotificationSmallIconMovieFilter)
            setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            color = ContextCompat.getColor(context, org.michaelbel.movies.ui.R.color.primary)
            setDefaults(NotificationCompat.DEFAULT_LIGHTS)
            setGroupSummary(true)
            setGroup(GROUP_NAME)
            setContentIntent(push.pendingIntent())
            setVibrate(VIBRATE_PATTERN)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_MAX
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }.build()

        notificationManager.notify(TAG, push.notificationId, notification)
    }

    private fun createChannel() {
        val notificationChannel: NotificationChannelCompat = NotificationChannelCompat.Builder(
            channelId,
            NotificationManagerCompat.IMPORTANCE_HIGH
        ).apply {
            setName(context.getString(R.string.notification_channel_name))
            setDescription(context.getString(R.string.notification_channel_description))
            setShowBadge(true)
        }.build()
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun MoviesPush.pendingIntent(): PendingIntent {
        return PendingIntent.getActivity(
            context,
            notificationId,
            Intent(Intent.ACTION_VIEW, "movies://details/$movieId".toUri()),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private companion object {
        private const val TAG = "PUSH"
        private const val GROUP_NAME = "App"
        private val VIBRATE_PATTERN: LongArray = longArrayOf(1000)
        private val ONE_DAY_MILLS: Long = TimeUnit.DAYS.toMillis(1)
    }
}