// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.Cursor;
// import java.awt.Dimension;
// import java.awt.FlowLayout;
// import java.awt.Font;
// import java.awt.Graphics;
// import java.awt.GridLayout;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.ResultSet;
// import java.sql.ResultSetMetaData;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.Properties;
// import java.util.Vector;

// import javax.imageio.ImageIO;
// import javax.swing.BorderFactory;
// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JOptionPane;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;
// import javax.swing.JTextArea;
// import javax.swing.SwingUtilities;
// import javax.swing.UIManager;
// import javax.swing.border.EmptyBorder;
// import javax.swing.border.LineBorder;
// import javax.swing.border.TitledBorder;
// import javax.swing.table.DefaultTableModel;

// import com.formdev.flatlaf.FlatLightLaf;

// public class ModernSQLUI extends JFrame {

//     private JTextArea queryInput, sqlDisplay;
//     private JTable resultTable;
//     private JLabel statusLabel;
//     private JButton generateBtn, clearBtn;
//     private Connection connection;

//     public ModernSQLUI() {
//         setTitle("🧠 GenAI SQL Assistant");
//         setSize(1200, 800);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         try {
//             setContentPane(new BackgroundPanel("C:\\Users\\SHUBHANGI\\OneDrive\\Desktop\\AML\\GenAI-with-DB2-Making-Legacy-Data-Talk-Smarter\\src\\background.jpg")); // Use uploaded background
//         } catch (Exception e) {
//             e.printStackTrace();
//         }

//         setLayout(new BorderLayout());
//         initializeComponents();
//         connectToDB();
//     }

//     private void initializeComponents() {
//         Font font = new Font("Segoe UI", Font.PLAIN, 15);

//         JLabel title = new JLabel("🧠 GenAI SQL Assistant", JLabel.CENTER);
//         title.setFont(new Font("Segoe UI", Font.BOLD, 28));
//         title.setForeground(Color.WHITE);
//         title.setBorder(new EmptyBorder(20, 0, 20, 0));

//         JPanel topPanel = new JPanel(new BorderLayout());
//         topPanel.setOpaque(false);
//         topPanel.add(title, BorderLayout.CENTER);
//         add(topPanel, BorderLayout.NORTH);

//         queryInput = styledTextArea("Type your natural language query here...");
//         sqlDisplay = styledTextArea("SQL will appear here...");

//         JPanel ioPanel = new JPanel(new GridLayout(2, 1, 15, 15));
//         ioPanel.setOpaque(false);
//         ioPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
//         ioPanel.add(glassPanel("💬 Natural Language Query", queryInput));
//         ioPanel.add(glassPanel("🧾 Generated SQL", sqlDisplay));

//         generateBtn = createButton("⚙ Generate & Run", new Color(40, 167, 69));
//         clearBtn = createButton("🧹 Clear", new Color(220, 53, 69));

//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
//         buttonPanel.setOpaque(false);
//         buttonPanel.add(generateBtn);
//         buttonPanel.add(clearBtn);

//         JPanel centerPanel = new JPanel(new BorderLayout());
//         centerPanel.setOpaque(false);
//         centerPanel.add(ioPanel, BorderLayout.CENTER);
//         centerPanel.add(buttonPanel, BorderLayout.SOUTH);
//         add(centerPanel, BorderLayout.CENTER);

//         resultTable = new JTable();
//         resultTable.setFont(font);
//         resultTable.setRowHeight(26);
//         resultTable.setFillsViewportHeight(true);
//         resultTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

//         JScrollPane tableScroll = new JScrollPane(resultTable);
//         tableScroll.setBorder(new TitledBorder("📊 Query Results"));
//         tableScroll.setOpaque(false);
//         tableScroll.getViewport().setOpaque(false);

//         add(tableScroll, BorderLayout.SOUTH);

//         statusLabel = new JLabel("✅ Ready.");
//         statusLabel.setForeground(Color.WHITE);
//         statusLabel.setFont(font);
//         statusLabel.setBorder(new EmptyBorder(10, 15, 10, 10));
//         add(statusLabel, BorderLayout.PAGE_END);

