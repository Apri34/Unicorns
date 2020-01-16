package com.unicorns.unicorns.database;

import android.view.View;

import com.unicorns.unicorns.utils.ColorUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;

/**
 * Unicorn Model created by Andreas Pribitzer 13.01.2020
 * This class is used as both the Entity for the local SQLite database and for Json-Pojo conversion with Gson
 */

@Entity
public class Unicorn {

    @Id
    @NotNull
    @Property(nameInDb = "_id")
    private String _id;

    @NotNull
    @Property(nameInDb = "name")
    private String name;

    @NotNull
    @Property(nameInDb = "age")
    private int age;

    @NotNull
    @Property(nameInDb = "colour")
    private String colour;

    //Method to generate unique id
    public void generateId() {
        _id = UUID.randomUUID().toString();
    }

    @Generated(hash = 1160137022)
    public Unicorn(@NotNull String _id, @NotNull String name, int age,
            @NotNull String colour) {
        this._id = _id;
        this.name = name;
        this.age = age;
        this.colour = colour;
    }

    @Generated(hash = 1572971779)
    public Unicorn() {
    }

    @BindingAdapter("android:setColor")
    public static void setColor(View view, String color) {
        int _color = ColorUtil.getColor(view.getContext(), color);
        ((CardView) view).setCardBackgroundColor(_color);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof Unicorn)) return false;
        return this.age == ((Unicorn) obj).age &&
                this.name.equals(((Unicorn) obj).name) &&
                this._id.equals(((Unicorn) obj)._id) &&
                this.colour.equals(((Unicorn) obj).colour);
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}