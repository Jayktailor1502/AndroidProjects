package com.fs.antitheftsdk.camera;

import android.util.Log;

import com.fs.antitheftsdk.base.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializer {

    private ObjectSerializer() {
    }

    public static String serialize(Serializable obj) throws IOException {
        String images = "";
        if (obj == null) {
            images = "";
        } else {
            try {
                ByteArrayOutputStream serialObj = new ByteArrayOutputStream();
                ObjectOutputStream objStream = new ObjectOutputStream(serialObj);
                objStream.writeObject(obj);
                objStream.close();
                images = encodeBytes(serialObj.toByteArray());
            } catch (Exception e) {
                Log.e(Constants.EXCEPTION, e.toString());
            }
        }
        return images;
    }

    public static Object deserialize(String str) throws IOException {
        Object obj = null;
        if (str == null || str.length() == 0) {
            return null;
        } else {
            try {
                ByteArrayInputStream serialObj = new ByteArrayInputStream(decodeBytes(str));
                ObjectInputStream objStream = new ObjectInputStream(serialObj);
                obj = objStream.readObject();
            } catch (Exception e) {
                Log.e(Constants.EXCEPTION, e.toString());
            }
        }
        return obj;
    }

    public static String encodeBytes(byte[] bytes) {
        StringBuilder strBuf = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }

    public static byte[] decodeBytes(String str) {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length(); i += 2) {
            char c = str.charAt(i);
            bytes[i / 2] = (byte) ((c - 'a') << 4);
            c = str.charAt(i + 1);
            bytes[i / 2] += (c - 'a');
        }
        return bytes;
    }

}
