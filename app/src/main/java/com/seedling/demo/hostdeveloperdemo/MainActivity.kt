package com.seedling.demo.hostdeveloperdemo

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.oplus.pantanal.seedling.bean.SeedlingIntent
import com.oplus.pantanal.seedling.bean.SeedlingIntentFlagEnum
import com.oplus.pantanal.seedling.intent.IIntentResultCallBack
import com.oplus.pantanal.seedling.util.SeedlingTool
import com.seedling.demo.hostdeveloperdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CardSelectionListener {

    companion object{
        lateinit var bank_info: String
        const val TAG = "MainActivity"
        var cardSelectedPosition = 0
        lateinit var cardList: CardList
    }

    private lateinit var card: Card

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private lateinit var intentFilters: Array<IntentFilter>

    private val scanLauncher =
        registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    Toast.makeText(this, "The QR code is INVALID", Toast.LENGTH_SHORT).show()
                }
                else {
                    val intent = Intent(this, PaymentActivity::class.java)
                    startActivity(intent)
                }
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
                if (isGranted) {
                    showCamera()
                } else {
                    Toast.makeText(this, "You need to allow CAMERA PERMISSION in setting", Toast.LENGTH_LONG).show()
                }
        }

    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan QR code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)
    }

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


        initViewClick()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        Toast.makeText(this, "NewIntent: ${intent?.action}", Toast.LENGTH_LONG).show()
        if (intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            nfcAdapter.disableReaderMode(this)
            Log.d(TAG, "nfc TAG detected")
            // Extract data from the intent, if needed
            // For example, you might want to read data from an NFC tag

            // Update the description
            updateTvDescription()
            val bottomSheet = CategorySelectionBottomSheet()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        Toast.makeText(this, "ActivityMain on Resume", Toast.LENGTH_SHORT).show()
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        Toast.makeText(this, "ActivityMain on Pause", Toast.LENGTH_SHORT).show()
        nfcAdapter.disableForegroundDispatch(this)
    }

    private fun updateCardInfo() {
        Log.d(TAG, "update card info")

        card = cardList.cards.get(cardSelectedPosition)
//        Toast.makeText(this, "cardSelectedPosition: $cardSelectedPosition", Toast.LENGTH_SHORT).show()
        bank_info = "${card.cardNumber}\n${card.cardHolderName}"
        findViewById<TextView>(R.id.bank_info).text = bank_info
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
            checkPermissionCamera(this)
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
//            val bottomSheet = CardSelectionBottomSheet()
//            bottomSheet.setCardSelectionListener(this)
//            bottomSheet.setCardList(cardList)
//            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            SeedlingTool.registerResultCallBack(this, arrayOf("pantanal.intent.business.app.system.HOST_CARD_DEMO"))
            // call card
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
        }

        findViewById<ImageView>(R.id.ivBankCard).setOnClickListener {
            resetTvDescription()
        }

        findViewById<ImageButton>(R.id.ibBank).setOnClickListener {
            val intent = Intent(this, BankTransferActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.ibQrCode).setOnClickListener {
            val intent = Intent(this, ReceiveQrActivity::class.java)
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

    private fun checkPermissionCamera(context: Context) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            showCamera()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
            Toast.makeText(context, "CAMERA permission required", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SeedlingTool.unRegisterResultCallBack(this)
    }

    override fun onCardSelected(selectedCard: Card) {
        updateCardInfo()
    }

}