package ru.devnotepad.librarynavigation.impl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.devnotepad.librarynavigation.impl.adapters.MenuItemAdapter
import ru.devnotepad.librarynavigation.impl.databinding.LibraryNavigationFragmentBinding

class LibraryNavigationFragment : Fragment() {

    private val viewModel: LibraryNavigationViewModel by viewModels()
    private lateinit var binding: LibraryNavigationFragmentBinding

    private val menuItemsAdapter by lazy { MenuItemAdapter(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = LibraryNavigationFragmentBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
    }

    private fun initRecycler() = with(binding.recycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = menuItemsAdapter.apply { setMenuItems(viewModel.getFakeMenuItems()) }
    }
}