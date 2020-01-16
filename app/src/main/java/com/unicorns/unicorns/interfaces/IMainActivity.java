package com.unicorns.unicorns.interfaces;

import com.unicorns.unicorns.database.Unicorn;

/**
 * Interface for data binding created by Andreas Pribitzer 12.01.2020
 */

public interface IMainActivity {
    void addUnicorn();
    void createUnicorn(String name, String _age, String color);
    void showDeleteDialog(Unicorn unicorn);
}
