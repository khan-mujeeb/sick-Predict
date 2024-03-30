package com.example.sickpredict.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.sickpredict.data.Message.Message
import com.example.sickpredict.data.Message.UserInfo
import com.example.sickpredict.data.prediction.PreductionResult
import com.example.sickpredict.data.user.User

class ViewModel(private val repository: Repository = Repository()): androidx.lifecycle.ViewModel() {

    private val _createUserSuccess = MutableLiveData<Boolean>()
    val createUserSuccess: LiveData<Boolean> = _createUserSuccess

    /*
    function to get User List
     */
    fun getUserList(uid: String, callback: (uid: String, List<User>) -> Unit) {
        repository.getUserList(uid) { userList ->
            callback(uid, userList)
        }
    }


    /*
     add user to patient list
     */
    fun addUserToPatientList(
        user: User,
        receverUid: String
    ) {
        repository.addUserToPatientList(user, receverUid)
    }


    fun sendMessages(senderRoom: String, reciverRoom: String, message: Message, randomkey: String, recever_fcm_token: String, receverUid: String) {
        repository.sendMessages(senderRoom, reciverRoom, message, randomkey,recever_fcm_token, receverUid)
    }

    fun addPatientRecord(doctorUid: String, patientUid: String, patientRecord: PreductionResult) {
        repository.addPatientRecord(doctorUid, patientUid, patientRecord)
    }

    /*
    read patient record
     */
    fun readPatientRecord(doctorUid: String, patientUid: String, callback: (List<PreductionResult>?) -> Unit) {
        repository.readPatientRecord(doctorUid, patientUid) { records ->
            callback(records)
        }
    }

}