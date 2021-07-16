def nexus(COMPONENT) {
    get_branch="env | grep GIT_BRANCH | awk -F / '{print \$NF}'| xargs echo -n"
    def get_branch_exec=sh(returnStdout: true, script:get_branch)
    def FILENAME=COMPONENT+'-'+get_branch_exec+'.zip'

    command="curl -f -v -u admin:admin123 --upload-file ${FILENAME} http://172.31.8.88:8081/repository/${COMPONENT}/${FILENAME}"
    def execute_state=sh(returnStdout: true, script: command)
}

def make_artifacts(APP_TYPE, COMPONENT) {
    get_branch="env | grep GIT_BRANCH | awk -F / '{print \$NF}'| xargs echo -n"
    def get_branch_exec=sh(returnStdout: true, script:get_branch)
    def FILENAME=COMPONENT+'-'+get_branch_exec+'.zip'
    if(APP_TYPE == "NODEJS") {
        command="zip -r ${FILENAME} .*"
        def execute_com=sh(returnStdout: true, script: command)
        println(execute_com)
    } else if(COMPONENT == "login1") {
        command="zip -r ${FILENAME} main.go user.go tracing.go"
        def execute_com=sh(returnStdout: true, script: command)
        println(execute_com)
    } else if(COMPONENT == "users") {
        command="cp target/*.jar ${COMPONENT}.jar && zip -r ${FILENAME} ${COMPONENT}.jar"
        def execute_com=sh(returnStdout: true, script: command)
        println(execute_com)
    }
}

def code_build(COMPONENT) {
    if(COMPONENT == "frontend1") {
        command = "npm install && npm run build"
        def execute_com=sh(returnStdout: true, script: command)
        println(execute_com)
    }  else if(COMPONENT == "users") {
        command = "mvn clean package"
        def execute_com=sh(returnStdout: true, script: command)
        println(execute_com)
    }  else if(COMPONENT == "login1") {
        command = " go get github.com/openzipkin/zipkin-go && go get github.com/openzipkin/zipkin-go/middleware/http &&go get github.com/openzipkin/zipkin-go/reporter/http && go get github.com/labstack/echo && go get github.com/labstack/echo/middleware &&go get github.com/labstack/gommon/log && go build main.go user.go tracing.go"
        def execute_com=sh(returnStdout: true, script: command)
    }  else if(COMPONENT == "todo1") {
        command = "npm install && npm i nodemon && npm link nodemon"
        def execute_com=sh(returnStdout: true, script: command)
        println(execute_com)
}