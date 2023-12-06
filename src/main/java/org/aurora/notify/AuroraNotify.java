package org.aurora.notify;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

/*
 *   ��Ϣ֪ͨ
 * */
public class AuroraNotify {

    /*
     *   ������ʾ��Ϣ֪ͨ
     * */
    public static void notifyError(Project project,
                                   String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Aurora Notification")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
    }

    public static void notifyWarning(Project project,
                                   String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Aurora Notification")
                .createNotification(content, NotificationType.WARNING)
                .notify(project);
    }

    public static void notifyMsg(Project project,
                                     String content) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup("Aurora Notification")
                .createNotification(content, NotificationType.INFORMATION)
                .notify(project);
    }
}
