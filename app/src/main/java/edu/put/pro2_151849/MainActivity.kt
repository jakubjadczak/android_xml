package edu.put.pro2_151849

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showGamesList(){
        val i = Intent(this, GamesActivity::class.java)
        startActivity(i)
    }

    fun showConfigActivity(){
        val i = Intent(this, ConfigActivity::class.java)
        startActivity(i)
    }

    fun showSyncActivity(){
        val i = Intent(this, SyncActivity::class.java)
        startActivity(i)
    }

    fun GamesListClick(v: View){
        showGamesList()
    }

    fun ConfigClick(v: View){
        showConfigActivity()
    }

    fun SyncClick(v: View){
        showSyncActivity()
    }
}