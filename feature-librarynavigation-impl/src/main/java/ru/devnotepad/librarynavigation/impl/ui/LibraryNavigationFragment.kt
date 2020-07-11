package ru.devnotepad.librarynavigation.impl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        observeMenuItemContent(INITIAL_MENU_ITEMS_PARENT_ID)
        initOnBackPressedPolicy()
    }

    private fun initRecycler() = with(binding.recycler) {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = menuItemsAdapter
    }

    override fun onMenuItemClick(nestedMenuItem: NestedMenuItem) = openMenuItem(nestedMenuItem)

    private fun openMenuItem(nestedMenuItem: NestedMenuItem) =
        observeMenuItemContent(nestedMenuItem.idFromServer)

    private fun observeMenuItemContent(menuItemId: Int) {
        viewModel.getMenuItems(menuItemId).observe(viewLifecycleOwner) { childrenList ->
            if (childrenList.isEmpty()) {
                openArticle(menuItemId)
            } else {
                viewModel.increaseImmersionLevel()
                viewModel.recordUserPathIds(menuItemId)
                menuItemsAdapter.setMenuItems(childrenList)
            }
        }
    }

    private fun openArticle(articleId: Int) =
        Toast.makeText(requireContext(), "open article $articleId", Toast.LENGTH_SHORT).show()

    /**
     * TODO: implement popBackStack() instead finish().
     * TODO: BACK PRESS DOESN'T WORK CORRECTLY! SOLVE THIS PROBLEM.
     * */
    private fun initOnBackPressedPolicy() =
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.immersionLevel == 0) activity?.finish()
                observeMenuItemContent(viewModel.getPreviousPathPoint())
                viewModel.decreaseImmersionLevel()
            }
        })
}