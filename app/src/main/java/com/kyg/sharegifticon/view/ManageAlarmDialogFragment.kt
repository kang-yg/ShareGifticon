package com.kyg.sharegifticon.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Button
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.MyReceiver
import com.kyg.sharegifticon.R
import com.kyg.sharegifticon.databinding.DialogManageAlarmBinding
import com.kyg.sharegifticon.model.repository.MainRepository
import com.kyg.sharegifticon.viewmodel.MainActivityViewModel
import com.kyg.sharegifticon.viewmodel.ManageAlarmDialogFragmentViewModel

class ManageAlarmDialogFragment(
    private val activity: MainActivity,
    private val mainActivityViewModel: MainActivityViewModel
) : BaseDialogFragment<DialogManageAlarmBinding>(R.layout.dialog_manage_alarm) {
    private val manageAlarmDialogFragmentViewModel: ManageAlarmDialogFragmentViewModel = ManageAlarmDialogFragmentViewModel(mainActivityViewModel)
    private val alarmManager: AlarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarmIntent: Intent = Intent(activity, MyReceiver::class.java)
    private val alarmPendingIntent: PendingIntent =
        PendingIntent.getBroadcast(
            activity,
            Constants.notificationIdExpirationDate,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataBinding().manageAlarmDialogFragment = this
        setDialogSize()
    }

    fun btNoAlarmClick() {
        getDataBinding().btNoAlarm.setOnClickListener {
            alarmManager.cancel(alarmPendingIntent)
            activity.showToast(activity.getString(R.string.ALARM_SET_CANCEL))
        }
    }

    fun btOneDayAlarmClick() {
        getDataBinding().btOneDayAlarm.setOnClickListener {
            manageAlarmDialogFragmentViewModel.setOneDayAlarm(1).forEach { triggerTime ->
                setAlarm(triggerTime)
            }
            showAlarmToast(it)
        }
    }

    fun btThreeDayAlarmClick() {
        getDataBinding().btThreeDayAlarm.setOnClickListener {
            manageAlarmDialogFragmentViewModel.setOneDayAlarm(3).forEach { triggerTime ->
                setAlarm(triggerTime)
            }
            showAlarmToast(it)
        }
    }

    fun btFiveDayAlarmClick() {
        getDataBinding().btFiveDayAlarm.setOnClickListener {
            manageAlarmDialogFragmentViewModel.setOneDayAlarm(5).forEach { triggerTime ->
                setAlarm(triggerTime)
            }
            showAlarmToast(it)
        }
    }

    private fun setAlarm(triggerTime: Long) {
        alarmManager.cancel(alarmPendingIntent)
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, alarmPendingIntent)
    }

    private fun showAlarmToast(view: View) {
        with(activity) {
            val toastMessage = String.format("%s %s", (view as Button).text.toString(), getString(R.string.ALARM_SET))
            showToast(toastMessage)
        }
    }
}