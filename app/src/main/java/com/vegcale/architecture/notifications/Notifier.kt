package com.vegcale.architecture.notifications

interface Notifier {
    fun postNewsNotifications(contentTitle: String, contextText: String)
}