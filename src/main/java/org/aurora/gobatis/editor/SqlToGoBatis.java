package org.aurora.gobatis.editor;

import com.goide.GoLanguage;
import com.goide.psi.GoFile;
import com.goide.psi.GoTypeSpec;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.sql.psi.SqlColumnDefinition;
import com.intellij.sql.psi.SqlConstraintDefinition;
import com.intellij.sql.psi.SqlTableKeyDefinition;
import com.intellij.sql.psi.impl.SqlCreateTableStatementImpl;
import org.aurora.notify.AuroraNotify;
import org.bouncycastle.util.Strings;
import org.aurora.gobatis.template.MappingTemplate;

import java.util.*;

public class SqlToGoBatis {
    static Map<String, String> type = new HashMap<>();

    static {
        // ��������
        type.put("INT", "int");
        type.put("INTEGER", "int");
        type.put("TINYINT", "int32");
        type.put("BIGINT", "int64");
        type.put("FLOAT", "float64");
        type.put("DOUBLE", "float64");
        type.put("DECIMAL", "float64");

        // ��������
        type.put("DATE", "string");
        type.put("TIME", "string");
        type.put("YEAR", "string");
        type.put("DATETIME", "string");
        type.put("TIMESTAMP", "string");

        //�ַ�������
        type.put("CHAR", "string");
        type.put("VARCHAR", "string");
        type.put("TINYBLOB", "string");
        type.put("TINYTEXT", "string");
        type.put("BLOB", "string");
        type.put("TEXT", "string");
        type.put("MEDIUMBLOB", "string");
        type.put("MEDIUMTEXT", "string");
        type.put("LONGBLOB", "string");
        type.put("LONGTEXT", "string");
        type.put("JSON", "string");
    }

    /*
     *   ���� sql �е��ж������� go ��Ӧ�� field ����
     * */
    public static Map<String, String> ColumnToFields(SqlCreateTableStatementImpl table) {
        List<SqlColumnDefinition> declaredColumns = table.getDeclaredColumns();
        Map<String, String> fields = new HashMap<>();
        for (SqlColumnDefinition sqlColumnDefinition : declaredColumns) {
            // �����ֶ�����
            String fieldName = sqlColumnDefinition.getName();
            // �����ֶ����Ͷ���
            String fieldType = sqlColumnDefinition.getDasType().getDescription();

            // ��������
            SqlTableKeyDefinition primaryKey = sqlColumnDefinition.getPrimaryKey();
            if (primaryKey != null) {
                primaryKey.getName();
            }

            // ���� ע��
            String com = sqlColumnDefinition.getComment();

            // ����Լ��
            List<SqlConstraintDefinition> constraints = sqlColumnDefinition.getConstraints();
            for (SqlConstraintDefinition constraint : constraints) {
                SqlConstraintDefinition.Type type = constraint.getConstraintType();
                String constraintName = constraint.getName();
                System.out.println();
            }
            if (fieldType.contains("(")) {
                int indexOf = fieldType.indexOf("(");
                fieldType = fieldType.substring(0, indexOf);
            }
            fieldType = Strings.toUpperCase(fieldType);
            fieldType = type.get(fieldType);
            if (fieldType == null) {
                continue;
            }
            fields.put(fieldName, fieldType);
        }
        return fields;
    }

    public static String BuildStructCode(String name, Map<String, String> fields) {
        StringBuilder struct = new StringBuilder();
        struct.append("type ");
        name = MappingTemplate.toGoName(name);
        struct.append(name);
        struct.append(" struct{ \r\n");
        Set<String> keySet = fields.keySet();
        for (String fieldName : keySet) {
            String field = MappingTemplate.toGoName(fieldName);
            String tag = String.format("`column:\"%s\"`", fieldName);
            struct.append("\t").append(field).append("\t").append(fields.get(fieldName)).append("\t").append(tag).append("\r\n");
        }
        struct.append("}").append("\r\n");
        return struct.toString();
    }

    public static void CreateGoPsiFile(List<PsiElement> list, String structCode, Project project) {
        GoFile fileFromText = (GoFile) PsiFileFactory.getInstance(project).createFileFromText(GoLanguage.INSTANCE, StringUtil.convertLineSeparators("package test \r\n" + structCode));
        Collection<? extends GoTypeSpec> types = fileFromText.getTypes();
        PsiElement spec = null;
        for (GoTypeSpec typeSpec : types) {
            spec = typeSpec.getParent();
        }
        if (spec == null) {
            AuroraNotify.notifyError(project, "ORM ����ʧ��");
            return;
        }
        list.add(spec);
    }
}
