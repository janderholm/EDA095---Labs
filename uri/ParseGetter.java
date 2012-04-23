package uri;

import javax.swing.text.html.*;

public class ParseGetter extends HTMLEditorKit {
    // purely to make this method public
    public HTMLEditorKit.Parser getParser() {
        return super.getParser();
    }
}