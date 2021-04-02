package com.example.gongsanggongsang.Second

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home_second.*
import com.example.gongsanggongsang.R

class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home_second, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calender = Calendar.getInstance()

        var day_of_week = calender.get(Calendar.DAY_OF_WEEK)
        if(day_of_week == 1){
            day_of_week = 8
        }

        calender.add(Calendar.DATE,-(day_of_week)+2)
        var year = calender.get(Calendar.YEAR).toString()
        var month = (calender.get(Calendar.MONTH) + 1).toString()
        var date = calender.get(Calendar.DATE).toString()
        val startday = "$year-$month-$date"

        calender.add(Calendar.DATE,6)
        year = calender.get(Calendar.YEAR).toString()
        month = (calender.get(Calendar.MONTH) + 1).toString()
        date = calender.get(Calendar.DATE).toString()
        val lastday = "$year-$month-$date"


        val week : TextView = view.findViewById(R.id.this_week)
        week.text = "현재 주차 : " + startday + " ~ " + lastday

        monday_btn.setOnClickListener {
            time_block.setText("시간대 설정 칸")
        }
        tuesday_btn.setOnClickListener {
            time_block.setText("시간대 설정 칸")
        }
        wednesday_btn.setOnClickListener {
            time_block.setText("시간대 설정 칸")
        }
        thursday_btn.setOnClickListener {
            time_block.setText("시간대 설정 칸")
        }
        friday_btn.setOnClickListener {
            time_block.setText("시간대 설정 칸")
        }
        saturday_btn.setOnClickListener {
            time_block.setText("시간대 설정 칸")
        }
        sunday_btn.setOnClickListener {
            time_block.setText("시간대 설정 칸")
        }
        //val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_second) as NavHostFragment? ?: return
        //findNavController().navigate(R.id.second)
    }
}
