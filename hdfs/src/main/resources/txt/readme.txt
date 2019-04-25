可以直接通过 添加 hdfs-site.xml 文件，修改上传文件的副本数

这里配置的优先级要高于直接在集群上配置的优先级


conf.set("dfs.replication", "2");在代码中的优先级最高