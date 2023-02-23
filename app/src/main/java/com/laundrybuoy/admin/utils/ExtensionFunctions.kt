package com.laundrybuoy.admin.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.laundrybuoy.admin.MainActivity
import com.laundrybuoy.admin.R
import com.laundrybuoy.admin.model.order.AllOrderListModel
import com.laundrybuoy.admin.model.order.OrderDetailResponse
import com.laundrybuoy.admin.model.order.VendorOrderListingModel
import com.laundrybuoy.admin.model.order.OrderTagsModel
import com.laundrybuoy.admin.utils.Constants.BASE_URL
import io.reactivex.Observable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

fun Context.convertDpToPixels(dp: Float): Int {
    val scale: Float = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Long.getStandardizeCount(): String {
    return when {
        this < 1000 -> {
            this.toString()
        }
        this in 1000..999999 -> {
            (this / 1000).toString() + "K"
        }
        else -> {
            (this / 1000000).toString() + "M"
        }
    }
}

fun String.getNumberStringToInt(): Int {
    return when {
        this == "one" -> {
            1
        }
        this == "two" -> {
            2
        }
        this == "three" -> {
            3
        }
        this == "four" -> {
            4
        }
        this == "five" -> {
            5
        }
        else -> {
            0
        }
    }
}

fun TextView.makeGradientHeading(text: String) {
    this.text = text
    val shader = LinearGradient(
        0f,
        this.textSize / 2,
        0f,
        this.textSize,
        resources.getColor(R.color.white),
        resources.getColor(R.color.colorGradientHeadings),
        Shader.TileMode.CLAMP
    )
    this.paint.shader = shader
}

fun String.getRatingAnimation(): String {

    when (this) {

        "1" -> {
            return "star_1.json"
        }

        "2" -> {
            return "star_2.json"

        }

        "3" -> {
            return "star_3.json"

        }

        "4" -> {
            return "star_4.json"

        }

        "5" -> {
            return "star_5.json"

        }
    }

    return "star.json"

}

fun String.ratingForPerson(): String {
    return when (this) {
        "partner" -> "Partner"
        "pickupRider" -> "Pickup"
        "deliveryRider" -> "Delivery"
        else -> "Unknown"
    }
}

fun OrderDetailResponse.Data.getClothesTags(): MutableList<OrderTagsModel.OrderTagsItem> {

    val listOfTags: MutableList<OrderTagsModel.OrderTagsItem> = arrayListOf()

    this.orderDetails?.tagIds?.map {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "ff8935",
                tagName = "#TAG-${it}"
            )
        )
    }

    return listOfTags
}


fun AllOrderListModel.Data.Partner.getTags(): MutableList<OrderTagsModel.OrderTagsItem> {
    val listOfTags: MutableList<OrderTagsModel.OrderTagsItem> = arrayListOf()

    listOfTags.add(
        OrderTagsModel.OrderTagsItem(
            tagHex = "ff8935",
            tagName = "#${this.ordNum}"
        )
    )

    listOfTags.add(
        OrderTagsModel.OrderTagsItem(
            tagHex = "561fff",
            tagName = this.orderDate?.getFormattedDateWithTime()
        )
    )

    listOfTags.add(
        OrderTagsModel.OrderTagsItem(
            tagHex = "fc2254",
            tagName = this.orderStatus?.getOrderStatus()
        )
    )

    if (this.isPrime == true) {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "fd2dbf",
                tagName = "Prime"
            )
        )
    }

    return listOfTags
}

fun Int.getOrderStatus(): String {

    when (this) {
        -3 -> {
            return "Cancelled by user"
        }
        -2 -> {
            return "Not accepted by vendor"
        }
        -1 -> {
            return "Out of reach"
        }
        0 -> {
            return "Order Placed"
        }
        1 -> {
            return "Order Accepted"
        }
        2 -> {
            return "Rider Assigned For Pickup"
        }
        3 -> {
            return "Out For Pickup"
        }
        4 -> {
            return "Bill Generated"
        }
        5 -> {
            return "Order Received By Vendor"
        }
        6 -> {
            return "Service In Progress"
        }
        7 -> {
            return "Order Packed"
        }
        8 -> {
            return "Rider Assigned For Delivery"
        }
        9 -> {
            return "Out for delivery"
        }
        10 -> {
            return "Delivered"
        }
    }
    return ""
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    (this as MainActivity).showSnackBar("Copied to clipboard")
}

