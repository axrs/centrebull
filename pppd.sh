#!/bin/bash

pppd /dev/tty.usbserial-DN01IQ83 57600 172.16.0.1:172.16.0.2 proxyarp local noauth debug nodetach dump nocrtscts passive persist maxfail 0 holdoff 1
