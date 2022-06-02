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

    //Keys for bundle(it's a lot like a hashmap)
    val OFFSET_KET = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get stopwatch reference, views don't exist before setContentView
        stopwatch = findViewById(R.id.stopwatch)

        //Restore previous state, if applicable
        if(savedInstanceState != null){
            offset = savedInstanceState.getLong(OFFSET_KET)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if(running){
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }

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

    override fun onStop() {
        //you need to call the super class whenever you override an activity function
        super.onStop()
        saveOffset()
        stopwatch.stop()
    }

    override fun onRestart() {
        super.onRestart()
        if(running){
            setBaseTime()
            stopwatch.start()
            offset = 0
        }

    }

    //Updates the stopwatch.base time, allowing for any offset
    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    override fun onSaveInstanceState(savedState: Bundle) {
        savedState.putLong(OFFSET_KET,offset)
        savedState.putBoolean(RUNNING_KEY,running)
        savedState.putLong(BASE_KEY,stopwatch.base)
        super.onSaveInstanceState(savedState)
    }
}