fun Button.makeButtonDisabled() {
    alpha = 0.7f
    isEnabled = false
}

fun Button.makeButtonEnabled() {
    alpha = 1.0f
    isEnabled = true
}

fun View.makeViewVisible() {
    visibility = View.VISIBLE
}

fun View.makeViewGone() {
    visibility = View.GONE
}

fun View.makeViewInVisible() {
    visibility = View.INVISIBLE
}

fun ImageView.loadImageWithGlide(url: String) {
    Glide.with(this)
        .load(BASE_URL.substring(0, BASE_URL.length-1)+url)
        .centerCrop()
        .apply(RequestOptions.placeholderOf(R.drawable.user))
        .apply(RequestOptions.errorOf(R.drawable.user))
        .into(this)
}

fun ImageView.loadImageWithGlide(url: Uri) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .apply(RequestOptions.placeholderOf(R.drawable.user))
        .apply(RequestOptions.errorOf(R.drawable.user))
        .into(this)
}

fun String.getFormattedDate(): String? {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val output = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    val d = sdf.parse(this)
    val formattedTime = output.format(d)
    return formattedTime
}

fun String.getFormattedDateInString(): String? {
    //ISO to 24 Jan 2022
    val sdf = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    val output = SimpleDateFormat("dd LLL yyyy")
    val d = sdf.parse(this)
    val formattedTime = output.format(d)
    return formattedTime
}

fun Observable<CharSequence>.mapToString(): Observable<String> = this.map { it.toString() }

fun <T> Observable<T>.throttleFirstShort() = this.throttleFirst(500L, TimeUnit.MILLISECONDS)!!

@RequiresApi(Build.VERSION_CODES.O)
fun String.getWeekDayName(): String? {
    val dtfInput: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    val dtfOutput: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)
    return LocalDate.parse(this, dtfInput).format(dtfOutput)
}

fun EditText.getQueryTextChangeStateFlow(): StateFlow<String> {

    val query = MutableStateFlow("")

    doAfterTextChanged {
        query.value = it.toString()
    }

    return query

}

fun String.stringToDate(): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return try {
        format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        null
    }
}

fun Double.roundOffDecimal(): Double? {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this).toDouble()
}

fun String.getProgress(): Pair<Int, Double>? {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    var pair = Pair(0, 0.0)
    try {
        val deadlineDate = format.parse(this)
        val currentDate = format.parse(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())
        )
        return if (deadlineDate != null && currentDate != null) {
            val diff: Long = deadlineDate.time - currentDate.time
            val leftSeconds: Int = (diff / 1000).toInt()
            val totalSeconds: Int = 86400
            val progress: Double = ((leftSeconds.toDouble() / totalSeconds) * 100)
            pair = pair.copy(first = leftSeconds, second = progress)
            pair
        } else {
            pair
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        return null
    }
}

fun OrderDetailResponse.Data.OrderDetails.UserId.getCustomerPhoneNumbers(): MutableList<OrderTagsModel.OrderTagsItem> {
    val listOfTags: MutableList<OrderTagsModel.OrderTagsItem> = arrayListOf()

    if (this.mobile?.isEmpty() != true) {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "ff8935",
                tagName = "${this.mobile}"
            )
        )
    }

    return listOfTags

}

fun OrderDetailResponse.Data.OrderDetails.PartnerId.getVendorPhoneNumbers(): MutableList<OrderTagsModel.OrderTagsItem> {
    val listOfTags: MutableList<OrderTagsModel.OrderTagsItem> = arrayListOf()

    if (this.mobile?.isEmpty() != true) {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "ff8935",
                tagName = "${this.mobile}"
            )
        )
    }

    if (this.altMobile?.isEmpty() != true) {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "ff8935",
                tagName = "${this.altMobile}"
            )
        )
    }

    return listOfTags

}

