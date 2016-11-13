package android.bankwitt.com.bankwitt;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chris on 10/27/2016.
 */
public class DenominationAdapter extends RecyclerView.Adapter<DenominationAdapter.ViewHolder> {
    private ArrayList<Denomination> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton addCountButton;
        public ImageButton subtractCountButton;
        public View thisText;
        public TextView amountView;
        public EditText countView;
        public TextView displayAmountView;
        public ViewHolder(View v) {
            super(v);
            this.thisText = v;
            this.amountView = (TextView)v.findViewById(R.id.amount);
            this.countView = (EditText)v.findViewById(R.id.count);
            this.displayAmountView = (TextView)v.findViewById(R.id.displayAmount);
            this.addCountButton = (ImageButton)v.findViewById(R.id.addCount);
            this.subtractCountButton = (ImageButton)v.findViewById(R.id.subtractCount);
        }
    }

    public DenominationAdapter(ArrayList<Denomination> inDataset) {
        this.dataset = inDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.denomination, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public ArrayList<Denomination> getAllCurrentDatasets(){
        return dataset;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.amountView.setText(dataset.get(position).computeTotal());
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
            Log.d("BankWitt","addClicked ---- " + denomination.getCount());
            denomination.setCount(denomination.getCount()+1);
            notifyItemChanged(position);
        }
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
            Log.d("BankWitt","subtractClicked ---- " + denomination.getCount());
            denomination.setCount(denomination.getCount()-1);
            notifyItemChanged(position);
        }
    }
}
