package com.seedling.demo.hostdeveloperdemo

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategorySelectionBottomSheet : BottomSheetDialogFragment()  {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_category_selection, container, false)

        // all ImageView will add the transaction to add payment history
        view.findViewById<ImageView>(R.id.ivFood).setOnClickListener {
            dismiss()
        }

        view.findViewById<ImageView>(R.id.ivShopping).setOnClickListener {
            dismiss()
        }

        view.findViewById<ImageView>(R.id.ivEntertainment).setOnClickListener {
            dismiss()
        }

        view.findViewById<ImageView>(R.id.ivBill).setOnClickListener {
            dismiss()
        }

        view.findViewById<ImageView>(R.id.ivTransport).setOnClickListener {
            dismiss()
        }

        view.findViewById<ImageView>(R.id.ivOthers).setOnClickListener {
            dismiss()
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
    }


}