package ru.app.apteka.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.app.apteka.R
import ru.app.apteka.ui.activities.MainActivity

class ProfileFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        //(activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        //(activity as MainActivity).supportActionBar?.title = getString(R.string.profile)
        (activity as MainActivity).supportActionBar?.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //menu.findItem(R.id.action_search).isVisible = false
        //menu.findItem(R.id.action_filter).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }
}