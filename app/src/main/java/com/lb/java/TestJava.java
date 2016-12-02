package com.lb.java;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lb.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiuBo on 2016-09-29.
 */

public class TestJava {

    public static void main(String[] args) throws Exception {

        System.out.println("test start");

        String[] strs1 = {"零", "一","二","三","四","五","六","七","八","九","十"};
        String[] strs2 = {"0", "1","2","3","4","5","6","7","8","9","10"};

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd hh:mm");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        long time = System.currentTimeMillis();

        System.out.println("hh="+simpleDateFormat.format(new Date(time)));
        System.out.println("HH="+timeFormat.format(new Date(time)));

        System.out.println(Integer.toHexString((~1) & 1));
        System.out.println(Integer.toHexString(~2 & 2));

        /*Integer a = Integer.valueOf(1000); Integer b = Integer.valueOf(1000);
        Integer c = Integer.valueOf(100); Integer d = Integer.valueOf(100);
        Integer e = 10; Integer f = 10;
        System.out.println(a == b);
        System.out.println(c == d);
        System.out.println(e == f);
        print(a);
        print(b);
        print(c);
        print(d);*/



        System.out.println(formatFloatAccuracy(212123.123456F, 2));
        System.out.println(formatFloatAccuracy(22.123456F, 4));
        System.out.println(formatFloatAccuracy(22.123456F, 0));
        System.out.println(formatFloatAccuracy(22.123456F, 1));
        System.out.println(formatFloatAccuracy(22.1F, 2));
        System.out.println(formatFloatAccuracy(22, 2));

        System.out.println("test end");
    }

    public static String formatFloatAccuracy(float value, int decimal){
        if(decimal < 0 || decimal > 7){
            throw new IllegalArgumentException("精确位数不能小于0");
        }
        StringBuffer buffer = new StringBuffer("##0");

        if(decimal >= 1){
            buffer.append(".");
        }

        for(int i = 1; i <= decimal; i++){
            buffer.append("0");
        }

        DecimalFormat fnum = new DecimalFormat(buffer.toString());

        return fnum.format(value);
    }

    private static void print(Object o) {
        System.out.println(o.hashCode());
    }

    public static void printArray(List<Integer> list) {
        for (Integer integer : list) {
            System.out.println(integer);
        }
    }

    public static void testJson() {JSONObject jsonObject = new JSONObject();
        jsonObject.put("device", "wukong");
        jsonObject.put("result", new JSONArray());

        JSONArray thensn = new JSONArray();
        JSONObject objSn = new JSONObject();

        JSONArray array = new JSONArray();
        array.add("1");
        array.add("2");

        objSn.put("master", "808000010001");
        objSn.put("slave_sn", array);

        thensn.add(objSn);
        thensn.add(101);

        jsonObject.put("then_sn", thensn);

        System.out.println(jsonObject.toJSONString());

        JSONArray thensnArray = jsonObject.getJSONArray("then_sn");

        jsonObject.put("then_sn", thensnArray);

        for (int i = 0; i < thensnArray.size(); i++) {
            Object object = thensnArray.get(i);
            if (object instanceof JSONObject) {
                JSONObject jo = (JSONObject) object;
                System.out.println(jo);
            } else {
                int sn = (int) object;
                System.out.println(sn);
            }
        }

        //System.out.println(thensnArray.get(0));
        //System.out.println(thensnArray.get(1));
    }


    private static boolean test(String s) {
        return "1".equals(s);
    }

    public static String toChinesNormalNum(String[] srcs, int num) {
        if (srcs == null || num < 0 || num > 99) {
            return "error";
        }
        String first = srcs[0];
        if (first.equals("零")) {
            //中文
            if (num <= 10) {
                return srcs[num];
            } else if (num == 10) {
                return srcs[10];
            } else if (num > 10 && num < 20) {
                return srcs[10]+srcs[num % 10];
            } else if (num % 10 == 0) {
                return srcs[num / 10] + srcs[10];
            } else {
                return srcs[num / 10] + srcs[10] + srcs[num % 10];
            }
        } else {
            //其他语言
            if (num < 10) {
                return String.valueOf(num);
            } else {
                return srcs[num/10] + srcs[num % 10];
            }
        }
    }




    void test() {
        List<Sign> signs = new ArrayList<>();
        signs.add(new Plus());
        signs.add(new Sub());
        signs.add(new Multi());
        signs.add(new Divi());
        List<VirtualNum> virtual = new ArrayList<>();
        virtual.add(new Number(3));
        virtual.add(new Number(3));
        virtual.add(new Number(7));
        virtual.add(new Number(7));

        VirtualNum num = build(virtual);
        if (num != null) {
            System.out.println(num.print() + "="+num.getValue().print());
        } else {
            System.out.println("no result!");
        }
    }
    VirtualNum build(List<VirtualNum> nums) {
        return null;
    }

    abstract class VirtualNum {
        abstract String print();
        abstract VirtualNum getValue();
        abstract float toValue();
    }

    class Number extends VirtualNum {
        float v;
        public Number(float v) {
            this.v = v;
        }
        @Override
        String print() {
            return ""+ ((int) v);
        }

