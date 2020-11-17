package com.polyblack.contactsapp.presentation.presenters

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class ContactBaseViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
