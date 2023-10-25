package edu.uef.doan;

import static edu.uef.doan.LoginActivity.mList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignmentSubmittedTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignmentSubmittedTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lv;
    View parentholder;
    String[]SubmittedAssignmentsName={"Lab01","Lab02","Lab03"};
    String[]SubmittedAssignmentsDetail={"Ngay giao ... \nNgay nop...",
            "Ngay giao ... \nNgay nop...","Ngay giao ... \nNgay nop..."};
    Integer[]bgColor={2,0,1};
    List rowSubmittedItems;
    List<RowItem> rowItems;
    public AssignmentSubmittedTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignmentSubmittedTab.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignmentSubmittedTab newInstance(String param1, String param2) {
        AssignmentSubmittedTab fragment = new AssignmentSubmittedTab();
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
        parentholder = inflater.inflate(R.layout.fragment_assignment_submitted_tab, container, false);

        lv = (ListView) parentholder.findViewById(R.id.ListViewSubmittedAssignment);

        rowItems = new ArrayList<RowItem>();
        rowSubmittedItems = mList;
        for (Object obj : rowSubmittedItems) {
            AssignmentList assignments = (AssignmentList) obj;
            Assignment assignment = assignments.getAssignment();
            if(((AssignmentList) obj).getAssignment().getSubmitTime() != null) {
                RowItem item = new RowItem(assignment.getTopic(),assignment.getSubmitTime(),assignment.getAnswer());
                item.setType(assignment.getCategory());
                rowItems.add(item);
            }
        }
        rowItems.sort(new Comparator<RowItem>(){
            @Override
            public int compare(RowItem t0, RowItem t1) {
                DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance();
                Date date1 = null;
                Date date0 = null;
                try {
                    date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(t0.getSubmitdate()+" "+ t1.getSubmitdate());
                    date0 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(t1.getSubmitdate()+" "+ t0.getSubmitdate());
                    Log.v("Sort submitted ","Debugging." +
                            "\nt0:"+t0.getSubmitdate()+
                            "\nt1:"+t1.getSubmitdate());
                } catch (ParseException e) {
                    Log.e("Sort submitted ","Something went wrong with sorting." +
                            "\nt0:"+t0.getSubmitdate()+
                            "\nt1:"+t1.getSubmitdate());
                    return 0;
                }
                int retVal = dateTimeComparator.compare(date1, date0);
//                            Log.v("retVal",String.valueOf(retVal));
                if(retVal == 0)
                    //both dates are equal
                    return 0;
                else if(retVal < 0)
                    //myDateOne is before myDateTwo
                    return 1;
                else if(retVal > 0)
                    //myDateOne is after myDateTwo
                    return -1;
                return 0;
            }
        });
        ArrayAdapter<RowItem> mAdapter =
                new CustomArrayAdapter(getContext(),R.id.assignmenttab_layout,rowItems);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position
                    , long l) {
                Intent intent = new Intent(getActivity(), ViewBaiTap.class);
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return parentholder;
    }
}