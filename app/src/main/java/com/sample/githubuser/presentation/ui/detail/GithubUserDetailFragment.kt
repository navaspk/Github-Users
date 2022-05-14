package com.sample.githubuser.presentation.ui.detail

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sample.core.extensions.safeGet
import com.sample.github.databinding.FragmentDetailViewBinding
import com.sample.githubuser.base.BaseFragment
import com.sample.githubuser.utils.hide
import com.sample.githubuser.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail_view.*
import kotlinx.android.synthetic.main.shimmer_layout.view.*


@AndroidEntryPoint
class GithubUserDetailFragment :
    BaseFragment<FragmentDetailViewBinding>(FragmentDetailViewBinding::inflate) {

    // region LIFECYCLE

    override fun initUserInterface(view: View?) {

        activity?.intent?.let {
            // todo: shimmer layout is not getting displaying
            shimmerContainer?.shimmerFrameLayout?.show()
            viewDataBinding?.apply {
                arguments?.let {
                    webView.loadUrl(GithubUserDetailFragmentArgs.fromBundle(it).url)
                }
            }
        }


        viewDataBinding?.webView?.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url.safeGet())
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, facIcon: Bitmap?) {
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                shimmerContainer?.shimmerFrameLayout?.hide()
            }
        }


    }

    // endregion


    // region COMPANION

    companion object {
        const val URL_KEY = "url"
    }

    // endregion
}
