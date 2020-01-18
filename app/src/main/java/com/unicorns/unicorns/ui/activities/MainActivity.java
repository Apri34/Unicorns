package com.unicorns.unicorns.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.unicorns.unicorns.R;
import com.unicorns.unicorns.database.Unicorn;
import com.unicorns.unicorns.interfaces.IMainActivity;
import com.unicorns.unicorns.ui.fragments.ConfirmDeleteDialog;
import com.unicorns.unicorns.ui.fragments.CreateUnicornFragment;
import com.unicorns.unicorns.ui.fragments.MainFragment;
import com.unicorns.unicorns.utils.JsonFormatter;
import com.unicorns.unicorns.viewmodels.UnicornViewModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

/**
 * MainActivity created by Andreas Pribitzer 12.01.2020
 * The UI is split into two fragments, the MainFragment and the CreateUnicornFragment
 */

public class MainActivity extends AppCompatActivity implements IMainActivity, ConfirmDeleteDialog.IDeleteUnicorn {

    private static final String KEY_MAIN_FRAGMENT = "MainFragment";
    private static final String KEY_CREATE_UNICORN_FRAGMENT = "CreateUnicornFragment";

    private UnicornViewModel mUnicornViewModel;

    private MainFragment mainFragment;
    private CreateUnicornFragment createUnicornFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Initialize ViewModel to get UI relevant data
        mUnicornViewModel = ViewModelProviders.of(this).get(UnicornViewModel.class);
        mUnicornViewModel.init();

        //Instantiate the fragments
        if(savedInstanceState == null) {
            mainFragment = new MainFragment();
            createUnicornFragment = new CreateUnicornFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_main_activity, mainFragment)
                    .commit();
        } else {
            mainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_MAIN_FRAGMENT);
            createUnicornFragment = getSupportFragmentManager().getFragment(savedInstanceState, KEY_CREATE_UNICORN_FRAGMENT) != null ?
                    (CreateUnicornFragment) getSupportFragmentManager().getFragment(savedInstanceState, KEY_CREATE_UNICORN_FRAGMENT) :
                    new CreateUnicornFragment();
        }
    }

    //This method is called when the Button in MainFragment is pressed
    @Override
    public void addUnicorn() {
        //Move to CreateUnicornFragment
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frame_layout_main_activity, createUnicornFragment)
                .commit();
    }

    //This method is called when the Button in CreateUnicornFragment is pressed
    @Override
    public void createUnicorn(String name, String _age, String color) {
        //Check if the unicorn has been given a correct name
        if(name.length() == 0) {
            Toast.makeText(this, "Unicorns usually have a name!", Toast.LENGTH_LONG).show();
            return;
        }

        //Check if the unicorn has been given a correct age
        if(_age.length() == 0) {
            Toast.makeText(this, "How old is " + name + "?", Toast.LENGTH_LONG).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(_age);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "This is not a correct age!", Toast.LENGTH_LONG).show();
            return;
        }

        if(age < 0) {
            Toast.makeText(this, "This is not a correct age!", Toast.LENGTH_LONG).show();
            return;
        }

        if(age > 30) {
            Toast.makeText(this, "Unicorns usually only get 30 years old!", Toast.LENGTH_LONG).show();
            return;
        }

        //Instantiate new Unicorn
        Unicorn unicorn = new Unicorn();
        unicorn.generateId();
        unicorn.setAge(age);
        unicorn.setName(name);
        unicorn.setColour(color);

        //Check if a unicorn like that already exists
        List<Unicorn> currentUnicorns = mUnicornViewModel.getUnicorns().getValue() != null ? mUnicornViewModel.getUnicorns().getValue() : new ArrayList<Unicorn>();
        if(currentUnicorns.contains(unicorn)) {
            Toast.makeText(this, "This unicorn already exists!", Toast.LENGTH_LONG).show();
            return;
        }

        //Add Unicorn to the database through ViewModel and return to MainFragment
        mUnicornViewModel.addNewUnicorn(unicorn);
        getSupportFragmentManager().popBackStack();
        createUnicornFragment.clearText();
    }

    //Method gets called when Item in RecyclerView is long-clicked
    @Override
    public void showDeleteDialog(Unicorn unicorn) {
        //Serialize Unicorn Pojo to pass to Fragment
        String jsonUnicorn;
        try {
            jsonUnicorn = JsonFormatter.getParsedUnicornLocal(unicorn).toString();
            //Show Dialog
            ConfirmDeleteDialog dialog = ConfirmDeleteDialog.newInstance(jsonUnicorn);
            dialog.show(getSupportFragmentManager(), "ConfirmDeleteDialog");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUnicorn(Unicorn unicorn) {
        mUnicornViewModel.deleteUnicorn(unicorn);
    }
}
