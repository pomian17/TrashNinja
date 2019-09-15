package com.example.trashninja

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_summary.*

class GameSummaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val score = arguments?.getInt(EXTRA_SCORE)
        score_tv.text = "Your score: $score"
        try_again_button.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_gameSummaryFragment_to_gameFragment)
        }
        menu_button.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_gameSummaryFragment_to_mainFragment)
        }
    }


    companion object {
        const val EXTRA_SCORE = "extra.score"
    }
}