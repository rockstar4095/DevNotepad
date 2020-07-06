package ru.devnotepad.librarynavigation.impl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.devnotepad.librarynavigation.impl.adapters.MenuItemAdapter
import ru.devnotepad.librarynavigation.impl.data.MenuItemsRepository
import ru.devnotepad.librarynavigation.impl.databinding.LibraryNavigationFragmentBinding
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem

class LibraryNavigationFragment : Fragment(), OnMenuItemClickListener {

    private val viewModel: LibraryNavigationViewModel by viewModels()
    private lateinit var binding: LibraryNavigationFragmentBinding

    // temp repository
    private val menuItemsRepository by lazy {
        MenuItemsRepository(requireContext())
    }

    private val menuItemsAdapter by lazy {
        MenuItemAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = LibraryNavigationFragmentBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()

        CoroutineScope(Dispatchers.IO).launch {
            val menuItems = listOf(
                NestedMenuItem(
                    1,
                    0,
                    0,
                    "menu item 1",
                    1000
                ),

                NestedMenuItem(
                    2,
                    0,
                    0,
                    "menu item 2",
                    1000
                ),

                NestedMenuItem(
                    3,
                    0,
                    0,
                    "menu item 3",
                    1000
                ),

                NestedMenuItem(
                    4,
                    1,
                    0,
                    "menu item 4",
                    1000
                ),

                NestedMenuItem(
                    5,
                    1,
                    0,
                    "menu item 5",
                    1000
                ),

                NestedMenuItem(
                    6,
                    4,
                    0,
                    "menu item 6",
                    1000
                ),

                NestedMenuItem(
                    7,
                    6,
                    0,
                    "menu item 7",
                    1000
                )
            )

            for (menuItem in menuItems) {
                menuItemsRepository.insertMenuItem(menuItem)
            }
        }
    }

    private fun initRecycler() = with(binding.recycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = menuItemsAdapter.apply { setMenuItems(viewModel.getFakeMenuItems()) }
    }

    override fun onMenuItemClick(nestedMenuItem: NestedMenuItem) {
        openMenuItem(nestedMenuItem)
    }

    private fun openMenuItem(nestedMenuItem: NestedMenuItem) {
        menuItemsRepository.getMenuItems(nestedMenuItem.idFromServer).observe(viewLifecycleOwner, Observer {
            menuItemsAdapter.setMenuItems(it)
        })
    }
}