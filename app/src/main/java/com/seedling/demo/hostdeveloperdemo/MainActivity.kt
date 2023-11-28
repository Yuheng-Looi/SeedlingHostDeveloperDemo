package com.seedling.demo.hostdeveloperdemo


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
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
        initViewClick()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTvDescription() {
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        tvDescription.text = "Pay Successful!"
        val ivCircle = findViewById<ImageView>(R.id.ivTick)
        ivCircle.visibility = View.VISIBLE
    }

    private fun initViewClick() {
        findViewById<ImageButton>(R.id.ibScan).setOnClickListener {
            SeedlingTool.sendSeedling(
                this,
                SeedlingIntent(
                    action = "pantanal.intent.business.app.system.HOST_CARD_DEMO",
                    flag = SeedlingIntentFlagEnum.START
                ),
                callBack = object :IIntentResultCallBack{
                    override fun onIntentResult(action: String, flag: Int, isSuccess: Boolean) {
                        //决策是否成功的回调，不代表卡片是否显示
                        Log.e(TAG,"isSuccess:${isSuccess}  action:${action}")
                    }
                }
            )
            updateTvDescription()
        }

        findViewById<ImageButton>(R.id.ibAddCard).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java )
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.ibReport).setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        SeedlingTool.unRegisterResultCallBack(this)
    }
}