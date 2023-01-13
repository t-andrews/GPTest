package com.github.tandrews.gptest.actions

import com.github.tandrews.gptest.dialogs.GenerateUnitTestDialog
import com.github.tandrews.gptest.services.TestCreationService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileChooser.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.RegisterToolWindowTask
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import kotlinx.coroutines.runBlocking
import java.awt.BorderLayout
import java.time.LocalDate
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class GenerateUnitTestAction: AnAction() {

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = e.project != null
    }
    override fun actionPerformed(e: AnActionEvent) {
        GenerateUnitTestDialog(e.project!!).show()

//        val sourceFileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
//        sourceFileDescriptor.title = "Source File"
//        sourceFileDescriptor.description = "Source file to create unit tests for"
//        FileChooser.chooseFile(sourceFileDescriptor, project, null) {
//            val testFileDescriptor = FileSaverDescriptor("Test File", "Choose test file location", "ts")
//            val testFileContent = runBlocking { TestCreationService().generateTest(it.inputStream.bufferedReader().use { br -> br.readText() }) }
//            val saveDialog = FileChooserFactory.getInstance().createSaveFileDialog(testFileDescriptor, project)
//            val savedFile = saveDialog.save(null)?.file
//
//            if (savedFile != null) {
//                savedFile.writeText(testFileContent)
//                Messages.showMessageDialog("Generated tests saved to ${savedFile.path}", "Tests Generated", null)
//            }
//        }
    }
}
