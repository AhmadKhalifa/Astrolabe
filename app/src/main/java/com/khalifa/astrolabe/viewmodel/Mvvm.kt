package com.khalifa.astrolabe.viewmodel


import android.arch.lifecycle.*
import android.support.annotation.StringRes
import com.khalifa.astrolabe.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * @author Ahmad Khalifa
 */

open class BaseRxViewModel : ViewModel() {

    val event: MutableLiveData<Event> = MutableLiveData()
    val error: MutableLiveData<Error> = MutableLiveData()

    private val compositeDisposable: CompositeDisposable  = CompositeDisposable()

    protected fun <T> performAsync(action: () -> T?,
                                   onSuccess: (T?) -> Unit = {},
                                   onFailure: (Throwable) -> Unit = {}) {
        compositeDisposable.add(
                Flowable.fromCallable<T> {
                    try {
                        action()
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                        throw Exception(exception)
                    }
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onSuccess, onFailure)
        )
    }

    protected fun performAsync (action: () -> Unit) {
        performAsync(action, onSuccess = {}, onFailure = {})
    }

    protected fun notify(event: Event) {
        this.event.value = event
    }

    protected fun notify(error: Error) {
        this.error.value = error
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }
}

enum class Event(@StringRes val stringResId: Int) {
    INITIALIZATION_DONE(0)
}

enum class Error (@StringRes val stringResId: Int) {
    GENERAL_ERROR(R.string.general_error),
    NO_INTERNET_CONNECTION(R.string.no_internet_connection),
    NON_STABLE_CONNECTION(R.string.non_stable_connection),
    ERROR_LOADING_WMS_CAPABILITIES(R.string.error_loading_wms_capabilities)
}

interface BaseViewModelOwner<out VM : BaseRxViewModel> {

    fun registerEventHandlerSubscribers(lifecycleOwner: LifecycleOwner,
                                        viewModel: BaseRxViewModel) {
        viewModel.event.observe(
                lifecycleOwner,
                Observer { event -> event?.let(this::onEvent) }
        )
        viewModel.error.observe(
                lifecycleOwner,
                Observer { error -> error?.let(this::onError) }
        )
    }

    fun getViewModelInstance(): VM

    fun onEvent(event: Event): Unit?

    fun onError(error: Error): Unit?

    fun registerLiveDataObservers()
}