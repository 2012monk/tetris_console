#!/bin/bash

echo "Start"
tty_state=$(stty -g)
stty -icanon min 1
stty -echo

javac Main.java
java Main
stty "$tty_state"
