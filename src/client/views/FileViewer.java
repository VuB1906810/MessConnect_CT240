package client.views;


import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.engine.RenderingMode;
import com.teamdev.jxbrowser.view.swing.BrowserView;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class FileViewer {

    private String url;

    public FileViewer(String url) {
        this.url = url;
    }

    public void viewFile() {
        Engine engine = Engine.newInstance(EngineOptions.newBuilder(RenderingMode.OFF_SCREEN)
                .licenseKey("1BNDHFSC1G4KT3J8S1BT51TNPODMC24TCJDTBX96TFF6EUWM6V9CJDTSWP6H6ZFBZ2EWLR").build());
        Browser browser = engine.newBrowser();
        SwingUtilities.invokeLater(() -> {
            BrowserView view = BrowserView.newInstance(browser);
            JFrame frame = new JFrame("MessConnect File Viewer");
            frame.add(view, BorderLayout.CENTER);
            frame.setSize(900, 600);
            frame.setVisible(true);
            browser.navigation().loadUrl(this.url);
        });
    }

}
