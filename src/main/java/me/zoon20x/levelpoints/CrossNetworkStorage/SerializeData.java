package me.zoon20x.levelpoints.CrossNetworkStorage;

import java.io.*;
import java.util.Base64;

public class SerializeData {
    public static Object setData(String s) throws IOException, ClassNotFoundException {
        byte[] Byte_Data = Base64.getDecoder().decode(s);
        ObjectInputStream Object_Input_Stream =
                new ObjectInputStream(new ByteArrayInputStream(Byte_Data));
        Object Demo_Object = Object_Input_Stream.readObject();
        Object_Input_Stream.close();
        return Demo_Object;
    }
    public static String toString(Serializable Demo_Object) throws IOException {
        ByteArrayOutputStream Byte_Array_Output_Stream = new ByteArrayOutputStream();
        ObjectOutputStream Object_Output_Stream = new ObjectOutputStream(Byte_Array_Output_Stream);
        Object_Output_Stream.writeObject(Demo_Object);
        Object_Output_Stream.close();
        return Base64.getEncoder().encodeToString(Byte_Array_Output_Stream.toByteArray());
    }
}
