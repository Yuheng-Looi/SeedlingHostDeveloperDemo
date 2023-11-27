package com.seedling.demo.hostdeveloperdemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.oplus.pantanal.seedling.SeedlingCardWidgetProvider
import com.oplus.pantanal.seedling.bean.SeedlingCard
import com.oplus.pantanal.seedling.update.SeedlingCardOptions
import com.oplus.pantanal.seedling.util.SeedlingTool
import org.json.JSONObject

class CardLifeCycleProvider : SeedlingCardWidgetProvider() {
    companion object {
        private const val TAG = "CardLifeCycleProvider"
    }

    override fun onCardCreate(context: Context, card: SeedlingCard) {
        Log.d(TAG, "onCardCreate: card = $card")
        SharedPreferencesUtil.getInstance(context).saveSeedlingCard(card)
    }

    override fun onCardObserve(context: Context, cards: List<SeedlingCard>) {
        Log.d(TAG, "onCardObserve: cards = $cards")
    }

    override fun onDestroy(context: Context, card: SeedlingCard) {
        Log.d(TAG, "onDestroy: card = $card")
        SharedPreferencesUtil.getInstance(context).removeSeedlingCard(card)
    }

    override fun onHide(context: Context, card: SeedlingCard) {
        Log.d(TAG, "onHide: card = $card")

    }

    override fun onShow(context: Context, card: SeedlingCard) {
        Log.d(TAG, "onShow: card = $card")

    }

    override fun onSubscribed(context: Context, card: SeedlingCard) {
        Log.d(TAG, "onSubscribed: card = $card")

    }

    override fun onUnSubscribed(context: Context, card: SeedlingCard) {
        Log.d(TAG, "onUnSubscribed: card = $card")

    }

    override fun onUpdateData(context: Context, card: SeedlingCard, data: Bundle) {
        Log.d(TAG, "onUpdateData: card = $card")
        val initData = data.getString("data", null)?.let { JSONObject(it) }
        val cardOptions = SeedlingCardOptions(
            isMilestone = true,
            grade = SeedlingCardOptions.GRADE_4
        )
        SeedlingTool.updateData(card, initData, cardOptions)
    }
}