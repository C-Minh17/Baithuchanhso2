package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalAmount;
    private ListView lvExpenses;
    private Button btnAddExpense;

    private ArrayList<Expense> expenseList;
    private ExpenseAdapter adapter;

    private final ActivityResultLauncher<Intent> addExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Expense newExpense = (Expense) result.getData().getSerializableExtra("NEW_EXPENSE");
                    if (newExpense != null) {
                        expenseList.add(newExpense);
                        adapter.notifyDataSetChanged();
                        updateTotalAmount();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> detailExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    boolean isDeleted = result.getData().getBooleanExtra("IS_DELETED", false);
                    int position = result.getData().getIntExtra("EXPENSE_POSITION", -1);
                    if (isDeleted && position != -1 && position < expenseList.size()) {
                        expenseList.remove(position);
                        adapter.notifyDataSetChanged();
                        updateTotalAmount();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View headerBar = findViewById(R.id.headerBar);
        View contentArea = findViewById(R.id.contentArea);
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
                if (contentArea != null) {
                    contentArea.setPadding(
                            basePaddingPx,
                            basePaddingPx,
                            basePaddingPx,
                            systemBars.bottom + basePaddingPx
                    );
                }
                return insets;
            });
        }

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        lvExpenses = findViewById(R.id.lvExpenses);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        initSampleData();

        adapter = new ExpenseAdapter(this, expenseList);
        lvExpenses.setAdapter(adapter);

        updateTotalAmount();

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            addExpenseLauncher.launch(intent);
        });

        lvExpenses.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
            intent.putExtra("EXPENSE_DATA", expenseList.get(position));
            intent.putExtra("EXPENSE_POSITION", position);
            detailExpenseLauncher.launch(intent);
        });
    }

    private void initSampleData() {
        expenseList = new ArrayList<>();
        expenseList.add(new Expense("1", "Ăn sáng", 30000, "Ăn uống", "12/10/2026", "Bún bò"));
        expenseList.add(new Expense("2", "Đổ xăng", 50000, "Đi lại", "12/10/2026", "Xăng A95"));
        expenseList.add(new Expense("3", "Mua sách", 120000, "Học tập", "13/10/2026", "Sách Lập trình Android"));
    }

    private void updateTotalAmount() {
        double total = 0;
        for (Expense expense : expenseList) {
            total += expense.getAmount();
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);
        tvTotalAmount.setText(formatter.format(total) + " đ");
    }
}