package org.aurora.tool.orm.ormImp.gorm;

import com.goide.GoLanguage;
import com.goide.psi.GoFile;
import com.goide.psi.GoMethodDeclaration;
import com.goide.psi.GoTypeSpec;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;

import org.aurora.gobatis.template.MappingTemplate;
import org.aurora.notify.AuroraNotify;
import org.aurora.tool.orm.SQL_GO;
import org.aurora.tool.orm.SqlToGo;
import org.aurora.tool.orm.abs.AbstractOrm;
import org.bouncycastle.util.Strings;


import java.util.*;

public class Gorm extends AbstractOrm {

    boolean json = false;
    boolean gtype = false;
    boolean column = false;

    public Gorm() {

    }

    public Gorm(boolean column, boolean gtype, boolean json) {
        this.column = column;
        this.gtype = gtype;
        this.json = json;
    }

    @Override
    public String GoCode(String name, SQL_GO sqlGo) {
        String table = name;
        StringBuilder code = new StringBuilder();
        StringBuilder tag = null;
        code.append("type ");
        name = MappingTemplate.toGoName(name);
        code.append(name);
        code.append(" struct{ \r\n");
        Map<String, String> fields=sqlGo.mapping;
        List<String> keySet = sqlGo.fieldSort;
        for (String fieldName : keySet) {
            String field = MappingTemplate.toGoName(fieldName);
            String fieldType = fields.get(fieldName);
            String old = fieldType;

            if (fieldType.contains("(")) {
                int indexOf = fieldType.indexOf("(");
                fieldType = fieldType.substring(0, indexOf);
            }
            // 转换全大写 匹配go基础类型
            fieldType = Strings.toUpperCase(fieldType);
            fieldType = type.get(fieldType);
            if (fieldType == null) {
                continue;
            }
            tag = new StringBuilder();
            String temp = "`";
            tag.append(temp);
            if (column || gtype) {
                tag.append("gorm:\"");
            }
            if (column) {
                temp = String.format("column:%s", fieldName);
                tag.append(temp);
            }
            if (gtype) {
                temp = String.format(";type:%s", old);
                tag.append(temp);
            }
            if (column || gtype) {
                tag.append("\"");
            }
            if (json) {
                String toSnakeStr = SqlToGo.toHumpString(field);
                temp = String.format("json:\"%s\"", toSnakeStr);
                if (tag.length() > 1) {
                    temp = String.format(" json:\"%s\"", toSnakeStr);
                }
                tag.append(temp);
            }
            tag.append("`");
            if (tag.length() == 2) {
                tag = new StringBuilder();
            }
            code.append("\t").append(field).append("\t").append(fieldType).append("\t").append(tag);
            // 检查注释
            String comment = sqlGo.comment.get(fieldName);
            if (comment!=null&&!comment.equals("")){
                code.append("// ").append(comment);
            }
            code.append("\r\n");
        }
        code.append("}").append("\r\n");

        // 生成 表名方法
        code.append("func(");
        code.append("receiver");
        code.append(" *");
        code.append(name);
        code.append(")");
        code.append(" TableName () string {\r\n");
        code.append("\treturn \"");
        code.append(table);
        code.append("\"\r\n");
        code.append("}\r\n");

        return code.toString();
    }

    @Override
    public List<PsiElement> GoElement(String structCode, Project project) {
        GoFile fileFromText = (GoFile) PsiFileFactory.getInstance(project).createFileFromText(GoLanguage.INSTANCE, StringUtil.convertLineSeparators("package gobatis \r\n" + structCode));
        Collection<? extends GoTypeSpec> types = fileFromText.getTypes();
        List<PsiElement> list = new ArrayList<>();
        PsiElement spec = null;
        for (GoTypeSpec typeSpec : types) {
            spec = typeSpec.getParent();
        }
        if (spec == null) {
            AuroraNotify.notifyError(project, "ORM 生成失败");
            return null;
        }
        list.add(spec);

        List<GoMethodDeclaration> methods = fileFromText.getMethods();
        list.addAll(methods);
        return list;
    }
}