//         generateBtn.addActionListener(e -> generateAndExecute());
//         clearBtn.addActionListener(e -> clearAll());
//     }

//     private JTextArea styledTextArea(String hint) {
//         JTextArea area = new JTextArea(hint);
//         area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//         area.setWrapStyleWord(true);
//         area.setLineWrap(true);
//         area.setOpaque(false);
//         area.setForeground(Color.WHITE);
//         area.setCaretColor(Color.WHITE);
//         return area;
//     }

//     private JPanel glassPanel(String title, JTextArea area) {
//         JPanel panel = new JPanel(new BorderLayout());
//         panel.setOpaque(false);
//         panel.setBorder(BorderFactory.createTitledBorder(
//                 new LineBorder(Color.WHITE, 1, true),
//                 title, TitledBorder.LEFT, TitledBorder.TOP,
//                 new Font("Segoe UI", Font.BOLD, 14), Color.WHITE
//         ));

//         JScrollPane scroll = new JScrollPane(area);
//         scroll.setOpaque(false);
//         scroll.getViewport().setOpaque(false);
//         scroll.setBorder(null);
//         scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

//         panel.add(scroll, BorderLayout.CENTER);
//         return panel;
//     }

//     private JButton createButton(String text, Color color) {
//         JButton btn = new JButton(text);
//         btn.setBackground(color);
//         btn.setForeground(Color.WHITE);
//         btn.setFocusPainted(false);
//         btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
//         btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//         btn.setBorder(new LineBorder(color.darker(), 2, true));
//         btn.setPreferredSize(new Dimension(160, 40));

//         btn.addMouseListener(new MouseAdapter() {
//             public void mouseEntered(MouseEvent e) {
//                 btn.setBackground(color.darker());
//             }

//             public void mouseExited(MouseEvent e) {
//                 btn.setBackground(color);
//             }
//         });

//         return btn;
//     }

//     private void connectToDB() {
//         try {
//             Class.forName("com.ibm.db2.jcc.DB2Driver");
//             Properties props = new Properties();
//             props.setProperty("user", "DELL");
//             props.setProperty("password", "Kshitu@2211");
//             connection = DriverManager.getConnection("jdbc:db2://localhost:25000/USER", props);
//             statusLabel.setText("✅ Connected to DB2 database.");
//         } catch (Exception e) {
//             statusLabel.setText("❌ DB Connection Failed: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }

//     private void generateAndExecute() {
//         String nlQuery = queryInput.getText().trim();
//         if (nlQuery.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "Please enter a query.");
//             return;
//         }

//         try {
//             statusLabel.setText("🔄 Generating SQL...");
//             String sql = LlamaModel.generateSqlQuery(nlQuery); // replace with actual model call
//             sqlDisplay.setText(sql);
//             runSQL(sql);
//         } catch (Exception e) {
//             statusLabel.setText("❌ Error: " + e.getMessage());
//         }
//     }

//     private void runSQL(String sql) throws SQLException {
//         Statement stmt = connection.createStatement();
//         ResultSet rs = stmt.executeQuery(sql);
//         ResultSetMetaData meta = rs.getMetaData();

//         Vector<String> cols = new Vector<>();
//         for (int i = 1; i <= meta.getColumnCount(); i++) {
//             cols.add(meta.getColumnName(i));
//         }

//         Vector<Vector<Object>> data = new Vector<>();
//         while (rs.next()) {
//             Vector<Object> row = new Vector<>();
//             for (int i = 1; i <= meta.getColumnCount(); i++) {
//                 row.add(rs.getObject(i));
//             }
//             data.add(row);
//         }

//         resultTable.setModel(new DefaultTableModel(data, cols));
//         statusLabel.setText("✅ Query executed. Rows: " + data.size());
//     }

