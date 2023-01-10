package com.github.tandrews.gptest.actions

import com.github.tandrews.gptest.services.TestCreationService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.*
import com.intellij.openapi.ui.Messages
import kotlinx.coroutines.runBlocking

class GenerateUnitTestAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val sourceFileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        sourceFileDescriptor.title = "Source File"
        sourceFileDescriptor.description = "Source file to create unit tests for"
        FileChooser.chooseFile(sourceFileDescriptor, e.project, null) {
            val testFileDescriptor = FileSaverDescriptor("Test File", "Choose test file location", "ts")
            val testFileContent = runBlocking { TestCreationService().generateTest(it.inputStream.bufferedReader().use { br -> br.readText() }) }
            val saveDialog = FileChooserFactory.getInstance().createSaveFileDialog(testFileDescriptor, e.project)
            val savedFile = saveDialog.save(null)?.file

            if (savedFile != null) {
                savedFile.writeText(testFileContent)
                Messages.showMessageDialog("Generated tests saved to ${savedFile.path}", "Tests Generated", null)
            }
        }
    }
}
