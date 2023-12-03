package com.seedling.demo.hostdeveloperdemo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CardSelectionBottomSheet : BottomSheetDialogFragment() {

    private var cardSelectionListener: CardSelectionListener? = null
    private lateinit var cardList: CardList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_card_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.rvCardSelection)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = CardSelectionAdapter(cardList.cards) {
            card -> cardSelectionListener?.onCardSelected(card)
        }
        if (MainActivity.cardSelectedPosition < 0) MainActivity.cardSelectedPosition = 0
        recyclerView.adapter = adapter
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }

    fun setCardSelectionListener(listener: CardSelectionListener) {
        cardSelectionListener = listener
    }

    fun setCardList(cardList: CardList) {
        this.cardList = cardList
    }

}