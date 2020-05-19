package com.neuifo.mangareptile.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.BaseFragment

class TestFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trest, null)
    }

}