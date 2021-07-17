def call(Map params = [:]) {
    // start Default Arguments
    def args=[
            NEXUS_IP: '172.31.8.88'
    ]
    args << params

    // End Default + Required Arguments
    pipeline {
        agent {
            label"${args.SLAVE_LABEL}"
        }
        environment {
            COMPONENT="${args.COMPONENT}"
            NEXUS_IP="${args.NEXUS_IP}"
            PROJECT_NAME="${args.PROJECT_NAME}"
            SLAVE_LABEL="${args.SLAVE_LABEL}"
            APP_TYPE="${args.APP_TYPE}"
        }
        stages {
            stage('code build & install dependencies') {
                steps{
                    script{
                        build=new nexus()
                        build.code_build("${COMPONENT}")
                    }
                }
            }
            stage('prepare artifacts') {
                steps{
                    script{
                        prepare=new nexus()
                        prepare.make_artifacts("${COMPONENT}")
                    }
                }
            }
            stage('upload artifacts') {
                steps{
                    script{
                        prepare=new nexus()
                        prepare.nexus("${COMPONENT}")
                    }
                }
            }
        }
    }
}
