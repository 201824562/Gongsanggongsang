package com.example.adminapp.ui.main.reservation.edit

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class ReservationEditViewPagerAdapter (activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private var firstFragment : Fragment = ReservationEditEquipmentFragment()
    private var secondFragment : Fragment = ReservationEditFacilityFragment()

    fun deleteFragments() {
        firstFragment.onDestroyView()
        secondFragment.onDestroyView() }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        Log.e("checking", "Fragment Created")
        return when (position) {
            0 -> firstFragment
            1 -> secondFragment
            else -> error("no such position: $position")
        }
    }
}