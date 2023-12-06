package org.aurora.tool.orm.window;

import com.goide.GoLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class CreateGoStructAction extends AnAction {
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
        GoOrmDialog dialog = new GoOrmDialog(project, editor, elementAt,true);
        dialog.setSize(500,600);
        dialog.show();
    }
}
