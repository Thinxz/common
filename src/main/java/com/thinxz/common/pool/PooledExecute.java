package com.thinxz.common.pool;

import org.apache.commons.pool2.PooledObject;

/**
 * 对象池
 *
 * @param <T>
 * @author thinxz
 */
public interface PooledExecute<T> {

    /**
     * 创建对象
     *
     * @return
     * @throws Exception
     */
    T create() throws Exception;

    /**
     * 封装对象
     *
     * @param t
     * @return
     */
    PooledObject<T> wrap(T t);

    /**
     * 从对象池中获取对象
     *
     * @return
     * @throws Exception
     */
    T borrowObject() throws Exception;

    /**
     * 将对象返还给对象池
     *
     * @param t
     */
    void returnObject(T t);
}
