pipeline {
    agent any
    
    environment {
        PROJECT_NAME = 'batch-study'
        DOCKER_IMAGE_NAME = "${PROJECT_NAME}:${BUILD_NUMBER}"
        DOCKER_CONTAINER_NAME = "${PROJECT_NAME}"
        
        // 빌드 결과물 경로
        JAR_FILE = "build/libs/${PROJECT_NAME}-*.jar"
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '코드 체크아웃 중...'
                checkout scm
            }
        }
        
        // 2단계: 빌드
        stage('Build') {
            steps {
                echo '프로젝트 빌드 중...'
                script {
                    sh '''
                        # Java 버전 확인
                        java -version
                        
                        # Gradle 빌드
                        chmod +x ./gradlew || true
                        bash ./gradlew clean build -x test
                    '''
                }
            }
        }
        
        // 3단계: 테스트
        stage('Test') {
            steps {
                echo '테스트 실행 중...'
                sh 'bash ./gradlew test'
            }
            post {
                always {
                    // 테스트 결과 리포트 저장
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
        
        // 4단계: Docker 이미지 빌드
        stage('Docker Build') {
            steps {
                echo 'Docker 이미지 빌드 중...'
                script {
                    def dockerfileExists = fileExists('Dockerfile')
                    if (dockerfileExists) {
                        sh """
                            
                            docker build -t ${DOCKER_IMAGE_NAME} .
                            docker tag ${DOCKER_IMAGE_NAME} ${PROJECT_NAME}:latest
                        """
                    } else {
                        echo 'Dockerfile 업서용'
                    }
                }
            }
        }
        
        // 5단계: 배포
        stage('Deploy') {
            when {
                // main 브랜치에만 배포
                branch 'main'
            }
            steps {
                echo '배포 중...'
                script {
                    def dockerfileExists = fileExists('Dockerfile')
                    if (dockerfileExists) {
                        sh """
                        
                            docker network inspect batchstudy_kafka-network >/dev/null 2>&1 || \\
                            docker network create batchstudy_kafka-network || true
                            
                            # 기존 컨테이너 중지 및 제거
                            docker stop ${DOCKER_CONTAINER_NAME} || true
                            docker rm ${DOCKER_CONTAINER_NAME} || true
                            
                            # 새 컨테이너 실행
                            docker run -d \\
                                --name ${DOCKER_CONTAINER_NAME} \\
                                -p 9090:9090 \\
                                --network batchstudy_kafka-network \\
                                --restart unless-stopped \\
                                ${DOCKER_IMAGE_NAME}
                            
                            # 컨테이너 상태 확인
                            sleep 3
                            docker ps -a | grep ${DOCKER_CONTAINER_NAME} || echo '컨테이너 실행 실패'
                            docker logs --tail 50 ${DOCKER_CONTAINER_NAME} || echo '로그 확인 실패'
                        """
                    } else {
                        echo 'Dockerfile 없음'
                    }
                }
            }
        }
    }
    
    post {
        // 빌드 성공 시
        success {
            echo '빌드 성공!'
        }
        
        // 빌드 실패 시
        failure {
            echo '빌드 실패'
        }
        
        // 항상 실행
        always {
            // 빌드 아티팩트 저장
            archiveArtifacts artifacts: "${JAR_FILE}", allowEmptyArchive: true
            
            // 빌드 결과 정리
            cleanWs()
        }
    }
}
