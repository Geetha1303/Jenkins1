folder ('TODOCIPipelines') {
    displayName('TODOCIPipelines')
    description('TODOCIPipelines')
}
def component=["frontend1","login1","todo1","users"]
def count=(component.size()-1)
for (i in 0..count) {
    def j=component[i]
    println(j);
    pipelineJob("TODOCIPipelines/${j}-ci") {
        //println("CI-Pipelines/${j}-ci")
        configure { flowdefinition ->
            //flowdefinition / 'properties' << 'org.jenkinsci.plugins.workflow.job.properties.PipelineTriggersJobProperty' {
            // 'triggers' {
            //'hudson.triggers.SCMTrigger' {
            // 'spec'('*/2 * * * 1-5')
            // 'ignore PostCommitHooks'(false)
            // }
            // }
            //}

            flowdefinition << delegate.'definition'(class: 'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition', plugin: 'workflow-cps') {
                'scm'(class: 'hudson.plugins.git.GitSCM', plugin: 'git') {
                    'userRemoteConfigs' {
                        'hudson.plugins.git.UserRemoteConfig' {
                            'url'('https://github.com/Geetha1303/'+j+'.git')
                            'refspec'('\'+refs/tags/*\':\'refs/remotes/origin/tags/*\'')
                        }
                    }
                    'branches' {
                        'hudson.plugins.git.BranchSpec' {
                            'name' ('*/tags/*')
                        }
                    }
                }
                'scriptPath'('Jenkinsfile')
                'lightweight' (true)
            }
        }

    }
}
pipelineJob("Deployment Pipeline") {
    configure { flowdefinition ->
        flowdefinition << delegate.'definition'(class:'org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition',plugin:'workflow-cps') {
            'scm'(class:'hudson.plugins.git.GitSCM',plugin:'git') {
                'userRemoteConfigs' {
                    'hudson.plugins.git.UserRemoteConfig' {
                        'url'('https://github.com/Geetha1303/Jenkins1.git')
                    }
                }
                'branches' {
                    'hudson.plugins.git.BranchSpec' {
                        'name'('main')
                    }
                }
            }
            'scriptPath'('Jenkinsfile-Deployment')
            'lightweight'(true)
        }
    }
}