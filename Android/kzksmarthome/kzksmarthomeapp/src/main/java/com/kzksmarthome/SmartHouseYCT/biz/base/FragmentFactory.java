package com.kzksmarthome.SmartHouseYCT.biz.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.util.SparseArray;

import com.kzksmarthome.common.module.log.L;

/**
 * 
 * 类/接口注释
 * 
 * @author panrq
 * @createDate Dec 29, 2014
 *
 */
public class FragmentFactory {

    private SparseArray<BaseFragment> mFragmentCache = new SparseArray<BaseFragment>();

    public FragmentFactory() {
    }
    
    @SuppressWarnings("unchecked")
    public synchronized <T extends BaseFragment> T getFragment(Class<T> clazz, boolean useCache) {
        BaseFragment obj = mFragmentCache.get(clazz.hashCode());
        if (obj == null || !useCache) {
            try {
                Constructor<T> constructor = clazz  
                            .getDeclaredConstructor();
                constructor.setAccessible(true);//访问私有成员方法，这句很关键  
                T result = constructor.newInstance();
                if (useCache) {
                    L.d("Set the Fragment Cache, Fragment: %s", clazz.getName());
                    mFragmentCache.put(clazz.hashCode(), result);
                }
                return result;
            } catch (InstantiationException e) {
                L.w(e);
            } catch (IllegalAccessException e) {
                L.w(e);
            } catch (IllegalArgumentException e) {
                L.w(e);
            } catch (NoSuchMethodException e) {
                L.w(e);
            } catch (InvocationTargetException e) { 
                L.w(e);
            }
            throw new RuntimeException(String.format("Can not init fragment : " + clazz));
        }
        return (T) obj;
    }
    
    @SuppressWarnings("unchecked")
    public synchronized BaseFragment getBaseFragment(Class<?> clazz, boolean useCache) {
        BaseFragment obj = mFragmentCache.get(clazz.hashCode());
        if (obj == null || !useCache) {
            try {
                Constructor<?> constructor = clazz  
                            .getDeclaredConstructor();
                constructor.setAccessible(true);//访问私有成员方法，这句很关键  
                BaseFragment result = (BaseFragment)constructor.newInstance();
                if (useCache) {
                    L.d("Set the Fragment Cache, Fragment: %s", clazz.getName());
                    mFragmentCache.put(clazz.hashCode(), result);
                }
                return result;
            } catch (InstantiationException e) {
                L.w(e);
            } catch (IllegalAccessException e) {
                L.w(e);
            } catch (IllegalArgumentException e) {
                L.w(e);
            } catch (NoSuchMethodException e) {
                L.w(e);
            } catch (InvocationTargetException e) { 
                L.w(e);
            }
            throw new RuntimeException(String.format("Can not init fragment : %s", clazz));
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseFragment> T getFragmentFromCache(Class<T> clazz) {
        BaseFragment fragment = mFragmentCache.get(clazz.hashCode());
        return (T)fragment;
    }
    
    public <T extends BaseFragment> void removeFragmentFromCache(Class<T> clazz) {
        mFragmentCache.remove(clazz.hashCode());
    }
}
