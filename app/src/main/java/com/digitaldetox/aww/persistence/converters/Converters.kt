package com.digitaldetox.aww.persistence.converters

import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.io.IOException
import java.io.StringReader
import java.io.StringWriter
import java.util.*

class Converters {

    private val TAG = "lgx_Converters"

    @TypeConverter
    fun listStringToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToListString(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()


    @TypeConverter
    fun fromStringSet(strings: HashSet<String?>?): String? {
        if (strings == null) {
            return null
        }
        val result = StringWriter()
        val json = JsonWriter(result)
        try {
            json.beginArray()
            for (s in strings) {
                json.value(s)
            }
            json.endArray()
            json.close()
        } catch (e: IOException) {
            Log.d(TAG, "Exception creating JSON", e)
        }
        return result.toString()
    }

    @TypeConverter
    fun toStringSet(strings: String?): HashSet<String>? {
        if (strings == null) {
            return null
        }
        val reader = StringReader(strings)
        val json = JsonReader(reader)
        val result: HashSet<String> = HashSet()
        try {
            json.beginArray()
            while (json.hasNext()) {
                result.add(json.nextString())
            }
            json.endArray()
        } catch (e: IOException) {
            Log.d(TAG, "Exception parsing JSON", e)
        }
        return result
    }


}
