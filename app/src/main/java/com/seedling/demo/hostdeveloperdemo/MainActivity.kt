package com.seedling.demo.hostdeveloperdemo

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.NdefFormatable
import android.nfc.tech.NfcA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.oplus.pantanal.seedling.bean.SeedlingIntent
import com.oplus.pantanal.seedling.bean.SeedlingIntentFlagEnum
import com.oplus.pantanal.seedling.intent.IIntentResultCallBack
import com.oplus.pantanal.seedling.util.SeedlingTool

class MainActivity : AppCompatActivity(), CardSelectionListener {

    companion object{
        const val TAG = "MainActivity"
        var cardSelectedPosition = 0
        lateinit var cardList: CardList
    }

    private lateinit var dialog: BottomSheetDialog
    private lateinit var cardAdaptor: CardSelectionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var card: Card



    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private lateinit var intentFilters: Array<IntentFilter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardList = CardList()
        cardList.initCardList()
        updateCardInfo()

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC Adapter not available", Toast.LENGTH_SHORT).show()
        }

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )

        intentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))

        SeedlingTool.registerResultCallBack(this, arrayOf("pantanal.intent.business.app.system.HOST_CARD_DEMO"))
        initViewClick()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        Toast.makeText(this, "NewIntent: ${intent?.action}", Toast.LENGTH_LONG).show()
        if (intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            Log.d(TAG, "nfc TAG detected")
            // Extract data from the intent, if needed
            // For example, you might want to read data from an NFC tag

            // Update the description
            updateTvDescription()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
    }

    private fun updateCardInfo() {
        Log.d(TAG, "update card info")

        //TODO: ACCESS THE CARD SELECTED - val card = ??
        card = cardList.cards.get(cardSelectedPosition)
        Toast.makeText(this, "cardSelectedPosition: $cardSelectedPosition", Toast.LENGTH_SHORT).show()
        findViewById<TextView>(R.id.bank_info).text = "${card.cardNumber}\n${card.cardHolderName}"
    }

    @SuppressLint("SetTextI18n")
    private fun updateTvDescription() {
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        tvDescription.text = "Pay Successful!"
        Toast.makeText(this, "Pay Successful!", Toast.LENGTH_SHORT).show()
        val ivCircle = findViewById<ImageView>(R.id.ivTick)
        ivCircle.visibility = View.VISIBLE
    }

    private fun resetTvDescription() {
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        tvDescription.text = getString(R.string.startDetect)
        val ivCircle = findViewById<ImageView>(R.id.ivTick)
        ivCircle.visibility = View.GONE
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

        findViewById<ImageButton>(R.id.ibSwapCard).setOnClickListener {
            val bottomSheet = CardSelectionBottomSheet()
            bottomSheet.setCardSelectionListener(this)
            bottomSheet.setCardList(cardList)
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        findViewById<ImageView>(R.id.ivBankCard).setOnClickListener {
            resetTvDescription()
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

    override fun onCardSelected(selectedCard: Card) {
        updateCardInfo()
    }

}