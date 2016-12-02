package test.apache;

import com.sun.net.httpserver.*;

/**
 * Created by LiuBo on 2016-11-21.
 */

public class FirstHandler extends HttpHandler{
    @Override
    public void doGet(Request request, Response response) {
        System.out.println("doGet");

        System.out.println(request.getParamter("aaa"));
        System.out.println(request.getParamter("bbb"));

        response.write("helloWorld.....");
    }


    @Override
    public void doPost(Request request, Response response) {
        System.out.println("doPost");
        System.out.println(request.getRequestBody());

        response.write("helloWorld.....");
    }

}
