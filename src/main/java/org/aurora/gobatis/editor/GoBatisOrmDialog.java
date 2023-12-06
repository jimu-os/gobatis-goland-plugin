package org.aurora.gobatis.editor;

import com.goide.psi.impl.GoFunctionDeclarationImpl;
import com.goide.psi.impl.GoMethodDeclarationImpl;
import com.goide.psi.impl.GoTypeDeclarationImpl;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.sql.SqlFileType;
import com.intellij.sql.psi.SqlLanguage;
import com.intellij.sql.psi.impl.SqlCreateTableStatementImpl;
import com.intellij.ui.components.JBBox;
import com.intellij.ui.components.JBScrollPane;
import org.aurora.notify.AuroraNotify;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoBatisOrmDialog extends DialogWrapper {

    JBBox mainBox=JBBox.createVerticalBox();
    JBBox butBox=JBBox.createHorizontalBox();
    Project project;
    PsiElement insert;
    Editor editor;
    Editor create;
    public GoBatisOrmDialog(@Nullable Project project, Editor editor ,PsiElement element, boolean canBeParent) {
        super(project, canBeParent);
        this.project=project;
        this.editor=editor;
        this.insert=element;
        setTitle("Create GoBatis ORM");
//        setOKActionEnabled(false);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        Document document = EditorFactory.getInstance().createDocument(StringUtil.convertLineSeparators(""));
        create = EditorFactory.getInstance().createEditor(document,project, SqlFileType.INSTANCE,false);
        Border border = BorderFactory.createEmptyBorder();
        JComponent jComponent = create.getComponent();
        jComponent.setFocusable(true);
        JBScrollPane scrollPane = new JBScrollPane(jComponent);

        scrollPane.setBorder(border);
        mainBox.setBorder(border);
        mainBox.add(scrollPane);
        return mainBox;
    }

    protected  JComponent createSouthPanel(){
        JButton ok = new JButton("Ok");
        ok.setIcon(AllIcons.Actions.Edit);
        JButton close=new JButton("Close");
        close.setIcon(AllIcons.Actions.Close);
        ok.addActionListener(e -> {
            // 检查输入的 内容如果
            String sql = create.getDocument().getText();
            if (sql.equals("")){
                AuroraNotify.notifyWarning(project,"请输入需要生成的SQL语句");
                return;
            }
            // 内容不为空
            PsiFile sqlFile= PsiFileFactory.getInstance(project).createFileFromText(SqlLanguage.INSTANCE, StringUtil.convertLineSeparators(sql));
            if (sqlFile==null)return;
            PsiElement[] children = sqlFile.getChildren();
            List<PsiElement> list=new ArrayList<>();
            for (PsiElement element:children){
                if (element instanceof SqlCreateTableStatementImpl table){
                    String tableName = table.getName();
                    Map<String, String> fields = SqlToGoBatis.ColumnToFields(table);
                    String structCode = SqlToGoBatis.BuildStructCode(tableName, fields);
                    SqlToGoBatis.CreateGoPsiFile(list, structCode, project);
                }
                if (element instanceof PsiErrorElement){
                    AuroraNotify.notifyError(project,"ORM 生成失败，请检查SQL语法是否正确!");
                    this.close(-1,false);
                    return;
                }
            }
            if (insert==null){
                this.close(-1,false);
                return;
            }
            PsiElement index;
            index = PsiTreeUtil.getParentOfType(insert, GoTypeDeclarationImpl.class);
            if (index!=null) {
                AuroraNotify.notifyWarning(project,"创建失败，请选择一个空白位置");
                return;
            };
            index = PsiTreeUtil.getParentOfType(insert, GoMethodDeclarationImpl.class);
            if (index!=null) {
                AuroraNotify.notifyWarning(project,"创建失败，请选择一个空白位置");
                return;
            };
            index = PsiTreeUtil.getParentOfType(insert, GoFunctionDeclarationImpl.class);
            if (index!=null) {
                AuroraNotify.notifyWarning(project,"创建失败，请选择一个空白位置");
                return;
            };
            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    PsiElement parent = insert.getParent();
                    int offset = insert.getTextOffset();
                    PsiElement add=insert;
                    for (PsiElement element:list){
                        add= parent.addAfter(element, add);
                    }
                    CaretModel caretModel = editor.getCaretModel();
                    caretModel.moveToOffset(offset);
                    ScrollingModel scrollingModel = editor.getScrollingModel();
                    scrollingModel.scrollVertically(offset);
                }
            });
            this.close(0,true);
        });

        close.addActionListener(e -> {
            System.out.println("Close");
            this.close(0,true);
        });
        butBox.add(ok);
        return butBox;
    }
}
