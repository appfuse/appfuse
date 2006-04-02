#!/bin/sh 
#content of /opt/cruisecontrol/init script
# chkconfig: 345 99 05 
# description: CruiseControl build loop (see /home/tools)

# based on http://confluence.public.thoughtworks.org/display/CC/RunningCruiseControlFromUnixInit
# adapted for multiple projects

#
# Cruise Control startup: Startup and kill script for Cruise Control
#

PATH=/sbin:/usr/sbin:/usr/bin:/bin
export PATH

NAME=cruisecontrol
DESC="CruiseControl 2.2 continuous integration build loop"

CC_USER=mraible
CC_WORK_DIR=/opt/tools/cruisecontrol
CC_INSTALL_DIR=/opt/tools/cruisecontrol

CC_DAEMON=$CC_INSTALL_DIR/cruise.sh
CC_CONFIG_FILE=$CC_WORK_DIR/config.xml
CC_LOG_FILE=$CC_WORK_DIR/cruisecontrol.log
CC_PORT=8082
CC_RMIPORT=
CC_COMMAND="$CC_DAEMON -configfile $CC_CONFIG_FILE -port $CC_PORT -rmiport $CC_RMIPORT"

# overwrite settings from default file
if [ -f /etc/default/cruisecontrol ]; then
  . /etc/default/cruisecontrol
fi

test -f $CC_DAEMON || exit 0

if [ `id -u` -ne 0 ]; then
        echo "Not starting/stopping $DESC, you are not root."
        exit 4
fi

# PPID is read-only in my shell - GNU bash, version 2.05b.0(1)-release (i586-mandrake-linux-gnu)
PARPID=`ps -ea -o "pid ppid args" | grep -v grep | grep "${CC_DAEMON}" | sed -e 's/^  *//' -e 's/ .*//'`

if [ "${PARPID}" != "" ]
then
  PID=`ps -ea -o "pid ppid args" | grep -v grep | grep java | grep "${PARPID}" | \
      sed -e 's/^  *//' -e 's/ .*//'`
fi

case "$1" in
 
  'start')
  # going into the work dir allows for use of relative PATHs in the config file
    cd $CC_WORK_DIR
    su $CC_USER -c "$CC_COMMAND >> $CC_LOG_FILE 2>&1" & RETVAL=$? 
    echo "$NAME started with jmx on port ${CC_PORT}"
    ;;

  'stop')
    if [ "${PID}" != "" ]
    then
     kill -9 ${PID} ${PARPID}
      $0 status
      RETVAL=$?
    else
      echo "$NAME is not running"
      RETVAL=1
    fi
    ;;

  'status')
    # echo PARPIDs $PARPID
    # echo PIDs $PID
    kill -0 $PID >/dev/null 2>&1
    if [ "$?" = "0" ]
    then
      echo $NAME \(pids $PARPID $PID\) is running
      RETVAL=0
    else
      echo "$NAME is stopped"
      RETVAL=1
    fi
    ;;

  'restart')
    $0 stop && $0 start
    RETVAL=$?
    ;;

  *)
    echo "Usage: $0 { start | stop | status | restart }"
    exit 1
    ;;
esac
#echo ending $0 $$....
exit 0;

