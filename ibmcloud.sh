#!/bin/sh
read -p "请输入电子邮件:" account
read -p "请输入账号密码:" password
read -p "请输入应用程序名称:" appname
read -p "请设置你的容器内存大小(默认256):" ramsize
if [ -z "$ramsize" ];then
	ramsize=256
fi
rm -rf cloudfoundry
mkdir cloudfoundry
cd cloudfoundry

echo '<!DOCTYPE html> '>>index.php
echo '<html> '>>index.php
echo '<body>'>>index.php
echo '<?php '>>index.php
echo 'echo "Hello World!"; '>>index.php
echo '?> '>>index.php
echo '<body>'>>index.php
echo '</html>'>>index.php

wget https://github.com/v2ray/v2ray-core/releases/latest/download/v2ray-linux-64.zip
unzip -d v2ray1 v2ray-linux-64.zip
cd v2ray1
chmod 777 *
cd ..
rm -rf v2ray-linux-64.zip
mv $HOME/cloudfoundry/v2ray1/v2ray $HOME/cloudfoundry/v2r
mv $HOME/cloudfoundry/v2ray1/v2ctl $HOME/cloudfoundry/v2ctl
rm -rf $HOME/cloudfoundry/v2ray1
cat >  $HOME/cloudfoundry/start.sh  << EOF
#!/bin/bash
./v2r&
sleep 120s
./ibmcloud config --check-version=false
./ibmcloud login -a "https://cloud.ibm.com" -r "us-south" -u "${account}" -p "${password}"
./ibmcloud cf install -f -v 6.15.0
./ibmcloud target --cf
./ibmcloud cf restart ${appname}
EOF
chmod 0755 $HOME/cloudfoundry/start.sh
echo "配置完成。"
curl -L -H 'Cache-Control: no-cache' -o "ibmcloud" "https://github.com/rootmelo92118/IBMYes-edit-from-CCChieh/raw/master/IBM_Cloud_CLI/ibmcloud"
curl -L -H 'Cache-Control: no-cache' -o "ibmcloud-analytics" "https://github.com/rootmelo92118/IBMYes-edit-from-CCChieh/raw/master/IBM_Cloud_CLI/ibmcloud-analytics"

uuid=`cat /proc/sys/kernel/random/uuid`
path=`echo $uuid | cut -f1 -d'-'`
echo '{"inbounds":[{"port":8080,"protocol":"vmess","settings":{"clients":[{"id":"'$uuid'","alterId":64}]},"streamSettings":{"network":"ws","wsSettings":{"path":"/'$path'"}}}],"outbounds":[{"protocol":"freedom","settings":{}}]}'>$HOME/cloudfoundry/config.json
echo 'applications:'>>manifest.yml
echo '- path: .'>>manifest.yml
echo '  command: '/app/htdocs/start.sh'' >>manifest.yml
echo '  name: '$appname''>>manifest.yml
echo '  random-route: true'>>manifest.yml
echo '  memory: '$ramsize'M'>>manifest.yml
ibmcloud target --cf
echo "N"|ibmcloud cf install
ibmcloud cf push
domain=`ibmcloud cf app $appname | grep routes | cut -f2 -d':' | sed 's/ //g'`
vmess=`echo '{"add":"'$domain'","aid":"64","host":"","id":"'$uuid'","net":"ws","path":"/'$path'","port":"443","ps":"IBM_Cloud","tls":"tls","type":"none","v":"2"}' | base64 -w 0`
cd ..
echo 容器已经成功启动
echo 地址: $domain
echo UUID: $uuid
echo path: /$path
echo vmess://$vmess
