package isel.dam.tutorial.colorapp

import android.os.Handler
import android.os.Message
import android.os.SystemClock

open abstract class RepeatableTimer(millisInFuture: Long, countDownInterval: Long) {

    private val initialDelay = 0
    private val repeatableDelay: Long = 0
    private val MSG = 1
    private var mCancelled = false
    private var mMillisInFuture: Long = 0
    private var mStopTimeInFuture: Long = 0
    private var mCountdownInterval: Long = 0

    open fun RepeatableTimer(
        millisInFuture: Long,
        countDownInterval: Long
    ) {
        mMillisInFuture = millisInFuture
        mCountdownInterval = countDownInterval
    }


    abstract fun onTick(millisUntilFinished: Long)
    abstract fun onFinish()


    @Synchronized
    fun cancel() {
        mCancelled = true
        handler.removeMessages(MSG)
    }

    @Synchronized
    fun start(): RepeatableTimer? {
        mCancelled = false
        if (mMillisInFuture <= 0) {
            onFinish()
            return this
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture
        handler.sendMessage(handler.obtainMessage(MSG))
        return this
    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            synchronized(this@RepeatableTimer) {
                if (mCancelled) {
                    return
                }
                val millisLeft =
                    mStopTimeInFuture - SystemClock.elapsedRealtime()
                if (millisLeft <= 0) {
                    onFinish()
                } else {
                    val lastTickStart = SystemClock.elapsedRealtime()
                    onTick(millisLeft)

                    // take into account user's onTick taking time to execute
                    val lastTickDuration =
                        SystemClock.elapsedRealtime() - lastTickStart
                    var delay: Long
                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) delay = 0
                    } else {
                        delay = mCountdownInterval - lastTickDuration

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval
                    }
                    sendMessageDelayed(obtainMessage(MSG), delay)
                }
            }
        }
    }
}