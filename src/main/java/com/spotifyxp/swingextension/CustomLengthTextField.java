package com.spotifyxp.swingextension;

import java.awt.KeyboardFocusManager;
import javax.swing.InputVerifier;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CustomLengthTextField extends JTextField {

    protected boolean upper = false;
    protected int maxlength = 0;

    public CustomLengthTextField() {
        this(-1);
    }

    public CustomLengthTextField(int length, boolean upper) {
        this(length, upper, null);
    }

    public CustomLengthTextField(int length, InputVerifier inpVer) {
        this(length, false, inpVer);
    }

    /**
     *
     * @param length - maksimalan length
     * @param upper - turn it to upercase
     * @param inpVer - InputVerifier
     */
    public CustomLengthTextField(int length, boolean upper, InputVerifier inpVer) {
        super();
        this.maxlength = length;
        this.upper = upper;
        if (length > 0) {
            AbstractDocument doc = (AbstractDocument) getDocument();
            doc.setDocumentFilter(new DocumentSizeFilter());
        }
        setInputVerifier(inpVer);
    }

    public CustomLengthTextField(int length) {
        this(length, false);
    }

    public void setMaxLength(int length) {
        this.maxlength = length;
    }

    class DocumentSizeFilter extends DocumentFilter {

        public void insertString(FilterBypass fb, int offs, String str, AttributeSet a)
                throws BadLocationException {

            //This rejects the entire insertion if it would make
            //the contents too long. Another option would be
            //to truncate the inserted string so the contents
            //would be exactly maxCharacters in length.
            if ((fb.getDocument().getLength() + str.length()) <= maxlength) {
                super.insertString(fb, offs, str, a);
            }
        }

        public void replace(FilterBypass fb, int offs,
                            int length,
                            String str, AttributeSet a)
                throws BadLocationException {

            if (upper) {
                str = str.toUpperCase();
            }

            //This rejects the entire replacement if it would make
            //the contents too long. Another option would be
            //to truncate the replacement string so the contents
            //would be exactly maxCharacters in length.
            int charLength = fb.getDocument().getLength() + str.length() - length;

            if (charLength <= maxlength) {
                super.replace(fb, offs, length, str, a);
                if (charLength == maxlength) {

                }
            } else {

            }
        }

        private void focusNextComponent() {
            if (CustomLengthTextField.this == KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
            }
        }
    }
}