package edu.put.pro2_151849

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
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

class GamesActivity : AppCompatActivity() {
    var gamesList:MutableList<Games>? = null

    private lateinit var tableGames:TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        tableGames = findViewById(R.id.gamesTable)
        loadData()
        showData()
        downloadFile()
    }

    fun downloadFile(){
        val urlString = "https://www.boardgamegeek.com/xmlapi2/collection?username=loutre_on_fire"
        val xmlDirectory = File("$filesDir/XML")
        if (!xmlDirectory.exists()) xmlDirectory.mkdir()
        var fileName = "$xmlDirectory/gry.xml"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(urlString)
                val reader = url.openStream().bufferedReader()
                val downloadFile = File(fileName).also { it.createNewFile() }
                val writer = FileWriter(downloadFile).buffered()
                var line: String
                while (reader.readLine().also { line = it?.toString() ?:""} != null)
                    writer.write(line)
                reader.close()
                writer.close()

                withContext(Dispatchers.Main){
                    loadData()
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main) {
                    when (e){
                        is MalformedURLException ->
                            print("Malformed URL")
                        else ->
                            print("Error")
                    }
                    val incompleteFile = File(fileName)
                    if (incompleteFile.exists()) incompleteFile.delete()
                }
            }
        }
    }

    fun loadData() {
        gamesList = mutableListOf()

        val filename = "gry.xml"
        val path = filesDir
        val inDir = File(path,"XML")

        if (inDir.exists()) {
            val file = File(inDir,filename)
            if (file.exists()) {
                val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)

                xmlDoc.documentElement.normalize()

                val items: NodeList = xmlDoc.getElementsByTagName("item")

                for (i in 0..items.length-1) {
                    val itemNode: Node = items.item(i)
                    if (itemNode.nodeType == Node.ELEMENT_NODE) {
                        val elem = itemNode as Element
                        val children = elem.childNodes

                        var gamesListCurrent:MutableList<Games>? = null
                        var currentName: String? = null
                        var currentYearPublished: String? = null
                        var currentImage: String? = null
                        var currentThumbnail: String? = null

                        gamesListCurrent = gamesList

                        for (j in 0..children.length-1) {
                            val node = children.item(j)
                            if (node is Element) {
                                when (node.nodeName) {
                                    "name" -> {
                                        currentName = node.textContent
                                    }
                                    "yearpublished" -> {
                                        currentYearPublished = node.textContent
                                    }
                                    "image" -> {
                                        currentImage = node.textContent
                                    }
                                    "thumbnail" -> {
                                        currentThumbnail = node.textContent
                                    }
                                }
                            }
                        }

                        if (currentName!=null && currentYearPublished!=null && currentImage!=null && currentThumbnail!=null) {
                            val c=Games(currentName, currentYearPublished, currentImage, currentThumbnail)
                            gamesListCurrent?.add(c)
                        }

                    }

                }
            }
        }
    }

    fun showGames(games:List<Games>) {
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
        val rows = games.count()
        supportActionBar!!.setTitle("Gry")
        var textSpacer: TextView? = null


        // -1 oznacza nagłówek
        for (i in -1..rows - 1) {
            var row: Games? = null

            if (i < 0) {
                //nagłówek
                textSpacer = TextView(this)
                textSpacer.text = ""
            } else {
                row = games.get(i)
            }


            val tv = TextView(this)
            tv.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
            tv.gravity = Gravity.LEFT
            tv.setPadding(20, 15, 20, 15)
            tv.setText(row?.name)
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())

            val tv2 = TextView(this)
            tv2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT)
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mediumTextSize.toFloat())
            tv2.gravity = Gravity.LEFT
            tv2.setPadding(20, 15, 20, 15)
            tv2.setText(row?.yearPublished.toString())

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
            tv3.setText(row?.thumbnail)
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

    fun showData() {
        showGames(gamesList!!)
    }

    fun refreshClick(v: View){
        downloadFile()
    }

}