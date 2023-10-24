package edu.uef.doan;

import static edu.uef.doan.LoginActivity.mList;
import static edu.uef.doan.LoginActivity.user;
import static edu.uef.doan.LoginActivity.userDocument;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;


public class EditActivity extends AppCompatActivity {
    private Calendar startDateTime = Calendar.getInstance();
    private Calendar endDateTime = Calendar.getInstance();
    private Spinner tagSpinner;
    private EditText customTagEditText;
    private EditText title;
    private EditText topic;
    private TextView startDate;
    private TextView startTime;
    private TextView endDate;
    private TextView endTime;
    private Spinner category;
    private LinearLayout customTagLayout;
    private Button okButton;
    private TextView selectionPrompt;


    private List<String> tags;
    private ArrayAdapter<String> tagAdapter;
    private static final int PICK_FILES_REQUEST_CODE = 1;
    private static final int OPEN_FILE_REQUEST_CODE = 2;
    AppCompatButton btn1;
    private Uri selectedFUri;
    private ImageButton attachmentButton,return_btn;
    private TextView attachmentTextView;
    // Khai báo biến cho Firestore
    private FirebaseFirestore db;
    String value;

    private String getMimeType(String filePath) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private List<Uri> selectedFiles = new ArrayList<>(); // Danh sách các tệp đã chọn
    private List<String> selectedFileNames = new ArrayList<>(); // Danh sách các tên tệp đã chọn


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ImageButton startDatePickerButton = findViewById(R.id.startDatePickerButton);
        ImageButton startTimePickerButton = findViewById(R.id.startTimePickerButton);
        ImageButton endDatePickerButton = findViewById(R.id.endDatePickerButton);
        ImageButton endTimePickerButton = findViewById(R.id.endTimePickerButton);
        return_btn = findViewById(R.id.returnButton);
        final TextView displayStartDateTextView = findViewById(R.id.displayStartDateTextView);
        final TextView displayStartTimeTextView = findViewById(R.id.displayStartTimeTextView);
        final TextView displayEndDateTextView = findViewById(R.id.displayEndDateTextView);
        final TextView displayEndTimeTextView = findViewById(R.id.displayEndTimeTextView);
        tagSpinner = findViewById(R.id.tagSpinner);
        customTagEditText = findViewById(R.id.customTagEditText);
        customTagLayout = findViewById(R.id.customTagLayout);
        okButton = findViewById(R.id.okButton);
        selectionPrompt = findViewById(R.id.selectionPrompt);
        btn1=  findViewById(R.id.createnew_btn);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("assignment_pos");
        }
        AssignmentList selected_assignment = (AssignmentList) mList.get(Integer.parseInt(value));
        title = (EditText) findViewById(R.id.editText1);
        title.setText(selected_assignment.getAssignment().getTitle());
        topic = (EditText) findViewById(R.id.editText2);
        topic.setText(selected_assignment.getAssignment().getTopic());
        startDate = (TextView) findViewById(R.id.displayStartDateTextView);
        startDate.setText(selected_assignment.getAssignment().getStartDate());
        startTime = (TextView) findViewById(R.id.displayStartTimeTextView);
        startTime.setText(selected_assignment.getAssignment().getStartTime());
        endDate = (TextView) findViewById(R.id.displayEndDateTextView);
        endDate.setText(selected_assignment.getAssignment().getEndDate());
        endTime = (TextView) findViewById(R.id.displayEndTimeTextView);
        endTime.setText(selected_assignment.getAssignment().getEndTime());
        category = (android.widget.Spinner) findViewById(R.id.tagSpinner);
        String categoryString = selected_assignment.getAssignment().getCategory();
        category = (Spinner) findViewById(R.id.tagSpinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{categoryString});
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        // Khởi tạo Spinner với các mục "Chọn thể loại", "Essay", "Examination" và "Other"
        tags = new ArrayList<>();
        tags.add("None");
        tags.add("Essay");
        tags.add("Homework");
        tags.add("Other");


        tagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tags);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagAdapter);

        attachmentButton = findViewById(R.id.attachmentButton);
        attachmentTextView = findViewById(R.id.attachmentTextView);
        attachmentButton.setOnClickListener(v -> openFilePicker());
        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Lấy dữ liệu từ các trường nhập liệu
                String title = ((EditText) findViewById(R.id.editText1)).getText().toString();
                String topic = ((EditText) findViewById(R.id.editText2)).getText().toString();
                String startDate = ((TextView) findViewById(R.id.displayStartDateTextView)).getText().toString();
                String startTime = ((TextView) findViewById(R.id.displayStartTimeTextView)).getText().toString();
                String endDate = ((TextView) findViewById(R.id.displayEndDateTextView)).getText().toString();
                String endTime = ((TextView) findViewById(R.id.displayEndTimeTextView)).getText().toString();
                String category = tagSpinner.getSelectedItem().toString();

                // Kiểm tra xem đã chọn "Other" từ Spinner chưa
                if (category.equals("Other")) {
                    category = customTagEditText.getText().toString();
                }

                // Kiểm tra xem người dùng đã nhập đủ thông tin chưa
                if (title.isEmpty() || topic.isEmpty() || startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty() || category.isEmpty()) {
                    Toast.makeText(EditActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                } else {
                    // Lưu dữ liệu vào Firestore
                    updateDataInFirestore(title, topic, startDate, startTime, endDate, endTime, category);
                }
            }
        });

        // Bắt sự kiện khi chọn một mục từ Spinner
        tagSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTag = tags.get(position);
                // Nếu chọn "Other", hiển thị EditText và nút OK
                if (selectedTag.equals("Other")) {
                    customTagLayout.setVisibility(View.VISIBLE);
                } else {
                    customTagLayout.setVisibility(View.GONE);
                }
                // Ẩn hoặc hiển thị câu chú thích tùy thuộc vào việc có chọn mục hay không
                selectionPrompt.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do here
            }
        });
        // Bắt sự kiện khi nhấn nút OK
        // Bắt sự kiện khi nhấn nút "Done"
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu từ các trường nhập liệu
                String title = ((EditText) findViewById(R.id.editText1)).getText().toString();
                String topic = ((EditText) findViewById(R.id.editText2)).getText().toString();
                String startDate = ((TextView) findViewById(R.id.displayStartDateTextView)).getText().toString();
                String startTime = ((TextView) findViewById(R.id.displayStartTimeTextView)).getText().toString();
                String endDate = ((TextView) findViewById(R.id.displayEndDateTextView)).getText().toString();
                String endTime = ((TextView) findViewById(R.id.displayEndTimeTextView)).getText().toString();
                String category = tagSpinner.getSelectedItem().toString();

                // Kiểm tra xem đã chọn "Other" từ Spinner chưa
                if (category.equals("Other")) {
                    category = customTagEditText.getText().toString();
                }

                // Kiểm tra xem người dùng đã nhập đủ thông tin chưa
                if (title.isEmpty() || topic.isEmpty() || startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty() || category.isEmpty()) {
                    Toast.makeText(EditActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                } else {
                    // Cập nhật dữ liệu vào Firestore
                    updateDataInFirestore(title, topic, startDate, startTime, endDate, endTime, category);
                }
            }
        });


