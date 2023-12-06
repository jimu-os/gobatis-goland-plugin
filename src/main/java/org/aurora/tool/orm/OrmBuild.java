package org.aurora.tool.orm;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.sql.psi.impl.SqlCreateTableStatementImpl;

import java.util.List;
import java.util.Map;

public interface OrmBuild {

    SQL_GO ColumnToFields(SqlCreateTableStatementImpl table);

    /*
     *   GoCode ���� Go����
     * */
    String GoCode(String name, SQL_GO sqlGo);

    /*
     *   GenGo ������Ҫ������go���� psi Ԫ��
     * */
    List<PsiElement> GoElement(String structCode, Project project);
}
