package cn.lb.screen.serial;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LiuBo on 2016-11-11.
 */

public class TestPar implements Parcelable {
    protected TestPar(Parcel in) {
    }

    public static final Creator<TestPar> CREATOR = new Creator<TestPar>() {
        @Override
        public TestPar createFromParcel(Parcel in) {
            return new TestPar(in);
        }

        @Override
        public TestPar[] newArray(int size) {
            return new TestPar[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
