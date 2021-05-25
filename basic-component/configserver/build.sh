#!/bin/bash

source /etc/profile



if [ -z "$1" ]; then
    printf "usage: $0 {dev|test|prod|local} \n"
    exit 1
fi

function check() {
  if [ $1 != 0 ];then
    echo "exec fail"
    exit 1
  fi
}

PROJECT_NAME=$2
script_path=$3
appName=$4
ARTIFACT_FILE="${PROJECT_NAME}.tar.gz"

echo $"[$1] environment building ..."

if [ -f $ARTIFACT_FILE ]
then
    rm ${ARTIFACT_FILE}
fi

echo ":${PROJECT_NAME}:bootJar}"
./gradlew  :basic-component:${PROJECT_NAME}:bootJar

check $?
cd basic-component/$PROJECT_NAME/build/libs
cp ${script_path}/scripts/service.sh ./
cp ${script_path}/scripts/start.sh ./
rm -rf *sources.jar *.original
mv *.jar ${PROJECT_NAME}.jar
mkdir -p ${PROJECT_NAME}
mv *.jar *.sh ${PROJECT_NAME}
sed -i "s/xxx/${PROJECT_NAME}/g" ${PROJECT_NAME}/service.sh
sed -i "s/xxx/${appName}/g" ${PROJECT_NAME}/start.sh
tar -czf ${ARTIFACT_FILE} ${PROJECT_NAME}
mv ${ARTIFACT_FILE} ../../
cd ../../../../
./gradlew  clean

echo "Medical_SUCCESS"