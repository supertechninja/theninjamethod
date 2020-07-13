package com.mcwilliams.theninjamethod.ui.routines

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.mcwilliams.theninjamethod.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RoutinesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_routines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }
}
