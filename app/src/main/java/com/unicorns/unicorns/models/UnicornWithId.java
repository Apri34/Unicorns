package com.unicorns.unicorns.models;

import com.unicorns.unicorns.database.Unicorn;

public class UnicornWithId {
    private Unicorn unicorn;
    private String _id;

    public UnicornWithId(String _id, Unicorn unicorn) {
        this.unicorn = unicorn;
        this._id = _id;
    }

    public Unicorn getUnicorn() {
        return unicorn;
    }

    public String getId() {
        return _id;
    }
}
