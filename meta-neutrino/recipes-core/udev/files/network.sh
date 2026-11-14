#!/bin/sh

# During boot, the background workers handle auto Wi-Fi and Ethernet.
case "$INTERFACE:$ACTION" in
    wlan0:add|eth0:add)
        if [ -x /usr/sbin/ni-wifi-boot ] && [ ! -e "/run/ni-wifi-boot/$INTERFACE.boot-complete" ]; then
            exit 0
        fi
        ;;
esac

# We get two "add" events for hostap cards due to wifi0
echo "$INTERFACE" | grep -q wifi && exit 0

# udevd does clearenv(). Export shell PATH to children.
export PATH

# if this interface has an entry in /etc/network/interfaces, let ifupdown
# handle it, but only if the interface is defined as "auto up"
if grep -q "^iface \+$INTERFACE" /etc/network/interfaces; then
	if grep -q "^auto \+$INTERFACE" /etc/network/interfaces; then
		case $ACTION in
			add)
				ifconfig | grep -q "^$INTERFACE" || ifup $INTERFACE
				;;
			 remove)
				ifdown $INTERFACE
				;;
		esac
	fi
	exit 0
fi
