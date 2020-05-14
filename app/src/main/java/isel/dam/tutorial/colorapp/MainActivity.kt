package isel.dam.tutorial.colorapp

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.MotionEvent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var currentRedValue: Int = 0
    private var currentGreenValue: Int = 0
    private var currentBlueValue: Int = 0
    private var intervalTimer: Long = 0
    private var counterTime: Long = 0
    private var initialDelay: Long = 0

    private var timestamp = SystemClock.elapsedRealtime()

    private lateinit var greenAddTimer: CountDownTimer
    private lateinit var greenSubTimer: CountDownTimer
    private lateinit var blueAddTimer: RepeatableTimer
    private lateinit var blueSubTimer: RepeatableTimer

    // Red Component
    private val redLayout: LinearLayout
        get() = findViewById(R.id.RedLayout)
    private val redAdd: Button
        get() = findViewById(R.id.RedInc)
    private val redSub: Button
        get() = findViewById(R.id.RedDec)
    private lateinit var redValueButton: Button

    // Green Component
    private val greenAdd: Button
        get() = findViewById(R.id.GreenAdd)
    private val greenSub: Button
        get() = findViewById(R.id.GreenSub)
    private lateinit var greenValueButton: Button

    // Blue Component
    private val blueAdd: Button
        get() = findViewById(R.id.BlueInc)
    private val blueSub: Button
        get() = findViewById(R.id.BlueDec)
    private lateinit var blueValueButton: Button

    // Result Component
    private lateinit var resultText: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redValueButton = findViewById(R.id.RedValue)
        greenValueButton = findViewById(R.id.GreenValue)
        blueValueButton = findViewById(R.id.BlueValue)
        resultText = findViewById(R.id.ResultValue)

        currentRedValue = resources.getInteger(R.integer.default_value)
        currentGreenValue = resources.getInteger(R.integer.default_value)
        currentBlueValue = resources.getInteger(R.integer.default_value)

        counterTime = resources.getInteger(R.integer.timer).toLong()
        intervalTimer = resources.getInteger(R.integer.intervalTimer).toLong()
        initialDelay = resources.getInteger(R.integer.initialDelay).toLong()

        setControlLayout()

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            redLayout.viewTreeObserver.addOnGlobalLayoutListener { landscapeAdjust() }
        }

        redAdd.setOnClickListener {
            setRedBackground(if (currentRedValue + 1 > 255) currentRedValue else ++currentRedValue)
            setRedValueText()
            setResultText()
        }

        redAdd.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    if (SystemClock.elapsedRealtime() >= timestamp + counterTime){
                        v.performClick()
                        timestamp = SystemClock.elapsedRealtime()
                    }
                }
            }
            true
        }

        redSub.setOnClickListener {
            setRedBackground(if (currentRedValue - 1 < 0) currentRedValue else --currentRedValue)
            setRedValueText()
            setResultText()
        }

        redSub.setOnTouchListener { v, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    if (SystemClock.elapsedRealtime() >= timestamp + counterTime){
                        v.performClick()
                        timestamp = SystemClock.elapsedRealtime()
                    }
                }
            }
            true
        }

        greenAdd.setOnClickListener {
            setGreenBackground(if (currentGreenValue + 1 > 255) currentGreenValue else ++currentGreenValue)
            setGreenValueText()
            setResultText()
        }

        greenAdd.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> greenAddTimer.start()
                MotionEvent.ACTION_UP -> greenAddTimer.cancel()
            }
            true
        }

        greenSub.setOnClickListener {
            setGreenBackground(if (currentGreenValue - 1 < 0) currentGreenValue else --currentGreenValue)
            setGreenValueText()
            setResultText()
        }

        greenSub.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> greenSubTimer.start()
                MotionEvent.ACTION_UP -> greenSubTimer.cancel()
            }
            true
        }

        blueAdd.setOnClickListener {
            setBlueBackground(if (currentBlueValue + 1 > 255) currentBlueValue else ++currentBlueValue)
            setBlueValueText()
            setResultText()
        }

        blueAdd.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> blueAddTimer.start()
                MotionEvent.ACTION_UP -> blueAddTimer.cancel()
            }
            true
        }

        blueSub.setOnClickListener {
            setBlueBackground(if (currentBlueValue - 1 < 0) currentBlueValue else --currentBlueValue)
            setBlueValueText()
            setResultText()

        }

        blueSub.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> blueSubTimer.start()
                MotionEvent.ACTION_UP -> blueSubTimer.cancel()
            }
            true
        }
    }

    private fun setGreenSubTimer() {
        greenSubTimer = object : CountDownTimer(intervalTimer, counterTime) {
            override fun onTick(millisUntilFinished: Long) {
                greenSub.performClick()
            }

            override fun onFinish() {}
        }
    }

    private fun setGreenAddTimer() {
        greenAddTimer = object : CountDownTimer(intervalTimer, counterTime) {
            override fun onTick(millisUntilFinished: Long) {
                greenAdd.performClick()
            }

            override fun onFinish() {}
        }
    }

    private fun setBlueSubTimer() {
        blueSubTimer = object : RepeatableTimer(intervalTimer, counterTime) {
            override fun onTick() {
                blueSub.performClick()
            }
        }
    }

    private fun setBlueAddTimer() {
        blueAddTimer = object : RepeatableTimer(intervalTimer, counterTime) {
            override fun onTick() {
                blueAdd.performClick()
            }
        }
    }

    private fun setControlLayout() {
        setRedValueText()
        setGreenValueText()
        setBlueValueText()
        setRedBackground()
        setGreenBackground()
        setBlueBackground()
        setResultText()
        setGreenAddTimer()
        setGreenSubTimer()
        setBlueAddTimer()
        setBlueSubTimer()
    }

    private fun setRedValueText() {
        redValueButton.text = currentRedValue.toString()
    }

    private fun setGreenValueText() {
        greenValueButton.text = currentGreenValue.toString()
    }

    private fun setBlueValueText() {
        blueValueButton.text = currentBlueValue.toString()
    }

    private fun setRedBackground(value: Int = currentRedValue) =
        redValueButton.setBackgroundColor(Color.argb(255, value, 0, 0))

    private fun setGreenBackground(value: Int = currentGreenValue) =
        greenValueButton.setBackgroundColor(
            Color.argb(255, 0, value, 0)
        )

    private fun setBlueBackground(value: Int = currentBlueValue) =
        blueValueButton.setBackgroundColor(Color.argb(255, 0, 0, value))

    private fun setResultText() {
        resultText.text =
            getString(R.string.result_text, currentRedValue, currentGreenValue, currentBlueValue)
        resultText.setBackgroundColor(
            Color.argb(
                255,
                currentRedValue,
                currentGreenValue,
                currentBlueValue
            )
        )
    }

    private fun landscapeAdjust() {
        val heightNeeded = redLayout.height + resources.getDimension(R.dimen.ContentMargin) * 2
        val mainLayout: LinearLayout = findViewById(R.id.MainLayout)

        if (mainLayout.height <= heightNeeded * 3) {
            val controlLayout: LinearLayout = findViewById(R.id.ControlLayout)
            val blueLayout: LinearLayout = findViewById(R.id.BlueLayout)
            controlLayout.removeView(blueLayout)
            val resultLayout: LinearLayout = findViewById(R.id.ResultLayout)
            val resultValue: TextView = findViewById(R.id.ResultValue)
            resultLayout.removeAllViews()
            resultLayout.addView(blueLayout)

            resultValue.layoutParams.height = mainLayout.height - blueLayout.height

            resultLayout.addView(resultValue)
            mainLayout.removeAllViews()
            mainLayout.addView(controlLayout)
            mainLayout.addView(resultLayout)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("RedValue", currentRedValue)
        outState.putInt("GreenValue", currentGreenValue)
        outState.putInt("BlueValue", currentBlueValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentRedValue = savedInstanceState.getInt("RedValue")
        currentGreenValue = savedInstanceState.getInt("GreenValue")
        currentBlueValue = savedInstanceState.getInt("BlueValue")
        setControlLayout()
    }
}
