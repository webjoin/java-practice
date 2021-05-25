package com.iquickmove.common.util;

import com.alibaba.fastjson.JSON;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author echo
 * @Description
 * @Date 2019-05-04
 */
public class Beanutils {
    /**
     * 合并两个对象非空的属性  如果两个对象都有某个属性的值，则不会覆盖
     *
     * @param target
     * @param destination
     * @param <M>
     * @throws Exception
     */
    //merge two bean by discovering differences
    public static <M> void merges(M target, M destination) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());

        // Iterate over all the attributes
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {

            // Only copy writable attributes
            if (descriptor.getWriteMethod() != null) {
                Object originalValue = descriptor.getReadMethod()
                        .invoke(target);

                // Only copy values values where the destination values is null
                if (originalValue == null) {
                    Object defaultValue = descriptor.getReadMethod().invoke(
                            destination);
                    descriptor.getWriteMethod().invoke(target, defaultValue);
                }

            }
        }
    }

    /**
     * 从一个对象变为另一个对象
     * 需要注意的是  另个bean必须属性名一样  不然无法转化
     * @param obj
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T beanToBean(Object obj, Class<T> clz) {
        if (obj == null) {
            return null;
        } else {
            if (obj instanceof String) {
                return JSON.parseObject((String) obj, clz);
            } else {
                return JSON.parseObject(JSON.toJSONString(obj), clz);
            }
        }

    }


    /**
     * 获取两个对象同名属性内容不相同的列表
     * @param class1 对象1
     * @param class2 对象2
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    public static List<Map<String, Object>> compareTwoClass(Object class1, Object class2) throws ClassNotFoundException, IllegalAccessException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //获取对象的class
        Class<?> clazz1 = class1.getClass();
        Class<?> clazz2 = class2.getClass();
        //获取对象的属性列表
        Field[] field1 = clazz1.getDeclaredFields();
        Field[] field2 = clazz2.getDeclaredFields();
        //遍历属性列表field1
        for (int i = 0; i < field1.length; i++) {
            //遍历属性列表field2
            for (int j = 0; j < field2.length; j++) {
                //如果field1[i]属性名与field2[j]属性名内容相同
                if (field1[i].getName().equals(field2[j].getName())) {
                    field1[i].setAccessible(true);
                    field2[j].setAccessible(true);
                    //如果field1[i]属性值与field2[j]属性值内容不相同
                    if (!compareTwo(field1[i].get(class1), field2[j].get(class2))) {
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("属性", field1[i].getName());
                        map2.put("原值", field1[i].get(class1));
                        map2.put("新值", field2[j].get(class2));
                        list.add(map2);
                    }
                    break;
                }
            }
        }

        return list;
    }
    //对比两个数据是否内容相同
    public static boolean compareTwo(Object object1, Object object2) {

        if (object1 == null && object2 == null) {
            return true;
        }
        //以下注掉代码，看具体需求。因有时会出现源数据是没有进行赋值，因此是null；而通过EditText获取值的时候，虽然没有值，但是会变成""，但是本质是没有赋值的。
        //if (object1 == "" && object2 == null) {
        //    return true;
        //}
        //if (object1 == null && object2 == "") {
        //    return true;
        // }
        if (object1 == null && object2 != null) {
            return false;
        }
        if (object1.equals(object2)) {
            return true;
        }
        return false;
    }

    /**
     * 将一个对象非空的属性赋值给另一个对象，空值不处理
     *
     * @param origin
     * @param destination
     * @param <T>
     */
    public static <T> void mergeObject(T origin, T destination) {
        if (origin == null || destination == null)
            return;
        if (!origin.getClass().equals(destination.getClass()))
            return;

        Field[] fields = origin.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].setAccessible(true);
                Object value = fields[i].get(origin);
                if (null != value) {
                    fields[i].set(destination, value);
                }
                fields[i].setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }


    /**
     * bean 转化成 String map
     *
     * @param obj
     * @return
     */
    public static Map<String, String> beanStringMap(Object obj) {
        try {
            Map<String, String> map = new HashMap<>();

            JSON.parseObject(JSON.toJSONString(obj))
                    .entrySet()
                    .forEach(e -> map.put(e.getKey(), e.getValue().toString()));

            return map;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * bean 转化成 map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj) {
        try {
            Map<String, Object> map = new HashMap<>();

            JSON.parseObject(JSON.toJSONString(obj))
                    .entrySet()
                    .forEach(e -> map.put(e.getKey(), e.getValue()));

            return map;
        } catch (Exception e) {
            return null;
        }
    }


    public static Object getValueByFieldName(Object obj,String fieldName) {
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            if (fieldName.equals(f.getName())){
                f.setAccessible(true); // 设置些属性是可以访问的
                try {
                    return  f.get(obj);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将不同类型对象 相同属性名且值不为空的赋值给目标对象
     * @param source  源对象
     * @param dest  目标对象
     */
    public static void copyProperties(final Object source,final Object dest){
        try{
            //获取属性
            BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), Object.class);
            PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();

            BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(), Object.class);
            PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();


            Map<String,PropertyDescriptor> destPropertyDescriptorMap = new HashMap<>();
            for (PropertyDescriptor propertyDescriptor : destProperty) {
                destPropertyDescriptorMap.put(propertyDescriptor.getName(),propertyDescriptor);
            }

            for(int i=0;i<sourceProperty.length;i++){
                String name = sourceProperty[i].getName();
                PropertyDescriptor destProperties = destPropertyDescriptorMap.get(name);
                if(Objects.isNull(destProperties)) {
                    continue;
                }
                Object invoke = sourceProperty[i].getReadMethod().invoke(source);
                if(Objects.nonNull(invoke)) {
                    //调用source的getter方法和dest的setter方法
                    destProperties.getWriteMethod().invoke(dest, invoke);
                }
//                for(int j=0;j<destProperty.length;j++){
//                    if(name.equals(destProperty[j].getName())){
//                        Object invoke = sourceProperty[i].getReadMethod().invoke(source);
//                        if(Objects.nonNull(invoke)) {
//                            //调用source的getter方法和dest的setter方法
//                            destProperty[j].getWriteMethod().invoke(dest, invoke);
//                            break;
//                        }
//
//                    }
//                }
            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @Description：拷贝list元素对象，将origs中的元素信息，拷贝覆盖至dests中
     * @param origins 源list对象
     * @param destinations 目标list对象
     * @param origsElementTpe 源list元素类型对象
     * @param destElementTpe 目标list元素类型对象
     * @param <T1> 源list元素类型
     * @param <T2> 目标list元素类型
     */
    public static <T1,T2> void copyProperties(final List<T1> origins, final List<T2> destinations, Class<T1> origsElementTpe, Class<T2> destElementTpe){
        if(origins==null||destinations==null){
            return ;
        }
        if(destinations.size()!=0){
            //防止目标对象被覆盖，要求必须长度为零
            throw new RuntimeException("目标对象存在值");
        }
        try{
            for (T1 orig: origins) {
                T2 t = destElementTpe.newInstance();
                destinations.add(t);
                copyProperties(orig,t);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
