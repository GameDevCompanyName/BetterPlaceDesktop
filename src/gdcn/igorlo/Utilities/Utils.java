package gdcn.igorlo.Utilities;

import javafx.scene.image.Image;
import org.json.simple.JSONArray;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Random;

public class Utils {

    public static String[] jsonArrayToStringArray(JSONArray array) {
        String string = array.toString().substring(1, array.toString().length() - 1);
        return string.split(",");
    }

}