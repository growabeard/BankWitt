package android.bankwitt.com.bankwitt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.plus.PlusOneButton;

import java.math.BigDecimal;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link AddDenominationListener} interface
 * to handle interaction events.
 * Use the {@link AddDenominationDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDenominationDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AddDenominationListener mListener;
    private EditText addDisplayAmount;
    private EditText addDecimalValue;
    private EditText addCount;

    public AddDenominationDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDenominationDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDenominationDialogFragment newInstance(String param1, String param2) {
        AddDenominationDialogFragment fragment = new AddDenominationDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public String getAddDisplayAmount() {
        return addDisplayAmount.getText().toString();
    }

    public Integer getAddCount() {
        return Integer.parseInt(addCount.getText().toString());
    }

    public BigDecimal getAddDecimalValue() {
        return new BigDecimal(addDecimalValue.getText().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddDenominationListener) {
            mListener = (AddDenominationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddDenominationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setTitle("Add Denomination");
        View view = inflater.inflate(R.layout.fragment_add_denomination_dialog, null);

        addDisplayAmount = (EditText) view.findViewById(R.id.addDisplayAmount);
        addDecimalValue = (EditText) view.findViewById(R.id.addDecimalValue);
        addCount = (EditText) view.findViewById(R.id.addCount);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
        // Add action buttons
        .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                        int count = Integer.parseInt(addCount.getText().toString());
                        String displayAmount = addDisplayAmount.getText().toString();
                        BigDecimal decValue = new BigDecimal(addDecimalValue.getText().toString());
                        Log.d("addStuff", "Added " + count + " of " + displayAmount + " at " + decValue);

                        mListener.addDenominationFromDialog(count, decValue, displayAmount);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AddDenominationDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface AddDenominationListener {
        public void addDenominationFromDialog(int count, BigDecimal value, String displayAmount);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

}
