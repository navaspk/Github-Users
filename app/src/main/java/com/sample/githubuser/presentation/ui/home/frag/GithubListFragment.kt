package com.sample.githubuser.presentation.ui.home.frag

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sample.core.business.data.cache.PrefDataStore
import com.sample.core.business.data.cache.UiMode
import com.sample.core.extensions.safeGet
import com.sample.github.R
import com.sample.github.databinding.FragmentUserHomeBinding
import com.sample.githubuser.GithubApp
import com.sample.githubuser.base.BaseFragment
import com.sample.githubuser.business.domain.state.NetworkState
import com.sample.githubuser.presentation.event.ClickEvent
import com.sample.githubuser.presentation.ui.home.adapter.GithubListAdapter
import com.sample.githubuser.presentation.home.viewmodel.GithubListViewModel
import com.sample.githubuser.presentation.ui.detail.GithubUserDetailFragment.Companion.URL_KEY
import com.sample.githubuser.presentation.ui.home.adapter.ItemOffsetDecoration
import com.sample.githubuser.utils.hasInternetConnection
import com.sample.githubuser.utils.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * The fragment is used for display the home screen content, ie list of github users
 */
@AndroidEntryPoint
class GithubListFragment :
    BaseFragment<FragmentUserHomeBinding>(FragmentUserHomeBinding::inflate) {

    // region VARIABLE

    private var listAdapter: GithubListAdapter? = null
    private val listViewModel: GithubListViewModel by viewModels()

    @Inject
    lateinit var dataStore: PrefDataStore

    private var event: (ClickEvent) -> Unit = { event ->
        when (event) {
            is ClickEvent.ItemClicked -> {
                onItemClick(event.pos)
            }
        }
    }

    // endregion

    // region LIFECYCLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listViewModel.let {
            lifecycleScope.launch {
                it.userIntent.send(GithubListViewModel.GithubIntent.GetSearchedGithub)
            }
        }
    }

    override fun initUserInterface(view: View?) {
        if (activity?.hasInternetConnection() == false) {
            viewDataBinding?.root?.showSnackBar(getString(R.string.error_internet_not_available))
            return
        }
        initRecyclerView()
        initObserverForStates()
        initMenuOption()
        //initUiMode()
    }

    // endregion


    // region UTILS

    private fun initRecyclerView(isSwitch: Boolean = false) {

        viewDataBinding?.recyclerView?.apply {
            layoutManager = if (GithubApp.instance.isListView) {
                LinearLayoutManager(activity)
            } else {
                GridLayoutManager(activity, 2)
            }
            if (listAdapter == null) {
                listAdapter = GithubListAdapter(event)
            }
            if (isSwitch.not())
                addItemDecoration(ItemOffsetDecoration(requireContext(), R.dimen.dimen_4_dp))
            adapter = listAdapter
        }
    }

    private fun initObserverForStates() {

        val data = listViewModel.githubUserList

        data?.networkState?.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkState.LOADING -> {
                    viewDataBinding?.progressBar?.visibility = View.VISIBLE
                }
                is NetworkState.ERROR -> {
                    viewDataBinding?.progressBar?.visibility = View.GONE
                    // Handling fail state
                }
                is NetworkState.LOADED -> {
                    viewDataBinding?.progressBar?.visibility = View.GONE
                }
            }
        }
        data?.pagedList?.observe(this) {
            listAdapter?.submitList(it)
        }
    }

    private fun initMenuOption() {
        viewDataBinding?.toolbar?.apply {
            inflateMenu(R.menu.custom_menu)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.switch_view -> {
                        lifecycleScope.launch {
                            when (GithubApp.instance.isListView) {
                                true -> {
                                    dataStore.setUiMode(UiMode.GRID)
                                }
                                false -> {
                                    dataStore.setUiMode(UiMode.LIST)
                                }
                            }
                            withContext(Dispatchers.Main) {
                                GithubApp.instance.isListView = GithubApp.instance.isListView.not()
                                initRecyclerView(true)
                            }
                        }
                    }
                }
                true
            }
        }
    }

    /*private fun initUiMode() {
        PrefDataStore(requireContext()).uiModeFlow.asLiveData().observe(this) { uiMode ->
            android.util.Log.d("prefff", "uimode=$uiMode")
            when (uiMode) {
                UiMode.LIST -> initRecyclerView(isSwitch = true, uiMode = true)
                UiMode.GRID -> initRecyclerView(true)
            }
        }
    }*/

    // endregion


    // region UTIL

    private fun onItemClick(position: Int) {
        if (activity?.hasInternetConnection() == true) {
            findNavController().navigate(
                R.id.move_to_details,
                bundleOf(URL_KEY to listAdapter?.currentList?.get(position)?.htmlUrl.safeGet())
            )
        } else {
            viewDataBinding?.root?.showSnackBar(getString(R.string.error_internet_not_available))
        }
    }

    // endregion

}
