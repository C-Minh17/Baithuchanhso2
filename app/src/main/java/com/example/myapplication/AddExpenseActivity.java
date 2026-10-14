package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.UUID;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etTitle, etAmount, etNote;
    private Spinner spCategory;
    private DatePicker dpExpenseDate;
    private Button btnSaveExpense;
    private ImageView btnBackHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        View headerBar = findViewById(R.id.headerBar);
        View contentScrollView = findViewById(R.id.contentScrollView);
        View mainView = findViewById(R.id.main);

        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                int basePaddingPx = (int) (16 * getResources().getDisplayMetrics().density);
                if (headerBar != null) {
                    headerBar.setPadding(
                            systemBars.left + basePaddingPx,
                            systemBars.top + basePaddingPx,
                            systemBars.right + basePaddingPx,
                            basePaddingPx
                    );
                }
                if (contentScrollView != null) {
                    contentScrollView.setPadding(
                            0,
                            0,
                            0,
                            systemBars.bottom
                    );
                }
                return insets;
            });
        }

        btnBackHeader = findViewById(R.id.btnBackHeader);
        etTitle = findViewById(R.id.etExpenseTitle);
        etAmount = findViewById(R.id.etExpenseAmount);
        spCategory = findViewById(R.id.spCategory);
        dpExpenseDate = findViewById(R.id.dpExpenseDate);
        etNote = findViewById(R.id.etExpenseNote);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);

        if (btnBackHeader != null) {
            btnBackHeader.setOnClickListener(v -> finish());
        }

        String[] categories = {"Ăn uống", "Đi lại", "Học tập", "Giải trí", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spCategory.setAdapter(adapter);

        btnSaveExpense.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("Vui lòng nhập tên khoản chi!");
                return;
            }

            if (amountStr.isEmpty()) {
                etAmount.setError("Vui lòng nhập số tiền!");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    etAmount.setError("Số tiền phải lớn hơn 0!");
                    return;
                }
            } catch (NumberFormatException e) {
                etAmount.setError("Số tiền không hợp lệ!");
                return;
            }

            String category = spCategory.getSelectedItem().toString();
            String note = etNote.getText().toString().trim();

            int day = dpExpenseDate.getDayOfMonth();
            int month = dpExpenseDate.getMonth() + 1;
            int year = dpExpenseDate.getYear();
            String date = String.format("%02d/%02d/%d", day, month, year);

            Expense newExpense = new Expense(UUID.randomUUID().toString(), title, amount, category, date, note);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("NEW_EXPENSE", newExpense);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}