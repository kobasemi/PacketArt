個人作業ログ jnetpcapのインストール
=========
使用方法：　scriptreplay jnetpcap.*

要するに
=========
>pushd .
>mkdir -p $HOME/.tmp_jnetpcap;
>cd $HOME/.tmp_jnetpcap;
>sudo apt-get install gcc build-essential;
>sudo apt-get install openjdk-6-jre;
>sudo apt-get install openjdk-6-jdk;
>sudo apt-get install openjdk-6-source;
>[ ! -e $HOME/.profile ] && touch $HOME/.profile;
>echo "export JAVA_HOME=/usr/lib/jvm/java-6-openjdk-i386" >> $HOME/.profile;
>sudo apt-get install libpcap-dev;
>sudo ln -s /usr/lib/libpcap.so.1.1.1 /usr/lib/libpcap.so.0.9
>wget http://downloads.sourceforge.net/project/jnetpcap/jnetpcap/1.3/jnetpcap-1.3.0-1.fc.i386.tgz;
>tar xzvf jnetpcap-1.3.0-1.fc.i386.tgz; 
>cd jnetpcap-1.3.0;
>sudo cp libjnetpcap.so /usr/lib/;
>sudo chmod 644 /usr/lib/libjnetpcap.so;
>sudo cp ./include/*.h /usr/include/;#/usr/local/include,/includeかも。
>sudo mkdir -p /usr/java;
>sudo cp jnetpcap.jar /usr/java/;
>echo "export CLASSPATH=\$CLASSPATH:/usr/java/jnetpcap.jar" >>~/.profile;
>>sudo chmod 644 /usr/java/*.jar;
>echo "export CLASSPATH=\$CLASSPATH:." >>~/.profile;
>. .profile;
>popd;

