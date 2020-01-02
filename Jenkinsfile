def label = "pipeline"

def yaml_file = "deploy.yaml"
def docker_hub = "hub.thinkinpower.net"
def service_name = "rfwallet-server"
def service_port = 8118
def team_ns = "platform"
def replicas = 2
def host_name = "wallet.thinkinpower.net"
def live_path = "/index"

def docker_file = "Dockerfile"
def jar_path = "build/libs"
def jar_name = "rfwallet_server-1.0.0-SNAPSHOT.jar"
def app_path = "/data/service/rfwallet_server"

podTemplate(label: label) {
  node(label){

     stage('Scm') {
        cleanWs()
        checkout scm
     }

     stage('Build') {
       container('gradle') {
         sh 'gradle clean :bootJar'
       }
     }

     stage('Docker') {
           withCredentials([usernamePassword(
                credentialsId: 'dockerhub',
                usernameVariable: 'user',
                passwordVariable: 'pass')]) {
             sh """
               sed -i "s#%{jar_path}#${jar_path}#g" ${docker_file}
               sed -i "s#%{jar_name}#${jar_name}#g" ${docker_file}
               sed -i "s#%{app_path}#${app_path}#g" ${docker_file}
               docker login -u ${user} -p ${pass} ${docker_hub}
               docker build -t ${docker_hub}/platform/${service_name}:${build_tag} .
               docker push ${docker_hub}/platform/${service_name}:${build_tag}
               """
           }
     }

     stage('Deploy') {
         withKubeConfig([credentialsId: 'pipeline']) {
            sh """
              sed -i "s#%{build_tag}#${build_tag}#g" ${yaml_file}
              sed -i "s#%{app_path}#${app_path}#g" ${yaml_file}
              sed -i "s#%{docker_hub}#${docker_hub}#g" ${yaml_file}
              sed -i "s#%{service_name}#${service_name}#g" ${yaml_file}
              sed -i "s#%{service_port}#${service_port}#g" ${yaml_file}
              sed -i "s#%{team_ns}#${team_ns}#g" ${yaml_file}
              sed -i "s#%{replicas}#${replicas}#g" ${yaml_file}
              sed -i "s#%{host_name}#${host_name}#g" ${yaml_file}
              sed -i "s#%{live_path}#${live_path}#g" ${yaml_file}
              cat  ${yaml_file}
              kubectl apply -f ${yaml_file}
              """
         }
     }

  }

}