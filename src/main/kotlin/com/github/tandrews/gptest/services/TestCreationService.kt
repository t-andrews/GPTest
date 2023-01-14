package com.github.tandrews.gptest.services

import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.intellij.openapi.components.Service

class TestCreationService {
    private val openAi: OpenAI  = OpenAI(API_KEY)

    companion object {
        const val API_KEY = "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";
    }

    suspend fun generateTest(fileContent: String): String {
        val suggestedTest = openAi.completion(CompletionRequest(
            model = ModelId("text-davinci-003"),
            prompt = "Write a unit test suite that covers all paths, lines and public methods using Typescript, mocha, sinon and chai for this Typescript class: \n$fileContent. It should be in the style of this existing test: import 'mocha';\n" +
                    "import { expect } from 'chai';\n" +
                    "import * as sinon from 'sinon';\n" +
                    "import { IConfig } from 'config';\n" +
                    "import { Logger } from '@ssense/logger';\n" +
                    "import { SFNClient } from '@aws-sdk/client-sfn';\n" +
                    "import App from '../../../../../src/application/App';\n" +
                    "import * as fixtures from '../../../fixtures/application/command/process-product-listed-handler-test.json';\n" +
                    "import { ErrorMetricService } from '../../../../../../shared/src/application/exception/ErrorMetricService';\n" +
                    "import { ProcessProductListedHandler } from '../../../../../src/application/command/process-product-listed/ProcessProductListedHandler';\n" +
                    "import { ProcessProductListedCommand } from '../../../../../src/application/command/process-product-listed/ProcessProductListedCommand';\n" +
                    "import { ProductTaskProbe } from '../../../../../src/domain/probe/ProductTaskProbe';\n" +
                    "import { TaskTokenRepository } from '../../../../../src/domain/repository/TaskTokenRepository';\n" +
                    "\n" +
                    "let logger: Logger;\n" +
                    "let config: IConfig;\n" +
                    "let probe: ProductTaskProbe;\n" +
                    "let errorMetricService: ErrorMetricService;\n" +
                    "let stepFunctionClient: SFNClient;\n" +
                    "let handler: ProcessProductListedHandler;\n" +
                    "let taskTokenRepo: TaskTokenRepository;\n" +
                    "\n" +
                    "describe('ProcessProductListedHandler', () => {\n" +
                    "    beforeEach(() => {\n" +
                    "        logger = App.get<Logger>('Logger');\n" +
                    "        config = App.get<IConfig>('Config');\n" +
                    "        probe = App.getNamed<ProductTaskProbe>('Probe', 'ProductTask');\n" +
                    "        errorMetricService = App.getNamed<ErrorMetricService>('Service', 'ErrorMetric');\n" +
                    "        stepFunctionClient = App.getNamed<SFNClient>('Client', 'StepFunction');\n" +
                    "        taskTokenRepo = App.getNamed<TaskTokenRepository>('Repository', 'TaskToken');\n" +
                    "        handler = new ProcessProductListedHandler(logger, config, probe, errorMetricService, stepFunctionClient, taskTokenRepo);\n" +
                    "    });\n" +
                    "    afterEach(() => {\n" +
                    "        sinon.restore();\n" +
                    "    });\n" +
                    "\n" +
                    "    describe('Positive scenarios', () => {\n" +
                    "        it('Should process a product creation listing and send the task success', async () => {\n" +
                    "            sinon.stub(taskTokenRepo, 'findOldestUpdateProductTaskToken').resolves(null);\n" +
                    "            const findTokenStub = sinon.stub(taskTokenRepo, 'findCreateProductTaskToken').resolves(<any>fixtures.creationToken);\n" +
                    "            const sendTaskSuccessStub = sinon.stub(stepFunctionClient, 'send').resolves();\n" +
                    "\n" +
                    "            await handler.handle(new ProcessProductListedCommand(fixtures.handlerInput, 'reqId'));\n" +
                    "\n" +
                    "            expect(findTokenStub.args[0][0]).to.be.equal(\n" +
                    "                fixtures.handlerInput.MATERIAL,\n" +
                    "                'findCreateProductTaskToken should be called with correct product code'\n" +
                    "            );\n" +
                    "            expect(sendTaskSuccessStub.calledOnce).to.be.equal(true, 'Task success should be sent');\n" +
                    "        });\n" +
                    "        it('Should process a product update listing and send the task success', async () => {\n" +
                    "            const findUpdateTokenStub = sinon.stub(taskTokenRepo, 'findOldestUpdateProductTaskToken').resolves(<any>fixtures.updateToken);\n" +
                    "            const findCreateTokenSpy = sinon.spy(taskTokenRepo, 'findCreateProductTaskToken');\n" +
                    "            const sendTaskSuccessStub = sinon.stub(stepFunctionClient, 'send').resolves();\n" +
                    "\n" +
                    "            await handler.handle(new ProcessProductListedCommand(fixtures.handlerInput, 'reqId'));\n" +
                    "\n" +
                    "            expect(findUpdateTokenStub.args[0][0]).to.be.equal(\n" +
                    "                fixtures.handlerInput.MATERIAL,\n" +
                    "                'findOldestUpdateProductTaskToken should be called with correct product code'\n" +
                    "            );\n" +
                    "            expect(findCreateTokenSpy.called).to.be.equal(false, 'findCreateProductTaskToken should not be called');\n" +
                    "            expect(sendTaskSuccessStub.calledOnce).to.be.equal(true, 'Task success should be sent');\n" +
                    "        });\n" +
                    "    });\n" +
                    "    describe('Negative scenarios', () => {\n" +
                    "        it('Should use the probe and return when token is not found', async () => {\n" +
                    "            const taskNotFoundStub = sinon.stub(probe, 'taskNotFound').resolves();\n" +
                    "            const taskSucceededStub = sinon.stub(probe, 'taskSucceeded').resolves();\n" +
                    "            sinon.stub(taskTokenRepo, 'findOldestUpdateProductTaskToken').resolves(null);\n" +
                    "            sinon.stub(taskTokenRepo, 'findCreateProductTaskToken').resolves(null);\n" +
                    "\n" +
                    "            await handler.handle(new ProcessProductListedCommand(fixtures.handlerInput, 'reqId'));\n" +
                    "            expect(taskNotFoundStub.args[0][0]).to.deep.equal(fixtures.handlerInput.MATERIAL);\n" +
                    "            expect(taskSucceededStub.called).to.equal(false, 'taskSucceeded should not be called');\n" +
                    "        });\n" +
                    "    });\n" +
                    "});" +
                    "\nInclude assumed import statements",
            temperature = 0.5,
            maxTokens = 2048,
            topP = 1.0,
        ))
        return suggestedTest.choices[0].text
    }
}
