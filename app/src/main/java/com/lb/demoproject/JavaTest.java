package com.lb.demoproject;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lb.demoproject.JavaTest.Color.Blue;
import static com.lb.demoproject.JavaTest.Color.Green;
import static com.lb.demoproject.JavaTest.Color.Red;

/**
 * Created by Administrator on 2016-08-01.
 */
public class JavaTest {

    enum Color {
        Red,
        Green,
        Blue,
    }

    public static void main(String[] args) {
        System.out.println("Start test");

        List<A> list = new ArrayList<>();
        list.add(new A(2));
        list.add(new A(1));
        list.add(new A(3));
        list.add(new A(1));


        Collections.sort(list);

        for (A a : list) {
            a.print();
        }

        System.out.println(Red.ordinal());
        System.out.println(Green.ordinal());
        System.out.println(Blue.name());



        Object a = new A(1);
        Object aa = new AA(1);
        A aaa = null;


        System.out.println(a instanceof A);
        System.out.println(aa instanceof A);
        System.out.println(aaa instanceof A);


    }



    private static class A implements Comparable<A> {
        private int value;
        public A(int v) {
            value = v;
        }
        public void print() {
            System.out.println("v = "+value);
        }
        @Override
        public int compareTo(A another) {
            if (value == another.value) {
                return 0;
            }
            return value > another.value ? 1 : -1;
        }
    }

    private static class AA extends  A {

        public AA(int v) {
            super(v);
        }
    }















    private static boolean isInRangeValue(int value, String...compare) {
        for (String comp : compare) {
            if (comp.contains("-")) {
                String[] cpv = comp.split("-");
                int lv = Integer.valueOf(cpv[0]);
                int rv = Integer.valueOf(cpv[1]);
                if (value >= lv && value <= rv) {
                    return true;
                }
            } else if (value == Integer.valueOf(comp)) {
                return true;
            }
        }
        return false;
    }

    public static float getAttrValue(String tag) {
        String suffixW = "%w";
        String suffixH = "%h";
        if (tag != null) {
            try {
                if (tag.endsWith(suffixW)) {
                    if (tag.length() > 2) {
                        return Float.valueOf(tag.substring(0, tag.length() - 2));
                    }
                } else if (tag.endsWith(suffixH)) {
                    if (tag.length() > 2) {
                        return Float.valueOf(tag.substring(0, tag.length() - 2));
                    }
                } else {
                    return Float.valueOf(tag);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return 0F;
    }



    static interface InterA<T> {
        T getThisType();
    }

    static class Type1 {
        @Override
        public String toString() {
            return "type";
        }
    }

    static class TypeA extends Type1  {

        @Override
        public String toString() {
            return "typeA";
        }
    }
    static class TypeB extends Type1 {

        @Override
        public String toString() {
            return "typeB";
        }
    }

    static class TypeAA extends TypeA {
        @Override
        public String toString() {
            return "typeAA";
        }
    }

    static class Runner implements Runnable {
        private boolean flag = true;
        public void setFlag(boolean s) {
            this.flag = s;
        }
        @Override
        public void run() {
            System.out.println("start");
            while (flag) {

            }
            System.out.println("stop");
        }
    }

    private static class RuleCollections {
        private int communityId;
        private List<RulesItem> items = new ArrayList<>();

        public int getCommunityId() {
            return communityId;
        }

        public void setCommunityId(int communityId) {
            this.communityId = communityId;
        }

        public List<RulesItem> getItems() {
            return items;
        }

        public void setItems(List<RulesItem> items) {
            this.items = items;
        }
    }

    private static float format(float f) {
        return Math.round(f * 100) /100f;
    }
}
