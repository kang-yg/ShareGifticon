package com.kyg.sharegifticon.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.kyg.sharegifticon.Constants
import com.kyg.sharegifticon.model.GifticonStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddGifticonActivityViewModel @Inject constructor() : BaseViewModel() {
    var selectedDate: String = ""
    var selectedImage: MutableLiveData<Uri> = MutableLiveData()
    var selectedCategory: String = ""
    var enteredBarcode: String = ""
    val uploadProgress: MutableLiveData<Double> = MutableLiveData()

    fun btAddGifticonClicked() {
        currentUser?.let {
            val key = currentUser?.uid.plus("+").plus(Calendar.getInstance().timeInMillis)
            uploadFile(key)
        }
    }

    private fun uploadFile(key: String) {
        currentUser?.let { user ->
            with(
                firebaseStorageRef.child(user.uid).child(selectedCategory).child(key)
            ) {
                selectedImage.value?.let { fileUri ->
                    this.putFile(fileUri)
                        .addOnProgressListener { (bytesTransferred, totalByteCount) ->
                            uploadProgress.postValue((100.0 * bytesTransferred) / totalByteCount)
                        }.continueWithTask { task ->
                            this.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                uploadInfo(key, task.result)
                            }
                        }
                }
            }
        }
    }

    private fun uploadInfo(key: String, downloadUri: Uri) {
        currentUser?.let {
            val firebaseDatabaseRef =
                firebaseDatabase.getReference(it.uid).child(selectedCategory).child(key)
            with(firebaseDatabaseRef) {
                child(Constants.imageUri).setValue(downloadUri.toString())
                child(Constants.barcode).setValue(enteredBarcode)
                child(Constants.expirationDate).setValue(selectedDate)
                child(Constants.registrant).setValue(it.displayName)
                child(Constants.status).setValue(GifticonStatus.AVAILABLE.toString())
                child(Constants.category).setValue(selectedCategory)
                child(Constants.registrantKey).setValue(it.uid)
                child(Constants.gifticonKey).setValue(key)
            }
        }
    }

    fun inputCheck(): Boolean {
        return selectedDate != "" && selectedImage.value != null && selectedCategory != "" && enteredBarcode != ""
    }
}