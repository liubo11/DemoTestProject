package com.lb.cfg;

import android.content.Context;
import android.content.res.Resources;

import com.lb.demoproject.R;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016-04-25.
 */
public class Config {

    public boolean is_support_show_tab = false;
    public int max_user_num = 0;
    public String test_name = "null";

    public Config(Context context) {


        try {
            Resources rs = context.getResources();
            Field[] fields = getClass().getFields();

            String rBoolean = "R.bool.";

            for (Field f : fields) {

                Class<?> c = f.getType();



                if ("int".equals(c.getName())) {

                } else if ("boolean".equals(c.getName())) {

                } else if (String.class.getName().equals(c.getName())) {

                }



                String f_name = rBoolean + f.getName();

                Field[] RFs = R.bool.class.getFields();
                for (Field rf : RFs) {
                    if (rf.getName().equals(f_name)) {
                        int id = rf.getInt(R.bool.class);
                        System.out.println("rx value = "+rs.getBoolean(id));
                    }
                }

                System.out.println("config f_name="+f_name+" classType="+c.getName());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
