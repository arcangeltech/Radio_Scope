package marwen.project.radioscope.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.NotificationUtil
import com.google.android.exoplayer2.util.Util
import marwen.project.radioscope.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import marwen.project.radioscope.BuildConfig.APPLICATION_ID
import marwen.project.radioscope.data.remote.dto.Radio
import marwen.project.radioscope.ui.MainActivity

import java.util.*

private const val PLAYBACK_CHANNEL_ID = 1
private const val PLAYBACK_NOTIFICATION_ID = 1
private const val MEDIA_SESSION_TAG = "sed_audio"

private const val PLAYBACK_TIMER_DELAY = 5 * 1000L

private const val ARG_RADIO_ID = "id"
private const val ARG_URL = "url"
private const val ARG_TITLE = "title"
private const val ARG_IMAGE = "image"

class AudioService : LifecycleService() {

    inner class AudioServiceBinder : Binder() {
        val service
            get() = this@AudioService

        val exoPlayer
            get() = this@AudioService.exoPlayer
    }

    companion object {

        @MainThread
        fun newIntent(context: Context, radio: Radio? = null, command:String) = Intent(context, AudioService::class.java).apply {
            radio?.let {

                putExtra(ARG_RADIO_ID, radio.radio_id.toString())
                putExtra("command",command)
                radio.radio_name?.let { title -> putExtra(ARG_TITLE, title) }
                radio.radio_url?.let{ uriString -> putExtra(ARG_URL, Uri.parse(uriString)) }
                radio.radio_image?.let{ image -> putExtra(ARG_IMAGE, Uri.parse(image)) }
            }
        }

    }

    private var playbackTimer: Timer? = null

    var radioId: String? = null
        private set
    private var radioTitle: String? = null
    var radioUrl =  Uri.parse("")

    lateinit var exoPlayer: ExoPlayer

    private var playerNotificationManager: PlayerNotificationManager? = null
    private var mediaSession: MediaSessionCompat? = null
    private var mediaSessionConnector: MediaSessionConnector? = null

    /*private val _playerStatusLiveData = MutableLiveData<PlayerStatus>()
    val playerStatusLiveData: LiveData<PlayerStatus>
        get() = _playerStatusLiveData*/
    private val _playerStatusLiveData = mutableStateOf(PlayerStatus())
    val playerStatusLiveData: State<PlayerStatus> = _playerStatusLiveData


    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayer.Builder(this).build()
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
            .build()
        exoPlayer.setAudioAttributes(audioAttributes, true)

        // Monitor ExoPlayer events.
        exoPlayer.addListener(PlayerEventListener())


