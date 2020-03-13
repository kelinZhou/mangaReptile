package com.neuifo.domain.model.common;

import java.util.List;

/**
 * 附属关系的接口
 */
public interface DependKeyValuePairs<T extends KeyValuePair> extends KeyValuePair {

    List<T> getDependList();
}
