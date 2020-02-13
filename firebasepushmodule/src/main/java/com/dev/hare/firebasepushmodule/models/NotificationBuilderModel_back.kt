package com.dev.hare.firebasepushmodule.models

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.RestrictTo
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationBuilderModel_back(context: Context, channelId: String) {
    private val MAX_CHARSEQUENCE_LENGTH = 5120
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    var mContext: Context = context
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    var mActions: ArrayList<NotificationCompat.Action>? = null
    internal lateinit var mInvisibleActions: ArrayList<NotificationCompat.Action>
    internal var mContentTitle: CharSequence? = null
    internal var mContentText: CharSequence? = null
    internal var mContentIntent: PendingIntent? = null
    internal var mFullScreenIntent: PendingIntent? = null
    internal var mTickerView: RemoteViews? = null
    internal var mLargeIcon: Bitmap? = null
    internal var mContentInfo: CharSequence? = null
    internal var mNumber: Int = 0
    internal var mPriority: Int = 0
    internal var mShowWhen: Boolean = false
    internal var mUseChronometer: Boolean = false
    internal var mStyle: NotificationCompat.Style? = null
    internal var mSubText: CharSequence? = null
    internal var mRemoteInputHistory: Array<CharSequence>? = null
    internal var mProgressMax: Int = 0
    internal var mProgress: Int = 0
    internal var mProgressIndeterminate: Boolean = false
    internal var mGroupKey: String? = null
    internal var mGroupSummary: Boolean = false
    internal var mSortKey: String? = null
    internal var mLocalOnly: Boolean = false
    internal var mColorized: Boolean = false
    internal var mColorizedSet: Boolean = false
    internal var mCategory: String? = null
    internal var mExtras: Bundle? = null
    internal var mColor: Int = 0
    internal var mVisibility: Int = 0
    internal var mPublicVersion: Notification? = null
    internal var mContentView: RemoteViews? = null
    internal var mBigContentView: RemoteViews? = null
    internal var mHeadsUpContentView: RemoteViews? = null
    internal var mChannelId: String = channelId
    internal var mBadgeIcon: Int = 0
    internal var mShortcutId: String? = null
    internal var mTimeout: Long = 0
    internal var mGroupAlertBehavior: Int = 0
    internal var mNotification: Notification

    @Deprecated("")
    var mPeople: ArrayList<String>

    init {
        this.mActions = ArrayList()
        this.mInvisibleActions = ArrayList()
        this.mShowWhen = true
        this.mLocalOnly = false
        this.mColor = 0
        this.mVisibility = 0
        this.mBadgeIcon = 0
        this.mGroupAlertBehavior = 0
        this.mNotification = Notification()
        this.mNotification.`when` = System.currentTimeMillis()
        this.mNotification.audioStreamType = -1
        this.mPriority = 0
        this.mPeople = ArrayList()
    }

    fun setWhen(`when`: Long): NotificationBuilderModel_back {
        this.mNotification.`when` = `when`
        return this
    }

    fun setShowWhen(show: Boolean): NotificationBuilderModel_back {
        this.mShowWhen = show
        return this
    }

    fun setUsesChronometer(b: Boolean): NotificationBuilderModel_back {
        this.mUseChronometer = b
        return this
    }

    fun setSmallIcon(icon: Int): NotificationBuilderModel_back {
        this.mNotification.icon = icon
        return this
    }

    fun setSmallIcon(icon: Int, level: Int): NotificationBuilderModel_back {
        this.mNotification.icon = icon
        this.mNotification.iconLevel = level
        return this
    }

    fun setContentTitle(title: CharSequence): NotificationBuilderModel_back {
        this.mContentTitle = limitCharSequenceLength(title)
        return this
    }

    fun setContentText(text: CharSequence): NotificationBuilderModel_back {
        this.mContentText = limitCharSequenceLength(text)
        return this
    }

    fun setSubText(text: CharSequence): NotificationBuilderModel_back {
        this.mSubText = limitCharSequenceLength(text)
        return this
    }

    fun setRemoteInputHistory(text: Array<CharSequence>): NotificationBuilderModel_back {
        this.mRemoteInputHistory = text
        return this
    }

    fun setNumber(number: Int): NotificationBuilderModel_back {
        this.mNumber = number
        return this
    }

    fun setContentInfo(info: CharSequence): NotificationBuilderModel_back {
        this.mContentInfo = limitCharSequenceLength(info)
        return this
    }

    fun setProgress(max: Int, progress: Int, indeterminate: Boolean): NotificationBuilderModel_back {
        this.mProgressMax = max
        this.mProgress = progress
        this.mProgressIndeterminate = indeterminate
        return this
    }

    fun setContent(views: RemoteViews): NotificationBuilderModel_back {
        this.mNotification.contentView = views
        return this
    }

    fun setContentIntent(intent: PendingIntent): NotificationBuilderModel_back {
        this.mContentIntent = intent
        return this
    }

    fun setDeleteIntent(intent: PendingIntent): NotificationBuilderModel_back {
        this.mNotification.deleteIntent = intent
        return this
    }

    fun setFullScreenIntent(intent: PendingIntent, highPriority: Boolean): NotificationBuilderModel_back {
        this.mFullScreenIntent = intent
        this.setFlag(128, highPriority)
        return this
    }

    fun setTicker(tickerText: CharSequence): NotificationBuilderModel_back {
        this.mNotification.tickerText = limitCharSequenceLength(tickerText)
        return this
    }

    fun setTicker(tickerText: CharSequence, views: RemoteViews): NotificationBuilderModel_back {
        this.mNotification.tickerText = limitCharSequenceLength(tickerText)
        this.mTickerView = views
        return this
    }

    fun setLargeIcon(icon: Bitmap): NotificationBuilderModel_back {
        this.mLargeIcon = icon
        return this
    }

    /*fun setLargeIcon(icon: Bitmap): NotificationBuilderModel {
        this.mLargeIcon = this.reduceLargeIconSize(icon)
        return this
    }*/

    /*private fun reduceLargeIconSize(icon: Bitmap?): Bitmap? {
        if (icon != null && Build.VERSION.SDK_INT < 27) {
            val res = this.mContext.resources
            val maxWidth = res.getDimensionPixelSize(dimen.compat_notification_large_icon_max_width)
            val maxHeight = res.getDimensionPixelSize(dimen.compat_notification_large_icon_max_height)
            if (icon.width <= maxWidth && icon.height <= maxHeight) {
                return icon
            } else {
                val scale = Math.min(
                    maxWidth.toDouble() / Math.max(1, icon.width).toDouble(),
                    maxHeight.toDouble() / Math.max(1, icon.height).toDouble()
                )
                return Bitmap.createScaledBitmap(
                    icon,
                    Math.ceil(icon.width.toDouble() * scale).toInt(),
                    Math.ceil(icon.height.toDouble() * scale).toInt(),
                    true
                )
            }
        } else {
            return icon
        }
    }*/

    fun setSound(sound: Uri): NotificationBuilderModel_back {
        this.mNotification.sound = sound
        this.mNotification.audioStreamType = -1
        if (Build.VERSION.SDK_INT >= 21) {
            this.mNotification.audioAttributes =
                android.media.AudioAttributes.Builder().setContentType(4).setUsage(5).build()
        }

        return this
    }

    fun setSound(sound: Uri, streamType: Int): NotificationBuilderModel_back {
        this.mNotification.sound = sound
        this.mNotification.audioStreamType = streamType
        if (Build.VERSION.SDK_INT >= 21) {
            this.mNotification.audioAttributes =
                android.media.AudioAttributes.Builder().setContentType(4).setLegacyStreamType(streamType).build()
        }

        return this
    }

    fun setVibrate(pattern: LongArray): NotificationBuilderModel_back {
        this.mNotification.vibrate = pattern
        return this
    }

    fun setLights(@ColorInt argb: Int, onMs: Int, offMs: Int): NotificationBuilderModel_back {
        this.mNotification.ledARGB = argb
        this.mNotification.ledOnMS = onMs
        this.mNotification.ledOffMS = offMs
        val showLights = this.mNotification.ledOnMS != 0 && this.mNotification.ledOffMS != 0
        this.mNotification.flags = this.mNotification.flags and -2 or if (showLights) 1 else 0
        return this
    }

    fun setOngoing(ongoing: Boolean): NotificationBuilderModel_back {
        this.setFlag(2, ongoing)
        return this
    }

    fun setColorized(colorize: Boolean): NotificationBuilderModel_back {
        this.mColorized = colorize
        this.mColorizedSet = true
        return this
    }

    fun setOnlyAlertOnce(onlyAlertOnce: Boolean): NotificationBuilderModel_back {
        this.setFlag(8, onlyAlertOnce)
        return this
    }

    fun setAutoCancel(autoCancel: Boolean): NotificationBuilderModel_back {
        this.setFlag(16, autoCancel)
        return this
    }

    fun setLocalOnly(b: Boolean): NotificationBuilderModel_back {
        this.mLocalOnly = b
        return this
    }

    fun setCategory(category: String): NotificationBuilderModel_back {
        this.mCategory = category
        return this
    }

    fun setDefaults(defaults: Int): NotificationBuilderModel_back {
        this.mNotification.defaults = defaults
        if (defaults and 4 != 0) {
            this.mNotification.flags = this.mNotification.flags or 1
        }

        return this
    }

    private fun setFlag(mask: Int, value: Boolean) {
        if (value) {
            this.mNotification.flags = this.mNotification.flags or mask
        } else {
            this.mNotification.flags = this.mNotification.flags and mask.inv()
        }

    }

    fun setPriority(pri: Int): NotificationBuilderModel_back {
        this.mPriority = pri
        return this
    }

    fun addPerson(uri: String): NotificationBuilderModel_back {
        this.mPeople.add(uri)
        return this
    }

    fun setGroup(groupKey: String): NotificationBuilderModel_back {
        this.mGroupKey = groupKey
        return this
    }

    fun setGroupSummary(isGroupSummary: Boolean): NotificationBuilderModel_back {
        this.mGroupSummary = isGroupSummary
        return this
    }

    fun setSortKey(sortKey: String): NotificationBuilderModel_back {
        this.mSortKey = sortKey
        return this
    }

    fun addExtras(extras: Bundle?): NotificationBuilderModel_back {
        if (extras != null) {
            if (this.mExtras == null) {
                this.mExtras = Bundle(extras)
            } else {
                this.mExtras!!.putAll(extras)
            }
        }

        return this
    }

    fun setExtras(extras: Bundle): NotificationBuilderModel_back {
        this.mExtras = extras
        return this
    }

    fun getExtras(): Bundle {
        if (this.mExtras == null) {
            this.mExtras = Bundle()
        }

        return this.mExtras!!
    }

    fun addAction(icon: Int, title: CharSequence, intent: PendingIntent): NotificationBuilderModel_back {
        this.mActions!!.add(NotificationCompat.Action(icon, title, intent))
        return this
    }

    fun addAction(action: NotificationCompat.Action): NotificationBuilderModel_back {
        this.mActions!!.add(action)
        return this
    }

    @RequiresApi(21)
    fun addInvisibleAction(icon: Int, title: CharSequence, intent: PendingIntent): NotificationBuilderModel_back {
        return this.addInvisibleAction(NotificationCompat.Action(icon, title, intent))
    }

    @RequiresApi(21)
    fun addInvisibleAction(action: NotificationCompat.Action): NotificationBuilderModel_back {
        this.mInvisibleActions.add(action)
        return this
    }

    /*fun setStyle(style: NotificationCompat.Style): NotificationBuilderModel {
        if (this.mStyle !== style) {
            this.mStyle = style
            if (this.mStyle != null) {
                this.mStyle!!.setBuilder(this)
            }
        }

        return this
    }*/

    fun setColor(@ColorInt argb: Int): NotificationBuilderModel_back {
        this.mColor = argb
        return this
    }

    fun setVisibility(visibility: Int): NotificationBuilderModel_back {
        this.mVisibility = visibility
        return this
    }

    fun setPublicVersion(n: Notification): NotificationBuilderModel_back {
        this.mPublicVersion = n
        return this
    }

    fun setCustomContentView(contentView: RemoteViews): NotificationBuilderModel_back {
        this.mContentView = contentView
        return this
    }

    fun setCustomBigContentView(contentView: RemoteViews): NotificationBuilderModel_back {
        this.mBigContentView = contentView
        return this
    }

    fun setCustomHeadsUpContentView(contentView: RemoteViews): NotificationBuilderModel_back {
        this.mHeadsUpContentView = contentView
        return this
    }

    fun setChannelId(channelId: String): NotificationBuilderModel_back {
        this.mChannelId = channelId
        return this
    }

    fun setTimeoutAfter(durationMs: Long): NotificationBuilderModel_back {
        this.mTimeout = durationMs
        return this
    }

    fun setShortcutId(shortcutId: String): NotificationBuilderModel_back {
        this.mShortcutId = shortcutId
        return this
    }

    fun setBadgeIconType(icon: Int): NotificationBuilderModel_back {
        this.mBadgeIcon = icon
        return this
    }

    fun setGroupAlertBehavior(groupAlertBehavior: Int): NotificationBuilderModel_back {
        this.mGroupAlertBehavior = groupAlertBehavior
        return this
    }

    /*fun extend(extender: NotificationCompat.Extender): NotificationBuilderModel {
        extender.extend(this)
        return this
    }*/

    /* @Deprecated("")
    fun getNotification(): Notification {
        return this.build()
    } */

    /* fun build(): Notification {
        return NotificationCompatBuilder(this).build()
    } */

    protected fun limitCharSequenceLength(cs: CharSequence?): CharSequence? {
        var cs = cs
        if (cs == null) {
            return cs
        } else {
            if (cs.length > 5120) {
                cs = cs.subSequence(0, 5120)
            }

            return cs
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun getContentView(): RemoteViews? {
        return this.mContentView
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun getBigContentView(): RemoteViews? {
        return this.mBigContentView
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun getHeadsUpContentView(): RemoteViews? {
        return this.mHeadsUpContentView
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun getWhenIfShowing(): Long {
        return if (this.mShowWhen) this.mNotification.`when` else 0L
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun getPriority(): Int {
        return this.mPriority
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun getColor(): Int {
        return this.mColor
    }
}