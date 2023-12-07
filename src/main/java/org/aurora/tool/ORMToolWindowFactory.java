package org.aurora.tool;

import com.goide.GoIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class ORMToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ORMWindow orm = new ORMWindow(toolWindow, project);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(orm.getUI(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
