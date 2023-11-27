/***********************************************************
 * Copyright (C), 2020-2030, OPLUS Mobile Comm Corp., Ltd.
 * File: -LogBroadcastReceiver.kt
 * Description:
 * Version: 1.0
 * Date : 2022/10/28
 * Author: v-houyuhang1@oppo.com
 * -------------------Revision History: --------------------
 * <author>            <date>      <version>   <desc>
 * v-houyuhang1@oppo.com    2022/10/28   1.0         create
 **********************************************************/
package com.seedling.demo.hostdeveloperdemo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.oplus.pantanal.seedling.bean.SeedlingCard

class SharedPreferencesUtil private constructor() {
    companion object {
        private const val TAG = "SharedPreferencesUtil"

        @Volatile
        private var instance: SharedPreferencesUtil? = null
        private lateinit var preferences: SharedPreferences
        private const val PREFS_NAME = "seedling_card_config"

        fun getInstance(context: Context): SharedPreferencesUtil = instance ?: synchronized(this) {
            instance ?: buildInstance(context).also { instance = it }
        }

        private fun buildInstance(context: Context): SharedPreferencesUtil {
            preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return SharedPreferencesUtil()
        }
    }

    fun getLastUpdateVoice(serviceId: String, updateJson: String): Boolean {
        val lastUpdate = preferences.getString("voice_$serviceId", null)
        if (lastUpdate.isNullOrEmpty()) {
            setLastUpdateVoice("voice_$serviceId", updateJson)
            return true
        } else {
            if (lastUpdate != updateJson) {
                setLastUpdateVoice("voice_$serviceId", updateJson)
                return true
            } else {
                return false
            }
        }
    }

    private fun setLastUpdateVoice(serviceId: String, updateJson: String) {
        preferences.edit().putString(serviceId, updateJson).apply()
    }

    fun saveSeedlingCard(card: SeedlingCard) {
        val oldSeedlingIdString = preferences.getString(card.serviceId, "")
        Log.d(TAG, "saveSeedlingCard, oldSeedlingIdString=$oldSeedlingIdString}")
        val oldSeedlingIdSet = oldSeedlingIdString?.toIdSet()
        val seedlingCardId = card.getSeedlingCardId()
        if (oldSeedlingIdSet!!.contains(seedlingCardId)) {
            return
        }
        val newSeedlingIdString = oldSeedlingIdSet.plus(seedlingCardId).toIdString()
        if (newSeedlingIdString.isNotEmpty()) {
            preferences.edit().putString(card.serviceId, newSeedlingIdString).apply()
        }
        Log.d(TAG, "saveSeedlingCard, newSeedlingIdString=$newSeedlingIdString}")
        preferences.edit().putString(card.serviceId, newSeedlingIdString).apply()
    }

    fun removeSeedlingCard(card: SeedlingCard) {
        val oldSeedlingIdString = preferences.getString(card.serviceId, "")
        Log.d(
            TAG, "removeSeedlingCard, oldSeedlingIdString=$oldSeedlingIdString}"
        )
        val oldSeedlingIdSet = oldSeedlingIdString?.toIdSet()
        val seedlingCardId = card.getSeedlingCardId()
        if (!oldSeedlingIdSet!!.contains(seedlingCardId)) {
            return
        }
        val newSeedlingIdString = oldSeedlingIdSet.minus(seedlingCardId).toIdString()
        Log.d(
            TAG, "removeSeedlingCard, newSeedlingIdString=$newSeedlingIdString}"
        )
        preferences.edit().putString(card.serviceId, newSeedlingIdString).apply()
    }


    fun getSeedlingCards(serviceId: String): List<String>? {
        val seedlingIdString = preferences.getString(serviceId, "")
        return seedlingIdString?.toIdSet()?.toList()
    }

    fun getFirstStartStatus(): Boolean {
        return preferences.getBoolean("isFirst", false)
    }

    fun getOldVersionCode(): Int {
        return preferences.getInt("vCode", 0)
    }

    fun setOldVersionCode(versionCode: Int) {
        preferences.edit().putInt("vCode", versionCode).apply()
    }

    fun setFirstStart(isFirst: Boolean) {
        preferences.edit().putBoolean("isFirst", isFirst).apply()
    }
}


private fun <E> Set<E>.toIdString(): String {
    var result = ""
    for (item in this) {
        result = result + SPLIT_FLAG + item.toString()
    }
    return result
}

private fun String?.toIdSet(): Set<String> {
    return this?.split(SPLIT_FLAG)?.filterNot { isNullOrBlank() }?.toSet() ?: emptySet()
}

const val SPLIT_FLAG = "~"
