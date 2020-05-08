package com.example.devnotepad.ui.fragment_directions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devnotepad.DirectionOfStudy

import com.example.devnotepad.R
import com.example.devnotepad.ui.fragment_topics.TopicsFragment
import kotlinx.android.synthetic.main.directions_fragment.*

class DirectionsFragment : Fragment(),
    OnDirectionClickListener {

    companion object {
        fun newInstance() =
            DirectionsFragment()

        const val directionKey = "direction"
    }

    private lateinit var viewModel: DirectionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.directions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(DirectionsViewModel::class.java)

        val adapter = DirectionsAdapter(requireContext(), this)

        directionsRecyclerView.adapter = adapter
        directionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.makeRequestForDirections()
        viewModel.allDirections.observe(viewLifecycleOwner, Observer { directions ->
            directions?.let { adapter.setDirections(it) }
        })
    }

    private fun openTopicsFragment(directionOfStudy: DirectionOfStudy) {
        val topicsFragment: Fragment = TopicsFragment()

        val bundle = Bundle()
        bundle.putInt(directionKey, directionOfStudy.idFromServer)
        topicsFragment.arguments = bundle

        activity?.supportFragmentManager?.beginTransaction()
            ?.setCustomAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right)

            ?.replace(R.id.fragmentContainer, topicsFragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun onDirectionClick(directionOfStudy: DirectionOfStudy) {
        openTopicsFragment(directionOfStudy)
    }
}
