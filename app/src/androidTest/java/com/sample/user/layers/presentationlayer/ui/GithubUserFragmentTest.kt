package com.sample.user.layers.presentationlayer.ui

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import com.sample.github.R
import com.sample.githubuser.base.BaseViewHolder
import com.sample.githubuser.business.domain.model.ResponseItem
import com.sample.githubuser.presentation.ui.home.frag.GithubListFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class GithubUserFragmentTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    @Test
    fun launchArticleList_checkIfRecyclerView_isDisplayed() {
        launchArticleListFragment()
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun launchArticleList_scrollToPosition_isDisplayed() {
        launchArticleListFragment()
        runBlocking {
            delay(2000)
        }
        onView(withId(R.id.recyclerView)).perform(
            scrollToPosition<BaseViewHolder<ResponseItem>>(
                10
            )
        )
    }

    @Test
    fun launchArticleList_clickOnItem() {
        launchArticleListFragment()
        runBlocking {
            delay(2000)
        }
        onView(withId(R.id.recyclerView)).perform(
            actionOnItemAtPosition<BaseViewHolder<ResponseItem>>(
                1,
                click()
            )
        )
    }

    private fun launchArticleListFragment() {
        launchFragmentInHiltContainer<GithubListFragment> {
            navController.setGraph(R.navigation.nav_graph)
            navController.setCurrentDestination(R.id.nav_host_container)
            this.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    // The fragment’s view has just been created
                    Navigation.setViewNavController(this.requireView(), navController)
                }
            }
        }
    }
}