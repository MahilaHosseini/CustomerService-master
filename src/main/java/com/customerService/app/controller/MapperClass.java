package com.customerService.app.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


public class MapperClass {

    public static <E, D> E mapper(E destinationObj, D sourceObj) {


        Class sourceClass = sourceObj.getClass();

        Method[] sourceMethodArr = sourceClass.getMethods();

        ArrayList<Method> sourceMethods = new ArrayList<>(Arrays.asList(sourceMethodArr));
        ArrayList<Method> removalMethods = new ArrayList<>();
        for (Method sourceMethod : sourceMethods) {
            if (!sourceMethod.getName().contains("get")) {
                removalMethods.add(sourceMethod);
            }


        }

        sourceMethods.removeAll(removalMethods);

        Field[] sourceFieldNamesArray = sourceClass.getDeclaredFields();

        ArrayList<Field> sourceFieldNames = new ArrayList(Arrays.asList(sourceFieldNamesArray));

        sourceFieldNamesArray = sourceObj.getClass().getSuperclass().getDeclaredFields();

        sourceFieldNames .addAll(new ArrayList(Arrays.asList(sourceFieldNamesArray)));

        Map<Field , Method > sourceFieldsAndMethods = new HashMap<>();
        for(Field field : sourceFieldNames){

            for (Method sourceMethod : sourceMethods) {
                if(field.getName().equalsIgnoreCase(sourceMethod.getName().substring(3))){
                    sourceFieldsAndMethods.put(field,sourceMethod);
                }


            }
        }

        Class destinationClass = destinationObj.getClass();

        Method[] destinationMethods = destinationClass.getMethods();

        for (Field sourceFieldName :sourceFieldNames) {



                if (sourceFieldName.getType().toString().contains("List") && Objects.isNull(sourceFieldName.getDeclaredAnnotation(MapTo.class)) ) {

                    for (Method destinationMethod : destinationMethods) {

                        if (destinationMethod.getName().startsWith("set") && Objects.equals(destinationMethod.getName(), "set" + sourceFieldsAndMethods.get(sourceFieldName).getName().substring(3)) ) {

                            try {

                                Object invokedMethod = sourceFieldsAndMethods.get(sourceFieldName).invoke(sourceObj);
                                destinationMethod.invoke(destinationObj, invokedMethod);

                            } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            continue;

        } else if (sourceFieldName.getType().toString().contains("List") && Objects.nonNull(sourceFieldName.getDeclaredAnnotation(MapTo.class))) {
            try {
                MapTo mapTo = sourceFieldName.getAnnotation(MapTo.class);
                Class targetDestinationClass = mapTo.targetEntity();
                Object mapToDestionationObj = targetDestinationClass.newInstance();

                        List invokedMethod = (ArrayList)sourceFieldsAndMethods.get(sourceFieldName).invoke(sourceObj);
                        List mappedDtoList = new ArrayList();

                        for (Object invokedList : invokedMethod) {

                            mappedDtoList.add(MapperClass.mapper(mapToDestionationObj , invokedList));
                        }

                        for (Method destinationMethod : destinationMethods) {

                            if (destinationMethod.getName().startsWith("set") && Objects.equals(destinationMethod.getName(), "set" + sourceFieldsAndMethods.get(sourceFieldName).getName().substring(3))) {

                                try {

                                    destinationMethod.invoke(destinationObj, mappedDtoList);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    continue;

                } else if ( Objects.nonNull(sourceFieldName.getDeclaredAnnotation(MapTo.class))) {

                    for (Method destinationMethod : destinationMethods) {

                        if (destinationMethod.getName().startsWith("set") && Objects.equals(destinationMethod.getName(), "set" + sourceFieldsAndMethods.get(sourceFieldName).getName().substring(3))) {

                            try {

                                MapTo mapTo = sourceFieldName.getAnnotation(MapTo.class);
                                Object invokedMethod = sourceFieldsAndMethods.get(sourceFieldName).invoke(sourceObj);
                                Class targetDestinationClass = mapTo.targetEntity();
                                Object mapToDestinationObj = targetDestinationClass.newInstance();
                                Object mappedObj = MapperClass.mapper(mapToDestinationObj , invokedMethod);
                                destinationMethod.invoke(destinationObj, mappedObj);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }


                    continue;

                } else if(Objects.isNull(sourceFieldName.getDeclaredAnnotation(NotMap.class))) {
                    for (Method destinationMethod : destinationMethods) {

                        if (destinationMethod.getName().startsWith("set") && Objects.equals(destinationMethod.getName() , "set" + sourceFieldsAndMethods.get(sourceFieldName).getName().substring(3))) {

                            try {


                                Object invokedMethod = sourceFieldsAndMethods.get(sourceFieldName).invoke(sourceObj);
                               if(Objects.nonNull(invokedMethod))
                                destinationMethod.invoke(destinationObj, invokedMethod);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }



        }


        return destinationObj;
    }


}

