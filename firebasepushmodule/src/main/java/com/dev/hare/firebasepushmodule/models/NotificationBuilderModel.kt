package com.dev.hare.firebasepushmodule.models

import android.app.Notification
import android.app.Notification.Extender
import android.app.PendingIntent
import android.app.Person
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class NotificationBuilderModel(context: Context, channelId: String) {
    var builder: Notification.Builder? = null
    var compatBuilder: NotificationCompat.Builder? = null

    /*
     * =================================================================================================================
     * Notification.Builder
     * START
     * =================================================================================================================
     */

    @Retention(RetentionPolicy.SOURCE)
    annotation class Priority

    @Retention(RetentionPolicy.SOURCE)
    annotation class Visibility


    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder = Notification.Builder(context, channelId)
        }
        this.compatBuilder = NotificationCompat.Builder(context, channelId)
    }

    fun setShortcutId(shortcutId: String): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setShortcutId(shortcutId)
        }
        this.compatBuilder?.setShortcutId(shortcutId)
        return this
    }

    fun setBadgeIconType(icon: Int): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setBadgeIconType(icon)
        }
        this.compatBuilder?.setBadgeIconType(icon)
        return this
    }

    fun setGroupAlertBehavior(@NotificationCompat.GroupAlertBehavior groupAlertBehavior: Int): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setGroupAlertBehavior(groupAlertBehavior)
        }
        this.compatBuilder?.setGroupAlertBehavior(groupAlertBehavior)
        return this
    }

    fun setChannelId(channelId: String): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setChannelId(channelId)
        }
        this.compatBuilder?.setChannelId(channelId)
        return this
    }

    fun setTimeoutAfter(durationMs: Long): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setTimeoutAfter(durationMs)
        }
        this.compatBuilder?.setTimeoutAfter(durationMs)
        return this
    }

    fun setWhen(time: Long): NotificationBuilderModel {
        this.builder?.setWhen(time)
        this.compatBuilder?.setWhen(time)
        return this
    }

    fun setShowWhen(show: Boolean): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            this.builder?.setShowWhen(show)
        }
        this.compatBuilder?.setShowWhen(show)
        return this
    }

    fun setUsesChronometer(b: Boolean): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setUsesChronometer(b)
        }
        this.compatBuilder?.setUsesChronometer(b)
        return this
    }

    fun setChronometerCountDown(countDown: Boolean): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setChronometerCountDown(countDown)
        }
        return this
    }

    fun setSmallIcon(icon: Int): NotificationBuilderModel {
        this.compatBuilder?.setSmallIcon(icon)
        this.builder?.setSmallIcon(icon)
        return this
    }

    fun setSmallIcon(@DrawableRes icon: Int, level: Int): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setSmallIcon(icon, level)
        }
        this.compatBuilder?.setSmallIcon(icon, level)
        return this
    }

    fun setContentTitle(text: CharSequence): NotificationBuilderModel {
        this.builder?.setContentTitle(text)
        this.compatBuilder?.setContentTitle(text)
        return this
    }

    fun setContentText(text: CharSequence): NotificationBuilderModel {
        this.compatBuilder?.setContentText(text)
        this.builder?.setContentText(text)
        return this
    }

    fun setSubText(text: CharSequence): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.builder?.setSubText(text)
        }
        this.compatBuilder?.setSubText(text)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setSettingsText(text: CharSequence): NotificationBuilderModel {
        this.builder?.setSettingsText(text)
        return this
    }

    fun setRemoteInputHistory(text: Array<CharSequence>): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.builder?.setRemoteInputHistory(text)
        }
        this.compatBuilder?.setRemoteInputHistory(text)
        return this
    }

    fun setNumber(number: Int): NotificationBuilderModel {
        this.builder?.setNumber(number)
        this.compatBuilder?.setNumber(number)
        return this
    }

    @Deprecated("use {@link #setSubText(CharSequence)} instead to set a text in the header.")
    fun setContentInfo(info: CharSequence): NotificationBuilderModel {
        this.builder?.setContentInfo(info)
        this.compatBuilder?.setContentInfo(info)
        return this
    }

    fun setProgress(max: Int, progress: Int, indeterminate: Boolean): NotificationBuilderModel {
        this.builder?.setProgress(max, progress, indeterminate)
        this.compatBuilder?.setProgress(max, progress, indeterminate)
        return this
    }

    @Deprecated("Use [.setCustomContentView] instead.")
    fun setContent(views: RemoteViews): NotificationBuilderModel {
        this.builder?.setContent(views)
        this.compatBuilder?.setContent(views)
        return this
    }

    fun setCustomContentView(contentView: RemoteViews): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.builder?.setCustomContentView(contentView)
        }
        this.compatBuilder?.setCustomContentView(contentView)
        return this
    }

    fun setCustomBigContentView(contentView: RemoteViews): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.builder?.setCustomBigContentView(contentView)
        }
        this.compatBuilder?.setCustomBigContentView(contentView)
        return this
    }

    fun setContentIntent(intent: PendingIntent): NotificationBuilderModel {
        this.builder?.setContentIntent(intent)
        this.compatBuilder?.setContentIntent(intent)
        return this
    }

    fun setDeleteIntent(intent: PendingIntent): NotificationBuilderModel {
        this.builder?.setDeleteIntent(intent)
        this.compatBuilder?.setDeleteIntent(intent)
        return this
    }

    fun setFullScreenIntent(intent: PendingIntent, highPriority: Boolean): NotificationBuilderModel {
        this.builder?.setFullScreenIntent(intent, highPriority)
        this.compatBuilder?.setFullScreenIntent(intent, highPriority)
        return this
    }

    @Deprecated("Obsolete version of {@link #setTicker(CharSequence)}.")
    fun setTicker(tickerText: CharSequence, views: RemoteViews): NotificationBuilderModel {
        this.builder?.setTicker(tickerText, views)
        this.compatBuilder?.setTicker(tickerText, views)
        return this
    }

    fun setLargeIcon(b: Bitmap): NotificationBuilderModel {
        this.builder?.setLargeIcon(b)
        this.compatBuilder?.setLargeIcon(b)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setLargeIcon(icon: Icon): NotificationBuilderModel {
        this.builder?.setLargeIcon(icon)
        return this
    }

    @Deprecated("use {@link NotificationChannel#setSound(Uri, AudioAttributes)} instead.")
    fun setSound(sound: Uri): NotificationBuilderModel {
        this.builder?.setSound(sound)
        this.compatBuilder?.setSound(sound)
        return this
    }

    @Deprecated("use {@link NotificationChannel#setSound(Uri, AudioAttributes)}.")
    fun setSound(sound: Uri, streamType: Int): NotificationBuilderModel {
        this.builder?.setSound(sound, streamType)
        this.compatBuilder?.setSound(sound, streamType)
        return this
    }

    @Deprecated("use {@link NotificationChannel#setSound(Uri, AudioAttributes)} instead.")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setSound(sound: Uri, audioAttributes: AudioAttributes): NotificationBuilderModel {
        this.builder?.setSound(sound, audioAttributes)
        return this
    }

    @Deprecated("use {@link NotificationChannel#setVibrationPattern(long[])} instead.")
    fun setVibrate(pattern: LongArray): NotificationBuilderModel {
        this.builder?.setVibrate(pattern)
        this.compatBuilder?.setVibrate(pattern)
        return this
    }

    @Deprecated("use {@link NotificationChannel#enableLights(boolean)} instead.")
    fun setLights(@ColorInt argb: Int, onMs: Int, offMs: Int): NotificationBuilderModel {
        this.builder?.setLights(argb, onMs, offMs)
        this.compatBuilder?.setLights(argb, onMs, offMs)
        return this
    }

    fun setOngoing(ongoing: Boolean): NotificationBuilderModel {
        this.builder?.setOngoing(ongoing)
        this.compatBuilder?.setOngoing(ongoing)
        return this
    }

    fun setColorized(colorize: Boolean): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setColorized(colorize)
        }
        this.compatBuilder?.setColorized(colorize)
        return this
    }

    fun setOnlyAlertOnce(onlyAlertOnce: Boolean): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.builder?.setOnlyAlertOnce(onlyAlertOnce)
        }
        this.compatBuilder?.setOnlyAlertOnce(onlyAlertOnce)
        return this
    }

    fun setAutoCancel(autoCancel: Boolean): NotificationBuilderModel {
        this.builder?.setAutoCancel(autoCancel)
        this.compatBuilder?.setAutoCancel(autoCancel)
        return this
    }

    fun setLocalOnly(localOnly: Boolean): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            this.builder?.setLocalOnly(localOnly)
        }
        this.compatBuilder?.setLocalOnly(localOnly)
        return this
    }

    @Deprecated(
        "use {@link NotificationChannel#enableVibration(boolean)} and\n" +
                "         {@link NotificationChannel#enableLights(boolean)} and\n" +
                "          {@link NotificationChannel#setSound(Uri, AudioAttributes)} instead."
    )
    fun setDefaults(defaults: Int): NotificationBuilderModel {
        this.builder?.setDefaults(defaults)
        this.compatBuilder?.setDefaults(defaults)
        return this
    }

    @Deprecated("use {@link NotificationChannel#setImportance(int)} instead.")
    fun setPriority(@Priority pri: Int): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.builder?.setPriority(pri)
        }
        this.compatBuilder?.priority = pri
        return this
    }

    fun setCategory(category: String): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.builder?.setCategory(category)
        }
        this.compatBuilder?.setCategory(category)
        return this
    }

    @Deprecated("use {@link #addPerson(Person)}")
    fun addPerson(uri: String): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.builder?.addPerson(uri)
        }
        this.compatBuilder?.addPerson(uri)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun addPerson(person: Person): NotificationBuilderModel {
        this.builder?.addPerson(person)
        return this
    }

    fun setGroup(groupKey: String): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            this.builder?.setGroup(groupKey)
        }
        this.compatBuilder?.setGroup(groupKey)
        return this
    }

    fun setGroupSummary(isGroupSummary: Boolean): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            this.builder?.setGroupSummary(isGroupSummary)
        }
        this.compatBuilder?.setGroupSummary(isGroupSummary)
        return this
    }

    fun setSortKey(sortKey: String): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            this.builder?.setSortKey(sortKey)
        }
        this.compatBuilder?.setSortKey(sortKey)
        return this
    }

    fun addExtras(extras: Bundle?): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            this.builder?.addExtras(extras)
        }
        this.compatBuilder?.addExtras(extras)
        return this
    }

    fun setExtras(extras: Bundle?): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.builder?.extras = extras
        }
        this.compatBuilder?.extras = extras
        return this
    }

    @Deprecated("Use {@link #addAction(Action)} instead.")
    fun addAction(icon: Int, title: CharSequence, intent: PendingIntent): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.builder?.addAction(icon, title, intent)
        }
        this.compatBuilder?.addAction(icon, title, intent)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    fun addAction(action: Notification.Action): NotificationBuilderModel {
        this.builder?.addAction(action)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    fun setActions(vararg actions: Notification.Action): NotificationBuilderModel {
        for (i in actions.indices) {
            if (actions[i] != null) {
                this.builder?.addAction(actions[i])
            }
        }
        return this
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setStyle(style: Notification.Style): NotificationBuilderModel {
        this.builder?.style = style
        return this
    }

    fun setVisibility(@NotificationBuilderModel.Visibility visibility: Int): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.builder?.setVisibility(visibility)
        }
        this.compatBuilder?.setVisibility(visibility)
        return this
    }

    fun setPublicVersion(n: Notification?): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.builder?.setPublicVersion(n)
        }
        this.compatBuilder?.setPublicVersion(n)
        return this
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    fun extend(extender: Extender): NotificationBuilderModel {
        this.builder?.extend(extender)
        return this
    }


    fun setColor(@ColorInt argb: Int): NotificationBuilderModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.builder?.setColor(argb)
        }
        this.compatBuilder?.color = argb
        return this
    }

    /*
     * =================================================================================================================
     * Notification.Builder
     * END
     * =================================================================================================================
     */

    /*
     * =================================================================================================================
     * NotificationCompat.Builder
     * START
     * =================================================================================================================
     */

    fun setTicker(tickerText: CharSequence): NotificationBuilderModel {
        this.compatBuilder?.setTicker(tickerText)
        return this
    }

    fun addAction(action: NotificationCompat.Action): NotificationBuilderModel {
        this.compatBuilder?.addAction(action)
        return this
    }

    @RequiresApi(21)
    fun addInvisibleAction(icon: Int, title: CharSequence, intent: PendingIntent): NotificationBuilderModel {
        return this.addInvisibleAction(NotificationCompat.Action(icon, title, intent))
    }

    @RequiresApi(21)
    fun addInvisibleAction(action: NotificationCompat.Action): NotificationBuilderModel {
        this.compatBuilder?.addInvisibleAction(action)
        return this
    }

    fun setStyle(style: NotificationCompat.Style): NotificationBuilderModel {
        this.compatBuilder?.setStyle(style)
        return this
    }

    fun setCustomHeadsUpContentView(contentView: RemoteViews): NotificationBuilderModel {
        this.compatBuilder?.setCustomHeadsUpContentView(contentView)
        return this
    }

    fun extend(extender: NotificationCompat.Extender): NotificationBuilderModel {
        this.compatBuilder?.extend(extender)
        return this
    }

    /*
     * =================================================================================================================
     * NotificationCompat.Builder
     * END
     * =================================================================================================================
     */
}