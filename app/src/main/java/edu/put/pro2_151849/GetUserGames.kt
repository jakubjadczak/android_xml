package edu.put.pro2_151849

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
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

class GetUserGames: AppCompatActivity() {

    var gamesList:MutableList<Games>? = null


    fun newGame(name: String, yearPublished: String, image: String, thumbnail: String){
        val dbHandler = DBHandler(this, null, null, 1)
        val game = GamesDB(name, yearPublished, image, thumbnail)

        dbHandler.addGame(game)
//        Toast.makeText(this, "Gra dodana!", Toast.LENGTH_SHORT).show()
    }

    fun deleteAll(){
        Log.d("Delete", "delete all")
        val dbHandler = DBHandler(this, null, null, 1)
        dbHandler.deleteAll()
    }


    fun downloadFile(){
        val dbHandler = DBHandler(this, null, null, 1)
        val username: String = dbHandler.getUserName()
        val urlString = "https://www.boardgamegeek.com/xmlapi2/collection?username=$username"
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

    fun saveGames(games:List<Games>) {
        val rows = games.count()


        // -1 oznacza nagłówek
        for (i in -1..rows - 1) {
            var row: Games? = null

            if (i < 0) {
                //nagłówek
            } else {
                row = games.get(i)
            }

            if (row != null) {
                newGame(row.name, row.yearPublished, row.image, row.thumbnail)
            }
        }
    }

    fun syncData() {
        deleteAll()
        Log.d("Sync", "delete")
//        downloadFile()
//        loadData()
//        saveGames(gamesList!!)
    }
}