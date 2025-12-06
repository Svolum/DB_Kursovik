package view;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class LetterDocumentFilter extends DocumentFilter {
    private int maxLength = -1;

    public LetterDocumentFilter() {}

    public LetterDocumentFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (string == null) return;

        String newText = getNewText(fb, offset, 0, string);
        if (isValidLetters(newText)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null) return;

        String newText = getNewText(fb, offset, length, text);
        if (isValidLetters(newText)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private String getNewText(FilterBypass fb, int offset, int length, String text)
            throws BadLocationException {
        javax.swing.text.Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);
        return sb.toString();
    }

    private boolean isValidLetters(String text) {
        // Разрешаем буквы (латиница и кириллица), пробелы и дефисы
        if (!text.matches("[a-zA-Zа-яА-Я\\s\\-]*")) {
            return false;
        }

        if (maxLength > 0 && text.length() > maxLength) {
            return false;
        }

        return true;
    }
}