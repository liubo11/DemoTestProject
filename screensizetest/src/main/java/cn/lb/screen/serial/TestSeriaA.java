package cn.lb.screen.serial;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by LiuBo on 2016-11-11.
 */

public class TestSeriaA implements Parcelable {
    protected TestSeriaA(Parcel in) {
        //初始化数据
    }

    public static final Creator<TestSeriaA> CREATOR = new Creator<TestSeriaA>() {
        @Override
        public TestSeriaA createFromParcel(Parcel in) {
            String clz = in.readString();///
            try {
                Class inst = Class.forName(clz);
                Constructor constructor = inst.getConstructor(Parcel.class);
                Object o = constructor.newInstance(in);
                if (o instanceof TestSeriaA) {
                    return (TestSeriaA)o;
                }
                throw new IllegalArgumentException("参数错误");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return new TestSeriaA(in);
        }

        @Override
        public TestSeriaA[] newArray(int size) {

            return new TestSeriaA[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //保存类名
        dest.writeString(getClass().getName());
    }
}