//     private void clearAll() {
//         queryInput.setText("");
//         sqlDisplay.setText("");
//         resultTable.setModel(new DefaultTableModel());
//         statusLabel.setText("🧹 Cleared.");
//     }

//     public static void main(String[] args) {
//         try {
//             UIManager.setLookAndFeel(new FlatLightLaf());
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         SwingUtilities.invokeLater(() -> new ModernSQLUI().setVisible(true));
//     }

//     class BackgroundPanel extends JPanel {
//         private final BufferedImage image;

//         public BackgroundPanel(String path) throws Exception {
//             image = ImageIO.read(new File(path));
//         }

//         @Override
//         protected void paintComponent(Graphics g) {
//             super.paintComponent(g);
//             g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
//         }
//     }
// }

// // Mock LLaMA Model
// class LlamaModel {
//     public static String generateSqlQuery(String input) {
//         return input.toLowerCase().contains("employee")
//                 ? "SELECT * FROM EMPLOYEES"
//                 : "SELECT * FROM SAMPLE_TABLE";
//     }
// }
// import java.awt.BorderLayout;
// import java.awt.Color;
// import java.awt.Component;
// import java.awt.Cursor;
// import java.awt.Dimension;
// import java.awt.FlowLayout;
// import java.awt.Font;
// import java.awt.Graphics;
// import java.awt.Image;
// import java.awt.Toolkit;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.ResultSet;
// import java.sql.ResultSetMetaData;
// import java.sql.Statement;

// import javax.swing.BorderFactory;
// import javax.swing.Box;
// import javax.swing.BoxLayout;
// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JPanel;
// import javax.swing.JScrollPane;
// import javax.swing.JTable;
// import javax.swing.JTextArea;
// import javax.swing.SwingUtilities;
// import javax.swing.border.EmptyBorder;
// import javax.swing.table.DefaultTableModel;

// public class GenAISQLAssistant extends JFrame {
//     private JTextArea inputArea;
//     private JTextArea sqlArea;
//     private JTable resultTable;
//     private JLabel statusLabel;

//     public GenAISQLAssistant() {
//         setTitle("GenAI SQL Assistant");
//         setSize(1200, 750);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);
//         setResizable(false);

//         // Load background image
//         Image bgImage = Toolkit.getDefaultToolkit().getImage("C:\\Users\\SHUBHANGI\\OneDrive\\Desktop\\AML\\GenAI-with-DB2-Making-Legacy-Data-Talk-Smarter\\src\\main\\java\\bg.jpg"); // <-- Update path here if needed
//         BackgroundPanel bgPanel = new BackgroundPanel(bgImage);
//         bgPanel.setLayout(new BorderLayout());

//         // Main panel on the right
//         JPanel rightPanel = new JPanel();
//         rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
//         rightPanel.setOpaque(false);
//         rightPanel.setBorder(new EmptyBorder(30, 30, 30, 50));

//         JLabel title = new JLabel("🧠 GenAI SQL Assistant");
//         title.setFont(new Font("Segoe UI", Font.BOLD, 26));
//         title.setForeground(Color.WHITE);
//         title.setAlignmentX(Component.RIGHT_ALIGNMENT);
//         rightPanel.add(title);
//         rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

//         // Input Area
//         inputArea = new JTextArea(5, 40);
//         configureTextArea(inputArea);
//         rightPanel.add(createLabeledBox("📝 Natural Language Query", inputArea));

//         // SQL Area
//         sqlArea = new JTextArea(3, 40);
//         configureTextArea(sqlArea);
//         rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
//         rightPanel.add(createLabeledBox("🧾 Generated SQL", sqlArea));

//         // Buttons
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//         buttonPanel.setOpaque(false);
//         JButton generateBtn = createStyledButton("Generate & Run", new Color(0, 100, 100));
//         JButton clearBtn = createStyledButton("Clear", new Color(0, 100, 100));

//         generateBtn.addActionListener(e -> generateAndRunSQL());
//         clearBtn.addActionListener(e -> clearAll());

