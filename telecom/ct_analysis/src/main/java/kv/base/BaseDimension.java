package kv.base;

import org.apache.hadoop.io.WritableComparable;

/**
 * 为了实现key排序s
 */
public abstract class BaseDimension implements WritableComparable<BaseDimension>{}