//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(CreateActivity.this,"Tao bai thanh cong " ,Toast.LENGTH_SHORT).show();
//
//                Intent intent=new Intent(CreateActivity.this,HomeActivity.class);
//                startActivity(intent);
//            }
//        });
        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        // Ngày bắt đầu
        startDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startDateTime.set(year, month, dayOfMonth);
                                if (startDateTime.before(Calendar.getInstance())) {
                                    // Nếu ngày bắt đầu trước ngày hiện tại, thiết lập ngày bắt đầu là ngày hiện tại
                                    startDateTime = Calendar.getInstance();
                                }
                                displayStartDateTextView.setText(startDateTime.get(Calendar.DAY_OF_MONTH) + "/" + (startDateTime.get(Calendar.MONTH) + 1) + "/" + startDateTime.get(Calendar.YEAR));
                            }
                        },
                        startDateTime.get(Calendar.YEAR),
                        startDateTime.get(Calendar.MONTH),
                        startDateTime.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


// Thời gian bắt đầu
        startTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                startDateTime.set(Calendar.MINUTE, minute);
                                if (startDateTime.before(Calendar.getInstance())) {
                                    // Nếu thời gian bắt đầu trước thời gian hiện tại, thiết lập thời gian bắt đầu là thời gian hiện tại
                                    startDateTime = Calendar.getInstance();
                                }
                                displayStartTimeTextView.setText(startDateTime.get(Calendar.HOUR_OF_DAY) + ":" + startDateTime.get(Calendar.MINUTE));
                            }
                        },
                        startDateTime.get(Calendar.HOUR_OF_DAY),
                        startDateTime.get(Calendar.MINUTE),
                        true);
                timePickerDialog.show();
            }
        });


// Ngày kết thúc
        endDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endDateTime.set(year, month, dayOfMonth);
                                if (endDateTime.before(startDateTime)) {
                                    // Nếu ngày kết thúc trước ngày bắt đầu, thiết lập ngày kết thúc là ngày bắt đầu
                                    endDateTime = (Calendar) startDateTime.clone();
                                }
                                displayEndDateTextView.setText(endDateTime.get(Calendar.DAY_OF_MONTH) + "/" + (endDateTime.get(Calendar.MONTH) + 1) + "/" + endDateTime.get(Calendar.YEAR));
                            }
                        },
                        endDateTime.get(Calendar.YEAR),
                        endDateTime.get(Calendar.MONTH),
                        endDateTime.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(startDateTime.getTimeInMillis());
                datePickerDialog.show();
            }
        });


