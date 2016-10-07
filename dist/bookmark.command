#!/bin/bash 
cd ~/Desktop/bookmark
for filename in *.pdf
do
    java -jar "bookmark.jar" "$filename"
    mv "$filename" old
done

for filename in ~/Desktop/bookmark/output/*.pdf
do
    mv "$filename" ~/Desktop
done