fun OrderDetailResponse.Data.OrderDetails.RiderId.getRiderPhoneNumbers(): MutableList<OrderTagsModel.OrderTagsItem> {
    val listOfTags: MutableList<OrderTagsModel.OrderTagsItem> = arrayListOf()

    if (this.mobile?.isEmpty() != true) {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "fc2254",
                tagName = "${this.mobile}"
            )
        )
    }

    if (this.altMobile?.isEmpty() != true) {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "fc2254",
                tagName = "${this.altMobile}"
            )
        )
    }

    return listOfTags


}

fun List<String>.getPincodeTags(): MutableList<OrderTagsModel.OrderTagsItem> {
    val listOfTags: MutableList<OrderTagsModel.OrderTagsItem> = arrayListOf()

    this.map {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "fc2254",
                tagName = "${it}"
            )
        )
    }

    return listOfTags

}

fun List<String>.getServicesTags(): MutableList<OrderTagsModel.OrderTagsItem> {
    val listOfTags: MutableList<OrderTagsModel.OrderTagsItem> = arrayListOf()

    this.map {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "ff8935",
                tagName = "${it}"
            )
        )
    }

    return listOfTags

}

private val ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
private val NORMAL_DATE = "dd/MM/yyyy"
fun String.getFormattedDateWithTime(): String? {
    //ISO to 24 Jan 2022
    val sdf = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    val output = SimpleDateFormat("dd LLL yyyy hh:mm aa")
    val d = sdf.parse(this)
    val formattedTime = output.format(d)
    return formattedTime
}

fun String.normalDateToISO(): String? {
    val sdf = SimpleDateFormat(NORMAL_DATE)
    val output = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    val d = sdf.parse(this)
    val formattedTime = output.format(d)
    return formattedTime
}

fun VendorOrderListingModel.Data.Partner.OrderId.getTags(): MutableList<OrderTagsModel.OrderTagsItem> {

    val listOfTags: MutableList<OrderTagsModel.OrderTagsItem> = arrayListOf()

    listOfTags.add(
        OrderTagsModel.OrderTagsItem(
            tagHex = "ff8935",
            tagName = "#${this.ordNum}"
        )
    )

    listOfTags.add(
        OrderTagsModel.OrderTagsItem(
            tagHex = "561fff",
            tagName = this.orderDate?.getFormattedDateWithTime()
        )
    )

    listOfTags.add(
        OrderTagsModel.OrderTagsItem(
            tagHex = "fc2254",
            tagName = this.orderStatus?.getOrderStatus()
        )
    )

    if (this.isPrime == true) {
        listOfTags.add(
            OrderTagsModel.OrderTagsItem(
                tagHex = "fd2dbf",
                tagName = "Prime"
            )
        )
    }

    return listOfTags
}

fun Context.getFirstAndLastDate(month: Int): Pair<String?, String?> {
    val sdf: DateFormat = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT)
    var beginning: Date? = null
    var end: Date? = null
    var pair = Pair("", "")
    run {
        val calendar = getCalendarForNow()
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        setTimeToBeginningOfDay(calendar)
        beginning = calendar.time
    }
    run {
        val calendar = getCalendarForNow()
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        setTimeToEndOfDay(calendar)
        end = calendar.time
    }
    return pair.copy(first = sdf.format(beginning!!), second = sdf.format(end!!))
}

private fun getCalendarForNow(): Calendar {
    val calendar = GregorianCalendar.getInstance()
    calendar.time = Date()
    return calendar
}

private fun setTimeToBeginningOfDay(calendar: Calendar) {
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
}

private fun setTimeToEndOfDay(calendar: Calendar) {
    calendar[Calendar.HOUR_OF_DAY] = 23
    calendar[Calendar.MINUTE] = 59
    calendar[Calendar.SECOND] = 59
    calendar[Calendar.MILLISECOND] = 999
}

