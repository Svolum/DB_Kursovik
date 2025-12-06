package view;

import controller.DataController;
import controller.ExcelExporter;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OutDocumentsMainWindow extends JFrame {
    private JButton btnArchiveDocs;
    private JButton btnDocsOnHands;
    private JButton btnJournal;
    private DataController dataController;

    public OutDocumentsMainWindow() {
        dataController = new DataController();
        setTitle("Архив документов - Главное меню");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        setupListeners();
//        setVisible(true);
    }

    private void initComponents() {
        btnArchiveDocs = new JButton("Архивные документы");
        btnDocsOnHands = new JButton("Документы на руках");
        btnJournal = new JButton("Журнал регистрации");

        // Стилизация кнопок
        Font buttonFont = new Font("Arial", Font.PLAIN, 16);
        btnArchiveDocs.setFont(buttonFont);
        btnDocsOnHands.setFont(buttonFont);
        btnJournal.setFont(buttonFont);

        // Делаем кнопки больше
        Dimension buttonSize = new Dimension(250, 50);
        btnArchiveDocs.setPreferredSize(buttonSize);
        btnDocsOnHands.setPreferredSize(buttonSize);
        btnJournal.setPreferredSize(buttonSize);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // Заголовок
        JLabel titleLabel = new JLabel("Система управления архивом документов", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Основные функции
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        gbc.gridy = 0;
        centerPanel.add(btnArchiveDocs, gbc);

        gbc.gridy = 1;
        centerPanel.add(btnDocsOnHands, gbc);

        gbc.gridy = 2;
        centerPanel.add(btnJournal, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Нижняя панель с информацией
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        JLabel infoLabel = new JLabel("Учебный проект - Архив документов");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomPanel.add(infoLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        btnArchiveDocs.addActionListener(e -> showArchiveDocuments());
        btnDocsOnHands.addActionListener(e -> showDocumentsOnHands());
        btnJournal.addActionListener(e -> showJournalRegistration());
    }

    private void showArchiveDocuments() {
        try {
            List<DocumentModel> documents = dataController.getArchiveDocuments();

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

            OutDocumentResultFrame frame = new OutDocumentResultFrame("Архивные документы", columns);

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
            frame.getExportButton().addActionListener(e -> exportToExcel(frame));

            frame.setVisible(true);

        } catch (Exception e) {
            showErrorMessage("Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showDocumentsOnHands() {
        try {
            List<DocumentOnHandsModel> documents = dataController.getDocumentsOnHands();

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

            OutDocumentResultFrame frame = new OutDocumentResultFrame("Документы на руках", columns);

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
            frame.getExportButton().addActionListener(e -> exportToExcel(frame));

            frame.setVisible(true);

        } catch (Exception e) {
            showErrorMessage("Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showJournalRegistration() {
        try {
            List<JournalModel> journals = dataController.getJournalRegistration();

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

            OutDocumentResultFrame frame = new OutDocumentResultFrame("Журнал регистрации выдачи и возврата документов", columns);

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
            frame.getExportButton().addActionListener(e -> exportToExcel(frame));

            frame.setVisible(true);

        } catch (Exception e) {
            showErrorMessage("Ошибка при загрузке данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void exportToExcel(OutDocumentResultFrame frame) {
        String title = frame.getFrameTitle();
        String[] headers = frame.getColumnNames();
        List<Object[]> data = frame.getDataForExport();

        boolean success = ExcelExporter.exportToExcel(title, headers, data);

        if (success) {
            System.out.println("Экспорт в Excel выполнен успешно");
        }
    }

    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(OutDocumentsMainWindow::new);
    }
}