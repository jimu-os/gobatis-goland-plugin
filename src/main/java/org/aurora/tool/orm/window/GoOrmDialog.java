package org.aurora.tool.orm.window;


import com.goide.psi.impl.GoFunctionDeclarationImpl;
import com.goide.psi.impl.GoMethodDeclarationImpl;
import com.goide.psi.impl.GoTypeDeclarationImpl;

import com.intellij.database.util.SqlDialects;
import com.intellij.icons.AllIcons;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.sql.SqlFileType;
import com.intellij.sql.dialects.SqlLanguageDialect;
import com.intellij.sql.dialects.mssql.AzureDialect;
import com.intellij.sql.dialects.mysql.MysqlDialect;
import com.intellij.sql.dialects.postgres.PgDialect;
import com.intellij.sql.psi.*;
import com.intellij.sql.psi.impl.SqlCreateTableStatementImpl;
import com.intellij.sql.psi.impl.SqlPsiElementFactory;

import com.intellij.ui.components.*;

import org.aurora.notify.AuroraNotify;
import org.aurora.tool.orm.DefaultOrm;
import org.aurora.tool.orm.OrmBuild;
import org.aurora.tool.orm.SQL_GO;
import org.aurora.tool.orm.ormImp.gobatis.GoBatis;
import org.aurora.tool.orm.ormImp.gorm.Gorm;
import org.jetbrains.annotations.Nullable;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GoOrmDialog extends DialogWrapper {

    JBBox mainBox = JBBox.createVerticalBox();
    JBBox butBox = JBBox.createHorizontalBox();
    JBBox sqlBox = JBBox.createHorizontalBox();
    Project project;
    PsiElement insert;
    Editor editor;
    Editor create;

    JBCheckBox json;
    JBCheckBox column;
    JBCheckBox type;

    JBCheckBox def;

    JBLabel label;

    ComboBox<String> comboBox;

    ComboBox<String> sqlLanguage;

    public GoOrmDialog(@Nullable Project project, Editor editor, PsiElement element, boolean canBeParent) {
        super(project, canBeParent);
        this.project = project;
        this.editor = editor;
        this.insert = element;
        this.json = new JBCheckBox("json");
        this.type = new JBCheckBox("type");
        this.column = new JBCheckBox("column");
        this.def = new JBCheckBox("default");
        this.label = new JBLabel("");
        this.label.setMaximumSize(new Dimension(1000, 25));
        setTitle("Create ORM");
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {

        JBLabel select = new JBLabel("选择: ");
        comboBox = new ComboBox<>();

        comboBox.addItem("选择..");
        comboBox.addItem("GoBatis");
        comboBox.addItem("GORM");
        comboBox.setMaximumSize(new Dimension(10000, 25));

        JBBox sqlIcon = JBBox.createHorizontalBox();

        JBLabel mysql = new JBLabel();
        JBLabel mssql = new JBLabel();
        JBLabel pgsql = new JBLabel();

        mysql.setIcon(AllIcons.Providers.Mysql);
        mssql.setIcon(AllIcons.Providers.SqlServer);
        pgsql.setIcon(AllIcons.Providers.Postgresql);
        mysql.setVisible(true);
        mssql.setVisible(false);
        pgsql.setVisible(false);
        sqlIcon.add(mysql);
        sqlIcon.add(mssql);
        sqlIcon.add(pgsql);
        sqlLanguage=new ComboBox<>();
        sqlLanguage.addItem("MySQL");
        sqlLanguage.addItem("MSSQL");
        sqlLanguage.addItem("PostgreSQL");
        sqlLanguage.setSelectedItem("MySQL");
        sqlLanguage.setMaximumSize(new Dimension(10000, 25));

        sqlLanguage.addItemListener(e -> {
            ItemSelectable itemSelectable = e.getItemSelectable();
            Object[] objects = itemSelectable.getSelectedObjects();
            String item = (String) objects[0];
            switch (item){
                case "MySQL"->{
                    mysql.setVisible(true);
                    mssql.setVisible(false);
                    pgsql.setVisible(false);
                }
                case "MSSQL"->{
                    mysql.setVisible(false);
                    mssql.setVisible(true);
                    pgsql.setVisible(false);
                }
                case "PostgreSQL"->{
                    mysql.setVisible(false);
                    mssql.setVisible(false);
                    pgsql.setVisible(true);
                }
            }
            sqlIcon.updateUI();
        });

        JBBox check = JBBox.createHorizontalBox();
        TitledBorder titledBorder = new TitledBorder( "配置");
        check.setMaximumSize(new Dimension(10000, 50));
        check.setBorder(titledBorder);
        check.add(def);
        check.add(json);
        check.add(column);
        check.add(type);
        check.add(label);
        def.setSelected(true);
        // 默认不展示
        column.setVisible(false);
        type.setVisible(false);
        json.setVisible(false);

        // 根据选项状态 动态切换配置
        comboBox.addItemListener(e -> {
            ItemSelectable itemSelectable = e.getItemSelectable();
            Object[] objects = itemSelectable.getSelectedObjects();
            String item = (String) objects[0];
            switch (item) {
                case "GoBatis"->{
                    // 关闭选项
                    def.setVisible(false);
                    column.setVisible(false);
                    type.setVisible(false);

                    // 取消选择
                    def.setSelected(false);
                    type.setSelected(false);
                    column.setSelected(false);
                    json.setSelected(false);

                    json.setVisible(true);
                }
                case "GORM"->{
                    // 关闭选项
                    def.setVisible(false);


                    def.setSelected(false);
                    type.setSelected(false);
                    column.setSelected(true);
                    json.setSelected(false);

                    column.setVisible(true);
                    type.setVisible(true);
                    json.setVisible(true);
                }
                default -> {
                    column.setVisible(false);
                    type.setVisible(false);
                    json.setVisible(false);

                    json.setSelected(false);
                    type.setSelected(false);
                    column.setSelected(false);

                    def.setVisible(true);
                    def.setSelected(true);
                }
            }
            check.updateUI();
        });

        JBBox box = JBBox.createHorizontalBox();
        box.add(select);
        box.add(comboBox);

        JBBox box2 = JBBox.createHorizontalBox();

        box2.add(sqlIcon);
        box2.add(sqlLanguage);

        Document document = EditorFactory.getInstance().createDocument(StringUtil.convertLineSeparators(""));
        create = EditorFactory.getInstance().createEditor(document, project, SqlFileType.INSTANCE, false);

        Border border = BorderFactory.createEmptyBorder();
        JComponent jComponent = create.getComponent();
        jComponent.setFocusable(true);
        JBScrollPane scrollPane = new JBScrollPane(jComponent);

        mainBox.setBorder(border);
        mainBox.add(box);
        mainBox.add(box2);
        mainBox.add(check);
        mainBox.add(scrollPane);
        return mainBox;
    }

    protected JComponent createSouthPanel() {
        JButton ok = new JButton();
        ok.setIcon(AllIcons.General.InspectionsOK);
        JButton close = new JButton();
        close.setIcon(AllIcons.Actions.Close);
        ok.addActionListener(e -> {
            // 检查输入的 内容如果
            String sql = create.getDocument().getText();
            if (sql.equals("")) {
                AuroraNotify.notifyWarning(project, "请输入需要生成的SQL语句");
                return;
            }
            String sqlCode = StringUtil.convertLineSeparators(sql);
            // 内容不为空
            PsiFile sqlFile = PsiFileFactory.getInstance(project).createFileFromText(SqlLanguage.INSTANCE,sqlCode );
            String sqlLanguageItem = sqlLanguage.getItem();
            switch (sqlLanguageItem){
                case "MySQL"->{
                    SqlLanguageDialect mysql = SqlDialects.findDialectById(MysqlDialect.INSTANCE.getID());
                    sqlFile=SqlPsiElementFactory.createROFileFromText(sqlCode, mysql, project, SqlLanguage.INSTANCE);
                }
                case "MSSQL"->{
                    SqlLanguageDialect mssql = SqlDialects.findDialectById(AzureDialect.INSTANCE.getID());
                    sqlFile=SqlPsiElementFactory.createROFileFromText(sqlCode, mssql, project, SqlLanguage.INSTANCE);
                }
                case "PostgreSQL"->{
                    SqlLanguageDialect pg = SqlDialects.findDialectById(PgDialect.INSTANCE.getID());
                    sqlFile=SqlPsiElementFactory.createROFileFromText(sqlCode, pg, project, SqlLanguage.INSTANCE);
                }
            }

            if (sqlFile == null) return;
            PsiElement[] children = sqlFile.getChildren();
            List<PsiElement> list = new ArrayList<>();
            String item = comboBox.getItem();
            OrmBuild build;
            SQL_GO sqlGo;
            String goCode;
            List<PsiElement> goElement;
            for (PsiElement element : children) {
                if (element instanceof SqlCreateTableStatementImpl table) {
                    String tableName = table.getName();
                    switch (item) {
                        case "GoBatis" -> {
                            build = new GoBatis(json.isSelected());
                        }
                        case "GORM" -> {
                            build = new Gorm(column.isSelected(), type.isSelected(), json.isSelected());
                        }
                        default -> {
                            build = new DefaultOrm(def.isSelected());
                        }
                    }
                    sqlGo = build.ColumnToFields(table);
                    goCode = build.GoCode(tableName, sqlGo);
                    goElement = build.GoElement(goCode, project);
                    list.addAll(goElement);
                }
                if (element instanceof PsiErrorElement) {
                    AuroraNotify.notifyError(project, "ORM 生成失败，请检查SQL语法是否正确!");
                    this.close(-1, false);
                    return;
                }
            }
            if (insert == null) {
                this.close(-1, false);
                return;
            }
            PsiElement index;
            index = PsiTreeUtil.getParentOfType(insert, GoTypeDeclarationImpl.class);
            if (index != null) {
                AuroraNotify.notifyWarning(project, "创建失败，请选择一个空白位置");
                return;
            }
            index = PsiTreeUtil.getParentOfType(insert, GoMethodDeclarationImpl.class);
            if (index != null) {
                AuroraNotify.notifyWarning(project, "创建失败，请选择一个空白位置");
                return;
            }
            index = PsiTreeUtil.getParentOfType(insert, GoFunctionDeclarationImpl.class);
            if (index != null) {
                AuroraNotify.notifyWarning(project, "创建失败，请选择一个空白位置");
                return;
            }


            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    ProgressManager instance = ProgressManager.getInstance();
                    boolean process = instance.runProcessWithProgressSynchronously(new Runnable() {
                        @Override
                        public void run() {
                            PsiElement parent = insert.getParent();
                            int offset = insert.getTextOffset();
                            PsiElement add = insert;
                            for (PsiElement element : list) {
                                add = parent.addAfter(element, add);
                            }
                            CaretModel caretModel = editor.getCaretModel();
                            caretModel.moveToOffset(offset);
                            ScrollingModel scrollingModel = editor.getScrollingModel();
                            scrollingModel.scrollVertically(offset);

                        }
                    }, "Create struct", true, project);
                    if (process){
                        AuroraNotify.notifyMsg(project,"执行成功");
                    }else {
                        AuroraNotify.notifyWarning(project,"已取消");
                    }
                }
            });
            this.close(0, true);
        });

        close.addActionListener(e -> {
            this.close(0, true);
        });
        JBLabel label = new JBLabel("");
        label.setMaximumSize(new Dimension(1000, 25));
        butBox.add(label);
        butBox.add(ok);
        butBox.add(close);
        JBBox verticalBox = JBBox.createVerticalBox();
        verticalBox.add(butBox);
        return verticalBox;
    }
}
