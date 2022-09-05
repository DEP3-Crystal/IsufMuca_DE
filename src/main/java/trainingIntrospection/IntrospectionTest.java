package main.java.trainingIntrospection;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntrospectionTest {
    public static void main(String[] args) {


        // Exercise - GET ALL FIELDS from  A extends B (A has fields a1,a2, B has fields b1,b2) - use introspection to get superclass of a given class

        ClassB myObject = new ClassB("lorem", "ipsum", "dolor", "sit");

        //this part of code reads only fields from ClassB
        Field[] fieldsFromClassB = myObject.getClass().getDeclaredFields();

        //this part of code reads only fields from ClassA
        Field[] fieldsFromClassA = myObject.getClass().getSuperclass().getDeclaredFields();

        //concatenating arrays
        List<Field> fields = Stream.concat(Arrays.stream(fieldsFromClassA), Arrays.stream(fieldsFromClassB)).collect(Collectors.toList());

        fields.forEach(f -> System.out.println(f.getName()));
    }
}
