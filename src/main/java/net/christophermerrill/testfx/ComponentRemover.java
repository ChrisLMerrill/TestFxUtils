package net.christophermerrill.testfx;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ComponentRemover
    {
    public static void waitForRemoval(Pane parent, Node child) throws InterruptedException
        {
        final Object sync = "any old object";
        Platform.runLater(() ->
            {
            parent.getChildren().remove(child);
            synchronized (sync)
                {
                sync.notify();
                }
            });
        synchronized (sync)
            {
            sync.wait();
            }
        }
    }


