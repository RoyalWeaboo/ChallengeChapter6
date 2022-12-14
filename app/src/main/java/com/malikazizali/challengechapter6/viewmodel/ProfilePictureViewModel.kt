package com.malikazizali.challengechapter6.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.worker.BlurImageWorker
import com.malikazizali.challengechapter6.worker.KEY_IMAGE_URI

class ProfilePictureViewModel (application : Application): ViewModel() {
    //  var untuk instance  WorkManager di ViewModel
    private val workManager = WorkManager.getInstance(application)
    private var imageUri: Uri? = null

    fun setImageUri(uri : Uri){
        imageUri = uri
    }

    //    make WorkRequest & beritahu WM untuk jalankan
    internal fun applyBlur() {
        val blurRequest = OneTimeWorkRequestBuilder<BlurImageWorker>()
            .setInputData(createInputDataForUri())
            .build()
        workManager.enqueue(blurRequest)
    }

    //create URI img
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    //    get image URI
//    private fun getImageUri(context: Context): Uri {
//        val resources = context.resources
//
//        return Uri.Builder()
//            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
//            .authority(resources.getResourcePackageName(R.drawable.ic_baseline_person_24))
//            .appendPath(resources.getResourceTypeName(R.drawable.ic_baseline_person_24))
//            .appendPath(resources.getResourceEntryName(R.drawable.ic_baseline_person_24))
//            .build()
//    }

}

class BlurViewModelFactory(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfilePictureViewModel::class.java)) {
            ProfilePictureViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}