package org.aurora.gobatis.mark.mapper.func;

import com.intellij.util.Function;

/*
 *   WebMarkHint 行标记鼠标提示语
 * */
public class MapperFuncMarkHint implements Function {
    private String msg;

    public MapperFuncMarkHint() {
        this.msg = "default";
    }

    public MapperFuncMarkHint(String msg) {
        this.msg = msg;
    }

    @Override
    public Object fun(Object o) {
        return this.msg;
    }
}
