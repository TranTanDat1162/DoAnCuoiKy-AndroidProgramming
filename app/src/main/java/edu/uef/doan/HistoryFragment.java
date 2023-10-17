package edu.uef.doan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lv;
    View parentholder;
    String[]assignmentsName={"Lab04","Lab04","Lab05","Lab05"};
    String[]assignmentsDetail={"Ngay tao 10/11/2023 ","Ngay nop 12/11/2023","Ngay tao 15/11/2023","Ngay nop 20/11/2023 "};
    Integer[]bgColor={0,1,2,0};
    List<RowItem> rowItems;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentholder = inflater.inflate(R.layout.fragment_history, container, false);

        lv = (ListView) parentholder.findViewById(R.id.ListViewHistory);
        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < assignmentsName.length; i++) {
            RowItem item = new RowItem(assignmentsName[i], assignmentsDetail[i],bgColor[i]);
            rowItems.add(item);
        }
        ArrayAdapter<RowItem> mAdapter =
                new CustomArrayAdapter(getContext(),R.id.ListViewHistory,rowItems);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position
                    , long l) {
                TextView tv_selected = (TextView) view;
                Toast.makeText(parentholder.getContext(), tv_selected.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        return parentholder;
    }
}