package com.chudsaviet.gradle.avrohugger


import com.chudsaviet.gradle.avrohugger.common.TestProjectConfig
import org.gradle.testkit.runner.BuildResult
import org.junit.Rule
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class AvrohuggerPluginBuildFunctionalSpec extends Specification {

    @Rule
    com.chudsaviet.gradle.avrohugger.common.TestProject project = new com.chudsaviet.gradle.avrohugger.common.TestProject(new TestProjectConfig(
        buildDefinition: com.chudsaviet.gradle.avrohugger.common.Resources.read('sample.gradle').text,
        inputDirectories:['src-avro'],
        outputDirectories: ['src-scala']
    ))

    def "should generate and compile scala classes during build with custom config"() {
        given:
        project.inputFile('input.avsc') << com.chudsaviet.gradle.avrohugger.common.Resources.read('sample.avsc')

        when:
        final result = project.build()

        then:
        buildWasSuccessfull(result)
        compiledScalaClass().exists()
    }

    /*
     * Private helper methods
     */
    private static boolean buildWasSuccessfull(BuildResult result) {
        result.task(":build").outcome == SUCCESS
    }

    private File compiledScalaClass() {
        project.projectFile('build', 'classes', 'scala', 'main', 'com', 'chudsaviet', 'FullName.class')
    }
}
