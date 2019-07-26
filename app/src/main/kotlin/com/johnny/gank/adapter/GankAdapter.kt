package com.johnny.gank.adapter

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.TextAppearanceSpan
import com.johnny.gank.R
import com.johnny.gank.util.AppHolder

/**
 * Created by johnny on 2017/6/17.
 */

fun getGankTitleStr(desc: String, who: String?): CharSequence {
    if (who.isNullOrEmpty()) return desc
    val builder = SpannableStringBuilder(desc)
    val spannableString = SpannableString(" ($who)")
    spannableString.setSpan(TextAppearanceSpan(AppHolder.app, R.style.SummaryTextAppearance), 0, spannableString.length, 0)
    builder.append(spannableString)
    return builder
}

fun getGankTitleStr(desc: String, who: String?, type: String): CharSequence {
    if (who.isNullOrEmpty()) return desc
    val builder = SpannableStringBuilder(desc)
    val spannableWho = SpannableString(" ($who)")
    val spannableType = SpannableString(" ($type)")
    spannableWho.setSpan(TextAppearanceSpan(AppHolder.app, R.style.SummaryTextAppearance), 0, spannableWho.length, 0)
    spannableType.setSpan(TextAppearanceSpan(AppHolder.app, R.style.SummaryTextAppearance), 0, spannableType.length, 0)
    builder.append(spannableWho)
    builder.append(spannableType)
    return builder
}