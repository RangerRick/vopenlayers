package org.vaadin.vol.demo;

import java.io.File;

import com.vaadin.Application;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.ResizeEvent;

public class VolApplication extends Application {

    private Container testClassess;

    @Override
    public Window getWindow(String name) {
        Window window = super.getWindow(name);
        if (window == null && name != null && !"".equals(name)
                && !name.contains(".ico") && name.matches("[A-Z][a-z].*")) {
            try {

                String className = getClass().getPackage().getName() + "."
                        + name;
                Class<?> forName = Class.forName(className);
                if (forName != null) {
                    Window newInstance = (Window) forName.newInstance();
                    window = newInstance;
                    addWindow(window);
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return window;
    }

    @Override
    public void init() {
        Window window = new Window("VOL tests and demos");
        window.setImmediate(true);
        window.addListener(new Window.ResizeListener() {
            public void windowResized(ResizeEvent e) {
                // hack to do test case loading only when first UIDL request is
                // done.
                loadTestClasses(e.getWindow());
            }
        });
        setMainWindow(window);
    }

    private void loadTestClasses(Window window) {
        window.addComponent(new Label(
                "Note, all selections below will not work! "
                        + "They are mostly code examples and tests that might e.g."
                        + " require a local or some custom configured map server. "
                        + "Those marked suitable have worked at least at some point"
                        + " online."));
        if (testClassess != null) {
            return;
        }
        testClassess = listTestClasses();
        Table table = new Table("Test cases", testClassess);
        table.addGeneratedColumn("name", new ColumnGenerator() {
            public Object generateCell(Table source, Object itemId,
                    Object columnId) {
                String name = (String) source.getItem(itemId)
                        .getItemProperty(columnId).getValue();
                Link link = new Link(name,
                        new ExternalResource(getURL() + name));
                link.setTargetName("_new");
                return link;
            }
        });
        table.addGeneratedColumn("description", new ColumnGenerator() {
            public Object generateCell(Table source, Object itemId,
                    Object columnId) {
                String description = (String) source.getItem(itemId)
                        .getItemProperty(columnId).getValue();
                return new Label(description);
            }
        });
        table.setSizeFull();
        table.setColumnExpandRatio("description", 1);
        window.addComponent(table);
    }

    private Container listTestClasses() {
        IndexedContainer indexedContainer = new IndexedContainer();
        indexedContainer.addContainerProperty("name", String.class, "");
        indexedContainer.addContainerProperty("description", String.class, "");
        indexedContainer.addContainerProperty("Suitble as online demo",
                Boolean.class, Boolean.FALSE);

        File file = new File("./src/test/java/org/vaadin/vol/demo");
        File[] listFiles = file.listFiles();
        for (File f : listFiles) {
            try {
                String name = f.getName();
                String simpleName = name.substring(0, name.indexOf(".java"));
                String fullname = "org.vaadin.vol.demo." + simpleName;
                Class<?> forName = Class.forName(fullname);
                if (AbstractVOLTest.class.isAssignableFrom(forName)) {
                    AbstractVOLTest newInstance = (AbstractVOLTest) forName
                            .newInstance();
                    Object id = indexedContainer.addItem();
                    Item item = indexedContainer.getItem(id);
                    item.getItemProperty("name").setValue(simpleName);
                    // TODO load class and add description (also to test cases)
                    item.getItemProperty("description").setValue(
                            newInstance.getDescription());
                    item.getItemProperty("Suitble as online demo").setValue(
                            newInstance.isSuitebleOnlineDemo());
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }

        }

        return indexedContainer;
    }

}
