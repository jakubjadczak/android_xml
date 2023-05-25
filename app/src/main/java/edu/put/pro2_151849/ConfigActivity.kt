package edu.put.pro2_151849

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
    }

    fun getUsername(v: View){
        val username: String = findViewById<TextView>(R.id.username).text.toString()
        val text = findViewById<TextView>(R.id.textView)
        text.text = username
        print(username)
    }
}