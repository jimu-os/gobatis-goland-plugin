package org.aurora.gobatis.template.xml;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.lang.xml.XmlTemplateTreePatcher;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NotNull;

public class GoBatisSelectTag extends TemplateContextType {
    protected GoBatisSelectTag() {
        super("Select");
    }

    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        return templateActionContext.getFile().getName().endsWith(".xml");
    }
}
