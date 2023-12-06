package org.aurora.gobatis.mark.mapper.func;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollingModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;

import java.awt.event.MouseEvent;

/*
 *   GutterIconNavigationHandler 实现 点击标记导航
 * */
public class MapperFuncMgmtNavigationHandler implements GutterIconNavigationHandler {

    VirtualFile xml;

    // 导航目标
    PsiElement element;

    public MapperFuncMgmtNavigationHandler() {

    }

    public MapperFuncMgmtNavigationHandler(VirtualFile xml, PsiElement element) {
        this.xml = xml;
        this.element = element;
    }

    @Override
    public void navigate(MouseEvent e, PsiElement elt) {
        int offset = this.element.getTextOffset();
        Project project = elt.getProject();
        // 点击按钮生成选项
        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, this.xml);
        FileEditorManager editorManager = FileEditorManager.getInstance(project);
        Editor xmlFile = editorManager.openTextEditor(openFileDescriptor, true);
        if (xmlFile == null) return;
        ScrollingModel scrollingModel = xmlFile.getScrollingModel();
        CaretModel caretModel = xmlFile.getCaretModel();
        caretModel.moveToOffset(offset);
        LogicalPosition logicalPosition = caretModel.getLogicalPosition();
        scrollingModel.scrollVertically(offset);
        logicalPosition.leanForward(true);
    }
}
