package edu.uef.doan;

import static edu.uef.doan.LoginActivity.mList;

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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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
    Date[] dates ={new Date(), new Date()};
//    LocalTime[] times = { LocalTime.now() , LocalTime.now()};
    String[] descriptions ={"Đã tạo bài tập IT_03 ","Đã nộp bài tập IT_03"};
    Integer[]bgColor={0,1,2,0};
    List<AssignmentList> rowSubmitItems,rowAssignedItems;
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
//        for (int i = 0; i < descriptions.length; i++) {
//            RowItem item = new RowItem(descriptions[i], bgColor[i],dates[i]);
//            rowItems.add(item);
//        }
        try {
//            rowAssignedItems = new ArrayList<RowItem>();
//            rowItems = new ArrayList<RowItem>();
//            rowAssignedItems.subList(0,rowAssignedItems.size()-1).sort(new Comparator<AssignmentList>(){
//                @Override
//                public int compare(AssignmentList t0, AssignmentList t1) {
//                    DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance();
//                    Date date1 = null;
//                    Date date0 = null;
//                    try {
//                        date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(t0.getAssignment().getCreateTime() +" "+ t0.getAssignment().getCreateTime());
//                        date0 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(t1.getAssignment().getCreateTime() +" "+ t1.getAssignment().getCreateTime());
//                    } catch (ParseException e) {
//                        Log.e("Sort history ","Something went wrong with sorting." +
//                                "\nt0:"+t0.getAssignment().getCreateTime()+
//                                "\nt1:"+t1.getAssignment().getCreateTime());
//                        return 0;
//                    }
//                    int retVal = dateTimeComparator.compare(date1, date0);
////                            Log.v("retVal",String.valueOf(retVal));
//                    if(retVal == 0)
//                        //both dates are equal
//                        return 0;
//                    else if(retVal < 0)
//                        //myDateOne is before myDateTwo
//                        return 1;
//                    else if(retVal > 0)
//                        //myDateOne is after myDateTwo
//                        return -1;
//                    return 0;
//                }
//            });
//            for (Object obj : rowAssignedItems) {
////                RowItem item = new RowItem(assignment.getTopic(),0,assignment.getCreateTime());
////                if(assignment.getSubmitTime() != null){
////                    rowSubmitItems.add(item);
////                }
////                rowAssignedItems.add(item);
//                AssignmentList assignments = (AssignmentList) obj;
//                Assignment assignment = assignments.getAssignment();
//                if(((AssignmentList) obj).getAssignment().getSubmitTime() != null) {
//                    rowSubmitItems.add(assignments);
//                }
//                rowSubmitItems.add(assignments);
//
//            }
            rowAssignedItems = mList;
            for (Object obj : rowAssignedItems) {
                AssignmentList assignments = (AssignmentList) obj;
                Assignment assignment = assignments.getAssignment();
                if(((AssignmentList) obj).getAssignment().getSubmitTime() != null) {
                    RowItem item = new RowItem(assignment.getTopic(),1,assignment.getSubmitTime());
                    rowItems.add(item);
                }
                RowItem item = new RowItem(assignment.getTopic(),0,assignment.getCreateTime());
                rowItems.add(item);
            }
            rowItems.sort(new Comparator<RowItem>(){
                @Override
                public int compare(RowItem t0, RowItem t1) {DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance();
                    Date date1 = null;
                    Date date0 = null;
                    try {
                        date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(t0.getDate()+" "+ t1.getDate());
                        date0 = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(t1.getDate()+" "+ t0.getDate());
                        Log.v("Sort history ","Debugging." +
                                "\nt0:"+t0.getTitle()+
                                "\nt1:"+t1.getTitle());
                    } catch (ParseException e) {
                        Log.e("Sort history ","Something went wrong with sorting." +
                                "\nt0:"+t0.getDate()+
                                "\nt1:"+t1.getDate());
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
        }
        catch (Exception e){
            Log.v("Assignment","Empty");
        }
        ArrayAdapter<RowItem> mAdapter =
                new CustomArrayAdapterNotification(getContext(),R.id.ListViewHistory,rowItems);
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                TextView tv_selected = (TextView) view;
//                Toast.makeText(parentholder.getContext(), tv_selected.getText(),
//                        Toast.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        return parentholder;
    }
}