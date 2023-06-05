package edu.put.pro2_151849

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.*
import kotlinx.coroutines.withContext
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory

import android.database.sqlite.SQLiteDatabase

class GamesActivity : AppCompatActivity() {
    var gamesList:MutableList<GamesDB>? = null

    private lateinit var tableGames:TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        tableGames = findViewById(R.id.gamesTable)
        showGames()
    }

    fun showGames() {
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0
        var textSize = 0
        var smallTextSize = 0
        var mediumTextSize = 0

        textSize = resources.getDimension(R.dimen.font_size_verysmall).toInt()
        smallTextSize = resources.getDimension(R.dimen.font_size_small).toInt()
        mediumTextSize = resources.getDimension(R.dimen.font_size_medium).toInt()
        supportActionBar!!.setTitle("Gry")
        var textSpacer: TextView? = null
        var i = 0

        val dbHandler = DBHandler(this, null, null, 1)
        var listOfGame = dbHandler.selectAll()
        Log.d("DATA: ", listOfGame.toString())

        // -1 oznacza nagłówek
        if (listOfGame != null) {
            for (row in listOfGame) {
                val tv = TextView(this)
                tv.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
                tv.gravity = Gravity.LEFT
                tv.setPadding(20, 15, 20, 15)
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"))
                val name = row.name
                val year = row.yearPublished
                val tog = i.toString() + ". " +name + ", " + year
                tv.setText(tog)
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())

                val tv2 = TextView(this)
                tv2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT)
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
                tv2.gravity = Gravity.LEFT
                tv2.setPadding(20, 15, 20, 15)
                tv2.setBackgroundColor(Color.parseColor("#ffffff"))
                tv2.setTextColor(Color.parseColor("#000000"))
                tv2.setText(row.yearPublished.toString())

                val layCustomer = LinearLayout(this)
                layCustomer.orientation = LinearLayout.VERTICAL
                layCustomer.setPadding(20, 10, 20, 10)
                layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"))

                val tv3 = TextView(this)
                tv3.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT)
                tv3.setPadding(5, 0, 0, 5)
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())

                tv3.gravity = Gravity.TOP
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
                tv3.setText(row.thumbnail)

                layCustomer.addView(tv3)


                // add table row
                val tr = TableRow(this)
                tr.id = i + 1
                val trParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT)
                trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)
                tr.setPadding(10, 0, 10, 0)
                tr.layoutParams = trParams

                tr.addView(tv)
                tr.addView(tv2)
                tr.addView(layCustomer)

                tableGames.addView(tr, trParams)
                val trSep = TableRow(this)
                val trParamsSep = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT)
                trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)

                trSep.layoutParams = trParamsSep
                val tvSep = TextView(this)
                val tvSepLay = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT)
                tvSepLay.span = 4
                tvSep.layoutParams = tvSepLay
                tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"))
                tvSep.height = 1

                trSep.addView(tvSep)
                tableGames.addView(trSep, trParamsSep)
                i += 1


            }
        }
        val trDate = TableRow(this)
        val trParamsSep = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT)
        trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin)

        trDate.layoutParams = trParamsSep
        val tvSep = TextView(this)
        val tvSepLay = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.MATCH_PARENT)
        tvSepLay.span = 4
        tvSep.layoutParams = tvSepLay
        tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"))

        tvSep.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())

        trDate.addView(tvSep)
        tableGames.addView(trDate, trParamsSep)
    }

    fun showMainActivity(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    fun MainClick(v: View){
        showMainActivity()
    }

}