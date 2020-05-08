package isel.dam.tutorial.colorapp

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var current_redValue : Int = 0
    private var current_greenValue : Int = 0
    private var current_blueValue : Int = 0
    private var intervalTimer : Long = 0
    private var counterTime : Long = 0

    // Red Component
    private val redAdd: Button
        get() = findViewById(R.id.RedAdd)
    private val redSub: Button
        get() = findViewById(R.id.RedSub)
    private lateinit var redValue: Button

    // Green Component
    private val greenAdd: Button
        get() = findViewById(R.id.GreenAdd)
    private val greenSub: Button
        get() = findViewById(R.id.GreenSub)
    private lateinit var greenValue: Button

    // Blue Component
    private val blueAdd: Button
        get() = findViewById(R.id.BlueAdd)
    private val blueSub: Button
        get() = findViewById(R.id.BlueSub)
    private lateinit var blueValue: Button

    // Result Component
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        current_redValue = resources.getInteger(R.integer.default_value)
        current_greenValue = resources.getInteger(R.integer.default_value)
        current_blueValue = resources.getInteger(R.integer.default_value)

        counterTime = resources.getInteger(R.integer.timer).toLong()
        intervalTimer = resources.getInteger(R.integer.intervalTimer).toLong()

        redValue = findViewById(R.id.RedValue)
        redValue.text = current_redValue.toString()

        greenValue = findViewById(R.id.GreenValue)
        greenValue.text = current_greenValue.toString()

        blueValue = findViewById(R.id.BlueValue)
        blueValue.text = current_blueValue.toString()

        resultText = findViewById(R.id.ResultValue)

        setRedBackground()
        setGreenBackgroud()
        setBlueBackground()
        setResultText()

        redAdd.setOnClickListener {
            setRedBackground(if (current_redValue + 1 > 255) current_redValue else ++current_redValue)
            redValue.text = current_redValue.toString()
            setResultText()
        }
        
        //redAdd.setOnTouchListener()

        redSub.setOnClickListener {
            setRedBackground(if (current_redValue - 1 < 0) current_redValue else --current_redValue)
            redValue.text = current_redValue.toString()
            setResultText()
        }

        greenAdd.setOnClickListener {
            setGreenBackgroud(if (current_greenValue + 1 > 255) current_greenValue else ++current_greenValue)
            greenValue.text = current_greenValue.toString()
            setResultText()
        }

        greenSub.setOnClickListener {
            var counter = object : CountDownTimer(counterTime, intervalTimer) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    start()
                    setGreenBackgroud(if (current_greenValue - 1 < 0) current_greenValue else --current_greenValue)
                    greenValue.text = current_greenValue.toString()
                    setResultText()

                }
            }
            counter.start()
        }

        blueAdd.setOnClickListener {
            setBlueBackground(if (current_blueValue + 1 > 255) current_blueValue else ++current_blueValue)
            blueValue.text = current_blueValue.toString()
            setResultText()
        }

        blueSub.setOnClickListener {
            var counter = object : RepeatableTimer(counterTime, intervalTimer) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    start()
                    setBlueBackground(if (current_blueValue - 1 < 0) current_blueValue else --current_blueValue)
                    blueValue.text = current_blueValue.toString()
                    setResultText()

                }
            }
            counter.start()
        }
    }

    private fun setRedBackground(value: Int = current_redValue) =
        redValue.setBackgroundColor(Color.argb(255, value, 0, 0))

    private fun setGreenBackgroud(value: Int = current_greenValue) =
        greenValue.setBackgroundColor(Color.argb(255, 0, value,0)
    )

    private fun setBlueBackground(value: Int = current_blueValue) =
        blueValue.setBackgroundColor(Color.argb(255, 0, 0, value))

    private fun setResultText() {
        resultText.text = "${current_redValue}, ${current_greenValue}, ${current_blueValue}"
        resultText.setBackgroundColor(Color.argb(255, current_redValue, current_greenValue, current_blueValue))
    }

}
