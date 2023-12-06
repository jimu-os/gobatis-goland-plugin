package org.aurora.tool.orm;



import com.intellij.sql.psi.SqlColumnDefinition;
import com.intellij.sql.psi.impl.SqlCreateTableStatementImpl;



import java.util.*;

public class SqlToGo {

    /*
     *   ���� sql �е��ж������� go ��Ӧ�� field ����
     * */
    public static Map<String, String> ColumnToFields(SqlCreateTableStatementImpl table) {
        List<SqlColumnDefinition> declaredColumns = table.getDeclaredColumns();
        Map<String, String> fields = new HashMap<>();
        for (SqlColumnDefinition sqlColumnDefinition : declaredColumns) {
            String fieldName = sqlColumnDefinition.getName();
            String fieldType = sqlColumnDefinition.getSqlType().toString();
            fields.put(fieldName, fieldType);
        }
        return fields;
    }

    /*
     *   ���ַ���ת��Ϊ��д��ĸ��ͷ�ķ��������淶�ַ������������α�ʶ���ţ�ת���շ�
     * */
    public static String toGoName(String name) {
        char[] chars = name.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '_') {
                if (i + 1 == chars.length) {
                    continue;
                }
                c = chars[i + 1];
                builder.append(Character.toUpperCase(c));
                i++;
                continue;
            }
            if (i == 0) {
                builder.append(Character.toUpperCase(c));
                continue;
            }
            builder.append(c);
        }
        return builder.toString();
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
}
