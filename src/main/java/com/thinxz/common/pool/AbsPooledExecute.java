package com.thinxz.common.pool;

import lombok.Setter;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 对象池默认实现
 *
 * @param <T>
 * @author thinxz
 */
public abstract class AbsPooledExecute<T> extends BasePooledObjectFactory<T> implements PooledExecute<T> {

    @Setter
    private int maxStorageConnection = 10;

    /**
     * 对象池
     */
    private GenericObjectPool<T> pool;

    @Override
    public PooledObject<T> wrap(T t) {
        return new DefaultPooledObject<>(t);
    }

    @Override
    public T borrowObject() throws Exception {
        return getObjectPool().borrowObject();
    }

    @Override
    public void returnObject(T t) {
        getObjectPool().returnObject(t);
    }

    /**
     * 初始化对象池
     *
     * @return
     */
    protected synchronized GenericObjectPool<T> getObjectPool() {
        // Pool配置
        if (pool == null) {
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            // 设置超时时间
            poolConfig.setMinIdle(2);
            if (maxStorageConnection > 0) {
                poolConfig.setMaxTotal(maxStorageConnection);
            }

            // 初始化池
            pool = new GenericObjectPool<>(this, poolConfig);
        }
        return pool;
    }
}
