package isel.dam.tutorial.colorapp

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.os.SystemClock

abstract class RepeatableTimer(initialDelay: Long, repeatableDelay: Long) {

    private val MSG = 1
    private var mCancelled = false
    private var initialDelay: Long = 0
    private var repeatableDelay: Long = 0
    private var currentTime: Long = 0

    init {
        this.initialDelay = initialDelay
        this.repeatableDelay = repeatableDelay
    }

    abstract fun onTick()

    @Synchronized
    fun cancel() {
        mCancelled = true
        handler.removeMessages(MSG)
    }

    @Synchronized
    fun start(): RepeatableTimer? {
        mCancelled = false
        currentTime = SystemClock.elapsedRealtime()
        handler.sendMessage(handler.obtainMessage(MSG))
        return this
    }

    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            if (mCancelled) return

            if (SystemClock.elapsedRealtime() > currentTime + initialDelay) onTick()

            sendEmptyMessageDelayed(MSG, repeatableDelay)

        }
    }
}
