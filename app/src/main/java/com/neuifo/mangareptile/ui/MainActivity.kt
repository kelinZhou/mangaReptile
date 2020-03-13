package com.neuifo.mangareptile.ui

import android.os.Bundle
import com.neuifo.data.domain.utils.LogHelper
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.BaseActivity
import com.neuifo.mangareptile.ui.base.flyweight.adapter.CommonFragmentStatePagerAdapter
import com.neuifo.mangareptile.ui.base.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import me.ibrahimsn.lib.OnItemSelectedListener

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTitleBar(my_awesome_toolbar, toolbar_title, null, false)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val navigationAdapter = CommonFragmentStatePagerAdapter(supportFragmentManager)
        navigationAdapter.addPager("测试1", HomeFragment::class.java)
        navigationAdapter.addPager("测试2", HomeFragment::class.java)
        navigationAdapter.addPager("测试3", HomeFragment::class.java)
        main_root_pager.adapter = navigationAdapter
        bottomBar.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int) {
                main_root_pager.currentItem = pos
            }
        })
    }
}
