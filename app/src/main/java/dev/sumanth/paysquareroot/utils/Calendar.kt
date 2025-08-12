package dev.sumanth.paysquareroot.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getTimeString(s: String): String {
    return s.split(" ")[1]
}

fun getDateString(s: String): String {
    return s.split("T")[0]
}

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
    val timeFormat = SimpleDateFormat("yyyy-MM-DD", Locale.getDefault())

    val calendar = Calendar.getInstance()
    calendar.time = timeFormat.parse(s)!!
    return calendar.time
}

fun getTimeDifference(oldTime: Date, newTime: Date): String {
    println("Olddate "+ oldTime)
    println("New time " +newTime)
    val timeDifference = newTime.time - oldTime.time
    val hours = (timeDifference / (1000 * 60 * 60)) % 24
    val minutes = (timeDifference / (1000 * 60)) % 60

    return "$hours hrs $minutes min"
}

fun getFormattedDate(date: Date): String {
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(date)
    return formattedDate
}

fun getFormattedTime(date: Date): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(date)
    return formattedDate
}

fun getFormattedRemainingTime(date: Date) {

}