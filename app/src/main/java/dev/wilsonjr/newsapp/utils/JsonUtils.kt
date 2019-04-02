package dev.wilsonjr.newsapp.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Modifier
import java.util.*

object JsonUtils {

    private var gson: Gson? = null

    fun getGson(): Gson {

        return gson?:let {
            val gsonBuilder = GsonBuilder()
                .excludeFieldsWithModifiers(
                    Modifier.TRANSIENT,
                    Modifier.STATIC
                )
            gson = gsonBuilder
                .create()
            return gson!!
        }
    }

    fun <T> toJson(bodyObject: T): String {
        return getGson().toJson(bodyObject)
    }
}
