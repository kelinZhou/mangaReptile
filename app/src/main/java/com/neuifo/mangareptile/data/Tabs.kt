package com.neuifo.mangareptile.data

import com.neuifo.mangareptile.ui.home.HomeFragment

enum class Tabs(val index: Int, val klazz: Class<*>) {

    //HOME(0, "首页", HomeFragment::class.java, R.drawable.selector_ic_main_home_tab),
    //COMPANIES(0, "公司", CompaniesFragment::class.java, R.drawable.selector_ic_main_companies_tab),
    //MSG(1, "消息", ConversationsFragment::class.java, R.drawable.selector_ic_main_conversations_tab),
    //PERSONAL(2, "我的", MeFragment::class.java, R.drawable.selector_ic_main_me_tab);
    HOME(0, HomeFragment::class.java),
    BOOK(1, HomeFragment::class.java),
    PERSONAL(1, HomeFragment::class.java)
    ;

    val tag: String
        get() = this.toString()
}
