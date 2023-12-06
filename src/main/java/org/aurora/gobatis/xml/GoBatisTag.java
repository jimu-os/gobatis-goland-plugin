package org.aurora.gobatis.xml;

import com.goide.GoIcons;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.XmlTagNameProvider;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class GoBatisTag implements XmlTagNameProvider {
    @Override
    public void addTagNameVariants(List<LookupElement> elements, @NotNull XmlTag tag, String prefix) {
        addElement(elements,tag,prefix);
    }

    public void addElement(List<LookupElement> elements, @NotNull XmlTag tag, String prefix){
        XmlTagImpl xmlTag ;
        xmlTag= PsiTreeUtil.getParentOfType(tag, XmlTagImpl.class);
        if (xmlTag==null) return;
        String tagName = xmlTag.getName();
        LookupElement element;
        if (tagName.equals("mapper")){
            element = PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("select").
                            withItemTextForeground(new Color(69, 157, 192)).withIcon(GoIcons.ICON).withTypeText("GoBatis Select"), 0
            );
            elements.add(element);
            element = PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("insert").
                            withItemTextForeground(new Color(69, 157, 192)).withIcon(GoIcons.ICON).withTypeText("GoBatis Insert"), 0
            );
            elements.add(element);
            element = PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("update").
                            withItemTextForeground(new Color(69, 157, 192)).withIcon(GoIcons.ICON).withTypeText("GoBatis Update"), 0
            );
            elements.add(element);
            element = PrioritizedLookupElement.withPriority(
                    LookupElementBuilder.create("delete").
                            withItemTextForeground(new Color(69, 157, 192)).withIcon(GoIcons.ICON).withTypeText("GoBatis Delete"), 0
            );
            elements.add(element);
        }
    }
}
