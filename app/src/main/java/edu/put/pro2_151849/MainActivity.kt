package edu.put.pro2_151849

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dbHandler = DBHandler(this, null, null, 1)
        val username: String = dbHandler.getUserName()
        if (username == ""){
            showConfigActivity()
        }

        val usernameText = findViewById<TextView>(R.id.logedUsername)
        usernameText.text = dbHandler.getUserName()

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

    fun deleteButton(v: View){
        val dbHandler = DBHandler(this, null, null, 1)
        dbHandler.deleteUser()
        dbHandler.deleteAll()
        exitProcess(0)
    }
}