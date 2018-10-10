attrib -R desktop\build\jfx\native /d /s
attrib -R desktop\build\jfx\native\SnakeEaters\SnakeEaters.exe
mt.exe -manifest "SnakeEaters.manifest" -outputresource:"desktop\build\jfx\native\SnakeEaters\SnakeEaters.exe;#1"