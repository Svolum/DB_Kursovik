package view;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumericDocumentFilter extends DocumentFilter {
    private int maxLength = -1;

    public NumericDocumentFilter() {}

    public NumericDocumentFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (string == null) return;

        String newText = getNewText(fb, offset, 0, string);
        if (isValidNumeric(newText)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null) return;

        String newText = getNewText(fb, offset, length, text);
        if (isValidNumeric(newText)) {
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

    private boolean isValidNumeric(String text) {
        if (!text.matches("[0-9]*")) {
            return false;
        }

        if (maxLength > 0 && text.length() > maxLength) {
            return false;
        }

        return true;
    }
}