        // Setup notification and media session.
        playerNotificationManager = PlayerNotificationManager.Builder(
            applicationContext,
            PLAYBACK_CHANNEL_ID,
            createNotificationChannel("radio_scope1","player channel"))
            .setNotificationListener(object :PlayerNotificationManager.NotificationListener{
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    super.onNotificationPosted(notificationId, notification, ongoing)
                    if (ongoing){
                        startForeground(notificationId,notification)
                    }
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    super.onNotificationCancelled(notificationId, dismissedByUser)
                    stopSelf()
                    _playerStatusLiveData.value = PlayerStatus(isCancelled = true)
                }
            })
            .setChannelImportance(NotificationUtil.IMPORTANCE_HIGH)
            .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return radioTitle ?: "..."
                }

                @Nullable
                override fun createCurrentContentIntent(player: Player): PendingIntent? = PendingIntent.getActivity(
                    applicationContext,
                    0,
                    Intent(applicationContext, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )

                @Nullable
                override fun getCurrentContentText(player: Player): String? {
                    return null
                }

                @Nullable
                override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
                    return getBitmapFromVectorDrawable(applicationContext, R.mipmap.ic_logo)
                }
            })
            .build()
        // Omit skip previous and next actions.
        playerNotificationManager?.setUsePlayPauseActions(true)

        // Add stop action.
        playerNotificationManager?.setUseStopAction(true)

        playerNotificationManager?.setPlayer(exoPlayer)


        // Show lock screen controls and let apps like Google assistant manager playback.
        mediaSession = MediaSessionCompat(applicationContext, MEDIA_SESSION_TAG).apply {
            isActive = true
        }
        mediaSession?.let {
            playerNotificationManager?.setMediaSessionToken(it.sessionToken)

            mediaSessionConnector = MediaSessionConnector(it).apply {
                setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
                    override fun getMediaDescription(
                        player: Player,
                        windowIndex: Int
                    ): MediaDescriptionCompat {
                        val bitmap =
                            getBitmapFromVectorDrawable(applicationContext, R.mipmap.ic_logo)
                        val extras = Bundle().apply {
                            putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
                            putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap)
                        }

                        val title = radioTitle ?: "..."

                        return MediaDescriptionCompat.Builder()
                            .setIconBitmap(bitmap)
                            .setTitle(title)
                            .setExtras(extras)
                            .build()
                    }
                })

                setPlayer(exoPlayer)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)


        //if(radioId ==null) {  handleIntent(intent)}
        handleIntent(intent)
        return AudioServiceBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //if(radioId ==null) {  handleIntent(intent)}
        handleIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        cancelPlaybackMonitor()

        mediaSession?.release()
        mediaSessionConnector?.setPlayer(null)
        playerNotificationManager?.setPlayer(null)

        exoPlayer.release()

        super.onDestroy()
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.Blue.hashCode()
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    @MainThread
    private fun handleIntent(intent: Intent?) {
        // Play
        intent?.let {
            intent.getParcelableExtra<Uri>(ARG_URL)?.also { uri ->
                radioId = intent.getStringExtra(ARG_RADIO_ID)
                radioTitle = intent.getStringExtra(ARG_TITLE)
                radioUrl = uri
                if(intent.getStringExtra("command").equals("pause"))
                    pause()
                else
                    play(uri)
            } ?: Log.d("error","uri not loded")
        }
    }

    @MainThread
    fun play(uri: Uri) {
        val userAgent = Util.getUserAgent(applicationContext, APPLICATION_ID)
        val mediaSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(uri))

        exoPlayer.prepare(mediaSource, false, false)
        _playerStatusLiveData.value = PlayerStatus(isPlaying = true, radioId = radioId!!)
        exoPlayer.playWhenReady = true
    }

    @MainThread
    fun resume() {
        exoPlayer.playWhenReady = true
        exoPlayer.play()
        _playerStatusLiveData.value = PlayerStatus(isPlaying = true, radioId = radioId!!)
    }

    @MainThread
    fun pause() {
        exoPlayer.playWhenReady = false
        exoPlayer.stop()
        _playerStatusLiveData.value = PlayerStatus(isPaused = true, radioId = radioId!!)

    }

    @MainThread
    fun changePlaybackSpeed(playbackSpeed: Float) {
        exoPlayer.playbackParameters = PlaybackParameters(playbackSpeed)
    }

    /*@MainThread
    private fun saveLastListeningPosition() = lifecycleScope.launch {
        radioId?.let { appDatabase.listenedDao().insert(Listened(it, exoPlayer.contentPosition, exoPlayer.duration)) }
    }*/

    @MainThread
    private fun monitorPlaybackProgress() {
        if (playbackTimer == null) {
            playbackTimer = Timer()

            playbackTimer?.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {

                        lifecycleScope.launch {
                            withContext(Dispatchers.Main) {
                                if (exoPlayer.duration - exoPlayer.contentPosition <= PLAYBACK_TIMER_DELAY) {
                                    playbackTimer?.cancel()
                                }
                            }
                        }
                    }
                },
                PLAYBACK_TIMER_DELAY,
                PLAYBACK_TIMER_DELAY)
        }
    }

    @MainThread
    private fun cancelPlaybackMonitor() {

        playbackTimer?.cancel()
        playbackTimer = null
    }

    @MainThread
    private fun getBitmapFromVectorDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap? {
        return ContextCompat.getDrawable(context, drawableId)?.let {
            val drawable = DrawableCompat.wrap(it).mutate()

            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            bitmap
        }
    }

    private inner class PlayerEventListener : Player.Listener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                if (exoPlayer.playWhenReady) {
                    radioId?.let { _playerStatusLiveData.value = PlayerStatus(isPlaying = true, radioId = it) }
                } else {// Paused
                    radioId?.let { _playerStatusLiveData.value = PlayerStatus(isPaused = true, radioId = it) }
                }
            } else if (playbackState == Player.STATE_ENDED) {
                radioId?.let { _playerStatusLiveData.value = PlayerStatus(isEnded = true, radioId = it) }
            } else {
                radioId?.let { _playerStatusLiveData.value = PlayerStatus(isPlaying = true, radioId = it) }
            }

            // Only monitor playback to record progress when playing.
            if (playbackState == Player.STATE_READY && exoPlayer.playWhenReady) {
                monitorPlaybackProgress()
            } else {
                cancelPlaybackMonitor()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            radioId?.let { _playerStatusLiveData.value = PlayerStatus(error = error.message ?:"unknown error", radioId = it) }
        }

    }

}