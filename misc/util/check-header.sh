#!/bin/bash
if ! grep -q "Copyright (C)" "$1"; then
	echo "adding license to $1"
	cat header.txt "$1" > "$1.new" && mv "$1.new" "$1"
else
	echo "skipping $1"
fi
