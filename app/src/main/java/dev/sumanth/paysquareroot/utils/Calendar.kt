package dev.sumanth.paysquareroot.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getTimeObject(s: String): Date {
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    val parsedDate = timeFormat.parse(s)!!

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, parsedDate.hours)
    calendar.set(Calendar.MINUTE, parsedDate.minutes)
    calendar.set(Calendar.SECOND, parsedDate.seconds)
    return calendar.time
}

fun getDateObject(s: String): Date {
    val timeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = timeFormat.parse(s)!!
    return parsedDate
}

fun getDateObjectV2(s: String): Date {
    val timeFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val parsedDate = timeFormat.parse(s)!!
    return parsedDate
}

fun getTimeDifference(oldTime: Date, newTime: Date): String {
    val timeDifference = newTime.time - oldTime.time
    val hours = (timeDifference / (1000 * 60 * 60)) % 24
    val minutes = (timeDifference / (1000 * 60)) % 60

    return "$hours hrs $minutes min"
}

fun getFormattedDate(date: Date): String {
    val diff = Calendar.getInstance().time.year - date.year
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(date)
    return "$formattedDate (${if(diff == 1) "1 year" else "$diff years"})"
}

fun getFormattedDateV2(date: Date): String {
    val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(date)
    return formattedDate
}

fun getFormattedTime(date: Date): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(date)
    return formattedDate
}