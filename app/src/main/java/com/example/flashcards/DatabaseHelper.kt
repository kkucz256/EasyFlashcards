import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_DECKS_TABLE)
        db.execSQL(SQL_CREATE_CARDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_DECKS_TABLE)
        db.execSQL(SQL_DELETE_CARDS_TABLE)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Flashcards.db"
        private const val SQL_CREATE_DECKS_TABLE = "CREATE TABLE Decks (" +
                "deck_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT)"
        private const val SQL_CREATE_CARDS_TABLE = "CREATE TABLE Cards (" +
                "card_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "deck_id INTEGER," +
                "front TEXT," +
                "back TEXT," +
                "FOREIGN KEY(deck_id) REFERENCES Decks(deck_id))"
        private const val SQL_DELETE_DECKS_TABLE = "DROP TABLE IF EXISTS Decks"
        private const val SQL_DELETE_CARDS_TABLE = "DROP TABLE IF EXISTS Cards"
    }
}
