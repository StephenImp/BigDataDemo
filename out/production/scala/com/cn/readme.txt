①函数                      Function
②表达式                    Expression
③类                        ClassDemo.scala
④抽象类                    AbstractClassDemo.scala
⑤trait(接口)               AbstractClassDemo.scala
⑥apply and object          ApplyAndSingletonDemo.scala
⑦package                   PackageDemo
⑧模式匹配                  PatternMatching.scala
⑨高阶函数                  HighOrderFunc




Scala中“_”代表什么
1、作为“通配符”，类似Java中的*。如import scala.math._
2、:_* 作为一个整体，告诉编译器你希望将某个参数当作参数序列处理！
        例如val s = sum(1 to 5:_*)就是将1 to 5当作参数序列处理。

3、指代一个集合中的每个元素。例如我们要在一个Array a中筛出偶数，并乘以2，可以用以下办法：
    a.filter(_%2==0).map(2*_)   ⑨---16

4.使用模式匹配可以用来获取元组的组员

5.使用模式匹配可以用来获取元组的组员     ⑨---7

6、还有一点，下划线_代表的是某一类型的默认值。
对于Int来说，它是0。
对于Double来说，它是0.0
对于引用类型，它是null。