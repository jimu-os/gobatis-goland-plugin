package org.aurora.gobatis.editor;

import com.goide.GoLanguage;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.*;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CreateGoBatisStructAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiElement psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (!psiFile.getLanguage().equals(GoLanguage.INSTANCE)) return;
        CaretModel model = editor.getCaretModel();
        int offset = model.getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        if (elementAt==null){
            elementAt=psiFile.getLastChild();
        }
        GoBatisOrmDialog dialog = new GoBatisOrmDialog(project, editor, elementAt,true);
        dialog.setSize(500,400);
        dialog.show();
    }
}
