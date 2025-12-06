package view;

import controller.DataController;
import controller.ExcelExporter;
import model.DatabaseConnection;
import model.DocumentModel;
import model.DocumentOnHandsModel;
import model.JournalModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    private Connection connection;

    public LoginWindow() {
        setTitle("Авторизация");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents();
        connectToDatabase();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Заголовок
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("Вход в систему");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, gbc);

        // Поле для логина
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Логин:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(15);
        mainPanel.add(usernameField, gbc);

        // Поле для пароля
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Пароль:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        // Кнопка входа
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new JButton("Войти");
        loginButton.addActionListener(new LoginButtonListener());
        mainPanel.add(loginButton, gbc);

        // Статус
        gbc.gridy = 4;
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        mainPanel.add(statusLabel, gbc);

        add(mainPanel);
    }

    private void connectToDatabase() {
        try {
            // Параметры подключения к PostgreSQL
            String url = "jdbc:postgresql://localhost:5432/ваша_база_данных";
            String user = "ваш_пользователь";
            String password = "ваш_пароль";

            // Загрузка драйвера
            Class.forName("org.postgresql.Driver");

            // Установка соединения
            connection = DatabaseConnection.getConnection();
            statusLabel.setText("Соединение с БД установлено");
            statusLabel.setForeground(Color.GREEN);

        } catch (ClassNotFoundException e) {
            statusLabel.setText("Драйвер PostgreSQL не найден");
            e.printStackTrace();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка подключения к БД");
            e.printStackTrace();
        }
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Заполните все поля");
                return;
            }

            try {
                // SQL запрос для проверки пользователя
                String sql = "SELECT * FROM app_user WHERE app_user.username = ? AND app_user.password = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password); // В реальном приложении используйте хеширование паролей!

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println(rs.getString("access_level"));
                    // 1 уровень доступа - это АДМИН
                    // 2 уровень доступа - это ПОЛЬЗОВАТЕЛь
                    statusLabel.setText("Авторизация успешна!");
                    statusLabel.setForeground(Color.GREEN);

                    // Закрываем текущее окно и открываем главное
                    statusLabel.setText("logined");
                    dispose();
                    //openMainWindow(rs.getString("full_name"));
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);

                } else {
                    statusLabel.setText("Неверный логин или пароль");
                    statusLabel.setForeground(Color.RED);
                }

                rs.close();
                stmt.close();

            } catch (SQLException ex) {
                statusLabel.setText("Ошибка при авторизации");
                ex.printStackTrace();
            }
        }
    }

    private void openMainWindow(String userName) {
        JFrame mainFrame = new JFrame("Главное окно");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400);
        mainFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Добро пожаловать, " + userName + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.CENTER);

        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginWindow().setVisible(true);
        });
    }

    public static class ArchiveApp {
        private OutDocumentViev OutDocumentViev;
        private DataController dataController;

        public ArchiveApp() {
            dataController = new DataController();
            OutDocumentViev = new OutDocumentViev();
            setupListeners();
            OutDocumentViev.setVisible(true);
        }

        private void setupListeners() {
            OutDocumentViev.getBtnArchiveDocs().addActionListener(e -> showArchiveDocuments());
            OutDocumentViev.getBtnDocsOnHands().addActionListener(e -> showDocumentsOnHands());
            OutDocumentViev.getBtnJournal().addActionListener(e -> showJournalRegistration());
        }

        private void showArchiveDocuments() {
            try {
                java.util.List<DocumentModel> documents = dataController.getArchiveDocuments();

                if (documents.isEmpty()) {
                    showMessage("Архивные документы не найдены", "Информация");
                    return;
                }

                String[] columns = {
                        "Архивный №",
                        "Название документа",
                        "Тип документа",
                        "Организация",
                        "Автор",
                        "Год создания",
                        "Кол-во страниц"
                };

                ResultFrame frame = new ResultFrame("Архивные документы", columns);

                for (DocumentModel doc : documents) {
                    Object[] row = {
                            doc.getArchiveNumDoc(),
                            doc.getNameDoc(),
                            doc.getTypeDoc(),
                            doc.getNameOrg(),
                            doc.getAutor(),
                            doc.getYearCreation(),
                            doc.getNumPages()
                    };
                    frame.addRow(row);
                }

                frame.setRowCount(documents.size());

                // Добавляем обработчик для кнопки экспорта
                frame.getExportButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        exportToExcel(frame);
                    }
                });

                frame.setVisible(true);

            } catch (Exception e) {
                showErrorMessage("Ошибка при загрузке данных: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void showDocumentsOnHands() {
            try {
                java.util.List<DocumentOnHandsModel> documents = dataController.getDocumentsOnHands();

                if (documents.isEmpty()) {
                    showMessage("Документов на руках не найдено", "Информация");
                    return;
                }

                String[] columns = {
                        "Архивный №",
                        "Название",
                        "Тип",
                        "Организация",
                        "Автор",
                        "Год",
                        "Страниц",
                        "Дата выдачи",
                        "Отдел",
                        "Сотрудник"
                };

                ResultFrame frame = new ResultFrame("Документы на руках", columns);

                for (DocumentOnHandsModel doc : documents) {
                    Object[] row = {
                            doc.getArchiveNumDoc(),
                            doc.getNameDoc(),
                            doc.getTypeDoc(),
                            doc.getNameOrg(),
                            doc.getAutor(),
                            doc.getYearCreation(),
                            doc.getNumPages(),
                            doc.getDateIssue(),
                            doc.getDepartment(),
                            doc.getEmployee()
                    };
                    frame.addRow(row);
                }

                frame.setRowCount(documents.size());

                // Добавляем обработчик для кнопки экспорта
                frame.getExportButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        exportToExcel(frame);
                    }
                });

                frame.setVisible(true);

            } catch (Exception e) {
                showErrorMessage("Ошибка при загрузке данных: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void showJournalRegistration() {
            try {
                java.util.List<JournalModel> journals = dataController.getJournalRegistration();

                if (journals.isEmpty()) {
                    showMessage("Записей в журнале не найдено", "Информация");
                    return;
                }

                String[] columns = {
                        "№ записи",
                        "Архивный №",
                        "Название документа",
                        "Тип",
                        "Отдел",
                        "Сотрудник",
                        "Дата выдачи",
                        "Дата возврата"
                };

                ResultFrame frame = new ResultFrame("Журнал регистрации выдачи и возврата документов", columns);

                for (JournalModel journal : journals) {
                    Object[] row = {
                            journal.getNumRecord(),
                            journal.getArchiveNumDoc(),
                            journal.getNameDoc(),
                            journal.getTypeDoc(),
                            journal.getDepartment(),
                            journal.getEmployee(),
                            journal.getDateIssue(),
                            journal.getDateReturn()
                    };
                    frame.addRow(row);
                }

                frame.setRowCount(journals.size());

                // Добавляем обработчик для кнопки экспорта
                frame.getExportButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        exportToExcel(frame);
                    }
                });

                frame.setVisible(true);

            } catch (Exception e) {
                showErrorMessage("Ошибка при загрузке данных: " + e.getMessage());
                e.printStackTrace();
            }
        }

        private void exportToExcel(ResultFrame frame) {
            String title = frame.getFrameTitle();
            String[] headers = frame.getColumnNames();
            List<Object[]> data = frame.getDataForExport();

            boolean success = ExcelExporter.exportToExcel(title, headers, data);

            if (success) {
                System.out.println("Экспорт в Excel выполнен успешно");
            }
        }

        private void showMessage(String message, String title) {
            JOptionPane.showMessageDialog(OutDocumentViev, message, title, JOptionPane.INFORMATION_MESSAGE);
        }

        private void showErrorMessage(String message) {
            JOptionPane.showMessageDialog(OutDocumentViev, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

        public static void main(String[] args) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> new ArchiveApp());
        }
    }
}