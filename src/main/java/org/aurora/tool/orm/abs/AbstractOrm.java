package org.aurora.tool.orm.abs;

import com.intellij.sql.psi.SqlColumnDefinition;
import com.intellij.sql.psi.SqlConstraintDefinition;
import com.intellij.sql.psi.SqlTableKeyDefinition;
import com.intellij.sql.psi.impl.SqlCreateTableStatementImpl;
import org.aurora.tool.orm.OrmBuild;
import org.aurora.tool.orm.SQL_GO;



import java.util.*;

public abstract class AbstractOrm implements OrmBuild {
    public static Map<String,String> type =new HashMap<>();
    static {
        // 数字类型
        type.put("INT","int");
        type.put("INTEGER","int");
        type.put("TINYINT","int32");
        type.put("BIGINT","int64");
        type.put("SMALLINT","int");
        type.put("FLOAT","float64");
        type.put("DOUBLE","float64");
        type.put("DECIMAL","float64");
        type.put("MONEY","float64");
        type.put("SMALLMONEY","float64");
        type.put("NUMERIC","float64");

        // 日期类型
        type.put("DATE","string");
        type.put("TIME","string");
        type.put("YEAR","string");
        type.put("DATETIME","string");
        type.put("DATETIME2","string");
        type.put("DATETIMEOFFSET","string");
        type.put("TIMESTAMP","string");

        //字符串类型
        type.put("CHAR","string");
        type.put("VARCHAR","string");
        type.put("TINYBLOB","string");
        type.put("TINYTEXT","string");
        type.put("BLOB","string");
        type.put("TEXT","string");
        type.put("MEDIUMBLOB","string");
        type.put("MEDIUMTEXT","string");
        type.put("LONGBLOB","string");
        type.put("LONGTEXT","string");
        type.put("JSON","string");
        type.put("NUMERIC ","string");
        type.put("NVARCHAR","string");
        type.put("NCHAR","string");
        type.put("NTEXT","string");
        type.put("XML","string");

        //二进制
        type.put("VARBINARY","[]byte");
        type.put("REAL","[]byte");
    }

    /*
    *   ColumnToFields 把数据库字段的字段定义转化为 结构数据
    * */
    public SQL_GO ColumnToFields(SqlCreateTableStatementImpl table){
        SQL_GO sqlGo=new SQL_GO();
        List<SqlColumnDefinition> declaredColumns = table.getDeclaredColumns();
        for (SqlColumnDefinition sqlColumnDefinition:declaredColumns){
            // 解析字段名称
            String fieldName = sqlColumnDefinition.getName();
            // 解析字段类型定义
            String fieldType = sqlColumnDefinition.getDasType().getDescription();
            // 解析主键
            SqlTableKeyDefinition primaryKey = sqlColumnDefinition.getPrimaryKey();
            if (primaryKey!=null){
                String primaryKeyName = primaryKey.getText();
                sqlGo.pk.put(fieldName,primaryKeyName);
            }

            // 字段默认值
            String aDefault = sqlColumnDefinition.getDefault();

            // 约束
            List<SqlConstraintDefinition> constraints = sqlColumnDefinition.getConstraints();
            if (!constraints.isEmpty()){
                List<String> constraint=new ArrayList<>();
                for (SqlConstraintDefinition columnDefinition:constraints){
                    String text = columnDefinition.getText();
                    constraint.add(text);
                }
                sqlGo.check.put(fieldName,constraint);
            }

            String comment = sqlColumnDefinition.getComment();
            if (comment!=null&& !comment.equals("")){
                sqlGo.comment.put(fieldName,comment);
            }

            sqlGo.fieldSort.add(fieldName);
            sqlGo.mapping.put(fieldName,fieldType);
        }
        return sqlGo;
    }
}
