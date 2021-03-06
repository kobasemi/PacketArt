#!/bin/sh
# encoding: utf-8

FULLPATH="jp/ac/kansai_u/kutc/firefly/packetArt";
MAINFILE="${FULLPATH}/Main";
JAVAC_OPTIONS=" -encoding UTF-8";
THIS_FILE=`basename $0`;
THIS_DIR=`dirname $0`;

alert(){
    echo -e "This Script Must be run as PacketArt/src/${THIS_FILE}";
    exit -1;
}

usage(){
    echo "##########################";
    echo -e "${THIS_FILE} compile";
    echo "           -  Compile PacketArt";
    echo -e "${THIS_FILE} start";
    echo "           -  Play PacketArt";
    echo "#########################";
    exit -1;
}

check(){
    pushd . > /dev/null;
    cd "${THIS_DIR}";
    [ ! -e "${FULLPATH}" ] && alert;
    popd > /dev/null;
}

check;
case "$1" in
    "start" | "star" | "sta" | "run" | "play")
        pushd . > /dev/null;
        cd "${THIS_DIR}"
        java "${MAINFILE}";
        [ "$?" == "0" ] && exit 0;
        popd > /dev/null;
        exit -1;
    ;;
    "compile" | "c" | "compi" | "make" | "mak")
        pushd . > /dev/null;
        cd "${THIS_DIR}"
        for filename in `find "${FULLPATH}" -name "*.java"`
        do
            javac ${JAVAC_OPTIONS} "$filename";
        done
        popd > /dev/null;
    ;;
    *)
        usage;
    ;;
esac
