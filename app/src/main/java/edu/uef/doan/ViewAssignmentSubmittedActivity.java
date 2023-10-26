package edu.uef.doan;

import static android.content.ContentValues.TAG;
import static edu.uef.doan.LoginActivity.mList;
import static edu.uef.doan.LoginActivity.storage;
import static edu.uef.doan.LoginActivity.user;
import static edu.uef.doan.LoginActivity.userDocument;
import static edu.uef.doan.SignupActivity.sdf3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAssignmentSubmittedActivity extends AppCompatActivity {
    private static final int PICK_FILES_REQUEST_CODE = 1;

    private TextView attachmentTextView, attachmentTextView2;

    private TextView category;
    private TextView task;
    private EditText answerView;

    private List<Uri> selectedFiles = new ArrayList<>(); // Danh sách các tệp đã chọn
    private List<String> selectedFileNames = new ArrayList<>(); // Danh sách các tên tệp đã chọn
    private List<Uri> selectedFiles2 = new ArrayList<>(); // Danh sách các tệp đã chọn
    private List<String> selectedFileNames2 = new ArrayList<>(); // Danh sách các tên tệp đã chọn
    private ImageButton returnButton, attachmentButton;

    private Button buttonDone;
    FirebaseFirestore db;
    StorageReference storageRef = storage.getReference();
    AssignmentList selected_assignment;

    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bai_tap);
        db = FirebaseFirestore.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        returnButton = findViewById(R.id.returnButton);
        attachmentButton = findViewById(R.id.attachmentButton2);
        attachmentTextView = findViewById(R.id.attachmentTextView);
        attachmentTextView2 = findViewById(R.id.attachmentTextView2);
        buttonDone = findViewById(R.id.btnDone);
        answerView = findViewById(R.id.editTextAnswerField);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("assignment_pos");
        }
        selected_assignment = (AssignmentList) mList.get(Integer.parseInt(value));
        category = findViewById(R.id.textViewCategory);
        category.setText(selected_assignment.getAssignment().getCategory());
        answerView.setText(selected_assignment.getAssignment().getAnswer());
        task = findViewById(R.id.textViewTask);
        task.setText(selected_assignment.getAssignment().getTopic());
        try {
            Fetch(db,ViewAssignmentSubmittedActivity.this);
            Fetch2(db,ViewAssignmentSubmittedActivity.this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAssignmentSubmittedActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        attachmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedFileList();
            }
        });
        attachmentTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedFileList2();
            }
        });

        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textViewCategory =  ((TextView) findViewById(R.id.textViewCategory)).getText().toString();
                String textViewTask = ((TextView) findViewById(R.id.textViewTask)).getText().toString();

                updateDataInFirestore(textViewCategory, textViewTask);
            }
        });
    }
    private void updateDataInFirestore(String textViewCategory, String textViewTask) {
        // Lấy ID của người dùng hiện tại từ Firebase Authentication
        String id = userDocument.getId();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // Lấy assignmentId từ Intent
        AssignmentList assignment = (AssignmentList) mList.get(Integer.parseInt(value));
        String assignmentId = assignment.getId();

        assignment.getAssignment().setAnswer(answerView.getText().toString());
        assignment.getAssignment().setSubmitTime(sdf3.format(timestamp));
        assignment.getAssignment().setNumAnsAttachments(selectedFiles2.size());
        Log.v("Assignment data",assignment.getAssignment().toString());
        // Cập nhật dữ liệu vào Firestore trong bảng "assignments" của người dùng hiện tại
        db.collection("users").document(id)
                .collection("assignment").document(assignmentId)
                .set(assignment.getAssignment())
                .addOnSuccessListener(aVoid -> {
                    // Lưu trữ tệp đính kèm vào Firebase Storage
                    for (int i = 0; i < selectedFiles2.size(); i++) {
                        Uri fileUri = selectedFiles2.get(i);
                        String fileName = getFileName(fileUri);
                        // Tạo đường dẫn đến thư mục lưu trữ tệp đính kèm
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                                .child(id)
                                .child("attachments")
                                .child(assignmentId)
                                .child("answer")
                                .child(fileName);

                        // Upload tệp đính kèm lên Firebase Storage
                        storageRef.putFile(fileUri)
                                .addOnSuccessListener(taskSnapshot -> {
                                    // Lấy URL của tệp đính kèm đã được tải lên
                                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                        // Lưu thông tin tên tệp và URL vào Firestore
                                        Log.v("Upload answer attachments","Successful");
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    // Xử lý khi tệp đính kèm không thể được tải lên Firebase Storage
                                    Toast.makeText(ViewAssignmentSubmittedActivity.this, "Lỗi khi tải tệp lên Firebase Storage: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }// Điều hướng hoặc thực hiện các hành động cần thiết sau khi cập nhật dữ liệu thành công
                    Log.v("Asnwer","submitted");
                    PopulateList.UpdateL(db, ViewAssignmentSubmittedActivity.this);
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi dữ liệu không thể được cập nhật vào Firestore
                    Log.v("Asnwer","failed");
                    Toast.makeText(ViewAssignmentSubmittedActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
//    private void saveAttachmentInfoToFirestore(String name, String fileName, String fileUrl) {
//        String id = userDocument.getId();
//        db.collection("users").document(id).set(user);
//        // Tạo một Map chứa thông tin về tệp đính kèm
//        Map<String, Object> attachmentInfo = new HashMap<>();
//        attachmentInfo.put("fileName", fileName);
//        attachmentInfo.put("fileUrl", fileUrl);
//
//        // Lưu thông tin tệp đính kèm vào Firestore trong bảng "attachments"
//        db.collection("users").document(id).collection("assignment")
//                .add(attachmentInfo)
//                .addOnSuccessListener(documentReference -> {
//                    // Xử lý khi thông tin tệp đính kèm được lưu thành công
//                    Log.d(TAG, "Tệp đính kèm được lưu thành công: " + documentReference.getId());
//                })
//                .addOnFailureListener(e -> {
//                    // Xử lý khi thông tin tệp đính kèm không thể được lưu vào Firestore
//                    Log.w(TAG, "Lỗi khi lưu thông tin tệp đính kèm vào Firestore", e);
//                });
//    }


    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều tệp
        startActivityForResult(intent, PICK_FILES_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILES_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri fileUri = clipData.getItemAt(i).getUri();
                    selectedFiles2.add(fileUri); // Lưu trữ Uri của tệp đã chọn
                    selectedFileNames2.add(getFileName(fileUri));
                }
                attachmentTextView2.setText("Đã chọn " + selectedFiles2.size() + " tệp");
                attachmentTextView2.setVisibility(View.VISIBLE);
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                selectedFiles2.add(fileUri); // Lưu trữ Uri của tệp đã chọn
                String fileName = getFileName(fileUri);
                selectedFileNames2.add(fileName);
                attachmentTextView2.setText(fileName);
                attachmentTextView2.setVisibility(View.VISIBLE);
                if(selectedFiles2.size() > 1){
                    attachmentTextView2.setText("Đã chọn " + selectedFiles2.size() + " tệp");
                    attachmentTextView2.setVisibility(View.VISIBLE);
                }
            }
        }
//
//        // Xử lý khi người dùng nhấp vào TextView để kiểm tra danh sách các tệp đã chọn
//
//        attachmentTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showSelectedFileList();
//            }
//        });
    }

    private void showSelectedFileList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Danh sách các tệp đã chọn");

        View selectedFilesView = getLayoutInflater().inflate(R.layout.selected_files_list, null);
        ListView selectedFilesListView = selectedFilesView.findViewById(R.id.selectedFilesListView);

        ViewAssignmentSubmittedActivity.SelectedFilesAdapter adapter = new ViewAssignmentSubmittedActivity.SelectedFilesAdapter(this, selectedFileNames, selectedFiles);
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
    private void showSelectedFileList2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Danh sách các tệp đã chọn");

        View selectedFilesView = getLayoutInflater().inflate(R.layout.selected_files_list, null);
        ListView selectedFilesListView = selectedFilesView.findViewById(R.id.selectedFilesListView);

        ViewAssignmentSubmittedActivity.SelectedFilesAdapter adapter = new ViewAssignmentSubmittedActivity.SelectedFilesAdapter(this, selectedFileNames2, selectedFiles2);
        selectedFilesListView.setAdapter(adapter);

        selectedFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Xử lý khi người dùng nhấp vào một tệp
                openSelectedFile(selectedFiles2.get(position));
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
            Toast.makeText(ViewAssignmentSubmittedActivity.this, "Không có ứng dụng nào có thể mở file này", Toast.LENGTH_SHORT).show();
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
                    updateAttachmentTextView2(); // Cập nhật TextView sau khi xóa tệp
                }
            });

            return convertView;
        }
    }

    private void updateAttachmentTextView2() {
        if (selectedFileNames.isEmpty()) {
            attachmentTextView2.setText(""); // Nếu không có tệp nào, xóa nội dung TextView
            attachmentTextView2.setVisibility(View.INVISIBLE);
        } else {
            attachmentTextView2.setText("Đã chọn " + selectedFileNames2.size() + " tệp");
        }
    }
    private void updateAttachmentTextView() {
        if (selectedFileNames.isEmpty()) {
            attachmentTextView.setText(""); // Nếu không có tệp nào, xóa nội dung TextView
            attachmentTextView.setVisibility(View.INVISIBLE);
        } else {
            attachmentTextView.setText("Đã chọn " + selectedFileNames.size() + " tệp");
        }
    }

    public void Fetch(FirebaseFirestore db, Context cxt) throws IOException {
        StorageReference attachmentRef = storageRef.child(userDocument.getId()+"/attachments/"+selected_assignment.getId());
        Files.createDirectories(Paths.get(getFilesDir() + "/user/assignments/"+selected_assignment.getId()));
        File localFile = new File(getFilesDir() + "/user/assignments/"+selected_assignment.getId()+"/");
        attachmentRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
//                            Log.v("DebugDownloadAttachment",item.getName()+" downloaded at"
//                                            + localFile.getPath()+"/"+ item.getName());
                            File generated = new File(localFile.getPath()+"/"+ item.getName());

                            item.getFile(generated)
                                    .addOnSuccessListener(taskSnapshot ->{
                                        Log.v("DownloadAttachment",item.getName()+" downloaded at"
                                                + localFile.getPath()+"/"+ item.getName());
                                        Uri uri = FileProvider.getUriForFile(ViewAssignmentSubmittedActivity.this, getApplication().getPackageName()+ ".fileprovider", generated);
                                        selectedFiles.add(uri);
                                        selectedFileNames.add(generated.getName());

                                        updateAttachmentTextView();
                                    })
                                    .addOnFailureListener(exception ->{
                                        Log.v("DownloadAttachment",item.getName() +"failed");
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Log.e("ErrorDownloadAttachment",e.getMessage());
                    }
                }).addOnCompleteListener(runnable -> {
                    Log.v("DownloadAttachment",runnable.getResult().toString());
                });
