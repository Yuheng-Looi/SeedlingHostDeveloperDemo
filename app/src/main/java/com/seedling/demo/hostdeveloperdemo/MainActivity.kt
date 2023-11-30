package com.seedling.demo.hostdeveloperdemo

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
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
        lateinit var cardList: List<Card>
    }

    private lateinit var dialog: BottomSheetDialog
    private lateinit var cardAdaptor: CardSelectionAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var card: Card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateCardInfo()
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC Adapter not available", Toast.LENGTH_SHORT).show()
        }

//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
//            PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val intentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED))
//        val techList = arrayOf(arrayOf<String>()) // You can specify the technologies your app support
//        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilters, techList)

        SeedlingTool.registerResultCallBack(this, arrayOf("pantanal.intent.business.app.system.HOST_CARD_DEMO"))
        initViewClick()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNFCIntent(intent)
    }

    private fun updateCardInfo() {
        cardList = getCardList()
        card = cardList[cardSelectedPosition]
        Toast.makeText(this, "cardSelectedPosition: $cardSelectedPosition", Toast.LENGTH_SHORT).show()
        findViewById<TextView>(R.id.bank_info).text = "${card.cardNumber}\n${card.cardHolderName}"
    }

    @SuppressLint("SetTextI18n")
    private fun updateTvDescription() {
        val tvDescription = findViewById<TextView>(R.id.tvDescription)
        tvDescription.text = "Pay Successful!"
        val ivCircle = findViewById<ImageView>(R.id.ivTick)
        ivCircle.visibility = View.VISIBLE
    }

    private fun handleNFCIntent(intent: Intent?) {
        Toast.makeText(this, "reached nfc intent", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, intent?.action, Toast.LENGTH_LONG).show()
        if (intent?.action == NfcAdapter.ACTION_NDEF_DISCOVERED || intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED || intent?.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            Log.d(TAG, "nfc action detected")
            // Extract data from the intent, if needed
            // For example, you might want to read data from an NFC tag
            // val tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG) as Tag
            // val data = readDataFromNFC(tag)

            // Update the description
            Toast.makeText(this, "NFC DETECTED", Toast.LENGTH_SHORT).show()
            updateTvDescription()
        }
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
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
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

    private fun cardSelection(){

    }

    override fun onDestroy() {
        super.onDestroy()
        SeedlingTool.unRegisterResultCallBack(this)
    }

    private fun getCardList(): List<Card> {
        // Implement this method to return a list of cards
        // You can fetch this list from your data source (e.g., database, API)
        // For now, I'm providing a sample list
        return listOf(
            Card("Card 1", "Name1", "visa"),
            Card("Card 2", "Yu Heng", "tng"),
            Card("Card 3", "Henry", "any")
            // Add more cards as needed
        )
    }

    override fun onCardSelected(selectedCard: Card) {
        updateCardInfo()
    }

}