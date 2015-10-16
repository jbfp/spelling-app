package dk.jbfp.staveapp;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public int id;
    public String name;
    public int seed;
    public byte[] photo;

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.seed);

        if (photo == null) {
            return;
        }

        dest.writeInt(this.photo.length);
        dest.writeByteArray(this.photo);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.seed = in.readInt();
        this.photo = new byte[in.readInt()];
        in.readByteArray(this.photo);
    }

}
