package com.dev.lokeshkalal.rxexamples.ui.rxexample.subjects


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lokeshkalal.rxexamples.R
import com.dev.lokeshkalal.rxexamples.ui.rxexample.model.Stock
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import kotlinx.android.synthetic.main.fragment_replay_subject.*
import java.util.concurrent.TimeUnit


class ReplaySubjectFragment : Fragment() {


    val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_replay_subject, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replay_subject_default.setOnClickListener { onReplayDefaultSubjectClicked() }
        replay_subject_sized.setOnClickListener { onReplaySizedSubjectClicked() }
        replay_subject_timed.setOnClickListener { onReplaySubjectTimedClicked() }

    }

    private fun onReplaySubjectTimedClicked() {
        try {

            unSubsribe()


            // A replay subject that will only replay the last two items.
            val stockReplaySubject =
                ReplaySubject.createWithTime<Stock>(250, TimeUnit.MILLISECONDS, Schedulers.trampoline())

            stockReplaySubject.onNext(Stock("GOOG", 715.09))
            Thread.sleep(100)
            stockReplaySubject.onNext(Stock("GOOG", 716.00))
            Thread.sleep(100)
            stockReplaySubject.onNext(Stock("GOOG", 714.00))
            Thread.sleep(100)

            // Only the last two items will be replayed to this subscriber (716 and 714)
            // because the first one has already expired.
            val defaultSub = stockReplaySubject.subscribe(getDefaultObserver())


            // This will also be emitted to the defaultSub above.
            stockReplaySubject.onNext(Stock("GOOG", 720.00))
            Thread.sleep(100)
            stockReplaySubject.onComplete() // Terminate the stream with a completed event.

            // Lets sleep for another 100 millis to simulate some time passing.
            Thread.sleep(100)

            // Subscribe again with a new subscriber. This time the only item that is valid is
            // the last item: '720' as all the others have expired.
            val tardySubscription = stockReplaySubject.subscribe(getTardyObserver())


        } catch (ex: InterruptedException) {
            Log.e("" + ex, ex.message)
        }

    }

    private fun onReplaySizedSubjectClicked() {
        unSubsribe()


        // A replay subject that will only replay the last two items.
        val stockReplaySubject = ReplaySubject.createWithSize<Stock>(2)

        stockReplaySubject.onNext(Stock("GOOG", 715.09))
        stockReplaySubject.onNext(Stock("GOOG", 716.00))
        stockReplaySubject.onNext(Stock("GOOG", 714.00))

        // Only the last two items will be replayed to this subscriber (716 and 714)
        stockReplaySubject.subscribe(getDefaultObserver())


        // This will also be emitted to the defaultSub above.
        stockReplaySubject.onNext(Stock("GOOG", 720.00))
        stockReplaySubject.onComplete() // Terminate the stream with a completed event.

        // Subscribe again, this time the subscriber will get the last two events and the terminal
        // event right away. The last two items are "replayed" even though the stream has completed.
        stockReplaySubject.subscribe(getTardyObserver())

    }

    private fun onReplayDefaultSubjectClicked() {

        val replaySubject = ReplaySubject.create<Stock>()

        replaySubject.onNext(Stock("GOOG", 715.09))
        replaySubject.onNext(Stock("GOOG", 716.00))
        replaySubject.onNext(Stock("GOOG", 714.00))

        replaySubject.subscribeWith(getDefaultObserver())
        //compositeSubscription.add(defaultSub) // All three values will be delivered.

        replaySubject.onNext(Stock("GOOG", 720.00))
        replaySubject.onComplete() // Terminate the stream with a completed event.

        // Subscribe again, this time the subscriber will get all events and the terminal event
        // right away. All items are "replayed" even though the stream has completed.
        replaySubject.subscribe(getTardyObserver())
    }

    private fun getTardyObserver(): Observer<Stock> {
        return object : Observer<Stock> {
            override fun onComplete() {
                Log.d("", "tardy subscriber completed called.")
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Stock) {
                Log.d("", "onNext on tardy subscriber: $t")
            }

            override fun onError(e: Throwable) {
                Log.e("", e.toString() + "Error called on tardy subscriber.")
            }

        }
    }

    private fun getDefaultObserver(): Observer<Stock> {
        return object : Observer<Stock> {
            override fun onComplete() {
                Log.d("", "Default subscriber completed called.")
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Stock) {
                Log.d("", "onNext on default subscriber: $t")
            }

            override fun onError(e: Throwable) {
                Log.e("", e.toString() + "Error called on default subscriber.")
            }

        }

    }


    private fun unSubsribe() {
        compositeDisposable?.clear()
    }

}
