package edu.put.pro2_151849
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
import android.util.Log


class DBHandler(
    context: Context,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int?
    ): SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){
        companion object {
            private val DATABASE_VERSION = 1
            private val DATABASE_NAME = "gamesDB.db"
            val TABLE_GAMES = "games"
            val COLUMN_ID = "_id"
            val COLUMN_NAME = "name"
            val COLUMN_YEARPUBLISHED = "yearPublished"
            val COLUMN_IMAGE = "image"
            val COLUMN_THUMBNAIL = "thumbnail"

            val TABLE_DATA = "data_table"
            val COLUMN_TYPE = "type"
            val COLUMN_DATA = "data"

        }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("DB", "On create")
        val CREATE_GAMES_TABLE = ("CREATE TABLE " + TABLE_GAMES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_YEARPUBLISHED + " TEXT," + COLUMN_IMAGE + " TEXT," + COLUMN_THUMBNAIL + ")")
        val CREATE_DATA_TABLE = ("CREATE TABLE " + TABLE_DATA + "(" + COLUMN_ID + "INTEGER PRIMARY KEY," + COLUMN_TYPE + " TEXT," + COLUMN_DATA + " TEXT)")
        db.execSQL(CREATE_DATA_TABLE)
        db.execSQL(CREATE_GAMES_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA)
        onCreate(db)
    }

    fun addGame(game: GamesDB){
        val values = ContentValues()
        values.put(COLUMN_NAME, game.name)
        values.put(COLUMN_YEARPUBLISHED, game.yearPublished)
        values.put(COLUMN_IMAGE, game.image)
        values.put(COLUMN_THUMBNAIL, game.thumbnail)
        val db = this.writableDatabase
        db.insert(TABLE_GAMES, null, values)
        db.close()
    }

    fun deleteAll():Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_GAMES"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        db.delete(TABLE_GAMES, null, null)
        db.execSQL("DELETE FROM $TABLE_GAMES")
        result=true

        return result
    }

    fun selectAll(): MutableList<GamesDB>?{

        var gamesList:MutableList<GamesDB>? = mutableListOf()
        var game: GamesDB? = null

        val query = "SELECT * FROM $TABLE_GAMES"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()){
            Log.d("DBHANDLER", "In first")
            val name = cursor.getString(1)
            val yearPublished = cursor.getString(2)
            val image = cursor.getString(3)
            val thumbnail = cursor.getString(4)
            val game = GamesDB(name, yearPublished, image, thumbnail)
            gamesList?.add(game)

            while (cursor.moveToNext()){
                val name = cursor.getString(1)
                val yearPublished = cursor.getString(2)
                val image = cursor.getString(3)
                val thumbnail = cursor.getString(4)
                val game = GamesDB(name, yearPublished, image, thumbnail)
                gamesList?.add(game)
            }
        }
        db.close()
        return gamesList
    }

    fun createTable(){
        Log.d("DB", "create table")
        val db = this.writableDatabase
        val CREATE_DATA_TABLE = ("CREATE TABLE " + TABLE_DATA + "(" + COLUMN_ID + "INTEGER PRIMARY KEY," + COLUMN_TYPE + " TEXT," + COLUMN_DATA + " TEXT)")
        db.execSQL(CREATE_DATA_TABLE)
    }

    fun createUser(username: String){
        val values = ContentValues()
        values.put(COLUMN_TYPE, "user")
        values.put(COLUMN_DATA, username)

        val db = this.writableDatabase
        db.insert(TABLE_DATA, null, values)
        db.close()
    }

    fun getUserName(): String{
        var username: String = ""
        val query = "SELECT * FROM $TABLE_DATA WHERE $COLUMN_TYPE LIKE \"user\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()){
            username = cursor.getString(2)
        }
        db.close()
        return username
    }

    fun deleteUser(): Boolean{
        var result = false
        val query = "SELECT * FROM $TABLE_DATA"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        db.delete(TABLE_GAMES, null, null)
        db.execSQL("DELETE FROM $TABLE_DATA WHERE $COLUMN_TYPE LIKE \"user\"")
        result=true

        return result
    }

    fun createSyncDate(username: String){
        val values = ContentValues()
        values.put(COLUMN_TYPE, "date")
        values.put(COLUMN_DATA, username)

        val db = this.writableDatabase
        db.insert(TABLE_DATA, null, values)
        db.close()
    }

    fun getSyncDate(): String{
        var username: String = ""
        val query = "SELECT * FROM $TABLE_DATA WHERE $COLUMN_TYPE LIKE \"date\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()){
            username = cursor.getString(2)
        }
        db.close()
        return username
    }

    fun deleteSyncDate(): Boolean{
        var result = false
        val query = "SELECT * FROM $TABLE_DATA"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        db.delete(TABLE_GAMES, null, null)
        db.execSQL("DELETE FROM $TABLE_DATA WHERE $COLUMN_TYPE LIKE \"date\"")
        result=true

        return result
    }
}