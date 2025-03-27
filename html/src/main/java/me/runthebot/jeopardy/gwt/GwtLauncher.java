package me.runthebot.jeopardy.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import me.runthebot.jeopardy.Main;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
        @Override
        public GwtApplicationConfiguration getConfig () {
            // Resizable application, uses available space in browser with no padding:
            GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(true);
            cfg.padVertical = 0;
            cfg.padHorizontal = 0;
            return cfg;
            // If you want a fixed size application, comment out the above resizable section,
            // and uncomment below:
            //return new GwtApplicationConfiguration(640, 480);
        }

        @Override
        public ApplicationListener createApplicationListener () {
            return new Main();
        }

        @Override
        public void exit() {
            // Call window.close() in JavaScript
            closeWindow();
        }

        /**
         * Native JavaScript method to close the browser window
         */
        private native void closeWindow() /*-{
            $wnd.location.replace("https://google.com");
        }-*/;
}
