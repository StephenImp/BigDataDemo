目的：将多个小文件合并成一个大文件。

        合并的过程中，n个文件,还是开启的n个mapTask在合并，并没有优化，
        但是小文件，都会变成大文件。

        日后，再启动job去处理分析这个大文件，效率就会提高。

          类似于   HDFS的    HAR文件。


       日后，想取出SequenceFile中的某一个文件，根据key就能取出，就能得到对应的value值，就能获取内容。


1.WholeFileInputformat
2.WholeRecordReader         自定义RecordReader并初始化
                            思路在类中。

3.SequenceFileMapper
4.SequenceFileReducer