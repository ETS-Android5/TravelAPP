package com.example.team05;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class MultiplexingFragment extends Fragment {
    private String name;
    private String name2;
    private TextView mText;

    public MultiplexingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            name = arguments.getString("string");
            name2 = arguments.getString("string2");
        }
    }

    //Add fragment
    public static Fragment getMultiplexing(String string, String string2) {
        MultiplexingFragment multiplexingFragment = new MultiplexingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("string", string);
        bundle.putString("string2", string2);
        multiplexingFragment.setArguments(bundle);
        return multiplexingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_multiplexing, container, false);
        initView(inflate);
        return inflate;
    }


    private void initView(View inflate) {
        mText = (TextView) inflate.findViewById(R.id.mText);
        mText.setText(name);  //Parameters passed
    }

}
