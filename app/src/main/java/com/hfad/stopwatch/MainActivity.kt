package com.hfad.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    lateinit var stopwatch:Chronometer //Stopwatch, not initialized on creation,
                                        // initialized on start button press
    var running = false //is running or not
    var offset : Long = 0 //base offset for stopwatch to account for pause and restart action



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get stopwatch reference, views don't exist before setContentView
        stopwatch = findViewById(R.id.stopwatch)

        //Start button starts stopwatch if it's not running
        val startButton = findViewById<Button>(R.id.start)
        startButton.setOnClickListener {
            if(!running){
                setBaseTime()//start @ 0
                stopwatch.start()
                running = true
            }
        }

        //Pause stopwatch if it's running
        val pauseButton = findViewById<Button>(R.id.pause)
        pauseButton.setOnClickListener {
            if(running){
                saveOffset()//saves how much time has elapsed
                stopwatch.stop()
                running = false
            }
        }

        //Sets offset and stopwatch to 0
        val resetButton = findViewById<Button>(R.id.reset)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }

    //Updates the stopwatch.base time, allowing for any offset
    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
}

