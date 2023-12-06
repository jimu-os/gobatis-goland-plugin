package org.aurora.tool.orm;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.sql.psi.impl.SqlCreateTableStatementImpl;

import java.util.List;
import java.util.Map;

public interface OrmBuild {

    SQL_GO ColumnToFields(SqlCreateTableStatementImpl table);

    /*
     *   GoCode 生成 Go代码
     * */
    String GoCode(String name, SQL_GO sqlGo);

    /*
     *   GenGo 生成需要创建的go代码 psi 元素
     * */
    List<PsiElement> GoElement(String structCode, Project project);
}
