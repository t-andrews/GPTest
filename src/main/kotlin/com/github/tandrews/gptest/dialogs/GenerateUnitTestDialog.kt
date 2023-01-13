package com.github.tandrews.gptest.dialogs

import com.github.tandrews.gptest.services.TestCreationService
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.textFieldWithBrowseButton
import com.intellij.ui.components.textFieldWithHistoryWithBrowseButton
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBUI
import kotlinx.coroutines.runBlocking
import javax.swing.JComponent


class GenerateUnitTestDialog(p: Project) : DialogWrapper(p, true) {
    var project: Project
    private var sourceFile: VirtualFile?
    private var testFile: VirtualFile?

    init {
        init()
        title = "Generate Unit Test"
        project = p
        sourceFile = getBaseSourceFile()
        testFile = null
        myOKAction.isEnabled = false
        setOKButtonText("Generate")
    }

    companion object {
        const val WIDTH = 1000
        const val HEIGHT = 400
    }

    private fun getBaseSourceFile(): VirtualFile? {
        val currentDoc: Document = FileEditorManager.getInstance(project).selectedTextEditor!!.document
        val currentFile: VirtualFile? = FileDocumentManager.getInstance().getFile(currentDoc)
        if (currentFile?.extension != "ts") {
            return null
        }
        return currentFile
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            val sourceFileChosen: (it: VirtualFile) -> String = {
                sourceFile = it
                evaluateIsOkEnabled()
                sourceFile!!.name
            }
            val testFileChosen: (it: VirtualFile) -> String = {
                testFile = it
                evaluateIsOkEnabled()
                testFile!!.name
            }
            val sourceFileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
            sourceFileDescriptor.title = "Source File"
            sourceFileDescriptor.description = "Choose a source file"

            // TODO: find a way to set default source field selection to sourceFile

            val testFileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
            testFileDescriptor.title = "Test File"
            testFileDescriptor.description = "Choose the test file to save to"

            row {
                label("Source file:")
                textFieldWithBrowseButton(fileChooserDescriptor = sourceFileDescriptor, project = project, fileChosen = sourceFileChosen)
            }

            row {
                label("Test file:  ")
                textFieldWithBrowseButton(fileChooserDescriptor = testFileDescriptor, project = project, fileChosen = testFileChosen)
            }

        }.apply {
            preferredSize = JBUI.size(WIDTH, HEIGHT)
        }
    }

    override fun doOKAction() {
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, "GPTest") {
            override fun run(progressIndicator: ProgressIndicator) {
                progressIndicator.isIndeterminate = true
                progressIndicator.text = "Generating unit tests"
//                runBlocking {
//                    TestCreationService().generateTest(sourceFile!!.inputStream.bufferedReader().use { br -> br.readText() })
//                }
                progressIndicator.text = "Done"
                // TODO: link to generated tests file
            }
        })
        super.doOKAction()
    }

    private fun evaluateIsOkEnabled() {
        myOKAction.isEnabled = sourceFile != null && testFile != null
    }
}
