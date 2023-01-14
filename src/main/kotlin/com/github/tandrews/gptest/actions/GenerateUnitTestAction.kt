package com.github.tandrews.gptest.actions

import com.github.tandrews.gptest.dialogs.GenerateUnitTestDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class GenerateUnitTestAction: AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = e.project != null
    }
    override fun actionPerformed(e: AnActionEvent) {
        GenerateUnitTestDialog(e.project!!).show()
    }
}
