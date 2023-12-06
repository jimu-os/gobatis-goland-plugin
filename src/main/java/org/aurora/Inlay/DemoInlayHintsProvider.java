package org.aurora.Inlay;

import com.intellij.codeInsight.hints.declarative.InlayHintsCollector;
import com.intellij.codeInsight.hints.declarative.InlayHintsProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.InlayModel;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DemoInlayHintsProvider implements InlayHintsProvider  {

    @Nullable
    @Override
    public InlayHintsCollector createCollector(@NotNull PsiFile psiFile, @NotNull Editor editor) {
        InlayModel inlayModel = editor.getInlayModel();
        return null;
    }
}
