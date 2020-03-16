①函数                      Function
②表达式                    Expression
③类                        ClassDemo.scala
④抽象类                    AbstractClassDemo.scala
⑤trait(接口)               AbstractClassDemo.scala
⑥apply and object          ApplyAndSingletonDemo.scala
⑦package                   PackageDemo
⑧模式匹配                  PatternMatching.scala
⑨高阶函数                  HighOrderFunc



(1)Scala中“_”代表什么
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


(2)scala中“=>”的4种使用场景  -- https://www.cnblogs.com/wjunge/p/10043079.html
1.表示函数的返回类型(Function Type)
    函数double的类型就是 (x: Int) => Int 或者 Int => Int。左边是参数类型，右边是方法返回值类型。
    备注： 当函数只有一个参数的时候，函数类型里面括起来函数参数的括号是可以省略的。

2.匿名函数
    匿名函数定义， 左边是参数 右边是函数实现体 （x: Int）=>{}

3.case语句
    在模式匹配 match 和 try-catch 都用 “=>” 表示输出的结果或返回的值

4.By-Name Parameters(传名参数)
    xxx



scala中常用但其他语言不常见的符号含义---https://www.cnblogs.com/xinlingyoulan/p/6031157.html