//         buttonPanel.add(generateBtn);
//         buttonPanel.add(clearBtn);
//         rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
//         rightPanel.add(buttonPanel);

//         // Table for Results
//         resultTable = new JTable();
//         JScrollPane tableScrollPane = new JScrollPane(resultTable);
//         tableScrollPane.setPreferredSize(new Dimension(1100, 220));
//         tableScrollPane.setBorder(BorderFactory.createTitledBorder("📊 Query Output Table"));

//         // Status Label
//         statusLabel = new JLabel(" ");
//         statusLabel.setForeground(Color.WHITE);
//         statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
//         statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

//         // Add all to background panel
//         bgPanel.add(statusLabel, BorderLayout.NORTH);
//         bgPanel.add(rightPanel, BorderLayout.CENTER);
//         bgPanel.add(tableScrollPane, BorderLayout.SOUTH);

//         setContentPane(bgPanel);
//     }

//     private void configureTextArea(JTextArea area) {
//         area.setLineWrap(true);
//         area.setWrapStyleWord(true);
//         area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//         area.setBorder(BorderFactory.createCompoundBorder(
//                 BorderFactory.createLineBorder(Color.LIGHT_GRAY),
//                 BorderFactory.createEmptyBorder(10, 10, 10, 10)
//         ));
//     }

//     private JPanel createLabeledBox(String labelText, JTextArea area) {
//         JLabel label = new JLabel(labelText);
//         label.setFont(new Font("Segoe UI", Font.BOLD, 14));
//         label.setForeground(Color.WHITE);

//         JPanel panel = new JPanel();
//         panel.setOpaque(false);
//         panel.setLayout(new BorderLayout(5, 5));
//         panel.add(label, BorderLayout.NORTH);
//         panel.add(new JScrollPane(area), BorderLayout.CENTER);
//         panel.setAlignmentX(Component.RIGHT_ALIGNMENT);

//         return panel;
//     }

//     private JButton createStyledButton(String text, Color bgColor) {
//         JButton button = new JButton(text);
//         button.setFont(new Font("Segoe UI", Font.BOLD, 14));
//         button.setForeground(Color.WHITE);
//         button.setBackground(bgColor);
//         button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//         button.setFocusPainted(false);
//         button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
//         return button;
//     }

//     private void generateAndRunSQL() {
//         String naturalQuery = inputArea.getText().trim();
//         if (naturalQuery.isEmpty()) {
//             statusLabel.setText("⚠️ Please enter a natural language query.");
//             return;
//         }

//         // Simulate LLM-to-SQL translation
//         String generatedSQL = "SELECT * FROM employees WHERE department = 'Sales';";
//         sqlArea.setText(generatedSQL);

//         // Run SQL
//         runSQL(generatedSQL);
//     }

//     private void runSQL(String sql) {
//         try {
//             // Replace with your DB2 connection details
//             Connection conn = DriverManager.getConnection("jdbc:db2://localhost:50000/MYDB", "user", "password");
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql);

//             ResultSetMetaData metaData = rs.getMetaData();
//             DefaultTableModel model = new DefaultTableModel();
//             int columnCount = metaData.getColumnCount();

//             for (int i = 1; i <= columnCount; i++) {
//                 model.addColumn(metaData.getColumnName(i));
//             }

//             while (rs.next()) {
//                 Object[] row = new Object[columnCount];
//                 for (int i = 1; i <= columnCount; i++) {
//                     row[i - 1] = rs.getObject(i);
//                 }
//                 model.addRow(row);
//             }

//             resultTable.setModel(model);
//             statusLabel.setText("✅ Query executed successfully.");

//             rs.close();
//             stmt.close();
//             conn.close();
//         } catch (Exception e) {
//             statusLabel.setText("❌ DB Error: " + e.getMessage());
//         }
//     }

//     private void clearAll() {
//         inputArea.setText("");
//         sqlArea.setText("");
//         resultTable.setModel(new DefaultTableModel());
//         statusLabel.setText("🧹 Cleared.");
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             GenAISQLAssistant app = new GenAISQLAssistant();
//             app.setVisible(true);
//         });
//     }