// Thời gian kết thúc
        endTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                endDateTime.set(Calendar.MINUTE, minute);
                                if (endDateTime.before(startDateTime)) {
                                    // Nếu thời gian kết thúc trước thời gian bắt đầu, thiết lập thời gian kết thúc là thời gian bắt đầu
                                    endDateTime = (Calendar) startDateTime.clone();
                                }
                                displayEndTimeTextView.setText(endDateTime.get(Calendar.HOUR_OF_DAY) + ":" + endDateTime.get(Calendar.MINUTE));
                            }
                        },

                        endDateTime.get(Calendar.HOUR_OF_DAY),
                        endDateTime.get(Calendar.MINUTE),
                        true);
                timePickerDialog.show();
            }
        });

        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }
//    private void saveDataToFirestore(String title, String topic, String startDate, String startTime, String endDate, String endTime, String category) {
//        // Lấy ID của người dùng hiện tại từ Firebase Authentication
//        String id = userDocument.getId();
//        db.collection("users").document(id).set(user);
//
//        // Tạo một Map chứa dữ liệu để lưu vào Firestore
//        Map<String, Object> assignmentData = new HashMap<>();
//        assignmentData.put("title", title);
//        assignmentData.put("topic", topic  );
//        assignmentData.put("startDate", startDate);
//        assignmentData.put("startTime", startTime);
//        assignmentData.put("endDate", endDate);
//        assignmentData.put("endTime", endTime);
//        assignmentData.put("category", category);
//
//        // Lưu dữ liệu vào Firestore trong bảng "assignments" của người dùng hiện tại
//        db.collection("users").document(id).collection("assignment")
//                .add(assignmentData)
//                .addOnSuccessListener(documentReference -> {
//                    // Xử lý khi dữ liệu được lưu thành công
//                    Toast.makeText(EditActivity.this, "Dữ liệu đã được lưu thành công vào Firestore.", Toast.LENGTH_SHORT).show();
//                    // Điều hướng hoặc thực hiện các hành động cần thiết sau khi lưu dữ liệu thành công
//                    PopulateList.UpdateL(db,EditActivity.this);
//                })
//                .addOnFailureListener(e -> {
//                    // Xử lý khi dữ liệu không thể được lưu vào Firestore
//                    Toast.makeText(EditActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }

    // Phương thức cập nhật dữ liệu vào Firestore
    private void updateDataInFirestore(String title, String topic, String startDate, String startTime, String endDate, String endTime, String category) {
        // Lấy ID của người dùng hiện tại từ Firebase Authentication
        String userId = userDocument.getId();

        // Lấy assignmentId từ Intent
        AssignmentList assignment = (AssignmentList) mList.get(Integer.parseInt(value));
        String assignmentId = assignment.getId();
        // Tạo một Map chứa dữ liệu để cập nhật vào Firestore
//        Map<String, Object> updatedData = new HashMap<>();
//        updatedData.put("title", title);
//        updatedData.put("topic", topic);
//        updatedData.put("startDate", startDate);
//        updatedData.put("startTime", startTime);
//        updatedData.put("endDate", endDate);
//        updatedData.put("endTime", endTime);
//        updatedData.put("category", category);
        Assignment updatedData = new Assignment();
        updatedData.setTitle(title);
        updatedData.setTopic(topic);
        updatedData.setStartDate(startDate);
        updatedData.setStartTime(startTime);
        updatedData.setEndDate(endDate);
        updatedData.setEndTime(endTime);
        updatedData.setCategory(category);

        // Cập nhật dữ liệu vào Firestore trong bảng "assignments" của người dùng hiện tại
        db.collection("users").document(userId)
                .collection("assignment").document(assignmentId)
                .set(updatedData)
                .addOnSuccessListener(aVoid -> {
                    // Xử lý khi dữ liệu được cập nhật thành công
                    Toast.makeText(EditActivity.this, "Dữ liệu đã được cập nhật thành công.", Toast.LENGTH_SHORT).show();
                    // Điều hướng hoặc thực hiện các hành động cần thiết sau khi cập nhật dữ liệu thành công
                    PopulateList.UpdateL(db, EditActivity.this);
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi dữ liệu không thể được cập nhật vào Firestore
                    Toast.makeText(EditActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều tệp
        startActivityForResult(intent, PICK_FILES_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILES_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri fileUri = clipData.getItemAt(i).getUri();
                    selectedFiles.add(fileUri); // Lưu trữ Uri của tệp đã chọn
                    selectedFileNames.add(getFileName(fileUri));
                }
                attachmentTextView.setText("Đã chọn " + selectedFiles.size() + " tệp");
                attachmentTextView.setVisibility(View.VISIBLE);
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                selectedFiles.add(fileUri); // Lưu trữ Uri của tệp đã chọn
                String fileName = getFileName(fileUri);
                selectedFileNames.add(fileName);
                attachmentTextView.setText(fileName);
                attachmentTextView.setVisibility(View.VISIBLE);
            }
        }

        // Xử lý khi người dùng nhấp vào TextView để kiểm tra danh sách các tệp đã chọn


        attachmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedFileList();
            }
        });
    }


    private void showSelectedFileList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Danh sách các tệp đã chọn");

        View selectedFilesView = getLayoutInflater().inflate(R.layout.selected_files_list, null);
        ListView selectedFilesListView = selectedFilesView.findViewById(R.id.selectedFilesListView);

        SelectedFilesAdapter adapter = new SelectedFilesAdapter(this, selectedFileNames, selectedFiles);
        selectedFilesListView.setAdapter(adapter);

        selectedFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý khi người dùng nhấp vào một tệp
                openSelectedFile(selectedFiles.get(position));
            }
        });

        builder.setView(selectedFilesView);
        builder.setPositiveButton("Xong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Xử lý khi người dùng ấn nút "Xử lý"
                // Thêm mã xử lý ở đây
            }
        });

        builder.show();
    }


    private void openSelectedFile(Uri fileUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(EditActivity.this, "Không có ứng dụng nào có thể mở file này", Toast.LENGTH_SHORT).show();
        }
    }


    // Cập nhật TextView sau khi xóa tệp khỏi danh sách
    private void updateAttachmentTextView() {
        if (selectedFileNames.isEmpty()) {
            attachmentTextView.setText(""); // Nếu không có tệp nào, xóa nội dung TextView
            attachmentTextView.setVisibility(View.INVISIBLE);
        } else {
            attachmentTextView.setText("Đã chọn " + selectedFileNames.size() + " tệp");
        }
    }



    // Phương thức để lấy tên tệp từ Uri
    private String getFileName(Uri uri) {
        String result = null;
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // Kiểm tra xem cột DISPLAY_NAME có tồn tại không
                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (displayNameIndex >= 0) {
                    result = cursor.getString(displayNameIndex);
                } else {
                    // Nếu cột không tồn tại, xử lý tương ứng (ví dụ: lấy tên từ đường dẫn)
                    result = uri.getPath();
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }


    public class SelectedFilesAdapter extends BaseAdapter {
        private List<String> fileNames;
        private List<Uri> fileUris;
        private Context context;

        public SelectedFilesAdapter(Context context, List<String> fileNames, List<Uri> fileUris) {
            this.context = context;
            this.fileNames = fileNames;
            this.fileUris = fileUris;
        }

        @Override
        public int getCount() {
            return fileNames.size();
        }

        @Override
        public Object getItem(int position) {
            return fileNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_selected_file, parent, false);
            }

            TextView fileNameTextView = convertView.findViewById(R.id.fileNameTextView);
            ImageView deleteButton = convertView.findViewById(R.id.deleteButton);

            fileNameTextView.setText(fileNames.get(position));

            // Thêm sự kiện OnClickListener cho fileNameTextView
            fileNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Lấy Uri tương ứng với vị trí được nhấn trong danh sách
                    Uri fileUri = fileUris.get(position);
                    openSelectedFile(fileUris.get(position));
                    if (fileUri != null) {
                        // Xử lý khi người dùng nhấp vào tên tệp để mở tệp
                        try {
                            InputStream fileInputStream = context.getContentResolver().openInputStream(fileUri);
                            // Xử lý InputStream tại đây (ví dụ: đọc dữ liệu từ InputStream)
                            // Ví dụ: Đọc dữ liệu từ InputStream
                            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                            StringBuilder stringBuilder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                stringBuilder.append(line).append("\n");
                            }
                            // stringBuilder.toString() chứa nội dung của tệp, bạn có thể xử lý nó theo nhu cầu của mình
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Xử lý lỗi khi không thể mở tệp
                            // Ví dụ: Hiển thị thông báo lỗi cho người dùng
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Xử lý khi người dùng ấn nút X để xóa tệp
                    fileNames.remove(position);
                    fileUris.remove(position); // Xóa Uri tương ứng
                    notifyDataSetChanged(); // Cập nhật danh sách
                    updateAttachmentTextView(); // Cập nhật TextView sau khi xóa tệp
                }
            });

            return convertView;
        }
    }

}


