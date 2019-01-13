package pl;

import java.io.FileInputStream;

public class Properties {

    private static java.util.Properties properties = null;

    private static void init(){
        try {
            properties = new java.util.Properties();
            FileInputStream propertiesInputStream = new FileInputStream("properties.txt");
            properties.load(propertiesInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean boolValue(String property){
        if(properties == null) init();
        return Boolean.valueOf(properties.getProperty(property));
    }

    public static String stringValue(String property){
        if(properties == null) init();
        return properties.getProperty(property);
    }

    public static double doubleValue(String property){
        if(properties == null) init();
        return Double.valueOf(properties.getProperty(property));
    }

    public static int intValue(String property){
        if(properties == null) init();
        return Integer.valueOf(properties.getProperty(property));
    }

}
