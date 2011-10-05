package org.vaadin.vol.demo;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class VolApplication extends Application {
    
    @Override
    public Window getWindow(String name) {
        Window window = (Window) super.getWindow(name);
        if (window == null && name != null && !"".equals(name)
                && !name.contains(".ico") && name.matches("[A-Z][a-z].*")) {

            try {

                String className = getClass().getPackage().getName() + "."
                        + name;
                Class<?> forName = Class.forName(className);
                if (forName != null) {
                    Window newInstance = (Window) forName
                            .newInstance();
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
        final Window mainWindow = new Demo();
        setMainWindow(mainWindow);
    }

}
