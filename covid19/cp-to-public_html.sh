#!/bin/bash
dest="$HOME/public_html/covid19"
rm -rvf $dest
echo "deleted old files"
mkdir $dest
echo "made new dir : $dest"
cp -vr ./index.html ./js/ ./css/ $dest
echo "copied files to $dest" 
exit;


