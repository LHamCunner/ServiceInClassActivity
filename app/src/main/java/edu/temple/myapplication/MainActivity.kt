package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    val handler = Handler(Looper.getMainLooper()){


        true
    }

    lateinit var timerBinder: TimerService.TimerBinder
    var isConnected = false
    val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            isConnected = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindService(Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE)

        var startNumber = findViewById<EditText>(R.id.editTextText)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if(isConnected) timerBinder.start((startNumber.text).toString().toInt(), handler)
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            if(isConnected) timerBinder.pause()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if(isConnected) timerBinder.stop()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var startNumber = findViewById<EditText>(R.id.editTextText)
        when(item.itemId){
            R.id.action_Start ->  if(isConnected) timerBinder.start((startNumber.text).toString().toInt(), handler)
            R.id.activity_Pause -> if(isConnected) timerBinder.pause()
            R.id.activity_Stop -> if(isConnected) timerBinder.stop()
        }

        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy(){
        super.onDestroy()

        unbindService(serviceConnection)
    }
}