//        attachmentRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
//            // Local temp file has been created
//            PopulateList.UpdateL(db,EditActivity.this);
//            Log.v("DownloadPfp","pfp downloaded");
//        }).addOnFailureListener(exception -> {
//            // Handle any errors
//            PopulateList.UpdateL(db,EditActivity.this);
//            Log.v("DownloadPfp","failed");
//        });
    }
    public void Fetch2(FirebaseFirestore db, Context cxt) throws IOException {
        StorageReference attachmentRef = storageRef.child(userDocument.getId()+"/attachments/"+selected_assignment.getId()+"/answer");
        Files.createDirectories(Paths.get(getFilesDir() + "/user/assignments/answer/"+selected_assignment.getId()));
        File localFile = new File(getFilesDir() + "/user/assignments/answer/"+selected_assignment.getId()+"/");
        attachmentRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
//                            Log.v("DebugDownloadAttachment",item.getName()+" downloaded at"
//                                            + localFile.getPath()+"/"+ item.getName());
                            File generated = new File(localFile.getPath()+"/"+ item.getName());

                            item.getFile(generated)
                                    .addOnSuccessListener(taskSnapshot ->{
                                        Log.v("DownloadAttachment",item.getName()+" downloaded at"
                                                + localFile.getPath()+"/"+ item.getName());
                                        Uri uri = FileProvider.getUriForFile(ViewAssignmentSubmittedActivity.this, getApplication().getPackageName()+ ".fileprovider", generated);
                                        selectedFiles2.add(uri);
                                        selectedFileNames2.add(generated.getName());

                                        updateAttachmentTextView2();
                                    })
                                    .addOnFailureListener(exception ->{
                                        Log.v("DownloadAttachment",item.getName() +"failed");
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Log.e("ErrorDownloadAttachment",e.getMessage());
                    }
                }).addOnCompleteListener(runnable -> {
                    Log.v("DownloadAttachment",runnable.getResult().toString());
                });
//        attachmentRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
//            // Local temp file has been created
//            PopulateList.UpdateL(db,EditActivity.this);
//            Log.v("DownloadPfp","pfp downloaded");
//        }).addOnFailureListener(exception -> {
//            // Handle any errors
//            PopulateList.UpdateL(db,EditActivity.this);
//            Log.v("DownloadPfp","failed");
//        });
    }

}