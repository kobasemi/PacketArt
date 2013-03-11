Windows向けjnetpcapインストール方法
=====

用意するもの
-----

- Java Runtime Environment (JRE)   
	- 6.0以上が望ましい?  
	<http://java.com/ja/download/>

- Java Development Kit (JDK)  
	- 新しいもの  
	<http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html>

- WinPcap  
	- jnetpcapのdllが参照するため  
	<http://www.winpcap.org/install/default.htm>

- jnetpcap  
	- これがなければコンパイルできない  
	<http://jnetpcap.com/>

- tcpdumpファイル  
	- これがなければ解析できない  
	<http://mawi.wide.ad.jp/mawi/samplepoint-F/2013/>

手順
-----

1. 	JRE,JDK,WinPcapをインストール
	- その際、JDKのインストール先を覚えておく

2. 	環境変数の設定
	- PATHにJDKのbinフォルダの場所を指定
	- JAVA_HOMEにJDKのbinフォルダの位置を指定

3.	ファイルの位置をメモっておく
	ダウンロードしたjnetpcap.zipを解凍したフォルダのdllファイルとjarファイルの位置  
	ここでこれらのファイルを好きなディレクトリに移動してもよい  
	ただし、jnetpcap.dllとjnetpcap.jarは同じフォルダに配置する

4.	コマンドプロンプト(Windows Power Shellでも可)を開く
	- set JAVA_HOME=<コンパイル後のclassファイルが入るフォルダのフルパス>
		- すでにJAVA_HOME環境変数を設定している場合不要
	- javac -classpath ;<jnetpcap.jarへの(相対||絶対)パス(.jarまで)> -encode UTF-8 <ファイル名>
	- java -classpath ;<jnetpcapへのパス(.jarまで)> - <クラス名> <引数>