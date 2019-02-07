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

open class BaseViewModel : ViewModel() {

    val event = MutableLiveData<Event>()
    val error = MutableLiveData<Error>()

    protected fun notify(event: Event) {
        this.event.value = event
    }

    protected fun notify(error: Error) {
        this.error.value = error
    }
}

open class BaseRxViewModel : BaseViewModel() {

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

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}

interface IViewModel

interface ISharedViewModel

open class BaseSharedViewModel : BaseRxViewModel()

enum class Event(@StringRes val stringResId: Int) {
    INITIALIZATION_DONE(0),
    STARTED_DRAWING(0),
    FINISHED_DRAWING(0)
}

enum class Error (@StringRes val stringResId: Int) {
    GENERAL_ERROR(R.string.general_error),
    NO_INTERNET_CONNECTION(R.string.no_internet_connection),
    NON_STABLE_CONNECTION(R.string.non_stable_connection),
    ERROR_LOADING_WMS_CAPABILITIES(R.string.error_loading_wms_capabilities)
}

interface BaseViewModelOwner<out VM : BaseViewModel> {

    fun registerEventHandlerSubscribers(lifecycleOwner: LifecycleOwner,
                                        viewModel: BaseViewModel) {
        viewModel.event.observe(
                lifecycleOwner,
                Observer { event -> event?.let(::onEvent) }
        )
        viewModel.error.observe(
                lifecycleOwner,
                Observer { error -> error?.let(::onError) }
        )
    }

    fun getViewModelInstance(): VM

    fun onEvent(event: Event): Unit?

    fun onError(error: Error): Unit?

    fun registerLiveDataObservers()
}

interface BaseSharedViewModelOwner<out SVM : BaseSharedViewModel> {

    fun registerSharedVMEventHandlerSubscribers(lifecycleOwner: LifecycleOwner,
                                                sharedViewModel: BaseSharedViewModel) {
        sharedViewModel.event.observe(
                lifecycleOwner,
                Observer { event -> event?.let(::onEvent) }
        )
        sharedViewModel.error.observe(
                lifecycleOwner,
                Observer { error -> error?.let(::onError) }
        )
    }

    fun getSharedViewModelInstance(): SVM

    fun onEvent(event: Event): Unit?

    fun onError(error: Error): Unit?
}