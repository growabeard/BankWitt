package android.bankwitt.com.bankwitt;

import android.app.DialogFragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        denominationAdapter = new DenominationAdapter(getDenominationData());
        refreshDenominationData(getDenominationData());
        denominationRecyclerView.setAdapter(denominationAdapter);

        setUpItemTouchHelper();
    }

    /**

     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method

     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view

     * background will be visible. That is rarely an desired effect.

     */

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(BankActivity.this, R.drawable.ic_close_light);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) BankActivity.this.getResources().getDimension(R.dimen.card_margin);
                initiated = true;
            }
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                DenominationAdapter testAdapter = (DenominationAdapter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                DenominationAdapter adapter = (DenominationAdapter)denominationRecyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }

                if (!initiated) {
                    init();
                }

                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();
                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        mItemTouchHelper.attachToRecyclerView(denominationRecyclerView);
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
        sqlHelper.addDenomination(denominationToAdd);

        denominationAdapter.addDenominationInRightPlace(denominationToAdd);
        denominationAdapter.notifyDataSetChanged();
    }

    private void insertInCorrectOrder(Denomination denomToAdd) {

    }

    private void saveDenominationData() {
        sqlHelper.saveAllDenominations(denominationAdapter.getAllChangedDatasets());

        List<Denomination> allDatasetsPendingDeletion = denominationAdapter.getAllDatasetsPendingDeletion();
        if(allDatasetsPendingDeletion.size() > 0) {
            sqlHelper.deleteDenominations(allDatasetsPendingDeletion);
        }
    }

    private void refreshDenominationData(ArrayList<Denomination> newDenomList) {
        denominationAdapter.notifyDataSetChanged();
    }

    private ArrayList<Denomination> getDenominationData() {
        ArrayList<Denomination> allDenominations = sqlHelper.getAllDenominations();

        return allDenominations;
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
            refreshDenominationData(getDenominationData());
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
