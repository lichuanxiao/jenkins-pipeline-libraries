def configureMaven(String name='maven', String opts="-Xmx1024m -XX:MaxPermSize=1024m") {
    def path = configureTool(name)
    env.MAVEN_HOME = path
    env.MAVEN_OPTS = opts
    echo("Configured MAVEN_OPTS: ${opts}")
    return path
}

def configureJava(String name='java') {
    def path = configureTool(name)
    env.JAVA_HOME = path
    echo("Configured JAVA_HOME: ${path}")
    return path
}

def configureAndroid(String name='android', String pathToExecutable='tools', String sdkOpts='"build-tools;25.0.2"') {
    def path = tool name
//    env.PATH = "${env.PATH}:${path}:${path}/tools:${path}/platform-tools"

//    env.PATH = "${env.PATH}:${path}"
    sh("mkdir ${path}/sdk")
    env.ANDROID_HOME = "${path}/sdk"
    env.PATH = "${env.PATH}:${path}/${pathToExecutable}"
    echo("Configured ANDROID_HOME: ${env.ANDROID_HOME}")
    echo("PATH is now ${env.PATH}")

    sh("(while sleep 4; do echo \"y\"; done) | ${path}/${pathToExecutable}/bin/sdkmanager ${sdkOpts}")

    // Android looks for the licenses in the wrong directory, so we symlink it.
    sh("ln -s ${path}/licenses ${path}/${pathToExecutable}/licenses")
//    sh("echo 'y' | ${path}/${pathToExecutable}/sdkmanager ${sdkOpts}")
//    sh("echo 'y' | android update sdk --no-ui --filter 1,2")
}

def configureTool(String name, String pathToExecutable="bin") {
    def path = tool name
    def executablePath = stripLastSlash("${path}/${pathToExecutable}")
    env.PATH = "${env.PATH}:${executablePath}"
    echo("Configured ${name}: ${path}")
    echo("Path is now ${env.PATH}")
    return path
}

private stripLastSlash(String path){
    (path.endsWith("/")) ? path.substring(0, path.lastIndexOf("/")) : path
}

return this
