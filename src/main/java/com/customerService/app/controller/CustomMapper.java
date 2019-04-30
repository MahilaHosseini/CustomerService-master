package com.customerService.app.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


public class CustomMapper {

    public static <T, K> T objectMapper(T destination, K source) {


        Class srcClass = source.getClass();
        Class destClass = destination.getClass();
        Method[] fromMethods = srcClass.getMethods();
        Method[] toMethods = destClass.getMethods();
        ArrayList<Method> srcGetterMethods = new ArrayList<>();
        ArrayList<Field> srcFieldsNames = new ArrayList(Arrays.asList(srcClass.getDeclaredFields()));
        srcFieldsNames.addAll(Arrays.asList(srcClass.getSuperclass().getDeclaredFields()));
        Map<Field, Method> srcFieldToGetterMap = new HashMap<>();


        //get getter methods
        for (Method sourceMethod : fromMethods)
            if (sourceMethod.getName().contains("get"))
                srcGetterMethods.add(sourceMethod);


        //generate a map of field name & corresponding getterMethod name
        for (Field field : srcFieldsNames)
            for (Method sourceMethod : srcGetterMethods)
                if (field.getName().equalsIgnoreCase(sourceMethod.getName().substring(3)))
                    srcFieldToGetterMap.put(field, sourceMethod);


        for (Field srcField : srcFieldsNames) {
            //lists of java classes
            if (srcField.getType().toString().contains("List") && Objects.isNull(srcField.getDeclaredAnnotation(MapTo.class))) {
                for (Method destMethod : toMethods) {
                    if (destMethod.getName().startsWith("set") && Objects.equals(destMethod.getName(), "set" + srcFieldToGetterMap.get(srcField).getName().substring(3))) {
                        try {
                            Object getterResult = srcFieldToGetterMap.get(srcField).invoke(source);
                            destMethod.invoke(destination, getterResult);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            //lists of our classes
            else if (srcField.getType().toString().contains("List") && !Objects.isNull(srcField.getDeclaredAnnotation(MapTo.class))) {
                List setterList = new ArrayList();
                Collection getterResult;
                try {
                    MapTo mapTo = srcField.getAnnotation(MapTo.class);
                    Class targetClass = mapTo.targetEntity();

                    getterResult = (Collection) srcFieldToGetterMap.get(srcField).invoke(source);

                    for (Object item : getterResult){
                        Object targetObj = targetClass.newInstance();
                        setterList.add(CustomMapper.objectMapper(targetObj, item));}

                    for (Method destinationMethod : toMethods) {
                        if (destinationMethod.getName().startsWith("set") && Objects.equals(destinationMethod.getName(), "set" + srcFieldToGetterMap.get(srcField).getName().substring(3))) {
                            try {
                                destinationMethod.invoke(destination, setterList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //non list but our classes fields
            else if (Objects.nonNull(srcField.getDeclaredAnnotation(MapTo.class))) {
                for (Method destinationMethod : toMethods) {
                    if (destinationMethod.getName().startsWith("set") && Objects.equals(destinationMethod.getName(), "set" + srcFieldToGetterMap.get(srcField).getName().substring(3))) {
                        try {
                            MapTo mapTo = srcField.getAnnotation(MapTo.class);
                            Object getterResult = srcFieldToGetterMap.get(srcField).invoke(source);
                            Class targetClass = mapTo.targetEntity();
                            Object targetObj = targetClass.newInstance();
                            destinationMethod.invoke(destination, CustomMapper.objectMapper(targetObj, getterResult));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            //
            else if (Objects.isNull(srcField.getDeclaredAnnotation(NotMap.class))) {
                for (Method destinationMethod : toMethods) {
                    if (destinationMethod.getName().startsWith("set") && Objects.equals(destinationMethod.getName(), "set" + srcFieldToGetterMap.get(srcField).getName().substring(3))) {
                        try {
                            Object getterResult = srcFieldToGetterMap.get(srcField).invoke(source);
                            if (Objects.nonNull(getterResult))
                                destinationMethod.invoke(destination, getterResult);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }


        }


        return destination;
    }


}

