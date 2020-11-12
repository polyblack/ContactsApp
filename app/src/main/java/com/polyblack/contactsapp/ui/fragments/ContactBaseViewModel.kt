package com.polyblack.contactsapp.ui.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

open class ContactBaseViewModel(application: Application) : AndroidViewModel(application) {
    val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
