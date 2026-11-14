#!/bin/sh
if [ -d /run/ni-wifi-boot ] && [ ! -e /run/ni-wifi-boot/wlan0.boot-complete ]; then
    touch /run/ni-wifi-boot/wlan0.manual
fi
exec /usr/sbin/ni-wifi-ifupdown start
