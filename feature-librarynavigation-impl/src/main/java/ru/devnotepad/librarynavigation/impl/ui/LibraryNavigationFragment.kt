package ru.devnotepad.librarynavigation.impl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import ru.devnotepad.librarynavigation.impl.adapters.MenuItemAdapter
import ru.devnotepad.librarynavigation.impl.databinding.LibraryNavigationFragmentBinding
import ru.devnotepad.librarynavigation.impl.di.ComponentHolder
import ru.devnotepad.librarynavigation.impl.entities.NestedMenuItem
import javax.inject.Inject

class LibraryNavigationFragment : Fragment(), OnMenuItemClickListener {

    companion object {
        private const val INITIAL_MENU_ITEMS_PARENT_ID = 0
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LibraryNavigationViewModel by viewModels { viewModelFactory }
    private lateinit var binding: LibraryNavigationFragmentBinding
    private var immersionLevel = 0
    private val userMenuPathIds = ArrayList<Int>()

    private val menuItemsAdapter by lazy {
        MenuItemAdapter(requireContext(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearComponent()
    }

    private fun initComponent() = ComponentHolder.initComponent(this)
    private fun clearComponent() = ComponentHolder.clearComponent()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = LibraryNavigationFragmentBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecycler()
        observeInitialMenuItems()
        initOnBackPressedPolicy()
    }

    private fun observeInitialMenuItems() =
        viewModel.getMenuItems(INITIAL_MENU_ITEMS_PARENT_ID).observe(viewLifecycleOwner) {
            menuItemsAdapter.setMenuItems(it)
        }

    private fun initRecycler() = with(binding.recycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = menuItemsAdapter
    }

    override fun onMenuItemClick(nestedMenuItem: NestedMenuItem) {
        removeInitialObserver()
        openMenuItem(nestedMenuItem)
    }

    private fun removeInitialObserver() = viewModel.getMenuItems(INITIAL_MENU_ITEMS_PARENT_ID)
        .removeObserver { observeInitialMenuItems() }

    private fun openMenuItem(nestedMenuItem: NestedMenuItem) {
        increaseImmersionLevel()
        recordUserPathIds(nestedMenuItem.idFromServer)
        observeMenuItemContent(nestedMenuItem.idFromServer)
    }

    private fun observeMenuItemContent(menuItemId: Int) {
        viewModel.getMenuItems(menuItemId).observe(viewLifecycleOwner) {
            menuItemsAdapter.setMenuItems(it)
        }
    }

    private fun recordUserPathIds(idFromServer: Int) =
        let { userMenuPathIds.add(idFromServer) }

    private fun increaseImmersionLevel() { immersionLevel++ }
    private fun decreaseImmersionLevel() { immersionLevel-- }

    /**
     * Doesn't work correctly for now.
     * */
    private fun initOnBackPressedPolicy() =
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                decreaseImmersionLevel()
                observeMenuItemContent(getPreviousPathPoint())
            }
        })

    private fun getPreviousPathPoint(): Int {
        if (userMenuPathIds.size > 2) return 0

        if (userMenuPathIds.size > 1) {
            userMenuPathIds.let {
                it.removeAt(it.lastIndex)
            }
        }

        return userMenuPathIds.last()
    }
}