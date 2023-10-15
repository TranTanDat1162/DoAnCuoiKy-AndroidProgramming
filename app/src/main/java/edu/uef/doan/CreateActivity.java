package edu.uef.doan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;



public class CreateActivity extends AppCompatActivity {
    private Calendar startDateTime = Calendar.getInstance();
    private Calendar endDateTime = Calendar.getInstance();
    private Spinner tagSpinner;
    private EditText customTagEditText;
    private LinearLayout customTagLayout;
    private Button okButton;
    private TextView selectionPrompt;


    private List<String> tags;
    private ArrayAdapter<String> tagAdapter;
    private static final int PICK_FILES_REQUEST_CODE = 2;




    private Uri selectedFUri;
    private ImageButton attachmentButton;
    private TextView attachmentTextView;
    private String getMimeType(String filePath) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ImageButton startDatePickerButton = findViewById(R.id.startDatePickerButton);
        ImageButton startTimePickerButton = findViewById(R.id.startTimePickerButton);
        ImageButton endDatePickerButton = findViewById(R.id.endDatePickerButton);
        ImageButton endTimePickerButton = findViewById(R.id.endTimePickerButton);
        final TextView displayStartDateTextView = findViewById(R.id.displayStartDateTextView);
        final TextView displayStartTimeTextView = findViewById(R.id.displayStartTimeTextView);
        final TextView displayEndDateTextView = findViewById(R.id.displayEndDateTextView);
        final TextView displayEndTimeTextView = findViewById(R.id.displayEndTimeTextView);
        tagSpinner = findViewById(R.id.tagSpinner);
        customTagEditText = findViewById(R.id.customTagEditText);
        customTagLayout = findViewById(R.id.customTagLayout);
        okButton = findViewById(R.id.okButton);
        selectionPrompt = findViewById(R.id.selectionPrompt);


        // Khởi tạo Spinner với các mục "Chọn thể loại", "Essay", "Examination" và "Other"
        tags = new ArrayList<>();
        tags.add("Chọn thể loại");
        tags.add("Essay");
        tags.add("Examination");
        tags.add("Other");


        tagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tags);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagAdapter);

        attachmentButton = findViewById(R.id.attachmentButton);
        attachmentTextView = findViewById(R.id.attachmentTextView);
        attachmentButton.setOnClickListener(v -> openFilePicker());

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
        okButton.setOnClickListener(v -> {
            // Lấy tag tùy chỉnh từ EditText
            String customTag = customTagEditText.getText().toString().trim();
            if (!customTag.isEmpty()) {
                // Thêm tag tùy chỉnh vào Spinner
                tags.add(customTag);
                tagAdapter.notifyDataSetChanged();
                // Chọn tag tùy chỉnh
                tagSpinner.setSelection(tagAdapter.getCount() - 1);
                // Ẩn EditText và nút OK
                customTagLayout.setVisibility(View.GONE);
                // Reset EditText
                customTagEditText.setText("");
                // Ẩn câu chú thích
                selectionPrompt.setVisibility(View.GONE);
                Toast.makeText(CreateActivity.this, "Tag đã được thêm vào Spinner.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateActivity.this, "Vui lòng nhập tag trước khi nhấn OK.", Toast.LENGTH_SHORT).show();
            }
        });


        // Ngày bắt đầu
        startDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateActivity.this,
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateActivity.this,
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateActivity.this,
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateActivity.this,
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
        attachmentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy đường dẫn đầy đủ của tệp tin từ TextView
                String filePath = attachmentTextView.getText().toString().trim();

                // Kiểm tra xem tệp tin có tồn tại không trước khi mở nó
                File file = new File(filePath);
                if (file.exists()) {
                    // Mở tệp tin sử dụng Intent
                    Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                    openFileIntent.setDataAndType(Uri.fromFile(file), getMimeType(filePath));
                    openFileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(openFileIntent);
                    } catch (ActivityNotFoundException e) {
                        // Xử lý trường hợp không tìm thấy ứng dụng để mở tệp tin
                        e.printStackTrace();
                        Toast.makeText(CreateActivity.this, "Không thể mở tệp tin.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateActivity.this, "Không tìm thấy tệp tin.", Toast.LENGTH_SHORT).show();
                }
            }
        });    }


    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều tệp tin
        startActivityForResult(intent, PICK_FILES_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILES_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                StringBuilder selectedFiles = new StringBuilder();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri fileUri = clipData.getItemAt(i).getUri();
                    String fileName = getFileName(fileUri);
                    selectedFiles.append(fileName).append("\n"); // Dùng "\n" để ngăn cách giữa các tên tệp tin
                }
                // Hiển thị danh sách các tệp tin đã chọn
                attachmentTextView.setText("Tệp đính kèm:\n" + selectedFiles.toString().trim());
                attachmentTextView.setVisibility(View.VISIBLE);
            } else {
                Uri fileUri = data.getData();
                String fileName = getFileName(fileUri);
                // Hiển thị tên tệp tin đã chọn
                attachmentTextView.setText("Tệp đính kèm:\n" + fileName);
                attachmentTextView.setVisibility(View.VISIBLE);
            }
        }
    }
    private String getFileName(Uri uri) {
        String result = null;
        String scheme = uri.getScheme();

        if (scheme != null && scheme.equals("content")) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(uri, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
                cursor.close();
            }
        }

        if (result == null) {
            DocumentFile documentFile = DocumentFile.fromSingleUri(this, uri);
            if (documentFile != null) {
                result = documentFile.getName();
            }
        }

        if (result == null) {
            // Fallback: Lấy tên file từ đường dẫn URI
            result = uri.getLastPathSegment();
        }

        return result;
    }

}

