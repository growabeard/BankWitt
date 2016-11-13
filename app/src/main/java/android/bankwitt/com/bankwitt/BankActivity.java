package android.bankwitt.com.bankwitt;

import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class BankActivity extends AppCompatActivity implements AddDenominationDialogFragment.AddDenominationListener {

    private RecyclerView denominationRecyclerView;
    private LinearLayoutManager denominationLayoutManager;
    private DenominationAdapter denominationAdapter;
    private DenominationSQLHelper sqlHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqlHelper = new DenominationSQLHelper(this.getApplicationContext());
        setContentView(R.layout.activity_bank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDenominationData();
                Snackbar.make(view, "Saved", Snackbar.LENGTH_LONG)
                        .setAction("SEND", sendResultsToContact(getDenominationData())).show();
            }
        });

        denominationRecyclerView = (RecyclerView) findViewById(R.id.denominationList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        denominationRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        denominationLayoutManager = new LinearLayoutManager(this);
        denominationRecyclerView.setLayoutManager(denominationLayoutManager);

        // specify an adapter (see also next example)
        refreshDenominationData();
        denominationRecyclerView.setAdapter(denominationAdapter);


    }

    private View.OnClickListener sendResultsToContact(ArrayList<Denomination> denominationData) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //send message with lovely laid out thing
            }
        };
    }

    private void openSettings() {
        //TODO create settings
    }

    private void addDenomination(Denomination denominationToAdd) {
        //TODO add denomination
    }

    private void saveDenominationData() {
        sqlHelper.saveAllDenominations(denominationAdapter.getAllCurrentDatasets());
    }

    private void refreshDenominationData() {
        denominationAdapter = new DenominationAdapter(getDenominationData());
    }

    private ArrayList<Denomination> getDenominationData() {
        ArrayList<Denomination> allDenominations = sqlHelper.getAllDenominations();
        ArrayList<Denomination> denomSet = new ArrayList<Denomination>();

        Denomination firstDenom = new Denomination();
        firstDenom.setValue(new BigDecimal(1.00));
        firstDenom.setDisplayAmount("$1");
        firstDenom.setCount(3);
        denomSet.add(firstDenom);
        Denomination secondDenom = new Denomination();
        secondDenom.setValue(new BigDecimal(0.25));
        secondDenom.setDisplayAmount("$0.25");
        secondDenom.setCount(3);
        denomSet.add(secondDenom);

        return denomSet;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }
        if (id == R.id.action_refresh) {
            refreshDenominationData();
            return true;
        }
        if (id == R.id.action_add_denomination) {
            showAddDenominationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAddDenominationDialog() {
        DialogFragment addDenominationDialog = new AddDenominationDialogFragment();
        addDenominationDialog.show(getFragmentManager(), "Add Denomination");
    }


    @Override
    public void addDenominationFromDialog(int count, BigDecimal value, String displayAmount) {
        Denomination denominationToAdd = new Denomination();
        denominationToAdd.setCount(count);
        denominationToAdd.setDisplayAmount(displayAmount);
        denominationToAdd.setValue(value);
        addDenomination(denominationToAdd);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
