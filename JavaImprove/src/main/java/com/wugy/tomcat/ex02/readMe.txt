ServletProcessor1和ServletProcessor2类

	ServletProcessor1类的process方法，向上转换Request为ServlerRequest，转换Response为ServletResponse，作为参数传递给Servlet的service方法，
这会危害安全性，知道这个 servlet 容器的内部运作的 Servlet 程序员可以分别把ServletRequest 和 ServletResponse 实 例 向 下 转 换 为Request 和Response，
并调用他们的公共方法。拥有一个 Request 实例，它们就可以调用 parse方法。拥有一个 Response 实例，就可以调用 sendStaticResource 方法。你不可以把 parse 
和 sendStaticResource 方法设置为私有的，因为它们将会被其他的类调用。不过，这两个方法是在个 servlet 内部是不可见的。其中一个解决办法就是让 Request 和Response 
类拥有默认访问修饰，所以它们不能在 ex02 包的外部使用。不过，这里有一个更优雅的解决办法：通过使用 facade 增加了两个 façade 类: RequestFacade 和 ResponseFacade。
	
ServletProcessor2：
	RequestFacade 实 现 了 ServletRequest 接 口 并 通 过 在 构 造 方 法 中 传 递 一 个 引 用 了ServletRequest 对象的 Request 实例作为参数来实例化。 ServletRequest
接口中每个方法的实现都调用了 Request 对象的相应方法。然而 ServletRequest 对象本身是私有的，并不能在类的外部访问。我们构造了一个 RequestFacade 对象并把它传递给
service 方法，而不是向下转换Request 对象为 ServletRequest 对象并传递给 service 方法。Servlet 程序员仍然可以向下转换ServletRequest 实例为 RequestFacade，
不过它们只可以访问 ServletRequest 接口里边的公共方法。现在 parseUri 方法就是安全的了。
