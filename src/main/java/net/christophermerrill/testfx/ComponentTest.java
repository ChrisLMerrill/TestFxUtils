package net.christophermerrill.testfx;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import org.hamcrest.*;
import org.junit.jupiter.api.*;
import org.testfx.api.*;
import org.testfx.framework.junit5.*;
import org.testfx.service.query.*;
import org.testfx.service.query.impl.*;
import org.testfx.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused,WeakerAccess") // public APIs
public abstract class ComponentTest extends ApplicationTest
    {
    public void waitForUiEvents()
        {
        WaitForAsyncUtils.waitForFxEvents();
        }

    @Override
    public void start(Stage stage) throws Exception
        {
        Node component = createComponentNode();

        Parent root;
        if (fillToWidthAndHeight())
	        {
	        BorderPane bordered = new BorderPane();
	        bordered.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
	        bordered.setCenter(component);
	        root = bordered;
	        }
        else
	        {
	        GridPane grid = new GridPane();
	        grid.add(component, 0, 0);
	        root = grid;
	        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
//        scene.getStylesheets().add(getClass().getResource("/ide.css").toExternalForm());
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

    protected boolean fillToWidthAndHeight() { return true; }

    protected abstract Node createComponentNode() throws Exception;

    protected void fillFieldAndTabAway(String locator, String text)
        {
        FxRobotInterface robot = clickOn(locator);
        clearText(locator);
        robot.write(text).push(KeyCode.TAB);
        }

    protected void fillFieldAndPressEnter(String locator, String text)
        {
        FxRobotInterface robot = clickOn(locator);
        clearText(locator);
        robot.write(text).push(KeyCode.ENTER);
        }

    protected void fillField(String locator, String text)
        {
        FxRobotInterface robot = clickOn(locator);
        clearText(locator);
        robot.write(text);
        }

    protected void fillFieldAndTabAway(Node node, String text)
        {
        FxRobotInterface robot = clickOn(node);
        ((TextInputControl)node).setText("");
        robot.write(text).push(KeyCode.TAB);
        }

    protected void fillComboAndTabAway(String locator, String text)
        {
        ComboBox combo = lookup(locator).queryComboBox();
        clickOn(combo).push(KeyCode.CONTROL, KeyCode.A).push(KeyCode.DELETE).write(text).push(KeyCode.TAB);
        }

    protected void clearText(String locator)
        {
        lookup(locator).queryTextInputControl().setText("");
        }
    
    protected void clearFieldAndTabAway(String locator)
        {
        FxRobotInterface robot = clickOn(locator);
        clearText(locator);
        robot.push(KeyCode.TAB);
        }

    protected void tabAway()
        {
        push(KeyCode.TAB);
        }

    protected void pressUndoKey(String locator)
        {
        if (OperatingSystem.macOS.equals(OperatingSystem.get()))
            clickOn(locator).push(KeyCode.COMMAND, KeyCode.Z);
        else
            clickOn(locator).push(KeyCode.CONTROL, KeyCode.Z);
        }

    protected void pressEscape(String locator)
	    {
        clickOn(locator).push(KeyCode.ESCAPE);
        }

    protected String quoted(Object value)
        {
        return "\"" + value.toString() + "\"";
        }

    protected String getTooltipText(Node node)
        {
        return ((Tooltip) node.getProperties().get(TOOLTIP_PROP_ID)).getText();
        }

    protected String textOf(Node node)
        {
        if (node instanceof TextInputControl)
            return ((TextInputControl)node).getText();
        else if (node instanceof Labeled)
            return ((Labeled)node).getText();
        else if (node instanceof ComboBox)
            return ((ComboBox)node).getSelectionModel().getSelectedItem().toString();
        Assertions.fail("Expected a Label, TextInputControl or ComboBox");
        return null;
        }

    protected String textOf(String query)
        {
        Node node = lookup(query).query();
        Assertions.assertNotNull(node, "node not found: " + query);
        return textOf(node);
        }

    /**
     * Tests if a node (located by the query) exists in the scene graph.
     */
    protected boolean exists(String query)
        {
        try
            {
            lookup(query).query();
            return true;
            }
        catch (EmptyNodeQueryException e)
            {
            return false;
            }
        }

    protected int numberOf(String query)
        {
        return lookup(query).queryAll().size();
        }

    protected String id(String id_value)
        {
        return "#" + id_value;
        }

    protected String withStyle(String style_name)
        {
        return "." + style_name;
        }

    public TableCell tableCell(String table_query, int row, int column)
        {
        return lookup(table_query).lookup(".table-row-cell").nth(row).lookup(".table-cell").nth(column).query();
        }

    public <T extends Node> T nodeOfClass(Class<T> node_class, Node root)
        {
        return from(root).lookup(new BaseMatcher<T>()
            {
            @Override
            public boolean matches(Object item)
                {
                return node_class.isAssignableFrom(item.getClass());
                }

            @Override
            public void describeTo(Description description)
                {

                }
            }).query();
        }

    protected String byClass(String class_name)
        {
        return "." + class_name;
        }

    protected PointQuery inside(Node query_node, final long x, final long y)
	    {
	    return new PointQueryBase()
		    {
		    @Override
		    public Point2D query()
			    {
			    final Bounds bounds = bounds(query_node).query();
			    return new Point2D(bounds.getMinX() + x, bounds.getMinY() + y);
			    }
		    };
	    }

    private final static String TOOLTIP_PROP_ID = "javafx.scene.control.Tooltip";  // copied from javafx.scene.control.Tooltip.TOOLTIP_PROP_KEY
    }


