package android.bankwitt.com.bankwitt;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chris on 10/27/2016.
 */
public class DenominationAdapter extends RecyclerView.Adapter<DenominationAdapter.ViewHolder> {
    private List<Denomination> dataset = new ArrayList<Denomination>();
    private List<Denomination> itemsPendingRemoval = new ArrayList<Denomination>();
    private boolean undoOn = true;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    private static final int PENDING_REMOVAL_TIMEOUT = 10000; // 3sec

    Map<Denomination, Runnable> pendingRunnables = new HashMap<Denomination, Runnable>(); // map of items to pending runnables,

    public int addDenominationInRightPlace(Denomination newDenom) {
        for(int i = 0; i < dataset.size(); i++) {
            Denomination denomination = dataset.get(i);
            if (denomination.getValue() > newDenom.getValue()) {
                dataset.add(i, newDenom);
                return i;
            }
        }
        dataset.add(newDenom);
        return dataset.size() - 1;
    }

    public List<Denomination> getAllChangedDatasets() {
        List<Denomination> changedDatasets = new ArrayList<Denomination>();

        for (Denomination denomToCheck : dataset) {
            if (denomToCheck.isChanged()) {
                changedDatasets.add(denomToCheck);
            }
        }

        return changedDatasets;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton addCountButton;
        public ImageButton subtractCountButton;
        public View thisText;
        public TextView valueView;
        public EditText countView;
        public TextView displayAmountView;
        public ViewHolder(View v) {
            super(v);
            this.thisText = v;
            this.valueView = (TextView)v.findViewById(R.id.amount);
            this.countView = (EditText)v.findViewById(R.id.count);
            this.displayAmountView = (TextView)v.findViewById(R.id.displayAmount);
            this.addCountButton = (ImageButton)v.findViewById(R.id.addCount);
            this.subtractCountButton = (ImageButton)v.findViewById(R.id.subtractCount);
        }
    }

    public DenominationAdapter(List<Denomination> inDataset) {
        this.dataset = inDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.denomination, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public List<Denomination> getAllCurrentDatasets(){
        return dataset;
    }

    public List<Denomination> getAllDatasetsPendingDeletion() { return itemsPendingRemoval; }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.valueView.setText(dataset.get(position).computeTotal());
        holder.countView.setText(Integer.toString(dataset.get(position).getCount()));
        holder.displayAmountView.setText(dataset.get(position).getDisplayAmount());
        holder.addCountButton.setOnClickListener(new AddClickListener(position, dataset.get(position)));
        holder.subtractCountButton.setOnClickListener(new SubtractClickListener(position, dataset.get(position)));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class AddClickListener implements View.OnClickListener {

        private final Denomination denomination;
        private final int position;

        public AddClickListener(int thatPosition, Denomination thatDenomination) {
            this.position = thatPosition;
            this.denomination = thatDenomination;
        }

        @Override
        public void onClick(View v) {
            denomination.setCount(denomination.getCount()+1);
            denomination.setChanged(true);
            notifyItemChanged(position);
        }
    }



    public void setUndoOn(boolean undoOn) {

        this.undoOn = undoOn;

    }



    public boolean isUndoOn() {

        return undoOn;

    }



    public void pendingRemoval(int position) {

        final Denomination item = dataset.get(position);

        if (!itemsPendingRemoval.contains(item)) {

            itemsPendingRemoval.add(item);

            // this will redraw row in "undo" state

            notifyItemChanged(position);

            // let's create, store and post a runnable to remove the item

            Runnable pendingRemovalRunnable = new Runnable() {

                @Override

                public void run() {

                    remove(dataset.indexOf(item));

                }

            };

            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);

            pendingRunnables.put(item, pendingRemovalRunnable);

        }

    }



    public void remove(int position) {

        Denomination item = dataset.get(position);

        if (itemsPendingRemoval.contains(item)) {

            itemsPendingRemoval.remove(item);

        }

        if (dataset.contains(item)) {

            dataset.remove(position);

            notifyItemRemoved(position);

        }

    }



    public boolean isPendingRemoval(int position) {

        Denomination item = dataset.get(position);

        return itemsPendingRemoval.contains(item);

    }

    public class SubtractClickListener implements View.OnClickListener {

        private final Denomination denomination;
        private final int position;

        public SubtractClickListener(int thatPosition, Denomination thatDenomination) {
            this.position = thatPosition;
            this.denomination = thatDenomination;
        }

        @Override
        public void onClick(View v) {
            denomination.setCount(denomination.getCount()-1);
            denomination.setChanged(true);
            notifyItemChanged(position);
        }
    }
}