        @Override
        VirtualNum getValue() {
            return this;
        }

        @Override
        float toValue() {
            return v;
        }
    }

    class Equalter extends VirtualNum {
        Sign sign;
        VirtualNum a;
        VirtualNum b;


        public Equalter(VirtualNum a, VirtualNum b, Sign sign){
            this.a = a;
            this.b = b;
            this.sign = sign;
        }

        @Override
        String print() {
            return "("+a.print() + sign.print() + b.print() +")";
        }

        @Override
        VirtualNum getValue() {
            return sign.calcu(a, b);
        }

        @Override
        float toValue() {
            return sign.calcu(a, b).toValue();
        }
    }

    abstract class Sign {
        abstract VirtualNum calcu(VirtualNum a, VirtualNum b);
        abstract String print();
    }

    class Plus extends Sign {
        @Override
        VirtualNum calcu(VirtualNum a, VirtualNum b) {
            return new Number(a.toValue()+b.toValue());
        }
        @Override
        String print() {
            return "+";
        }
    }
    class Sub extends Sign {
        @Override
        VirtualNum calcu(VirtualNum a, VirtualNum b) {
            return new Number(a.toValue()-b.toValue());
        }
        @Override
        String print() {
            return "-";
        }
    }
    class Multi extends Sign {
        @Override
        VirtualNum calcu(VirtualNum a, VirtualNum b) {
            return new Number(a.toValue()*b.toValue());
        }
        @Override
        String print() {
            return "*";
        }
    }
    class Divi extends Sign {
        @Override
        VirtualNum calcu(VirtualNum a, VirtualNum b) {
            return new Number(a.toValue()+b.toValue());
        }
        @Override
        String print() {
            return "+";
        }
    }


    int get24(int i,int j,int k,int t)
    {
        int op1,op2,op3;
        int flag=0;
        char[] op= new char[]{'#','+','-','*','/'};

        for(op1=1;op1<=4;op1++)
            for(op2=1;op2<=4;op2++)
                for(op3=1;op3<=4;op3++)
                {
                    if(cal_model_1(i,j,k,t,op1,op2,op3)==24)
                    {
                        System.out.printf("((%d%c%d)%c%d)%c%d=24\n",i,op[op1],j,op[op2],k,op[op3],t);
                        flag=1;
                    }
                    if(cal_model_2(i,j,k,t,op1,op2,op3)==24)
                    {
                        System.out.printf("(%d%c(%d%c%d))%c%d=24\n",i,op[op1],j,op[op2],k,op[op3],t);
                        flag=1;
                    }
                    if(cal_model_3(i,j,k,t,op1,op2,op3)==24)
                    {
                        System.out.printf("%d%c(%d%c(%d%c%d))=24\n",i,op[op1],j,op[op2],k,op[op3],t);
                        flag=1;
                    }
                    if(cal_model_4(i,j,k,t,op1,op2,op3)==24)
                    {
                        System.out.printf("%d%c((%d%c%d)%c%d)=24\n",i,op[op1],j,op[op2],k,op[op3],t);
                        flag=1;
                    }
                    if(cal_model_5(i,j,k,t,op1,op2,op3)==24)
                    {
                        System.out.printf("(%d%c%d)%c(%d%c%d)=24\n",i,op[op1],j,op[op2],k,op[op3],t);
                        flag=1;
                    }
                }
        return flag;
    }
    float cal_model_1(float i,float j,float k,float t,int op1,int op2,int op3)//((A#B)#C)#D
    {
        float r1,r2,r3;
        r1=cal(i,j,op1);
        r2=cal(r1,k,op2);
        r3=cal(r2,t,op3);
        return r3;
    }
    float cal_model_2(float i,float j,float k,float t,int op1,int op2,int op3)//(A#(B#C))#D
    {
        float r1,r2,r3;
        r1=cal(j,k,op2);
        r2=cal(i,r1,op1);
        r3=cal(r2,t,op3);
        return r3;
    }
    float cal_model_3(float i,float j,float k,float t,int op1,int op2,int op3)//A#(B#(C#D))
    {
        float r1,r2,r3;
        r1=cal(k,t,op3);
        r2=cal(j,r1,op2);
        r3=cal(i,r2,op1);
        return r3;
    }
    float cal_model_4(float i,float j,float k,float t,int op1,int op2,int op3)//A#((B#C)#D)
    {
        float r1,r2,r3;
        r1=cal(j,k,op2);
        r2=cal(r1,t,op3);
        r3=cal(i,r2,op1);
        return r3;
    }
    float cal_model_5(float i,float j,float k,float t,int op1,int op2,int op3)//(A#B)#(C#D)
    {
        float r1,r2,r3;
        r1=cal(i,j,op1);
        r2=cal(k,t,op3);
        r3=cal(r1,r2,op2);
        return r3;
    }
    float cal(float x,float y,int op)   //1+//2-//3*//4/
    {
        switch(op)
        {
            case 1:return x+y;
            case 2:return x-y;
            case 3:return x*y;
            case 4:return x/y;
        }
        return 0;
    }

}
