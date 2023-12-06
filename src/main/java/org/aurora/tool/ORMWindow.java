package org.aurora.tool;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.sql.SqlFileType;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ORMWindow {
    JBBox mainBox;

    JBBox checkDB;

    JBBox optBox;

    JBBox check;
    JBBox editBox;
    JBBox resultBox;
    String select;
    List<JBCheckBox> checkBoxList;

    Editor editor;
    Editor result;
    Project project;

    public ORMWindow(ToolWindow toolWindow, Project project) {
        this.project = project;
        mainBox = JBBox.createVerticalBox();
        // 创建 选择库表
        checkDB = JBBox.createHorizontalBox();
        check = JBBox.createHorizontalBox();
        optBox = JBBox.createHorizontalBox();
        editBox = JBBox.createHorizontalBox();
        resultBox = JBBox.createHorizontalBox();

        checkBoxList = new ArrayList<>();
        checkBoxList.add(new JBCheckBox("column"));
        checkBoxList.add(new JBCheckBox("type"));

        mainBox.setMaximumSize(toolWindow.getComponent().getMaximumSize());
    }

    public JComponent getUI() {
        JBLabel label = new JBLabel("选择:");
        label.setSize(200, 25);

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.addItem("GoBatis");
        comboBox.addItem("GORM");
        comboBox.setMaximumSize(new Dimension(10000, 25));
        checkDB.add(label);
        checkDB.add(comboBox);

        for (JBCheckBox checkBox : checkBoxList) {
            check.add(checkBox);
        }
        editBox.setMaximumSize(new Dimension(10000, 200));
        Document document = EditorFactory.getInstance().createDocument(StringUtil.convertLineSeparators(""));
        editor = EditorFactory.getInstance().createEditor(document, project, SqlFileType.INSTANCE, false);
        JComponent component = editor.getComponent();
        JPanel jPanel = new JPanel();
        jPanel.add(component);
        jPanel.setMaximumSize(new Dimension(10000, 200));

        EditorTextField source = new EditorTextField();
        source.setOneLineMode(false);

        JBScrollPane scrollPane = new JBScrollPane(jPanel);
        editBox.add(scrollPane);

        optBox.add(new JButton("生成"));

        Document resultDoc = EditorFactory.getInstance().createDocument(StringUtil.convertLineSeparators(""));
        result = EditorFactory.getInstance().createEditor(resultDoc, project, SqlFileType.INSTANCE, false);
        JComponent editorComponent = result.getComponent();
        editorComponent.setBorder(BorderFactory.createEmptyBorder());

        EditorTextField result = new EditorTextField();
        result.setOneLineMode(false);
        resultBox.add(result);
        resultBox.setMaximumSize(new Dimension(10000, 200));


        mainBox.add(checkDB);
        mainBox.add(check);
        mainBox.add(editBox);
        mainBox.add(optBox);
        mainBox.add(resultBox);
        return mainBox;
    }
}
