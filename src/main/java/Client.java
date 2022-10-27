import net.link.oath.*;
import spark.utils.IOUtils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;
import java.util.List;

public class Client {


    public static void main(String[] args) throws IOException, URISyntaxException, InvalidOcraSuiteException, InvalidDataModeException, InvalidHashException, InvalidCryptoFunctionException, InvalidSessionException, InvalidQuestionException {
        OCRASuite ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA256-8:QA08");
        String key32 = "12345678901234567890123456789012";

        OCRA ocra = new OCRA(ocraSuite, key32.getBytes(),0,0,0);

        URL url = new URL("http://147.175.182.155:4567/challange");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.connect();


        InputStream in = connection.getInputStream();
        String questionResponse = IOUtils.toString(in);


       String sign = ocra.generate(0,questionResponse,"","",0);
        url = new URL("http://147.175.182.155:4567/validation?signature="+ sign);

        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.connect();

        int code = connection.getResponseCode();

        if(code != 401) {
            in = connection.getInputStream();
            questionResponse = IOUtils.toString(in);
            if(code==200){
                System.out.println("Successfully signed, messege is: "+ questionResponse);
            }
        }
        else{
            System.out.println("Error ");

        }

    }





}
