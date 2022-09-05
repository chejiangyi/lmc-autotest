#!/bin/bash
echo "用途: 标准后端项目脚手架生成脚本"
echo "使用命令格式:"
echo "curl -O http://gitlab.codefr.com/linkhub-public/lmc-demo/raw/{version}/doc/client.sh"
echo "sh client.sh {projectName} {groupId} {version}"
## projectName is the artifcactId of Project
projectName=$1
groupId=$2
#package=$3
version=$3 #默认master
if [[ $projectName == "" ]]; then
	echo "错误: 请输入新项目名"
	exit 0
fi
if [[ $groupId == "" ]]; then
	#echo "错误: 请输入groupId~"
	groupId="com.lmc"
	#exit 0
fi
if [[ $version == "" ]]; then
	version="master"
fi

### 目录
path=$(cd $(dirname $0); pwd)
projectPath=$path/lmc-$projectName
echo "当前目录:$path,新项目名:$projectName,groupID:$groupId (例如：com.lmc),脚手架版本:$version"


### 下载项目模板
#删除上次的临时文件和包
rm -rf lmc-${projectName}*
rm -rf lmc-demo-master.tar.gz
#https://gitlab.infra.bitmart.com/open/lmc-demo/-/archive/master/lmc-demo-master.tar.gz
response=$(curl -o lmc-demo-master.tar.gz -O http://gitlab.codefr.com/linkhub-public/lmc-demo/-/archive/master/lmc-demo-master.tar.gz)

if [ -d "$projectPath" ];then
  echo "删除文件"
  rm -Rf $projectPath
fi
tar -zxvf lmc-demo-master.tar.gz
#unzip lmc-demo-${version}.tar.gz
mv -fiv $path/lmc-demo-master $path/lmc-${projectName}
#mv -fiv $path/lmc-demo $projectPath
echo "下载模板完毕:lmc-demo-master.tar.gz"

###循环所有文件 替换com.lmc.demo
function loopdic() {
  for f in `ls $1`
  do
    if [ -d $1"/"$f ];then
      loopdic $1"/"$f
    else
      local path2=$1"/"$f
      local name2=$f
			if [[ ($path2 == *.java) || ($path2 == *.xml) || ($path2 == *.properties) || ($path2 == *.md) ]]; then
				echo "扫描文件: ${path2}"
				if [ "$(uname)" == "Darwin" ]; then
					sed -i '' "s/lmc-demo/lmc-$projectName/g" $path2
					sed -i '' "s/com.lmc.demo/$groupId.$projectName/g" $path2
					sed -i '' "s/swagger.title=DEMO/swagger.title=$projectname/g" $path2
				else
					sed -i "s/lmc-demo/lmc-$projectName/g" $path2
					sed -i "s/com.lmc.demo/$groupId.$projectName/g" $path2
					sed -i "s/swagger.title=DEMO/swagger.title=$projectname/g" $path2
				fi
			fi
		fi
  done
}

loopdic $projectPath
echo "完毕:循环所有文件并替换demo信息"

### 循环替换模块文件夹
dirs=('lmc-demo-core' 'lmc-demo-provider' 'lmc-demo-dao' 'lmc-demo-service' 'lmc-demo-api' 'lmc-demo-task')
packagedir=${groupId//\./\/};
for d in ${dirs[@]}
do
    newd=${d/demo/${projectName}}
    echo "扫描文件夹: $projectPath/$d/"
    if [ ! -d "$projectPath/$d/src/main/java/$packagedir/" ];then
       mkdir -p $projectPath/$d/src/main/java/$packagedir/
    fi
		mv -fiv $projectPath/$d/src/main/java/com/lmc/demo $projectPath/$d/src/main/java/$packagedir/$projectName
		rm -Rf $projectPath/$d/src/main/java/com/lmc/demo
    mv -fiv $projectPath/$d $projectPath/$newd
done
echo "完毕:循环模块文件夹重命名所有demo目录"
echo "全部完成！！！享受它吧~~~~"
#删除原始包
rm -rf lmc-demo-master.tar.gz
