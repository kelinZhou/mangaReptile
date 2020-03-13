package com.neuifo.mangareptile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.SystemError
import com.neuifo.mangareptile.ui.base.BaseFragment
import com.neuifo.mangareptile.ui.base.delegate.BaseViewDelegate

/**
 * **描述:** 通用的错误页面。
 *
 * **创建人:** kelin
 *
 * **创建时间:** 2019/4/22  5:27 PM
 *
 * **版本:** v 1.0.0
 */
class CommonErrorFragment : BaseFragment() {

    companion object {

        private const val KEY_ERROR_MSG = "key_error_msg"

        fun createInstance(error: SystemError): CommonErrorFragment {
            return CommonErrorFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ERROR_MSG, error.text)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.state_layout_common, container, false)
        BaseViewDelegate.refreshStatePageView(view, BaseViewDelegate.SimpleStateOption(R.drawable.img_error_icon, arguments?.getString(KEY_ERROR_MSG) ?: "系统错误"))
        return view
    }
}