package com.kyg.sharegifticon.viewmodel

import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.model.MyAlarm
import com.kyg.sharegifticon.model.repository.MainRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class ManageAlarmDialogFragmentViewModel(private val mainActivityViewModel: MainActivityViewModel) {
    private val alarmList: ArrayList<MyAlarm> = ArrayList(mainActivityViewModel.mainRepository.readAllGifticons())
    private val alarmMillisecond = arrayListOf<Long>()
    private val dateFormat = SimpleDateFormat(Constants.myDateFormat)

    fun setOneDayAlarm(beforeDay: Int): ArrayList<Long> {
        alarmMillisecond.clear()
        alarmList.forEach { alarm ->
            val date: Date = dateFormat.parse(alarm.expirationDate)
            val calendar: Calendar = Calendar.getInstance().apply {
                time = date
            }.also {
                it.add(Calendar.DATE, beforeDay.absoluteValue * -1)
            }
            alarmMillisecond.add(calendar.timeInMillis)
        }
        return alarmMillisecond
    }
}