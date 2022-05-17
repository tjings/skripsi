package umn.ac.id.skripsijosh.ui.profile.uploadimage

import android.net.Uri
import android.util.Log
import umn.ac.id.skripsijosh.base.BasePresenter
import umn.ac.id.skripsijosh.pojo.UserData
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

class UploadImagePresenter (view: UploadImageView) : BasePresenter<UploadImageView>() {

    init {
        super.attachView(view)
    }

    fun addUploadRecordToDb(newUserData: UserData){
        db.collection("userData")
            .document(auth.uid!!)
            .set(newUserData)
            .addOnSuccessListener {
                view?.onUploadImageComplete()
            }
            .addOnFailureListener {
                view?.stopLoading()
                view?.showError(it.toString())
            }
        db.collection("userStreak")
            .document(auth.uid!!)
            .update("displayPic", newUserData.displayPic)
    }

    fun uploadImage(filePath: Uri, userData: UserData) {
        view?.startLoading()
        var uploadTask = storageReference.child(auth.uid.toString()).putFile(filePath)

        val ref = storageReference.child(auth.uid.toString())
        uploadTask = ref.putFile(filePath)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val newUserData = UserData(
                    displayName = userData.displayName,
                    level = userData.level,
                    gender = userData.gender,
                    bday = userData.bday,
                    height = userData.height,
                    weight = userData.weight,
                    displayPic = downloadUri.toString(),
                    isBiodataDone = userData.isBiodataDone
                )
                addUploadRecordToDb(newUserData)
                view?.stopLoading()
            } else {
                //error handling
            }
        }.addOnFailureListener {}
    }
}