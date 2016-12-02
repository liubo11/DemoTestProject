package test.apache;

/**
 * Created by LiuBo on 2016-11-21.
 */

public interface Handler {

    public void service(Request request, Response response);

    public void doGet(Request request, Response response);

    public void doPost(Request request, Response response);

}
