package com.neuifo.mangareptile.ui.base.search;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * <strong>描述: </strong> 搜索者。
 * <p><strong>创建人: </strong> kelin
 * <p><strong>创建时间: </strong> 2018/8/15  下午1:56
 * <p><strong>版本: </strong> v 1.0.0
 */
public interface Searcher {
    void search(String searchKey);

    void receiveAssociate(String keyword, List<String> associates);

    void setLocation(@NotNull String locationName);
}
