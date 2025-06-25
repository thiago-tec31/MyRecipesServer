package com.br.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonUtil {
    private fun getGson(): Gson {
        return Gson()
    }
    fun <T> deserialize(clazz: Class<T>, json: String): T {
        return getGson().fromJson(json, clazz)
    }

    fun <T> deserializeList(clazz: Class<T>, json: String): List<T> {
        val type = TypeToken.getParameterized(List::class.java, clazz).type
        return getGson().fromJson(json, type)
    }

    fun <T> serialize(model: T): String {
        return getGson().toJson(model)
    }

    fun <T> serialize(clazz: Class<T>, model: T): String {
        return getGson().toJson(model, clazz)
    }

    fun <T> serializeList(clazz: Class<T>, modelList: List<T>): String {
        val type = TypeToken.getParameterized(List::class.java, clazz).type
        return getGson().toJson(modelList, type)
    }
}