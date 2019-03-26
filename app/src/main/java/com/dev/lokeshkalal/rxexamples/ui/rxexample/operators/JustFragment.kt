package com.dev.lokeshkalal.rxexamples.ui.rxexample.operators

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.lokeshkalal.rxexamples.R
import com.google.android.gms.plus.PlusOneButton
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_just.*


class JustFragment : Fragment() {

    var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_just, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val disposable1 = Observable.just("red", "blue", "orange", "yellow")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { value -> "${content.text}\n$value" }
        compositeDisposable.add(disposable1)


        // With integers

        val disposable2 =
            Observable.just(1, 2, 3, 4)
                .subscribe { item ->
                    // onNext
                    content.text = "${content.text}\n$item"
                }
        compositeDisposable.add(disposable2)

        // With a complex object
        val fooSquare = FavoriteShape("Foo", FunShape("Red", "Square", 4))
        val barCircle = FavoriteShape("Bar", FunShape("Orange", "Circle", 0))
        val fizRectangle = FavoriteShape("Fiz", FunShape("Purple", "Rectangle", 4))
        val binTriangle = FavoriteShape("Bin", FunShape("Blue", "Triangle", 3))

        val disposable3 =
            Observable.just(fooSquare, barCircle, fizRectangle, binTriangle)
                .subscribe({ item ->
                    // onNext
                    // .toString() is automatically called here, building the output
                    // you see on the screen.
                    content.text = "${content.text}\n$item"
                }, { ex ->
                    // onError
                }, {
                    // onComplete
                })

        compositeDisposable.add(disposable3)

        // .just with a List
        // listOf gives us an immutable List<T>
        val listOne = listOf(2, 4, 8, 16)
        val listTwo = listOf(3, 6, 12, 24)
        val disposable4 = Observable.just(listOne, listTwo)
            .subscribe { item ->
                // onNext
                content.text = "${content.text}\n$item"
            }

        compositeDisposable.add(disposable4)


    }

    data class FavoriteShape(val favoriteName: String, val funShape: FunShape)

    class FunShape(val color: String, val shape: String, val sides: Int) {
        override fun toString(): String {
            if (shape.equals("Circle")) {
                throw NotImplementedError("Circles are special, they don't have 'sides'.")
            } else {
                return "color: $color, shape: $shape, sides: $sides"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}
