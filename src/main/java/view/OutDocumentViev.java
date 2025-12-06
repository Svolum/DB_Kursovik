package view;

import javax.swing.*;
import java.awt.*;

public class OutDocumentViev extends JFrame {
    private JButton btnArchiveDocs;
    private JButton btnDocsOnHands;
    private JButton btnJournal;

    public OutDocumentViev() {
        setTitle("Архив документов - Главное меню");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
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

    // Геттеры для кнопок
    public JButton getBtnArchiveDocs() { return btnArchiveDocs; }
    public JButton getBtnDocsOnHands() { return btnDocsOnHands; }
    public JButton getBtnJournal() { return btnJournal; }
}