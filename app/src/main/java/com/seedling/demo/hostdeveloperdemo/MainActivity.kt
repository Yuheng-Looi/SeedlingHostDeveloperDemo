package com.seedling.demo.hostdeveloperdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.oplus.pantanal.seedling.bean.SeedlingCard
import com.oplus.pantanal.seedling.bean.SeedlingIntent
import com.oplus.pantanal.seedling.bean.SeedlingIntentFlagEnum
import com.oplus.pantanal.seedling.intent.IIntentResultCallBack
import com.oplus.pantanal.seedling.util.SeedlingTool
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SeedlingTool.registerResultCallBack(this, arrayOf("pantanal.intent.business.app.system.HOST_CARD_DEMO"))
//        initViewClick()
    }

//    private fun initViewClick() {
//        findViewById<Button>(R.id.btn_start_intent).setOnClickListener {
//            SeedlingTool.sendSeedling(
//                this,
//                SeedlingIntent(
//                    action = "pantanal.intent.business.app.system.HOST_CARD_DEMO",
//                    flag = SeedlingIntentFlagEnum.START
//                ),
//                callBack = object :IIntentResultCallBack{
//                    override fun onIntentResult(action: String, flag: Int, isSuccess: Boolean) {
//                        //决策是否成功的回调，不代表卡片是否显示
//                        Log.e(TAG,"isSuccess:${isSuccess}  action:${action}")
//                    }
//                }
//            )
//        }
//        findViewById<Button>(R.id.btn_end_intent).setOnClickListener {
//            SeedlingTool.sendSeedling(
//                this,
//                SeedlingIntent(
//                    action = "pantanal.intent.business.app.system.HOST_CARD_DEMO",
//                    flag = SeedlingIntentFlagEnum.END
//                )
//            )
//        }
//        findViewById<Button>(R.id.btn_update_data).setOnClickListener {
//            val cards = SharedPreferencesUtil.getInstance(this).getSeedlingCards("268452000")
//            var currentCard:SeedlingCard?=null
//            if (!cards.isNullOrEmpty()){
//                Log.d(TAG, "initViewClick: currentCard = ${cards[cards.size-1]},cards = $cards")
//                currentCard = SeedlingCard.build(cards[cards.size-1])
//            }
//            currentCard?.let { it1 ->
//                SeedlingTool.updateAllCardData(
//                    it1,
//                    businessData = JSONObject("{\"defaultText\":\"新的更新数据\"}")
//                )
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        SeedlingTool.unRegisterResultCallBack(this)
    }
}