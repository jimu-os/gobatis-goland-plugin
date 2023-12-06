package org.aurora.gobatis.template;

import com.goide.psi.GoFieldDeclaration;
import com.goide.psi.GoFieldDefinition;
import com.goide.psi.impl.*;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PriorityAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
/*
*   MappingTemplate 批量实现对 Go Struct 中声明的字段添加 gobatis所需要的 tag标签
* */
public class MappingTemplate implements IntentionAction {

    @Override
    public @IntentionName @NotNull String getText() {
        return "Generate GoBatis field mappings";
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Gen all field column";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        // 通过光标找到 psi 文件内的元素位置
        PsiElement elementAt = file.findElementAt(offset);
        if (elementAt == null) return false;
        GoFieldDeclarationImpl ofType = PsiTreeUtil.getPrevSiblingOfType(elementAt, GoFieldDeclarationImpl.class);
        return ofType != null;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        // 通过光标找到 psi 文件内的元素位置
        PsiElement elementAt = file.findElementAt(offset);
        GoStructTypeImpl goStructType = PsiTreeUtil.getParentOfType(elementAt, GoStructTypeImpl.class);
        assert goStructType != null;
        List<GoFieldDeclaration> fieldDeclarationList = goStructType.getFieldDeclarationList();
        for (GoFieldDeclaration fieldDeclaration : fieldDeclarationList) {
            List<GoFieldDefinition> fieldDefinitionList = fieldDeclaration.getFieldDefinitionList();
            // 定义 go-batis 每个行不能声明多个同类型的字段
            if (fieldDefinitionList.size() > 1) {
                continue;
            }
            // todo 校验 字段类型 不能是函数

            String name = fieldDefinitionList.get(0).getName();
            assert name != null;
            name = toSnakeStr(name);
            LeafPsiElement leafPsiElement = GoElementFactory.createElement(project, "`column:\"" + name + "\"`", LeafPsiElement.class);
            assert leafPsiElement != null;
            fieldDeclaration.add(leafPsiElement);
        }

    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    public @NotNull PriorityAction.Priority getPriority() {
        return PriorityAction.Priority.TOP;
    }



    public static String toSnakeStr(String humpStr) {
        if (null == humpStr || humpStr.length() <= 1) {
            return humpStr;
        }
        StringBuilder sb = new StringBuilder(humpStr.length());
        char[] chars = humpStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            Character c = chars[i];
            if (Character.isUpperCase(c) && i > 0) {
                sb.append("_");
            }
            sb.append(c);
        }
        return sb.toString().toLowerCase();
    }

    public static String toHumpString(String humpStr) {
        if (null == humpStr || humpStr.length() <= 1) {
            return humpStr;
        }
        char[] chars = humpStr.toCharArray();
        char c = chars[0];
        if (!Character.isLowerCase(c)) {
            chars[0] = Character.toLowerCase(c);
        }
        return new String(chars);
    }

    /*
    *   把字符串转化为大写字母开头的非蛇形名规范字符串，根据蛇形标识符号，转化驼峰
    * */
    public static String toGoName(String name){
        char[] chars = name.toCharArray();
        StringBuilder builder=new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c=chars[i];
            if (c=='_'){
                if (i+1==chars.length){
                    continue;
                }
                c=chars[i+1];
                builder.append(Character.toUpperCase(c));
                i++;
                continue;
            }
            if (i==0){
                builder.append(Character.toUpperCase(c));
                continue;
            }
            builder.append(c);
        }
        return builder.toString();
    }
}
