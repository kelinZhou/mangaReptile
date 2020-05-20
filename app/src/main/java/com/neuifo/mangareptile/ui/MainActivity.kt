package com.neuifo.mangareptile.ui

import android.os.Bundle
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.BaseActivity
import com.neuifo.mangareptile.ui.base.flyweight.adapter.CommonFragmentStatePagerAdapter
import com.neuifo.mangareptile.ui.home.HomeFragment
import com.neuifo.mangareptile.ui.home.TestFragment
import kotlinx.android.synthetic.main.activity_main.*
import me.ibrahimsn.lib.OnItemSelectedListener

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTitleBar(my_awesome_toolbar, toolbar_title, null, false)
        supportActionBar?.hide()
        isDarkMode = true
        val navigationAdapter = CommonFragmentStatePagerAdapter(supportFragmentManager)
        navigationAdapter.setSaveState(false)
        navigationAdapter.addPager("测试1", HomeFragment::class.java)
        navigationAdapter.addPager("测试2", TestFragment::class.java)
        navigationAdapter.addPager("测试3", TestFragment::class.java)
        main_root_pager.adapter = navigationAdapter
        bottomBar.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int) {
                main_root_pager.currentItem = pos
            }
        })
    }
}
