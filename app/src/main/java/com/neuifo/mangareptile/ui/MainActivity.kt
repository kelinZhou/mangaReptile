package com.neuifo.mangareptile.ui

import android.os.Bundle
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.BaseActivity
import com.neuifo.mangareptile.ui.base.flyweight.adapter.CommonFragmentStatePagerAdapter
import com.neuifo.mangareptile.ui.base.home.HomeFragment
import com.neuifo.mangareptile.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_main.*
import me.ibrahimsn.lib.OnItemSelectedListener
import javax.net.ssl.TrustManagerFactory

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
        navigationAdapter.addPager("测试2", HomeFragment::class.java)
        navigationAdapter.addPager("测试3", HomeFragment::class.java)
        main_root_pager.adapter = navigationAdapter
        bottomBar.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int) {
                main_root_pager.currentItem = pos
            }
        })

        assets.open("lie.pem")

        ViewUtils.decodeBoldString("laksdjflkasdjfalsdjkaj")
        //TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    }
}
