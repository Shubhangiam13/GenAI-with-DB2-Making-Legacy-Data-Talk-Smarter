// Add this import if using custom Look & Feel like FlatLaf
// import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class NaturalLanguageQueryGUI extends JFrame {

    private JTextArea queryInput, sqlDisplay;
    private JTable resultTable;
    private JLabel statusLabel;
    private JButton generateBtn, executeBtn, clearBtn;
    private Connection connection;

    public ModernQueryUI() {
        setTitle("GenAI SQL Assistant");
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // FlatLaf.install(); // Optional
        setLayout(new BorderLayout());
        initializeComponents();
        connectToDB();
    }

    private void initializeComponents() {
        // HEADER
        JLabel title = new JLabel("GenAI Natural Language SQL Assistant", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(240, 240, 255));
        JPanel header = new JPanel();
        header.setBackground(new Color(63, 81, 181));
        header.setBorder(new EmptyBorder(20, 10, 20, 10));
        header.setLayout(new BorderLayout());
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // QUERY PANEL
        queryInput = new JTextArea(3, 30);
        queryInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        queryInput.setBorder(new TitledBorder("Enter your question (Natural Language)"));

        sqlDisplay = new JTextArea(3, 30);
        sqlDisplay.setFont(new Font("Consolas", Font.PLAIN, 13));
        sqlDisplay.setEditable(false);
        sqlDisplay.setBorder(new TitledBorder("Generated SQL Query"));

        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        inputPanel.add(new JScrollPane(queryInput));
        inputPanel.add(new JScrollPane(sqlDisplay));

        // BUTTONS
        generateBtn = createStyledButton("Generate SQL");
        executeBtn = createStyledButton("Execute Query");
        clearBtn = createStyledButton("Clear");

        executeBtn.setEnabled(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(generateBtn);
        buttonPanel.add(executeBtn);
        buttonPanel.add(clearBtn);

        // QUERY + BUTTONS WRAPPER
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // TABLE FOR RESULTS
        resultTable = new JTable();
        resultTable.setFillsViewportHeight(true);
        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resultTable.setRowHeight(25);
        resultTable.setGridColor(Color.LIGHT_GRAY);
        resultTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        resultTable.getTableHeader().setBackground(new Color(220, 230, 240));

        JScrollPane tablePane = new JScrollPane(resultTable);
        tablePane.setBorder(new TitledBorder("Query Results"));
        add(tablePane, BorderLayout.SOUTH);

        // STATUS BAR
        statusLabel = new JLabel("Ready.");
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        // ACTIONS
        generateBtn.addActionListener(e -> generateQuery());
        executeBtn.addActionListener(e -> executeQuery());
        clearBtn.addActionListener(e -> clearAll());
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(76, 175, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new LineBorder(Color.DARK_GRAY, 1, true));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(56, 142, 60));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(76, 175, 80));
            }
        });

        return btn;
    }

    private void connectToDB() {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
            Properties props = new Properties();
            props.setProperty("user", "DELL");
            props.setProperty("password", "Kshitu@2211");
            connection = DriverManager.getConnection("jdbc:db2://localhost:25000/USER", props);
            statusLabel.setText("✅ Connected to DB2 database.");
        } catch (Exception e) {
            statusLabel.setText("❌ DB Connection Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void generateQuery() {
        String naturalText = queryInput.getText().trim();
        if (naturalText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a question.");
            return;
        }

        try {
            statusLabel.setText("🧠 Generating SQL...");
            // Dummy SQL for now — plug in your actual NLP code here
            String sql = new NaturalLanguageQueryExecutor().generateSqlQuery(naturalText);
            sqlDisplay.setText(sql);
            statusLabel.setText("✅ SQL Generated. You can now execute it.");
            executeBtn.setEnabled(true);
        } catch (Exception e) {
            statusLabel.setText("❌ SQL generation error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void executeQuery() {
        String sql = sqlDisplay.getText().trim();
        if (sql.isEmpty()) return;

        try {
            long startTime = System.currentTimeMillis();
            statusLabel.setText("⏳ Executing query...");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Build table model
            ResultSetMetaData meta = rs.getMetaData();
            Vector<String> columnNames = new Vector<>();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                columnNames.add(meta.getColumnName(i));
            }

            Vector<Vector<Object>> rows = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= colCount; i++) {
                    Object val = rs.getObject(i);
                    row.add(val == null ? "NULL" : val.toString());
                }
                rows.add(row);
            }

            resultTable.setModel(new DefaultTableModel(rows, columnNames));
            statusLabel.setText("✅ Executed in " + (System.currentTimeMillis() - startTime) + " ms. Rows: " + rows.size());

            stmt.close();
            rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Execution error: " + e.getMessage());
            statusLabel.setText("❌ Execution failed.");
        }
    }

    private void clearAll() {
        queryInput.setText("");
        sqlDisplay.setText("");
        resultTable.setModel(new DefaultTableModel());
        executeBtn.setEnabled(false);
        statusLabel.setText("Cleared.");
    }

    public static void main(String[] args) {
        // UIManager.put("Component.arc", 10); // Optional rounded corners
        SwingUtilities.invokeLater(() -> {
            new ModernQueryUI().setVisible(true);
        });
    }
}