//     // Custom Panel with Background Image
//     static class BackgroundPanel extends JPanel {
//         private final Image bg;

//         public BackgroundPanel(Image img) {
//             this.bg = img;
//         }

//         @Override
//         protected void paintComponent(Graphics g) {
//             super.paintComponent(g);
//             g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
//         }
//     }
// }

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class GenAISQLAssistant extends JFrame {
    private JTextArea inputArea;
    private JTextArea sqlArea;
    private JTable resultTable;
    private JLabel statusLabel;

    public GenAISQLAssistant() {
        setTitle("GenAI SQL Assistant");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load background image
        Image bgImage = Toolkit.getDefaultToolkit().getImage("C:\\Users\\SHUBHANGI\\OneDrive\\Desktop\\AML\\GenAI-with-DB2-Making-Legacy-Data-Talk-Smarter\\src\\main\\java\\bg.jpg");
        BackgroundPanel bgPanel = new BackgroundPanel(bgImage);
        bgPanel.setLayout(new BorderLayout());

        // Main panel on the right
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(30, 30, 30, 50));

        JLabel title = new JLabel("🧠 GenAI SQL Assistant");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);
        rightPanel.add(title);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Input Area
        inputArea = new JTextArea(5, 40);
        configureTextArea(inputArea);
        rightPanel.add(createLabeledBox("📝 Natural Language Query", inputArea));

        // SQL Area
        sqlArea = new JTextArea(3, 40);
        configureTextArea(sqlArea);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(createLabeledBox("🧾 Generated SQL", sqlArea));

        // Buttons moved to LEFT
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);
        JButton generateBtn = createStyledButton("Generate & Run", new Color(0, 100, 100));
        JButton clearBtn = createStyledButton("Clear", new Color(0, 100, 100));

        generateBtn.addActionListener(e -> generateAndRunSQL());
        clearBtn.addActionListener(e -> clearAll());

        buttonPanel.add(generateBtn);
        buttonPanel.add(clearBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(buttonPanel); // Add to panel after SQL box

        // Table for Results
        resultTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(resultTable);
        tableScrollPane.setPreferredSize(new Dimension(1100, 220));
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("📊 Query Output Table"));

        // Status Label
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Add all to background panel
        bgPanel.add(statusLabel, BorderLayout.NORTH);
        bgPanel.add(rightPanel, BorderLayout.CENTER);
        bgPanel.add(tableScrollPane, BorderLayout.SOUTH);

        setContentPane(bgPanel);
    }

    private void configureTextArea(JTextArea area) {
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    private JPanel createLabeledBox(String labelText, JTextArea area) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.WHITE);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(5, 5));
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void generateAndRunSQL() {
        String naturalQuery = inputArea.getText().trim();
        if (naturalQuery.isEmpty()) {
            statusLabel.setText("⚠️ Please enter a natural language query.");
            return;
        }

        // Simulate LLM SQL generation
        String generatedSQL = "SELECT * FROM employees WHERE department = 'Sales';";
        sqlArea.setText(generatedSQL);

        // Run SQL
        runSQL(generatedSQL);
    }

    private void runSQL(String sql) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:db2://localhost:50000/MYDB", "user", "password");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();
            DefaultTableModel model = new DefaultTableModel();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            resultTable.setModel(model);
            statusLabel.setText("✅ Query executed successfully.");

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            statusLabel.setText("❌ DB Error: " + e.getMessage());
        }
    }

    private void clearAll() {
        inputArea.setText("");
        sqlArea.setText("");
        resultTable.setModel(new DefaultTableModel());
        statusLabel.setText("🧹 Cleared.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GenAISQLAssistant app = new GenAISQLAssistant();
            app.setVisible(true);
        });
    }

    // Custom Panel with Background Image
    static class BackgroundPanel extends JPanel {
        private final Image bg;

        public BackgroundPanel(Image img) {
            this.bg = img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
