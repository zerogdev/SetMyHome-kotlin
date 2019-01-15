package com.zerogdev.setmyhome.extensions

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


fun runOnIoScheduler(func: () -> Unit): Disposable
 = Completable.fromCallable(func).subscribeOn(Schedulers.io()).subscribe()
