package com.pc.function;

import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
@FunctionalInterface
public interface JoinFunction<T,E extends List> {
        Object joinResult(T t, E es, String tF, String eF);
}
