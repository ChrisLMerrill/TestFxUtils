package net.christophermerrill.testfx;

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import org.junit.*;
import org.testfx.framework.junit.*;
import org.testfx.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused,WeakerAccess") // public APIs
public abstract class ComponentTest extends ApplicationTest
    {
    @Before
    public void waitForUiEvents()
        {
        WaitForAsyncUtils.waitForFxEvents();
        }

    @Override
    public void start(Stage stage) throws Exception
        {
        Node component = createComponentNode();
        BorderPane root = new BorderPane();
        root.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
        root.setCenter(component);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(getDefaultWidth());
        stage.setHeight(getDefaultHeight());
        stage.show();
        }

    @Override
    public void stop() throws Exception
        {
        super.stop();
        }

    @Override
    public void init() throws Exception
        {
        super.init();
        }

    protected double getDefaultWidth()
        {
        return 600;
        }

    protected double getDefaultHeight()
        {
        return 200;
        }

    protected abstract Node createComponentNode();

    protected void fillFieldAndTabAway(String locator, String text)
        {
        clickOn(locator).push(KeyCode.CONTROL, KeyCode.A).write(text).push(KeyCode.TAB);
        }

    protected void fillField(String locator, String text)
        {
        clickOn(locator).push(KeyCode.CONTROL, KeyCode.A).write(text);
        }

    protected void clearFieldAndTabAway(String locator)
        {
        clickOn(locator).push(KeyCode.CONTROL, KeyCode.A).push(KeyCode.DELETE).push(KeyCode.TAB);
        }

    protected void pressUndoKey(String locator)
        {
        clickOn(locator).push(KeyCode.CONTROL, KeyCode.Z);
        }

    protected void fillFieldAndTabAway(Node node, String text)
        {
        clickOn(node).push(KeyCode.CONTROL, KeyCode.A).write(text).push(KeyCode.TAB);
        }

    protected String quoted(Object value)
        {
        return "\"" + value.toString() + "\"";
        }

    protected String getTooltipText(Node node)
        {
        return ((Tooltip) node.getProperties().get(TOOLTIP_PROP_ID)).getText();
        }

    protected String textOf(String query)
        {
        Node node = lookup(query).query();
        Assert.assertNotNull("node not found: " + query, node);
        if (node instanceof TextInputControl)
            return ((TextInputControl)node).getText();
        else if (node instanceof Labeled)
            return ((Labeled)node).getText();
        Assert.fail("Expected a Label or TextInputControl");
        return null;
        }

    /**
     * Tests if a node (located by the query) exists in the scene graph.
     */
    protected boolean exists(String query)
        {
        Node node = lookup(query).query();
        return node != null;
        }

    protected int numberOf(String query)
        {
        return lookup(query).queryAll().size();
        }

    protected String id(String id_value)
        {
        return "#" + id_value;
        }

    private final static String TOOLTIP_PROP_ID = "javafx.scene.control.Tooltip";  // copied from javafx.scene.control.Tooltip.TOOLTIP_PROP_KEY

    }


