package com.phoenix.base.privilege.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.phoenix.base.dal.dataobject.BaseDo;



public class PrivilegeObj extends HashMap<String, Object>{
    /**
     *
     */
    private static final long serialVersionUID = -1712500465465390138L;

    class ProperInfo {
        Method method;
        String propName;
    }

    static Map<String, List<ProperInfo>> mapping          = new HashMap<String, List<ProperInfo>>(); //以DO的类名为Key，Do可以get的属性方法和属性名列表



    public PrivilegeObj() {
        super();
    }

    public PrivilegeObj(PrivilegeObj pvgOld) {
        super();
        this.putAll(pvgOld);
    }

    protected List<ProperInfo> getProperList(Object obj) {
        String key = obj.getClass().getName();
        if (mapping.containsKey(key)) {
            return mapping.get(key);
        } else {
            List<ProperInfo> list = new ArrayList<ProperInfo>();

            Method[] methods = obj.getClass().getMethods();
            for (Method method : methods) {
                String name = method.getName();
                Class<?>[] paras = method.getParameterTypes();
                if (Modifier.isPublic(method.getModifiers()) && paras.length == 0) {
                    if (name.startsWith("get") && name.length() > 3) {
                        ProperInfo info = new ProperInfo();
                        info.method = method;
                        info.propName = methodName2PropName(name);
                        list.add(info);
                    }
                }
            }

            mapping.put(key, list);
            return list;
        }
    }

    @SuppressWarnings("unchecked")
    public void merge(Object obj) {
        if (obj instanceof BaseDo) {
            List<ProperInfo> properInfos = getProperList(obj);
            try {
                for (ProperInfo properInfo : properInfos) {
                    put(properInfo.propName, properInfo.method.invoke(obj, (Object[]) null));
                }
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("IllegalArgument", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("IllegalAccess", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("InvocationTarget", e);
            }
        } else if (obj instanceof Map) {
            putAll((Map<String, Object>) obj);
        } else {
            throw new RuntimeException("Not support type!");
        }
    }

    protected String methodName2PropName(String methodName) {
        return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
    }
}
