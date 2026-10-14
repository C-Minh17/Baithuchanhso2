package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ExpenseDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAmount, tvCategory, tvDate, tvNote;
    private Button btnBack, btnDelete;
    private ImageView btnBackHeader;
    private int expensePosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

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
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvAmount = findViewById(R.id.tvDetailAmount);
        tvCategory = findViewById(R.id.tvDetailCategory);
        tvDate = findViewById(R.id.tvDetailDate);
        tvNote = findViewById(R.id.tvDetailNote);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDeleteExpense);

        if (btnBackHeader != null) {
            btnBackHeader.setOnClickListener(v -> finish());
        }

        Expense expense = (Expense) getIntent().getSerializableExtra("EXPENSE_DATA");
        expensePosition = getIntent().getIntExtra("EXPENSE_POSITION", -1);

        if (expense != null) {
            tvTitle.setText(expense.getTitle());
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setGroupingSeparator('.');
            DecimalFormat formatter = new DecimalFormat("#,###", symbols);
            tvAmount.setText(formatter.format(expense.getAmount()) + " đ");
            tvCategory.setText(expense.getCategory());
            tvDate.setText(expense.getDate());
            tvNote.setText(expense.getNote().isEmpty() ? "(Không có ghi chú)" : expense.getNote());
        }

        btnBack.setOnClickListener(v -> finish());

        btnDelete.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("IS_DELETED", true);
            resultIntent.putExtra("EXPENSE_POSITION", expensePosition);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}