package com.example.sickpredict.data.Message

data class Message(
    var message:String = "",
    var sendUid:String = "",
    var timeSTamp: Long = 0,
    var messageId: String = "",
    var senderName: String = "",
    var type: String = "",
    var prediction : String = "",
    var accuracy: String = "",
    var symtomps: String = "",
    var medcines: String = ""
)

