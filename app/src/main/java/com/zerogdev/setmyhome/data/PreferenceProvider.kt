package com.zerogdev.setmyhome.data

import android.content.Context
import android.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class PreferenceProvider(private val context: Context) {

    companion object {
        private const val KEY_MODE_OUT = "mode_out"
    }

    val updateObserver: BehaviorSubject<Int> = BehaviorSubject.createDefault(getModeOut())

    fun updateModeOut(mode: Int):  Disposable{
        return Observable.fromCallable {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(KEY_MODE_OUT, mode)
                .apply()
        }.flatMap {
            Observable.just(getModeOut())
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {mode ->
                updateObserver.onNext(mode)
            }

    }

    val modeOut: Observable<Int>
        get() {
            return updateObserver
        }

    fun getModeOut(): Int = PreferenceManager.getDefaultSharedPreferences(context)
        .getInt(KEY_MODE_OUT, -1)

}