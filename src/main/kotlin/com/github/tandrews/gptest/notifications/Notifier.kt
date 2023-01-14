package com.github.tandrews.gptest.notifications

import com.github.tandrews.gptest.actions.OpenTestFileAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class Notifier {
    fun notifyTestsGenerated(
        project: Project,
        testFIle: VirtualFile
    ) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("GPTest Notifications")
            .createNotification("Test generation is saved in ${testFIle.path}", NotificationType.INFORMATION)
            .addAction(OpenTestFileAction(testFIle))
            .notify(project)
    }
}
