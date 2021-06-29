package com.example.gongsanggongsang.Second

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gongsanggongsang.R
import com.example.gongsanggongsang.databinding.FragmentCommunalCurrentBinding
import com.example.gongsanggongsang.databinding.FragmentCommunalEquipmentDialogBinding
import com.example.gongsanggongsang.databinding.FragmentCommunalEquipmentUsingItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class CommunalEquipmentDialogFragment : BottomSheetDialogFragment() {

    val database = FirebaseFirestore.getInstance()
    private lateinit var binding: FragmentCommunalEquipmentDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommunalEquipmentDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

}