package edu.uef.doan;

import static edu.uef.doan.LoginActivity.mList;
import static edu.uef.doan.LoginActivity.userDocument;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignmentTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignmentTab extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lv;
    View parentholder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Assignment assignment = new Assignment();
//    String[] assignmentsName = {assignment.getTitle()};
//    String[] assignmentsDetail = {assignment.getTopic()};
//    Integer[] bgColor = {0};
    List<RowItem> rowItems;
    public AssignmentTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssignmentTab.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignmentTab newInstance(String param1, String param2) {
        AssignmentTab fragment = new AssignmentTab();
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
        parentholder = inflater.inflate(R.layout.fragment_assignment_tab, container, false);
        lv = (ListView) parentholder.findViewById(R.id.ListViewAssignment);
        try {
            rowItems = new ArrayList<RowItem>();
            for (Object obj : mList) {
                RowItem item = new RowItem();
                AssignmentList assignments = (AssignmentList) obj;
                Assignment assignment = assignments.getAssignment();
                item.setTitle(assignment.getTitle());
                item.setDesc(assignment.getTopic());
                item.setDate(assignment.getEndDate() + " - " + assignment.getEndTime());
                item.setType(assignment.getCategory());
                rowItems.add(item);
            }
        }
        catch (Exception e){
            Log.v("Assignment","Empty");
        }

//        for (int i = 0; i < assignmentsName.length; i++) {
//            RowItem item = new RowItem(assignmentsName[i], assignmentsDetail[i],bgColor[i]);
//            rowItems.add(item);
//        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position
                    , long l) {
                Log.v("listview","item clicked");
                Intent intent = new Intent(getActivity(), ViewBaiTap.class);
                String i = new String(String.valueOf(position));
                intent.putExtra("assignment_pos", i );
                startActivity(intent);
            }
        });
        ArrayAdapter<RowItem> mAdapter =
                new CustomArrayAdapter(getContext(),R.id.assignmenttab_layout,rowItems.subList(0,rowItems.size()-1)){
                    @Override
                    public View getView(final int position, View convertView, ViewGroup parent) {
                        View inflatedView = super.getView(position, convertView, parent);
                        // set a click listener
                        // TODO change "R.id.buttonId" to reference the ID value you set for the button's android:id attribute in foodlist.xml
                        ImageButton deletebtn = inflatedView.findViewById(R.id.delete_btn);
                        deletebtn.setFocusable(false);
                        deletebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AssignmentList selected_item_id = (AssignmentList) mList.get(position);
                                db.collection("users")
                                        .document(userDocument.getId())
                                        .collection("assignment").document(selected_item_id.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                PopulateList.UpdateL(db,getContext());
                                                Log.d("Delete", "DocumentSnapshot successfully deleted!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Delete", "Error deleting document", e);
                                            }
                                        });
//                                Toast.makeText(v.getContext(), "Button 1  clicked for row position=" + selected_item.getId(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        ImageButton editbtn = inflatedView.findViewById(R.id.edit_btn);
                        editbtn.setFocusable(false);
                        editbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), EditActivity.class);
                                String i = new String(String.valueOf(position));
                                intent.putExtra("assignment_pos", i );
                                startActivity(intent);
                            }
                        });
                        return inflatedView;

                    }
                };
        lv.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return parentholder;

    }
}