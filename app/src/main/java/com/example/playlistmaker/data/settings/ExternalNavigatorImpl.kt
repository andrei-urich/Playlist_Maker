package com.example.playlistmaker.data.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.settings.ExternalNavigator

class ExternalNavigatorImpl(val context: Context) : ExternalNavigator {
    override fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.app_link))
        val chooserIntent =
            Intent.createChooser(shareIntent, context.getString(R.string.app_share_header))
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    override fun openTerms() {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        browserIntent.data = Uri.parse(context.getString(R.string.agreement_link))
        context.startActivity(browserIntent)
    }

    override fun openSupport() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.supportEmail)))
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            context.getString(R.string.callSupportDefaultSubject)
        )
        emailIntent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.callSupportDefaultMessage)
        )
        context.startActivity(emailIntent)
    }
}