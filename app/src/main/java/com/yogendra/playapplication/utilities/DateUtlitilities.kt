@file:Suppress("DEPRECATION")

package com.yogendra.socialmediamvvm.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

/**
 * Convert one date format string  to another date format string in android
 *
 * @param inputDateFormat Input SimpleDateFormat
 * @param outputDateFormat Output SimpleDateFormat
 * @param inputDate input Date String
 * @throws ParseException
 */


const val DATE_FORMAT_1 = "hh:mm a"//The output will be -: 10:37 am
const val DATE_FORMAT_2 = "h:mm a"//The output will be -: 10:37 am
const val DATE_FORMAT_3 = "yyyy-MM-dd"//The output will be -: 2018-12-05
const val DATE_FORMAT_4 = "dd-MMMM-yyyy"//The output will be -: 05-December-2018
const val DATE_FORMAT_5 = "dd MMMM yyyy"//The output will be -: 05 December 2018
const val DATE_FORMAT_6 = "dd MMMM yyyy zzzz"//The output will be -: 05 December 2018 UTC
const val DATE_FORMAT_7 = "EEE, MMM d, ''yy"//The output will be -: Wed, Dec 5, '18
const val DATE_FORMAT_8 = "yyyy-MM-dd HH:mm:ss"//The output will be -: 2018-12-05 10:37:43
const val DATE_FORMAT_9 = "h:mm a dd MMMM yyyy"//The output will be -: 10:37 am 05 December 2018
const val DATE_FORMAT_10 = "K:mm a, z"//The output will be -: 10:37 am, UTC
const val DATE_FORMAT_11 = "hh 'o''clock' a, zzzz"//The output will be -: 10 o'clock am, UTC
const val DATE_FORMAT_12 =
    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"//The output will be -: 2018-12-05T10:37:43.937Z
const val DATE_FORMAT_13 =
    "E, dd MMM yyyy HH:mm:ss z"//The output will be -: Wed, 05 Dec 2018 10:37:43 UTC
const val DATE_FORMAT_14 =
    "yyyy.MM.dd G 'at' HH:mm:ss z"//The output will be -: 2018.12.05 AD at 10:37:43 UTC
const val DATE_FORMAT_15 =
    "yyyyy.MMMMM.dd GGG hh:mm aaa"//The output will be -: 02018.D.05 AD 10:37 am
const val DATE_FORMAT_16 =
    "EEE, d MMM yyyy HH:mm:ss Z"//The output will be -: Wed, 5 Dec 2018 10:37:43 +0000
const val DATE_FORMAT_17 =
    "yyyy-MM-dd'T'HH:mm:ss.SSSZ"//The output will be -: 2018-12-05T10:37:43.946+0000
const val DATE_FORMAT_18 =
    "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" //The output will be -: 2018-12-05T10:37:43.949Z
const val DATE_FORMAT_19 = "dd-MMM-yyyy"//The output will be -: 05-Dec-2018
const val DATE_FORMAT_20 = "dd MMM yyyy, hh:mm a "//The output will be -: 05-Dec-2018
const val DATE_FORMAT_21 = "dd MMM, hh:mm a "//The output will be -: 05-Dec-2018

@Throws(ParseException::class)
fun formatDateFromDateString(
    inputDateFormat: String, outputDateFormat: String,
    inputDate: String
): String {
    val mParsedDate: Date
    val mOutputDateString: String
    val mInputDateFormat =
        SimpleDateFormat(inputDateFormat, Locale.getDefault())

    mParsedDate = mInputDateFormat.parse(inputDate)

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val cal = Calendar.getInstance()
    cal.time = mParsedDate
    val year = cal[Calendar.YEAR]

    if (year != currentYear) {
        val mOutputDateFormat =
            SimpleDateFormat(outputDateFormat, Locale.getDefault())
        mOutputDateString = mOutputDateFormat.format(mParsedDate)

    } else {
        val mOutputDateFormat =
            SimpleDateFormat(DATE_FORMAT_21, Locale.getDefault())
        mOutputDateString = mOutputDateFormat.format(mParsedDate)

    }
    return mOutputDateString
}