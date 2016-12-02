package test.apache;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Created by LiuBo on 2016-11-21.
 */

public class MyHttpHandler implements HttpHandler {
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpRequest request = new HttpRequest(httpExchange);
        HttpResponse response = new HttpResponse(httpExchange);
        Handler handler = Context.getHandler(request.getReuestURI().getPath());
        handler.service(request, response);

    }
}
