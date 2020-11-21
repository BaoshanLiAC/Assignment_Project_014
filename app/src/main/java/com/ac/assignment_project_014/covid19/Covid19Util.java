package com.ac.assignment_project_014.covid19;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Util class offering static methods to conver data between Object and byte[]
 */
public class Covid19Util {

    protected static byte [] createByteArray( Object obj){
        byte [] bArray = null;
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objOstream = new ObjectOutputStream(baos);
            objOstream.writeObject(obj);
            bArray = baos.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bArray;

    }

    protected static ArrayList<Covid19ProvinceData> readByteArray(byte[] bytes){
        ArrayList<Covid19ProvinceData> list = null;
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))){
            list = (ArrayList<Covid19ProvinceData>) ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
