package org.shoukaiseki.gui.jtextpane.autocompletion;

import java.util.ArrayList;

public interface CompletionJTextPaneFilter {
    ArrayList filter(String text);
    String getSepareta();
    String getIndexString();
}


