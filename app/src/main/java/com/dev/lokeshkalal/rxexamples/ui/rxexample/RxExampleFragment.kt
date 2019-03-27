package com.dev.lokeshkalal.rxexamples.ui.rxexample

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.lokeshkalal.rxexamples.R
import com.dev.lokeshkalal.rxexamples.ui.rxexample.operators.JustFragment
import com.dev.lokeshkalal.rxexamples.ui.rxexample.subjects.ReplaySubjectFragment
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.rx_example_fragment.*

class RxExampleFragment : Fragment() {

    companion object {
        fun newInstance() = RxExampleFragment()
    }

    private lateinit var viewModel: RxExampleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.rx_example_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        disposable_fragment.setOnClickListener { onDisposableClick() }
        just_fragment.setOnClickListener { onJustClick() }
        replay_subject_fragment.setOnClickListener { onReplaySubjectClicked() }
    }

    private fun onReplaySubjectClicked() {
        changeFragment(ReplaySubjectFragment())
    }

    private fun onDisposableClick() {

    }

    fun onJustClick() {
        changeFragment(JustFragment())
    }

    private fun changeFragment(fragment: Fragment) {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RxExampleViewModel::class.java)

    }
}
