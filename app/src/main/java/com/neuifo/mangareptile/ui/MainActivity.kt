package com.neuifo.mangareptile.ui

import android.os.Bundle
import com.neuifo.data.cache.CacheFactory
import com.neuifo.mangareptile.R
import com.neuifo.mangareptile.ui.base.BaseActivity
import com.neuifo.mangareptile.ui.base.flyweight.adapter.CommonFragmentStatePagerAdapter
import com.neuifo.mangareptile.ui.home.HomeFragment
import com.neuifo.mangareptile.ui.home.TestFragment
import com.neuifo.mangareptile.utils.FinishHelper
import com.neuifo.mangareptile.utils.StyleHelper
import com.neuifo.mangareptile.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.ibrahimsn.lib.OnItemSelectedListener

class MainActivity : BaseActivity() {


    suspend fun initDb() {
        val data = runBlocking {
            val inputStream = assets.open("dmzj_db")
            async { ViewUtils.decodeJson(inputStream) }
        }.await()
        CacheFactory.instance.comicCache?.saveComicUpdate(data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTitleBar(my_awesome_toolbar, toolbar_title, null, false)
        supportActionBar?.hide()
        isDarkMode = true

        val inited = CacheFactory.instance.comicCache?.queryComicInitEd()
        if (inited == false) {
            StyleHelper.showProgress(this)

            runBlocking(Dispatchers.IO) {
                initDb()
            }

            StyleHelper.hideProgress(this)
        }


        val navigationAdapter = CommonFragmentStatePagerAdapter(supportFragmentManager)
        navigationAdapter.setSaveState(false)
        navigationAdapter.addPager("测试1", HomeFragment::class.java)
        navigationAdapter.addPager("测试2", TestFragment::class.java)
        navigationAdapter.addPager("测试3", TestFragment::class.java)
        main_root_pager.adapter = navigationAdapter
        main_root_pager.offscreenPageLimit = 3
        bottomBar.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelect(pos: Int) {
                main_root_pager.currentItem = pos
            }
        })
    }

    private val finishHelper: FinishHelper = FinishHelper.createInstance()


    override fun onBackPressed() {
        if (finishHelper.canFinish()) {
            super.onBackPressed()
        }
    }
}
