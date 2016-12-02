package test.apache;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiuBo on 2016-11-21.
 */

public class Context {
    private static Map<String,HttpHandler> contextMap = new HashMap<String,HttpHandler>();
    public static String contextPath = "";
    public static void load(){
        try{
            String path = "E:\\android-studio-workspace\\DemoProject\\testjavelib\\src\\main\\java\\test\\apache";
            Document doc = XmlUtils.load(path+"\\context.xml");
            Element root = doc.getDocumentElement();

            contextPath = XmlUtils.getAttribute(root,"context");
            Element[] handlers = XmlUtils.getChildrenByName(root, "handler");
            for(Element ele : handlers){
                String handle_class = XmlUtils.getChildText(ele, "handler-class");
                String url_pattern = XmlUtils.getChildText(ele, "url-pattern");

                Class<?> cls = Class.forName(handle_class);
                Object newInstance = cls.newInstance();
                if(newInstance instanceof HttpHandler){
                    contextMap.put(contextPath+url_pattern, (HttpHandler)newInstance);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public static HttpHandler getHandler(String key){
        return contextMap.get(key);
    }

}
