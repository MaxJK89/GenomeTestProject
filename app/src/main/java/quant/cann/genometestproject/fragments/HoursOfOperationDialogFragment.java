package quant.cann.genometestproject.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import quant.cann.genometestproject.R;
import quant.cann.genometestproject.utils.TypeFaceHelper;

/**
 * Created by angboty on 10/3/2015.
 */
public class HoursOfOperationDialogFragment extends DialogFragment {
    @Bind(R.id.days_of_week_open)
    TextView txOpen;
    @Bind(R.id.closeMapRl)
    RelativeLayout closeRL;

    String monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    public static HoursOfOperationDialogFragment newInstance(
            String sunday, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday) {

        Bundle args = new Bundle();
        HoursOfOperationDialogFragment fragment = new HoursOfOperationDialogFragment();
        args.putString("monday", monday);
        args.putString("sunday", sunday);
        args.putString("tuesday", tuesday);
        args.putString("wednesday", wednesday);
        args.putString("thursday", thursday);
        args.putString("friday", friday);
        args.putString("saturday", saturday);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);

        if (getArguments() != null) {
            monday = getArguments().getString("monday");
            tuesday = getArguments().getString("tuesday");
            wednesday = getArguments().getString("wednesday");
            thursday = getArguments().getString("thursday");
            friday = getArguments().getString("friday");
            saturday = getArguments().getString("saturday");
            sunday = getArguments().getString("sunday");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hop_dialog_fragment, container, false);
        ButterKnife.bind(this, rootView);
        getDialog().setTitle("HOURS OF OPERATION");

        txOpen.setTypeface(new TypeFaceHelper(getActivity()).getOpenSansRegular());
        txOpen.setText(
                monday + "\n" +
                        tuesday + "\n" +
                        wednesday + "\n" +
                        thursday + "\n" +
                        friday
        );

        closeRL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }

}
