package org.aurora.tool.orm.abs;


import org.aurora.tool.orm.DefaultOrm;
import org.aurora.tool.orm.OrmBuild;
import org.aurora.tool.orm.ormImp.gobatis.GoBatis;
import org.aurora.tool.orm.ormImp.gorm.Gorm;

public class OrmFactory {
    public static OrmBuild Instance(String type){
        return switch (type) {
            case "GoBatis" -> new GoBatis();
            case "GORM" -> new Gorm();
            default -> new DefaultOrm();
        };
    }
}
