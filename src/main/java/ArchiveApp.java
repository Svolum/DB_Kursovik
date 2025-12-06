import view.OutDocumentViev;
import view.ResultFrame;
import controller.DataController;
import controller.ExcelExporter;
import model.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ArchiveApp {
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