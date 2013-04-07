#!/bin/sh
# encoding: utf-8

FULLPATH="jp/ac/kansai_u/kutc/firefly/packetArt";
MAINFILE="${FULLPATH}/Main";
JAVAC_OPTIONS=" -encoding UTF-8";

alert(){
    echo 'This Script Must be run as "PacketArt/src/$0"';
    exit -1;
}

usage(){
    echo "##########################";
    echo " $0 compile";
    echo "           -  Compile PacketArt";
    echo " $0 start";
    echo "           -  Play PacketArt";
    echo "";
    echo "#########################";
    exit -1;
}

check(){
    [ `basename $(dirname ${PWD})` != "PacketArt" ] && alert;
    [ ! -e "${FULLPATH}" ] && alert;
}

check;
case "$1" in
    "start" | "star" | "sta" | "run" | "play")
        java "${MAINFILE}";
        [ "$?" == "0" ] && exit 0;
        exit -1;
    ;;
    "compile" | "c" | "compi" | "make" | "mak")
        pushd . > /dev/null;
        cd "${FULLPATH}";
        javac ${JAVAC_OPTIONS} ./*.java;
        RET="$?";
        popd > /dev/null;
        [ "$RET" == "0" ] && exit 0;
        exit -1;
    ;;
    *)
        usage;
    ;;
esac
